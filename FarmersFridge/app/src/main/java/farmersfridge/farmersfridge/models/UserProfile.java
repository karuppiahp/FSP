package farmersfridge.farmersfridge.models;

import android.util.Log;

import java.util.HashMap;

import farmersfridge.farmersfridge.utils.ApiUrlGenerator;

/**
 * Created by karuppiah on 6/15/2016.
 */
public class UserProfile {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String code;

    public void setFname(String firstName) {
        this.firstName = firstName;
    }

    public String getFname() {
        return firstName;
    }

    public void setLname(String lastName) {
        this.lastName = lastName;
    }

    public String getLname() {
        return lastName;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }

    public UserProfile(){
        this.firstName = "";
        this.lastName = "";
        this.email = "";
        this.phone = "";
    }

    public UserProfile(String firstName, String lastName, String phone, String email){
        this.firstName = firstName;
        this.lastName = lastName;
        this.phone = phone;
        this.email = email;
    }

    public UserProfile(String phone){
        this.phone = phone;
    }

    /**
     * Returns API call params for general/register
     */
    public ApiCallParams register(){

        String endpoint= "general/register";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("first_name", ""+firstName);
        params.put("last_name", ""+lastName);
        params.put("phone_number", "+"+phone);
        params.put("email", ""+email);

        return new ApiCallParams(params, url, endpoint);
    }

    /**
     * Returns API call params for clients/activate_account
     */
    public ApiCallParams activateToken(){

        String endpoint= "clients/activate_account";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("confirmation_code", ""+code);

        return new ApiCallParams(params, url, endpoint);
    }

    /**
     * Returns API call params for /general/get_session_code/{phone}
     */
    /*public ApiCallParams getSessionCode(Context context){

        String endpoint= "general/get_session_code/%2B" + new SessionStores(context).getUserPhone();
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        return new ApiCallParams(null, url, endpoint);
    }*/

    /**
     * Returns API call params for general/start_session
     */
    public ApiCallParams startSession(){

        String endpoint= "general/start_session";
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("confirmation_code", ""+code);

        return new ApiCallParams(params, url, endpoint);
    }

    /**
     * Returns API call params for general/get_session_code/
     */
    public ApiCallParams login(){

        String endpoint= "general/get_session_code/%2B" + phone;
        String url = ApiUrlGenerator.getApiUrl(endpoint);

        return new ApiCallParams(null, url, endpoint);
    }

    /**
     * Returns API call params for clients/get_profile_data
     */
    public ApiCallParams getProfileDate(){

        String endpoint= "clients/get_profile_data";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, endpoint);
    }

    /**
     * Returns API call params for clients/update_profile_data
     */
    public ApiCallParams updateProfileDate(){

        String endpoint= "clients/update_profile_data";
        String url = ApiUrlGenerator.getApiUrl(endpoint);
        Log.e("Url>>", "" + url);

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("first_name", "" + firstName);
        params.put("last_name", "" + lastName);

        return new ApiCallParams(params, url, endpoint);
    }
}
