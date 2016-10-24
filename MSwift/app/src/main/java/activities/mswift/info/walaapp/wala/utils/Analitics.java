package activities.mswift.info.walaapp.wala.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kruno on 29.03.16..
 */
public class Analitics {

    public static JSONObject signUpStep1(String email, String phone, String username, String password) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("email", email);
            jsonObject.put("phone", phone);
            jsonObject.put("username", username);
            jsonObject.put("password", password);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public static JSONObject signUpFinishButton(String firstName, String midleName, String lastName, String country, String city, String dateOfBirth) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("firstName", firstName);
            jsonObject.put("midleName", midleName);
            jsonObject.put("lastName", lastName);
            jsonObject.put("country", country);
            jsonObject.put("city", city);
            jsonObject.put("dateOfBirth", dateOfBirth);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject finInfo4completed(String profile) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("finStep4", profile);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject balanceTimeSpan(String startDate, String endDate) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("startDate", startDate);
            jsonObject.put("endDate", endDate);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject editTransaction(String screen, String item) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("screen", screen);
            jsonObject.put("item", item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject addTransaction(String item) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item", item);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }

    public static JSONObject saveTransaction(String item, String amount, String date, int transactionID) {

        String tmpCathegory = "";
        String tmpAggregated = "";

        if (transactionID < 11) {
            tmpAggregated = "Expenses";

            if (transactionID < 6) {
                tmpCathegory = "Needs";
            } else if (transactionID < 11) {
                tmpCathegory = "Wants";
            }

        } else {
            tmpAggregated = "Income & Payment";

            if (transactionID == 11) {
                tmpCathegory = "Income";
            } else if (transactionID == 12) {
                tmpCathegory = "New Loan";
            } else if (transactionID == 13) {
                tmpCathegory = "Pay off loan";
            }
        }

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("item", item);
            jsonObject.put("amount", amount);
            jsonObject.put("date", date);
            jsonObject.put("aggregated", tmpAggregated);
            jsonObject.put("cathegory", tmpCathegory);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return jsonObject;
    }
}
