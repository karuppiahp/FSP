package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.model.UpdateAnswerModel;
import activities.mswift.info.walaapp.wala.prizes.GamesActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 3/8/2016.
 */
public class UpdateAnswerTask {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", answer, points;

    public UpdateAnswerTask(Context context, Map<String, String> params, String answer) {
        this.context = context;
        this.params = params;
        this.answer = answer;
        Constants.ANSWER_POINT_ARRAY.clear();
        ((TabHostFragments) context).progressBarVisible();
        Constants.ANSWER_POINT_ARRAY.clear();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.UPDATE_ANSWER)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                        if (jObject.has("status")) {
                            ((TabHostFragments) context).progressBarGone();
                            message = jObject.getString("message");
                            points = jObject.getString("point");
                            UpdateAnswerModel answerModel = new UpdateAnswerModel();
                            answerModel.setPoints(points);
                            Constants.ANSWER_POINT_ARRAY.add(answerModel);
                            SessionStores.saveQuizEntry("attend", context);
                            SessionStores.saveQuesDate(Utils.getCurrentDate(), context);

                            if (answer.equals("1")) {
                                /**Move to another fragment*/
                                SessionStores.setAnswer("1", context);
                                Constants.GAMES_PAGE = "Correct";
                                GamesActivity.refreshPage();
                            } else {
                                /**Move to another fragment*/
                                SessionStores.setAnswer("0", context);
                                Constants.GAMES_PAGE = "Wrong";
                                GamesActivity.refreshPage();
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
