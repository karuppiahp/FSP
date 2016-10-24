package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import activities.mswift.info.walaapp.wala.more.FinancialInfoStep4Activity;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;

/**
 * Created by karuppiah on 2/12/2016.
 */
public class GetMemberBudgetTasks {

    private Context context;
    private JSONObject jObject = null;
    private String result = "", status = "", message = "", authorizationKey;

    public GetMemberBudgetTasks(Context context) {
        this.context = context;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GETMONTHLY_EXPENSES)).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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
                            message = jObject.getString("Message");
                            JSONObject dataObj = jObject.getJSONObject("DataObject");
                            JSONArray budgetArray = dataObj.getJSONArray("MemberBudgetItems");
                            for (int i = 0; i < budgetArray.length(); i++) {
                                String category = budgetArray.getJSONObject(i).getString("Category");
                                String subCategory = budgetArray.getJSONObject(i).getString("SubCategory");
                                String freq = budgetArray.getJSONObject(i).getString("Frequency");
                                String amnt = budgetArray.getJSONObject(i).getString("Amount");

                                if (subCategory.equals("Rent")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForRent.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForRent.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("Groceries")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForGroceries.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForGroceries.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("AllTravelExpenses")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForTransport.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForTransport.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("AllMedicalExpenses")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForHealth.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForHealth.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("AllEducationExpenses")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForEducation.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForEducation.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("AllUtilityBills")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForUtilities.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForUtilities.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("Shopping")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForShopping.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForShopping.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("AllLeisureEntertainment")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForEntrtnmnt.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForEntrtnmnt.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("RestaurantsDiningOut")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForDining.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForDining.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("HolidayTravelAccommodation")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForTravel.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForTravel.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }

                                if (subCategory.equals("OtherLeisureEntertainment")) {
                                    try {
                                        if (!(amnt.equals("0.00") && Double.parseDouble(amnt) == 0.00)) {
                                            FinancialInfoStep4Activity.editTxtForOther.setText(amnt);
                                        } else {
                                            FinancialInfoStep4Activity.editTxtForOther.setHint("0.0");
                                        }
                                    } catch (NumberFormatException e) {
                                        e.printStackTrace();
                                    }
                                }
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
