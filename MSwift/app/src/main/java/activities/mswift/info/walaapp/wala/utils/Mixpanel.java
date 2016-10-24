package activities.mswift.info.walaapp.wala.utils;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by kruno on 29.03.16..
 */
public class Mixpanel {

    public JSONObject signUpStep1(String email, String phone, String username, String password){
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
}
