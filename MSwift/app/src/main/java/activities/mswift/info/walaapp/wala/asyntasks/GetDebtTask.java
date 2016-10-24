package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.more.FinancialInfoStep3Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 2/18/2016.
 */
public class GetDebtTask {

    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", accNumber, savingsType;

    public GetDebtTask(Context context, String accNumber) {
        this.context = context;
        this.accNumber = accNumber;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GETDEPT) + accNumber).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            String accType = dataObj.getString("AccountType");

                            if (accType.equals("PersonalLoan")) {
                                FinancialInfoStep3Activity.chkBoxForLoan.setChecked(true);
                            }

                            if (accType.equals("CreditCardAccount")) {
                                FinancialInfoStep3Activity.chkBoxForCreditCard.setChecked(true);
                            }

                            if (accType.equals("StudentLoan")) {
                                FinancialInfoStep3Activity.chkBoxForSchool.setChecked(true);
                            }

                            if (accType.equals("DebtOtherAccounts")) {
                                FinancialInfoStep3Activity.chkBoxForOther.setChecked(true);
                            }
                            FinancialInfoStep3Activity.debtOweType = accType;
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
