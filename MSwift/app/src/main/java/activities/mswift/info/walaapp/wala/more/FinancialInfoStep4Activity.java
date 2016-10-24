package activities.mswift.info.walaapp.wala.more;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.mixpanel.android.mpmetrics.MixpanelAPI;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.FinancialInfo5Task;
import activities.mswift.info.walaapp.wala.asyntasks.GetMemberBudgetTasks;
import activities.mswift.info.walaapp.wala.model.FinancialInfo5Model;
import activities.mswift.info.walaapp.wala.model.FinancialInfo5ModelBackend;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.Analitics;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 12/10/2015.
 */
public class FinancialInfoStep4Activity extends Fragment {

    private View v;
    private Button btnForSave;
    public static EditText editTxtForRent, editTxtForGroceries, editTxtForTransport, editTxtForHealth, editTxtForEducation, editTxtForUtilities, editTxtForShopping, editTxtForEntrtnmnt, editTxtForDining, editTxtForTravel, editTxtForOther;
    private ArrayList<FinancialInfo5Model> infoArrayList = new ArrayList<>();
    private ArrayList<FinancialInfo5ModelBackend> infoArrayListBackend = new ArrayList<>();
    private TextView txtForPopUp1, txtForPopUp2;
    public static String request = "", resultBackend = "0";
    private MixpanelAPI mixpanelAPI;
    private boolean wantsStatus = false, needsStatus = false;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (container == null) {
            return null;
        }
        ((TabHostFragments) getActivity()).tabColorWhite();

        v = inflater.inflate(R.layout.financial_expenses, container, false);
        editTxtForRent = (EditText) v.findViewById(R.id.editTxtForRent);
        editTxtForGroceries = (EditText) v.findViewById(R.id.editTxtForGroceries);
        editTxtForTransport = (EditText) v.findViewById(R.id.editTxtForTransportation);
        editTxtForHealth = (EditText) v.findViewById(R.id.editTxtForHealth);
        editTxtForEducation = (EditText) v.findViewById(R.id.editTxtForEducation);
        editTxtForUtilities = (EditText) v.findViewById(R.id.editTxtForUtilities);
        editTxtForShopping = (EditText) v.findViewById(R.id.editTxtForShopping);
        editTxtForEntrtnmnt = (EditText) v.findViewById(R.id.editTxtForEntertainment);
        editTxtForDining = (EditText) v.findViewById(R.id.editTxtForDining);
        editTxtForTravel = (EditText) v.findViewById(R.id.editTxtForTravel);
        editTxtForOther = (EditText) v.findViewById(R.id.editTxtForOther);
        btnForSave = (Button) v.findViewById(R.id.btnForSave);
        txtForPopUp1 = (TextView) v.findViewById(R.id.txtForPopUp1);
        txtForPopUp2 = (TextView) v.findViewById(R.id.txtForPopUp2);

        String popUpTxt = "<font color='#7d898c'>Estimate your monthly expenses for things that you </font>";
        String popUpNeed = "<b><font color='#7d898c'>need.</font></b>";
        String popUpWant = "<b><font color='#7d898c'>want.</font></b>";

        txtForPopUp1.setText(Html.fromHtml(popUpTxt + popUpNeed));
        txtForPopUp2.setText(Html.fromHtml(popUpTxt + popUpWant));

        // mixpanel screen tracking
        mixpanelAPI = MixpanelAPI.getInstance(getActivity(), Constants.MIXPANEL_TOKEN);
        mixpanelAPI.track(getString(R.string.mixpanel_financial_info_screen4));

        new GetMemberBudgetTasks(getActivity());

        btnForSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((TabHostFragments) getActivity()).hideKeyboard();
                infoArrayList.clear();
                infoArrayListBackend.clear();
                FinancialInfo5Model infoModel = new FinancialInfo5Model();
                try {
                    if (!(editTxtForRent.getText().toString().equals("")) && !(editTxtForRent.getText().toString().equals("0.00")) && !((Double.parseDouble(editTxtForRent.getText().toString()) % 1) == 0)) {
                        if (!(Double.parseDouble(editTxtForRent.getText().toString()) == 0.00)) {
                            needsStatus = true;
                            Utils.setInfo5Values("Rent", editTxtForRent.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("rent", editTxtForRent.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForRent.getText().toString().equals("") || ((Double.parseDouble(editTxtForRent.getText().toString()) % 1) == 0)) {
                        if (editTxtForRent.getText().toString().length() > 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("Rent", editTxtForRent.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("rent", editTxtForRent.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForRent.getText().toString().length() == 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("Rent", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("rent", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForGroceries.getText().toString().equals("")) && !(editTxtForGroceries.getText().toString().equals("0.00")) && !((Double.parseDouble(editTxtForGroceries.getText().toString()) % 1) == 0)) {
                        if (!(Double.parseDouble(editTxtForGroceries.getText().toString()) == 0.00)) {
                            needsStatus = true;
                            Utils.setInfo5Values("Groceries", editTxtForGroceries.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("groceries", editTxtForGroceries.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForGroceries.getText().toString().equals("") || ((Double.parseDouble(editTxtForGroceries.getText().toString()) % 1) == 0)) {
                        if (editTxtForGroceries.getText().toString().length() > 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("Groceries", editTxtForGroceries.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("groceries", editTxtForGroceries.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForGroceries.getText().toString().length() == 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("Groceries", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("groceries", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForTransport.getText().toString().equals("")) && !(editTxtForTransport.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForTransport.getText().toString()) == 0.00)) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllTravelExpenses", editTxtForTransport.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("transportation", editTxtForTransport.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForTransport.getText().toString().equals("") || ((Double.parseDouble(editTxtForTransport.getText().toString()) % 1) == 0)) {
                        if (editTxtForTransport.getText().toString().length() > 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllTravelExpenses", editTxtForTransport.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("transportation", editTxtForTransport.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForTransport.getText().toString().length() == 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllTravelExpenses", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("transportation", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForHealth.getText().toString().equals("")) && !(editTxtForHealth.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForHealth.getText().toString()) == 0.00)) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllMedicalExpenses", editTxtForHealth.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("health", editTxtForHealth.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForHealth.getText().toString().equals("") || ((Double.parseDouble(editTxtForHealth.getText().toString()) % 1) == 0)) {
                        if (editTxtForHealth.getText().toString().length() > 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllMedicalExpenses", editTxtForHealth.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("health", editTxtForHealth.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForHealth.getText().toString().length() == 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllMedicalExpenses", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("health", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForEducation.getText().toString().equals("")) && !(editTxtForEducation.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForEducation.getText().toString()) == 0.00)) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllEducationExpenses", editTxtForEducation.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("education", editTxtForEducation.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForEducation.getText().toString().equals("") || ((Double.parseDouble(editTxtForEducation.getText().toString()) % 1) == 0)) {
                        if (editTxtForEducation.getText().toString().length() > 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllEducationExpenses", editTxtForEducation.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("education", editTxtForEducation.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForEducation.getText().toString().length() == 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllEducationExpenses", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("education", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForUtilities.getText().toString().equals("")) && !(editTxtForUtilities.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForUtilities.getText().toString()) == 0.00)) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllUtilityBills", editTxtForUtilities.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("utilities", editTxtForUtilities.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForUtilities.getText().toString().equals("") || ((Double.parseDouble(editTxtForUtilities.getText().toString()) % 1) == 0)) {
                        if (editTxtForUtilities.getText().toString().length() > 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllUtilityBills", editTxtForUtilities.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("utilities", editTxtForUtilities.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForUtilities.getText().toString().length() == 0) {
                            needsStatus = true;
                            Utils.setInfo5Values("AllUtilityBills", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("utilities", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForShopping.getText().toString().equals("")) && !(editTxtForShopping.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForShopping.getText().toString()) == 0.00)) {
                            wantsStatus = true;
                            Utils.setInfo5Values("Shopping", editTxtForShopping.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("shopping", editTxtForShopping.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForShopping.getText().toString().equals("") || ((Double.parseDouble(editTxtForShopping.getText().toString()) % 1) == 0)) {
                        if (editTxtForShopping.getText().toString().length() > 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("Shopping", editTxtForShopping.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("shopping", editTxtForShopping.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForShopping.getText().toString().length() == 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("Shopping", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("shopping", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForEntrtnmnt.getText().toString().equals("")) && !(editTxtForEntrtnmnt.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForEntrtnmnt.getText().toString()) == 0.00)) {
                            wantsStatus = true;
                            Utils.setInfo5Values("AllLeisureEntertainment", editTxtForEntrtnmnt.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("entertainment", editTxtForEntrtnmnt.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForEntrtnmnt.getText().toString().equals("") || ((Double.parseDouble(editTxtForEntrtnmnt.getText().toString()) % 1) == 0)) {
                        if (editTxtForEntrtnmnt.getText().toString().length() > 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("AllLeisureEntertainment", editTxtForEntrtnmnt.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("entertainment", editTxtForEntrtnmnt.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForEntrtnmnt.getText().toString().length() == 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("AllLeisureEntertainment", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("entertainment", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForDining.getText().toString().equals("")) && !(editTxtForDining.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForDining.getText().toString()) == 0.00)) {
                            wantsStatus = true;
                            Utils.setInfo5Values("RestaurantsDiningOut", editTxtForDining.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("diningout", editTxtForDining.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForDining.getText().toString().equals("") || ((Double.parseDouble(editTxtForDining.getText().toString()) % 1) == 0)) {
                        if (editTxtForDining.getText().toString().length() > 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("RestaurantsDiningOut", editTxtForDining.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("diningout", editTxtForDining.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForDining.getText().toString().length() == 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("RestaurantsDiningOut", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("diningout", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForTravel.getText().toString().equals("")) && !(editTxtForTravel.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForTravel.getText().toString()) == 0.00)) {
                            wantsStatus = true;
                            Utils.setInfo5Values("HolidayTravelAccommodation", editTxtForTravel.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("travel", editTxtForTravel.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForTravel.getText().toString().equals("") || ((Double.parseDouble(editTxtForTravel.getText().toString()) % 1) == 0)) {
                        if (editTxtForTravel.getText().toString().length() > 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("HolidayTravelAccommodation", editTxtForTravel.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("travel", editTxtForTravel.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForTravel.getText().toString().length() == 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("HolidayTravelAccommodation", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("travel", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                try {
                    if (!(editTxtForOther.getText().toString().equals("")) && !(editTxtForOther.getText().toString().equals("0.00"))) {
                        if (!(Double.parseDouble(editTxtForOther.getText().toString()) == 0.00)) {
                            wantsStatus = true;
                            Utils.setInfo5Values("OtherLeisureEntertainment", editTxtForOther.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("other", editTxtForOther.getText().toString(), infoArrayListBackend);
                        }
                    } else if (editTxtForOther.getText().toString().equals("") || ((Double.parseDouble(editTxtForOther.getText().toString()) % 1) == 0)) {
                        if (editTxtForOther.getText().toString().length() > 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("OtherLeisureEntertainment", editTxtForOther.getText().toString(), infoArrayList);
                            Utils.setInfo5ValuesBackend("other", editTxtForOther.getText().toString(), infoArrayListBackend);
                        } else if (editTxtForOther.getText().toString().length() == 0) {
                            wantsStatus = true;
                            Utils.setInfo5Values("OtherLeisureEntertainment", "0.00", infoArrayList);
                            Utils.setInfo5ValuesBackend("other", "0.00", infoArrayListBackend);
                        }
                    }
                } catch (Exception e) {
                }

                if ((editTxtForRent.getText().toString().length() == 0 && editTxtForGroceries.getText().toString().length() == 0 && editTxtForTransport.getText().toString().length() == 0
                        && editTxtForHealth.getText().toString().length() == 0 && editTxtForEducation.getText().toString().length() == 0 && editTxtForUtilities.getText().toString().length() == 0
                        && editTxtForShopping.getText().toString().length() == 0 && editTxtForEntrtnmnt.getText().toString().length() == 0 && editTxtForDining.getText().toString().length() == 0
                        && editTxtForTravel.getText().toString().length() == 0 && editTxtForOther.getText().toString().length() == 0)) {
                    needsStatus = false;
                    wantsStatus = false;
                }

                if (SessionStores.getFinancialStep1(getActivity()) == null || SessionStores.getFinancialStep2(getActivity()) == null || SessionStores.getFinancialStep3(getActivity()) == null) {
                    resultBackend = "0";
                } else {
                    if (needsStatus == false && wantsStatus == false) {
                        resultBackend = "0";
                    } else {
                        resultBackend = "1";
                    }
                }

                FragmentManager fragmentManager = getFragmentManager();

                JSONArray jsonArray = new JSONArray();
                for (int i = 0; i < infoArrayList.size(); i++) {
                    try {
                        FinancialInfo5Model info5Model = infoArrayList.get(i);
                        JSONObject jsonobject_one = new JSONObject();
                        jsonobject_one.put("SubCategory", info5Model.getSubCategory());
                        jsonobject_one.put("Amount", info5Model.getAmount());
                        jsonArray.put(jsonobject_one);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


                JSONArray jsonArrayBackend = new JSONArray();
                for (int i = 0; i < infoArrayListBackend.size(); i++) {
                    try {
                        FinancialInfo5ModelBackend info5ModelBackend = infoArrayListBackend.get(i);
                        JSONObject jsonobject_one = new JSONObject();
                        jsonobject_one.put("SubCategory", info5ModelBackend.getSubCategory());
                        jsonobject_one.put("amount", info5ModelBackend.getAmount());
                        jsonArrayBackend.put(jsonobject_one);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                JSONObject jsonobject = new JSONObject();
                try {
                    jsonobject.put("user_email", SessionStores.getUserEmail(getActivity()));
                    jsonobject.put("result", resultBackend);
                    jsonobject.put("expenses", jsonArrayBackend);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String tmpProfileFinished = "not finished";
                if (infoArrayList.size() == 11) {
                    tmpProfileFinished = "finished";
                }
                //mixpanel save tracking
                mixpanelAPI.track(getString(R.string.mixpanel_financial_info_save4), Analitics.finInfo4completed(tmpProfileFinished));

                new FinancialInfo5Task(getActivity(), fragmentManager, jsonArray.toString(), jsonobject.toString());
            }

        });

        return v;
    }
}
