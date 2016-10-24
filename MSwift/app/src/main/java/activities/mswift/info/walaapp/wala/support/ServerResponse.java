package activities.mswift.info.walaapp.wala.support;

import android.content.Context;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by karuppiah on 12/2/2015.
 */
public class ServerResponse {
    public static final String tag = "ServerResponse";

    private static String url, result;

    private JSONObject jObject = null;

    private static StringRequest jsonObjectRequest;

    private static String requestString = "fail";

    private static RequestQueue queue;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    public ServerResponse(String url) {
        this.url = url;
        Log.i("Url ->", "" + url);
    }

    /*
     * To return JSONObject type response the following method has been used.
     */
    public static String getJSONObjectfromURL(RequestType requestType, final Map<String, String> params, final String authorizationKey, Context context, final String installationId, final VolleyResponseListener listener) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        if (requestType == RequestType.GET) {
            jsonObjectRequest = new StringRequest
                    (Request.Method.GET, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            listener.onResponse(response);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        listener.onError(error.toString());
                                        break;

                                    case 401:
                                        json = new String(response.data);
                                        json = trimMessage(json, "message");
                                        listener.onError(error.toString());
                                        break;
                                }
                                //Additional cases
                            }
                        }
                    }) {

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("Authorization", authorizationKey);
                    params.put("AppId", "WalaAndroidApp");
                    params.put("DeviceId", "132432345332456578798");
                    params.put("InstallationId", installationId);
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }
        if (requestType == RequestType.POST) {
            jsonObjectRequest = new StringRequest
                    (Request.Method.POST, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) {
                            listener.onResponse(response);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if (error instanceof NoConnectionError) {
                                listener.onError("No internet Access, Check your internet connection.");
                            }
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        json = trimMessage(json, "error_description");
                                        listener.onError(json);
                                        break;

                                    case 401:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        json = trimMessage(json, "error_description");
                                        listener.onError(json);
                                        break;
                                }
                                //Additional cases
                            }
                        }
                    }) {

                @Override
                public Priority getPriority() {
                    return Priority.IMMEDIATE;
                }

                @Override
                protected Map<String, String> getParams() {

                    return params;
                }

                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("Content-Type", "application/x-www-form-urlencoded");
                    params.put("Authorization", authorizationKey);
                    params.put("AppId", "WalaAndroidApp");
                    params.put("DeviceId", "132432345332456578798");
                    params.put("InstallationId", installationId);
                    return params;
                }

                @Override
                protected Response<String> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        return Response.success(jsonString, HttpHeaderParser.parseCacheHeaders(response));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000, 0, 1f));

        queue.add(jsonObjectRequest);

        // Access the RequestQueue through singleton class.
        //    VolleySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);
        return requestString;
    }

    public static String trimMessage(String json, String key) {
        String trimmedString = null;

        try {
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    public static enum RequestType {
        GET, POST
    }
}