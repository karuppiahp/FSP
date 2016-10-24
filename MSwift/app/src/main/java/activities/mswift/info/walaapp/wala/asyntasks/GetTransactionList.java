package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import activities.mswift.info.walaapp.wala.adapter.ManageTransactionAdapter;
import activities.mswift.info.walaapp.wala.model.RecentTransactionsModel;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.transactions.TransactionManageFragment;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 22.01.16..
 */
public class GetTransactionList {
    private Context context;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";
    private String savingWalletNumber;

    //modification
    private List<RecentTransactionsModel> transactionArray = new ArrayList<>();
    private String txtForTransactionDateStart;
    private String txtForTransactionDateEnd;
    private SwipeMenuListView mListView;

    public GetTransactionList(Context context, String txtForTransactionDateStart, String txtForTransactionDateEnd, SwipeMenuListView mListView) {
        this.context = context;
        ;
        this.txtForTransactionDateStart = txtForTransactionDateStart;
        this.txtForTransactionDateEnd = txtForTransactionDateEnd;
        this.mListView = mListView;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    //TODO waiting answer from tipsgo, for now we are using wallet transactions
    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        savingWalletNumber = SessionStores.getDebtWalletNumber(context);

        new ServerResponse(ApiClass.getApiUrl(Constants.GET_TRANSACTION) +
                "accountNumber=" + "" + "&startDate=" + txtForTransactionDateStart + "&endDate=" + txtForTransactionDateEnd + "&pageSize=20&pageNum=0").getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            mListView.setAdapter(new ManageTransactionAdapter(jsonArray, context));
                            TransactionManageFragment.jsonArray = jsonArray;
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
