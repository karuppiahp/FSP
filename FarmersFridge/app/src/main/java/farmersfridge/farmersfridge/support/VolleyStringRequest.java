package farmersfridge.farmersfridge.support;

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

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 7/5/2016.
 */
public class VolleyStringRequest {
    public static final String tag = "ServerResponse";

    private static String url, result;

    private JSONObject jObject = null;

    private static StringRequest jsonObjectRequest;

    private static String requestString = "fail";

    private static RequestQueue queue;

    // These tags will be used to cancel the requests
    private String tag_json_obj = "jobj_req", tag_json_arry = "jarray_req";

    public VolleyStringRequest(String url) {
        this.url = url;
        Log.i("Url ->", "" + url);
    }

    /*
     * To return JSONObject type response the following method has been used.
     */
    public static String getJSONObjectfromURL(RequestType requestType, final Map<String, String> params, final Context context, final VolleyResponseListener listener) {
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }
        if (requestType == RequestType.DELETE) {
            Log.e("Total params size", "" + params.size());
            jsonObjectRequest = new StringRequest
                    (Request.Method.DELETE, url, new Response.Listener<String>() {

                        @Override
                        public void onResponse(String response) { //Success response
                            listener.onResponse(response);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) { //Error response
                            if (error instanceof NoConnectionError) {
                                listener.onError("No internet Access, Check your internet connection.");
                            }
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if (response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        Log.e("json","json"+json);
                                        listener.onError(json);
                                        break;

                                    case 401:
                                        json = new String(response.data);
                                        Log.e("json","json"+json);
                                        listener.onError(json);
                                        break;

                                    case 403:
                                        json = new String(response.data);
                                        Log.e("json","json"+json);
                                        listener.onError(json);
                                        break;

                                    case 404:
                                        json = new String(response.data);
                                        Log.e("json","json"+json);
                                        listener.onError(json);
                                        break;

                                    case 422:
                                        json = new String(response.data);
                                        Log.e("json","json"+json);
                                        listener.onError(json);
                                        break;

                                    case 500:
                                        json = new String(response.data);
                                        Log.e("json","json"+json);
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
                    //Header params
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                    params.put("Content-Type", "application/json");
                    params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());

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

        return requestString;
    }

    public static enum RequestType {
        GET, POST, DELETE
    }
}
