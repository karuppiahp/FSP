package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

import activities.mswift.info.walaapp.wala.model.GetLotteryModel;
import activities.mswift.info.walaapp.wala.prizes.MonthlyLotteryActivity;
import activities.mswift.info.walaapp.wala.prizes.WeeklyLotteryActivity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 4/6/2016.
 */
public class GetCurrentLotteryTask {
    private Context context;
    private String url, result, status = "", message = "", type, daysLeftConvert;
    private JSONObject jObject = null;
    private Map<String, String> params;

    public GetCurrentLotteryTask(Context context, Map<String, String> params, String type) {
        this.context = context;
        this.params = params;
        this.type = type;
        Constants.LOTTERY_MODEL_ARRAY.clear();
        ((TabHostFragments) context).progressBarVisible();
        Constants.LOTTERY_MODEL_ARRAY.clear();
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(ApiClass.getBackendApiUrl(Constants.CURRENTLOTTERY)).getJSONObjectfromURL(ServerResponse.RequestType.POST, params, Constants.BEARER_KEY, context, "", new VolleyResponseListener() {
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
                            status = jObject.getString("status");
                            if (status.equals("success")) {
                                String id = jObject.getString("id");
                                String format = jObject.getString("format");
                                String prize = jObject.getString("prize");
                                String daysLeft = jObject.getString("left");
                                String points = jObject.getString("points");
                                String noOfParticipants = jObject.getString("no_of_participants");
                                String userTktsCount = jObject.getString("user_tickets_count");
                                String pointsLeft = jObject.getString("user_points_left");
                                String winnersCount = jObject.getString("no_of_winners");
                                GetLotteryModel lotteryModel = new GetLotteryModel();
                                lotteryModel.setId(id);
                                lotteryModel.setPoints(points);
                                lotteryModel.setTcktsCount(userTktsCount);
                                lotteryModel.setPointsLeft(pointsLeft);
                                lotteryModel.setParticipants(noOfParticipants);
                                Constants.LOTTERY_MODEL_ARRAY.add(lotteryModel);
                                if (format.equals("day")) {
                                    if (Integer.parseInt(daysLeft) > 1) {
                                        daysLeftConvert = daysLeft + " Days Left";
                                    } else {
                                        daysLeftConvert = daysLeft + " Day Left";
                                    }
                                } else {
                                    daysLeftConvert = Utils.calculateTimeLeft(daysLeft, context) + " Times Left";
                                }
                                if (type.equals("weekly")) {
                                    WeeklyLotteryActivity.txtForAmnt.setText(prize + " to Savings");
                                    WeeklyLotteryActivity.txtForDaysLeft.setText(daysLeftConvert);
                                    WeeklyLotteryActivity.txtForParticipants.setText(noOfParticipants + " Participants");
                                    WeeklyLotteryActivity.txtForPointsWinnerCount.setText("Enter the lottery for this week's great prize! Entry costs " + points + " points. " + winnersCount + " winners will be chosen!");
                                } else {
                                    MonthlyLotteryActivity.txtForAmnt.setText(prize + " to Savings");
                                    MonthlyLotteryActivity.txtForDaysLeft.setText(daysLeftConvert);
                                    MonthlyLotteryActivity.txtForParticipants.setText(noOfParticipants + " Participants");
                                    MonthlyLotteryActivity.txtForPointsWinnerCount.setText("Enter the lottery for this week's great prize! Entry costs " + points + " points. " + winnersCount + " winners will be chosen!");
                                }
                                ((TabHostFragments) context).progressBarGone();
                            } else {
                                if (type.equals("weekly")) {
                                    WeeklyLotteryActivity.btnForBuyTickets.setClickable(false);
                                } else {
                                    MonthlyLotteryActivity.btnForBuyTickets.setClickable(false);
                                }
                                message = jObject.getString("message");
                                ((TabHostFragments) context).progressBarGone();
                                Utils.ShowAlert(context, message);
                            }
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
