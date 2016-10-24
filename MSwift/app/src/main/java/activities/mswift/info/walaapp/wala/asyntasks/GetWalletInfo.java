package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import activities.mswift.info.walaapp.wala.more.FinancialInfoStep2Activity;
import activities.mswift.info.walaapp.wala.more.FinancialInfoStep3Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by kruno on 09.03.16..
 */
public class GetWalletInfo {
    private Context context;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", walletcode, balanance, tag, id;

    public GetWalletInfo(Context context, String walletcode, String tag) {
        this.context = context;
        this.walletcode = walletcode;
        this.tag = tag;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GET_WALLETINFO) + walletcode).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            id = dataObj.getString("ID");
                            balanance = dataObj.getString("CurrentBalance");

                            BigDecimal myNumber = new BigDecimal(balanance);
                            double myDouble = myNumber.doubleValue();
                            NumberFormat formatter = new DecimalFormat("#################");
                            formatter.setMaximumFractionDigits(4);
                            String balanceamt = (formatter.format(myDouble));

                            if (tag.equals("savings")) {
                                FinancialInfoStep2Activity.edittextForAmnt.setText(balanceamt);
                                FinancialInfoStep2Activity.oldAmount = balanceamt;
                            }
                            if (tag.equals("debt")) {
                                FinancialInfoStep3Activity.editTxtForCurrentlyOwe.setText(balanceamt);
                                FinancialInfoStep3Activity.oldAmount = balanceamt;
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
