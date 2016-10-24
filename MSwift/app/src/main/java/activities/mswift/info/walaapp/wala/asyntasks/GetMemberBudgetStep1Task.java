package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import activities.mswift.info.walaapp.wala.more.FinancialInfoStep1Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;





/**
 * Created by karuppiah on 2/12/2016.
 */
public class GetMemberBudgetStep1Task {

    private Context context;
    private JSONObject jObject = null;
    private String result = "", status = "", message = "", authorizationKey;
    private boolean amntAvail = false;

    public  GetMemberBudgetStep1Task(Context context) {
        this.context = context;
        ((TabHostFragments) context).progressBarVisible();
        Constants.PREVIOUS_DATE = "";
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GETMEMBER_BUDGET)).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                ((TabHostFragments) context).progressBarGone();
                Toast.makeText(context, ""+message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    if (jObject != null) {
                        if (jObject.has("Status")) {
                            message = jObject.getString("Message");
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            JSONArray budgetArray = dataObj.getJSONArray("MemberBudgetItems");
                            for(int i=0; i<budgetArray.length(); i++) {
                                String category = budgetArray.getJSONObject(i).getString("Category");
                                String subCategory = budgetArray.getJSONObject(i).getString("SubCategory");
                                String freq = Utils.frequencyRevert(budgetArray.getJSONObject(i).getString("Frequency"));
                                String amnt = budgetArray.getJSONObject(i).getString("Amount");
                                String lastUpdate = budgetArray.getJSONObject(i).getString("LastUpdatedDate");

                                if(Constants.PREVIOUS_DATE.length() > 0) {
                                    boolean currentDate = Utils.calculateRecentDayUpdate(Constants.PREVIOUS_DATE, lastUpdate);
                                    if(currentDate == true) {
                                        Constants.PREVIOUS_DATE = lastUpdate;
                                        Constants.AMOUNT = amnt;
                                        if (!(amnt.equals("0.00"))) {
                                            amntAvail = true;
                                        }
                                        if (subCategory.equals("RegularSalary")) {
                                            Constants.INCOME_TYPE = "Salary";
                                        }
                                        if (subCategory.equals("RegularHourlyWage")) {
                                            Constants.INCOME_TYPE = "Hourly Wage";
                                        }
                                        if (subCategory.equals("OtherIncome")) {
                                            Constants.INCOME_TYPE = "Other";
                                        }
                                        Constants.INCOME_FREQ = freq;
                                    }
                                } else {
                                    Constants.PREVIOUS_DATE = lastUpdate;
                                    Constants.AMOUNT = amnt;
                                    if (!(amnt.equals("0.00"))) {
                                        amntAvail = true;
                                    }
                                    if (subCategory.equals("RegularSalary")) {
                                        Constants.INCOME_TYPE = "Salary";
                                    }
                                    if (subCategory.equals("RegularHourlyWage")) {
                                        Constants.INCOME_TYPE = "Hourly Wage";
                                    }
                                    if (subCategory.equals("OtherIncome")) {
                                        Constants.INCOME_TYPE = "Other";
                                    }
                                    Constants.INCOME_FREQ = freq;
                                }

                                if(i == budgetArray.length()-1) {
                                    if (amntAvail == true) {
                                        FinancialInfoStep1Activity.editTxtAvgMonthlyIncm.setText(Constants.AMOUNT);
                                        FinancialInfoStep1Activity.txtForIncomeType.setText(Constants.INCOME_TYPE);
                                        FinancialInfoStep1Activity.txtForIncomeFreq.setText(Constants.INCOME_FREQ);
                                    }
                                }
                            }
                            EmpStatusTask();
                        }
                    } else {
                        ((TabHostFragments) context).progressBarGone();
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }

    public void EmpStatusTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GETLIFESTYLE)).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            message = jObject.getString("Message");
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            String careerStatus = dataObj.getString("CareerStatus");
                            FinancialInfoStep1Activity.empStatus=careerStatus;
                            if(careerStatus.equals("4")) {
                                FinancialInfoStep1Activity.txtForEmpStatus.setText("Full-Time Employed");
                            }

                            if(careerStatus.equals("8")) {
                                FinancialInfoStep1Activity.txtForEmpStatus.setText("Part-Time Employed");
                            }

                            if(careerStatus.equals("1")) {
                                FinancialInfoStep1Activity.txtForEmpStatus.setText("Unemployed");
                            }
                            ((TabHostFragments) context).progressBarGone();
                        }
                    } else {
                        ((TabHostFragments) context).progressBarGone();
                    }
                } catch (JSONException e) {
                    ((TabHostFragments) context).progressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
