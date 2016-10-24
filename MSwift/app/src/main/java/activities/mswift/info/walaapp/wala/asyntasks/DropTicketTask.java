package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.model.GetLotteryModel;
import activities.mswift.info.walaapp.wala.prizes.GamesActivity;
import activities.mswift.info.walaapp.wala.prizes.MonthlyLotteryActivity;
import activities.mswift.info.walaapp.wala.prizes.WeeklyLotteryActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 4/11/2016.
 */
public class DropTicketTask {
    private Context context;
    private String url, result, status = "", message = "";
    private JSONObject jObject = null;
    private Map<String, String> params;

    public DropTicketTask(Context context, Map<String, String> params) {
        this.context = context;
        this.params = params;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.DROP_TICKET)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BEARER_KEY, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                GamesActivity.btnForDropTicket.setClickable(true);
                GamesActivity.btnForDropTicket.setEnabled(true);
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        GamesActivity.btnForDropTicket.setClickable(true);
                        GamesActivity.btnForDropTicket.setEnabled(true);
                        if (jObject.has("status")) {
                            status = jObject.getString("status");
                            if (status.equals("success")) {
                                message = jObject.getString("message");
                                ((TabHostFragments) context).progressBarGone();
                                GetLotteryModel lotteryModel = Constants.LOTTERY_MODEL_ARRAY.get(0);
                                lotteryModel.setPointsLeft("" + (Integer.parseInt(lotteryModel.getPointsLeft()) + Integer.parseInt(lotteryModel.getPoints())));
                                lotteryModel.setTcktsCount("" + (Integer.parseInt(lotteryModel.getTcktsCount()) - 1));
                                lotteryModel.setParticipants("" + (Integer.parseInt(lotteryModel.getParticipants()) - 1));
                                if (Constants.WEEKLY_MONTHLY == 1) {
                                    WeeklyLotteryActivity.txtForParticipants.setText(lotteryModel.getParticipants() + " Participants");
                                } else {
                                    MonthlyLotteryActivity.txtForParticipants.setText(lotteryModel.getParticipants() + " Participants");
                                }
                                GamesActivity.popUpHide();
                                Toast.makeText(context, "" + message, Toast.LENGTH_SHORT).show();
                            } else {
                                message = jObject.getString("message");
                                GamesActivity.popUpHide();
                                ((TabHostFragments) context).progressBarGone();
                                Utils.ShowAlert(context, message);
                            }
                        }
                    }
                } catch (JSONException e) {
                    GamesActivity.btnForDropTicket.setClickable(true);
                    GamesActivity.btnForDropTicket.setEnabled(true);
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
