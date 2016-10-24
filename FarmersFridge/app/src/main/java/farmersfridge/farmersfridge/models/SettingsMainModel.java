package farmersfridge.farmersfridge.models;

import android.util.Log;

import java.util.HashMap;

import farmersfridge.farmersfridge.utils.ApiUrlGenerator;

/**
 * Created by karuppiah on 6/14/2016.
 */
public class SettingsMainModel {

    private String name, ques, ans, feedBkEmail, feedBkTitle, feedBkMsg;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setQues(String ques) {
        this.ques = ques;
    }

    public String getQues() {
        return ques;
    }

    public void setAns(String ans) {
        this.ans = ans;
    }

    public String getAns() {
        return ans;
    }

    public SettingsMainModel(){

        this.name = "";
    }

    public SettingsMainModel(String fdbkEmail, String fdbkTitle, String fdbkMsg){
        this.feedBkEmail = fdbkEmail;
        this.feedBkTitle = fdbkTitle;
        this.feedBkMsg = fdbkMsg;
    }

    /**
     * Returns API call params for general/get_terms_and_conditions
     */
    public ApiCallParams termsConditions(){

        String endpoint= "general/get_terms_and_conditions";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }

    /**
     * Returns API call params for general/get_faq
     */
    public ApiCallParams faq(){

        String endpoint= "general/get_faq";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }

    /**
     * Returns API call params for general/send_feedback
     */
    public ApiCallParams feedback(){

        String endpoint= "general/send_feedback";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("email", ""+feedBkEmail);
        params.put("title", ""+feedBkTitle);
        params.put("message", ""+feedBkMsg);

        return new ApiCallParams(params, url, endpoint);
    }

    /**
     * Returns API call params for GET general/get_about_us
     */
    public ApiCallParams aboutUs(){

        String endpoint= "general/get_about_us";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }
}
