package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import activities.mswift.info.walaapp.wala.adapter.ManageTransactionAdapter;
import activities.mswift.info.walaapp.wala.model.RecentTransactionsModel;
import activities.mswift.info.walaapp.wala.progress.BalanceDeptFragment;
import activities.mswift.info.walaapp.wala.progress.BalanceSavingsFragment;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 19.02.16..
 */
public class GetWalletTransactionListTask {
    private Context context;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";

    //modification
    private List<RecentTransactionsModel> transactionArray = new ArrayList<>();
    SwipeMenuListView transactionSavingsListView;
    private String txtForTransactionDateStart;
    private String txtForTransactionDateEnd;
    private String walletCode;
    private String TAG;


    public GetWalletTransactionListTask(Context context, String txtForTransactionDateStart, String txtForTransactionDateEnd, String walletCode, SwipeMenuListView transactionSavingsListView, String tag) {
        this.context = context;
        ;
        this.txtForTransactionDateStart = txtForTransactionDateStart;
        this.txtForTransactionDateEnd = txtForTransactionDateEnd;
        this.transactionSavingsListView = transactionSavingsListView;
        this.walletCode = walletCode;
        this.TAG = tag;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    //TODO waiting answer from tipsgo, for now we are using wallet transactions
    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);

        new ServerResponse(ApiClass.getApiUrl(Constants.WALLETTRANSACTION_LIST) +
                "walletCode=" + walletCode + "&startDate=" + txtForTransactionDateStart + "&endDate=" + txtForTransactionDateEnd + "&pageSize=20&pageNum=0").getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            ((TabHostFragments) context).progressBarGone();
                            message = jObject.getString("Message");

                            JSONArray jsonArray = jObject.getJSONArray("ListOfObjects");
                            transactionSavingsListView.setAdapter(new ManageTransactionAdapter(jsonArray, context));
                            getWalletBallance();

                        } else {
                            ((TabHostFragments) context).progressBarGone();
                        }
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }

    public void getWalletBallance() {
        new ServerResponse(ApiClass.getApiUrl(Constants.GET_WALLETINFO) + walletCode).getJSONObjectfromURL(ServerResponse.RequestType.GET, null,
                authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        ((TabHostFragments) context).progressBarGone();
                        Toast.makeText(context, "Please check your network availability", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onResponse(String response) {
                        result = response.toString();
                        try {
                            jObject = new JSONObject(result);
                            if (jObject != null) {
                                if (jObject.has("Status")) {
                                    ((TabHostFragments) context).progressBarGone();

                                    JSONObject dataObject = jObject.getJSONObject("DataObject");
                                    String walletBalance = dataObject.getString("CurrentBalance");
                                    if (TAG == "savings") {
                                        Constants.WALLET_BALANCE_SAVINGS = Double.parseDouble(walletBalance);
                                    } else {
                                        Constants.WALLET_BALANCE_DEBT = Double.parseDouble(walletBalance);
                                    }

                                    double value = Double.parseDouble(walletBalance);
                                    Locale locale = new Locale("en", SessionStores.getCurrencyCode(context));
                                    if (locale.toString().equals("en_AQ")) {
                                        locale = new Locale("en", "AI");
                                    }
                                    NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(locale);
                                    String balanceWallet = currencyFormatter.format(value).toString();

                                    if (TAG == "savings") {
                                        if (locale.toString().equals("en_ZM")) {
                                            BalanceSavingsFragment.updateBalanceTextview("Z" + balanceWallet);
                                        } else {
                                            BalanceSavingsFragment.updateBalanceTextview(balanceWallet);
                                        }
                                    }
                                    if (TAG == "debt") {
                                        if (locale.toString().equals("en_ZM")) {
                                            BalanceDeptFragment.updateBalanceTextview("Z" + balanceWallet);
                                        } else {
                                            BalanceDeptFragment.updateBalanceTextview(balanceWallet);
                                        }
                                    }

                                } else {
                                    ((TabHostFragments) context).progressBarGone();
                                }
                            }
                        } catch (JSONException e) {
                            ((TabHostFragments) context).progressBarGone();
                            e.printStackTrace();
                        }
                    }
                });
    }
}
