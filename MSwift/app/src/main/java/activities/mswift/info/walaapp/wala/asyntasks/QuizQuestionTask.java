package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.model.QuizQuestionModel;
import activities.mswift.info.walaapp.wala.prizes.GamesActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 3/3/2016.
 */
public class QuizQuestionTask {
    private Context context;
    private Map<String, String> params;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "";

    public QuizQuestionTask(Context context, Map<String, String> params) {
        this.context = context;
        this.params = params;
        ((TabHostFragments) context).progressBarVisible();
        Constants.QUES_ARRAY.clear();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.QUIZ_QUESTION)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            String questionId = jObject.getString("id");
                            String question = jObject.getString("question");
                            String correctAnswer = jObject.getString("correct_answer");
                            String wrongAnswer1 = jObject.getString("wrong_answer1");
                            String wrongAnswer2 = jObject.getString("wrong_answer2");
                            String tips = jObject.getString("tips");

                            QuizQuestionModel quesModel = new QuizQuestionModel();
                            quesModel.setQues(question);
                            quesModel.setQuesId(questionId);
                            quesModel.setCorrectAns(correctAnswer);
                            quesModel.setWrongAns1(wrongAnswer1);
                            quesModel.setWrongAns2(wrongAnswer2);
                            quesModel.setTips(tips);
                            Constants.QUES_ARRAY.add(quesModel);

                            /**Move to another fragment*/
                            Constants.GAMES_PAGE = "Daily";
                            GamesActivity.refreshPage();
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
