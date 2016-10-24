package activities.mswift.info.walaapp.wala.asyntasks;

import android.app.Activity;
import android.view.View;
import android.widget.ListAdapter;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 9/30/2016.
 */
public class GetUserDetailsTask {
    private Activity context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", arraySize;
    public ListAdapter adapter;
    private ProgressBar progressBar;


    public GetUserDetailsTask(Activity context, Map<String, String> params, ProgressBar progressBar) {
        this.context = context;
        this.params = params;
        this.progressBar = progressBar;
        ResponseTask();

    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.GET_USER_DETAILS)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                progressBar.setVisibility(View.GONE);
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {

                        if (jObject.has("email")) {
                            String country = jObject.getString("country");
                            String city = jObject.getString("city");
                            String quiz = jObject.getString("quiz");
                            String ques = jObject.getString("question");
                            String correctAns = jObject.getString("correct_answer");
                            String answer = jObject.getString("answer");
                            String points = jObject.getString("points");

                            if (quiz.equals("0")) {
                                SessionStores.saveQuesDate(Utils.getCurrentDate(), context);
                            }

                            SessionStores.setQuizQues(ques, context);
                            SessionStores.setQuizAns(correctAns, context);
                            SessionStores.setAnswer(answer, context);
                            SessionStores.setPlayPoints(points, context);
                            SessionStores.saveCurrencyCode(Utils.getCountryCodes().get(country), context);
                            SessionStores.saveCountry(country, context);
                            SessionStores.saveCity(city, context);
                        }

                        new GetWalletList(context, progressBar);
                    }
                } catch (JSONException e) {
                    progressBar.setVisibility(View.GONE);
                    e.printStackTrace();
                }

            }


        });

    }
}
