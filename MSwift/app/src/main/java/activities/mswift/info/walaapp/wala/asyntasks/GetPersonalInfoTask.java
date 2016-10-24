package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import activities.mswift.info.walaapp.wala.more.EditPersonalInfo;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.tabhost.TabHostFragments;
import activities.mswift.info.walaapp.wala.utils.ApiClass;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.SessionStores;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 1/12/2016.
 */
public class GetPersonalInfoTask {
    private Context context;
    private JSONObject jObject = null;
    private String authorizationKey, result = "", status = "", message = "", firtName, lastName, middleName, email, dob, gender, country, city, dateChanged;
    private static SimpleDateFormat sdf;
    private static Date ddObj;

    public GetPersonalInfoTask(Context context) {
        this.context = context;
        ((TabHostFragments) context).progressBarVisible();
        ResponseTask();
    }

    public void ResponseTask() {
        authorizationKey = "Bearer " + SessionStores.getAccessToken(context);
        new ServerResponse(ApiClass.getApiUrl(Constants.GET_PERSONALINFO) + SessionStores.getUserEmail(context)).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, authorizationKey, context, SessionStores.getInstallationId(context), new VolleyResponseListener() {
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

                            JSONObject dataObject = jObject.getJSONObject("DataObject");
                            if (dataObject.has("FirstName")) {
                                firtName = dataObject.getString("FirstName");
                            }
                            if (dataObject.has("LastName")) {
                                lastName = dataObject.getString("LastName");
                            }
                            if (dataObject.has("MiddleName")) {
                                middleName = dataObject.getString("MiddleName");
                            }
                            if (dataObject.has("Email")) {
                                email = dataObject.getString("Email");
                            }
                            if (dataObject.has("DateOfBirth")) {
                                dob = dataObject.getString("DateOfBirth");
                            }
                            if (dataObject.has("Gender")) {
                                gender = dataObject.getString("Gender");
                            }
                            country = SessionStores.getCountry(context);
                            city = SessionStores.getCity(context);

                            dateChanged = Utils.dateTimeFormatChange("yyyy-MM-dd HH:mm:ss", "dd/MM/yyyy", dob.replace("T", " "));

                            EditPersonalInfo.editTxtForFirstName.setText(firtName);
                            EditPersonalInfo.editTxtForMiddleName.setText(middleName);
                            EditPersonalInfo.editTxtForLastName.setText(lastName);
                            EditPersonalInfo.txtForDob.setText(dateChanged);
                            EditPersonalInfo.editTxtForEmail.setText(email);
                            if (country.length() > 0)
                                EditPersonalInfo.txtForCountry.setText(country);
                            if (city.length() > 0)
                                EditPersonalInfo.editTxtForCity.setText(city);

                            new GetContactInfoTask(context);
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

    public static String ddMMMMYYYYFormat(String ipDate) {
        String s = "";
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            ddObj = sdf.parse(ipDate);
            System.out.println(ddObj);
            s = new SimpleDateFormat("dd-MM-yyyy").format(ddObj);
            System.out.println(s);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;

    }
}
