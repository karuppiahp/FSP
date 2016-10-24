package farmersfridge.farmersfridge.support;

import android.content.Context;
import android.net.http.AndroidHttpClient;
import android.os.Build;
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
import com.android.volley.toolbox.HttpClientStack;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

import farmersfridge.farmersfridge.utils.Constants;
import farmersfridge.farmersfridge.utils.SessionStores;

/**
 * Created by karuppiah on 6/13/2016.
 */
public class ServerResponse {
    public static final String tag = "ServerResponse";

    private static String url, result;

    private static JsonObjectRequest jsonObjectRequest;

    private static JsonArrayRequest jsonArrayRequest;

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
    public static String getJSONObjectfromURL(RequestType requestType, final Map<String, String> params, final Context context, final VolleyResponseListener listener) {
        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19){
            httpStack = new CustomHurlStack();
        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
        {
            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        if (queue == null) {
            queue = Volley.newRequestQueue(context, httpStack);
        }
        //GET method
        if (requestType == RequestType.GET) {
            jsonObjectRequest = new JsonObjectRequest(Request.Method.GET,
                    url, "",
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { //Success response
                            result = response.toString();
                            listener.onResponse(result);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() { //Error response

                        @Override
                        public void onErrorResponse(VolleyError error) {
                            if(error instanceof NoConnectionError) {
                                String jsonMessage = "{\n" +
                                        "\t\"message\": \"No internet Access, Check your internet connection.\"\n" +
                                        "}";
                                listener.onError(jsonMessage);
                            }
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if(response != null && response.data != null) {
                                switch (response.statusCode) {
                                    case 400:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        listener.onError(json);
                                        break;

                                    case 401:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        listener.onError(json);
                                        break;

                                    case 403:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        listener.onError(json);
                                        break;

                                    case 404:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        listener.onError(json);
                                        break;

                                    case 422:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
                                        listener.onError(json);
                                        break;

                                    case 500:
                                        json = new String(response.data);
                                        Log.e("json", "json" + json);
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
                public Map<String, String> getHeaders() throws AuthFailureError { //Headaers params
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                    params.put("Content-Type", "application/json");
                    params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());
                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) { //Response from service
                    try {
                        Map<String, String> responseHeaders = response.headers;
                        Constants.API_DATE = response.headers.get("Last-Modified");
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        if(jsonString.contains("<") && jsonString.contains("</")) { //if response has tags
                            //xml converts to JSON
                            JSONObject soapDatainJsonObject = XML.toJSONObject(jsonString);
                            return Response.success(soapDatainJsonObject,
                                    HttpHeaderParser.parseCacheHeaders(response));
                        } else { //else json response
                            return Response.success(new JSONObject(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(response));
                        }
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }
        //POST method
        if (requestType == RequestType.POST) {
            JSONObject jsonObj = null;
            if(params!= null) {
                jsonObj = new JSONObject(params);
            }
            jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                    url, jsonObj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { //Success response
                            result = response.toString();
                            listener.onResponse(result);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                        @Override
                        public void onErrorResponse(VolleyError error) { //Error response
                            if(error instanceof NoConnectionError) {
                                String jsonMessage = "{\n" +
                                        "\t\"message\": \"No internet Access, Check your internet connection.\"\n" +
                                        "}";
                                listener.onError(jsonMessage);
                            }
                            String json = null;
                            NetworkResponse response = error.networkResponse;
                            if(response != null && response.data != null){
                                switch(response.statusCode){
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //Header params
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                    params.put("Content-Type", "application/json");
                    params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());
                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) { //response added to listenr
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        //if response contains only true or false
                        if (jsonString.equalsIgnoreCase("true") || jsonString.equalsIgnoreCase("false")) {
                            if (jsonString.equalsIgnoreCase("true")) {
                                jsonString = "{ \"result\": \"true\" }";
                            } else {
                                jsonString = "{ \"result\": \"false\" }";
                            }

                        }

                        if(jsonString.contains("<") && jsonString.contains("</")) { //if respons ehas XML tags
                            JSONObject soapDatainJsonObject = XML.toJSONObject(jsonString);
                            return Response.success(soapDatainJsonObject,
                                    HttpHeaderParser.parseCacheHeaders(response));
                        } else { //else JSON response
                            return Response.success(new JSONObject(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(response));
                        }
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }
        //PUT method
        if (requestType == RequestType.PUT) {
            JSONObject jsonObj = null;
            if(params!= null) {
                jsonObj = new JSONObject(params);
            }
            jsonObjectRequest = new JsonObjectRequest(Request.Method.PUT,
                    url, jsonObj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { //Success response
                            result = response.toString();
                            listener.onResponse(result);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) { //Error response
                    if(error instanceof NoConnectionError) {
                        String jsonMessage = "{\n" +
                                "\t\"message\": \"No internet Access, Check your internet connection.\"\n" +
                                "}";
                        listener.onError(jsonMessage);
                    }
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null){
                        switch(response.statusCode){
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //Headers params
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                    params.put("Content-Type", "application/json");
                    params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());
                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        //If response contains true or false only
                        if (jsonString.equalsIgnoreCase("true") || jsonString.equalsIgnoreCase("false")) {
                            if (jsonString.equalsIgnoreCase("true")) {
                                jsonString = "{ \"result\": \"true\" }";
                            } else {
                                jsonString = "{ \"result\": \"false\" }";
                            }

                        }

                        if(jsonString.contains("<") && jsonString.contains("</")) { //if response contains xml tags
                            JSONObject soapDatainJsonObject = XML.toJSONObject(jsonString);
                            return Response.success(soapDatainJsonObject,
                                    HttpHeaderParser.parseCacheHeaders(response));
                        } else { //else JSON response
                            return Response.success(new JSONObject(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(response));
                        }
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }

        //Delete method
        if (requestType == RequestType.DELETE) {
            JSONObject jsonObj = null;
            if(params!= null) {
                jsonObj = new JSONObject(params);
            }
            jsonObjectRequest = new JsonObjectRequest(Request.Method.DELETE,
                    url, jsonObj,
                    new Response.Listener<JSONObject>() {

                        @Override
                        public void onResponse(JSONObject response) { //Success response
                            result = response.toString();
                            listener.onResponse(result);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) { //Error response
                    if(error instanceof NoConnectionError) {
                        String jsonMessage = "{\n" +
                                "\t\"message\": \"No internet Access, Check your internet connection.\"\n" +
                                "}";
                        listener.onError(jsonMessage);
                    }
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    //Please comment the following response if loop once the proper response is available
                    if(response == null) {
                        json = "{ \"result\": \"true\" }";
                        listener.onError(json);
                    }
                    if(response != null && response.data != null){
                        switch(response.statusCode){
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //Header params
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                    params.put("Content-Type", "application/json");
                    params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());
                    return params;
                }

                @Override
                protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        //if response contains true or false
                        if (jsonString.equalsIgnoreCase("true") || jsonString.equalsIgnoreCase("false")) {
                            if (jsonString.equalsIgnoreCase("true")) {
                                jsonString = "{ \"result\": \"true\" }";
                            } else {
                                jsonString = "{ \"result\": \"false\" }";
                            }

                        }
                        if(jsonString.contains("<") && jsonString.contains("</")) { //if response contains xml tags
                            JSONObject soapDatainJsonObject = XML.toJSONObject(jsonString);
                            return Response.success(soapDatainJsonObject,
                                    HttpHeaderParser.parseCacheHeaders(response));
                        } else { //else json response
                            return Response.success(new JSONObject(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(response));
                        }
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }

        jsonObjectRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,0,1f));

        queue.add(jsonObjectRequest);

        return requestString;
    }

    public static String trimMessage(String json, String key){
        String trimmedString = null;

        try{
            JSONObject obj = new JSONObject(json);
            trimmedString = obj.getString(key);
        } catch(JSONException e){
            e.printStackTrace();
            return null;
        }

        return trimmedString;
    }

    /*
     * To return JSONArrya type response the following method has been used.
     */
    public static String getJSONArrayfromURL(RequestType requestType, final Map<String, String> params, final Context context, final VolleyResponseListener listener) {
        HttpStack httpStack;
        if (Build.VERSION.SDK_INT > 19){
            httpStack = new CustomHurlStack();
        } else if (Build.VERSION.SDK_INT >= 9 && Build.VERSION.SDK_INT <= 19)
        {
            httpStack = new OkHttpHurlStack();
        } else {
            httpStack = new HttpClientStack(AndroidHttpClient.newInstance("Android"));
        }
        if (queue == null) {
            queue = Volley.newRequestQueue(context, httpStack);
        }

        if (requestType == RequestType.GET) {
            jsonArrayRequest = new JsonArrayRequest(Request.Method.GET,
                    url, "",
                    new Response.Listener<JSONArray>() {

                        @Override
                        public void onResponse(JSONArray response) { //Success response
                            result = response.toString();
                            listener.onResponse(result);
                            requestString = "success";
                        }
                    }, new Response.ErrorListener() {

                @Override
                public void onErrorResponse(VolleyError error) { //Error response
                    if(error instanceof NoConnectionError) {
                        String jsonMessage = "{\n" +
                                "\t\"message\": \"No internet Access, Check your internet connection.\"\n" +
                                "}";
                        listener.onError(jsonMessage);
                    }
                    String json = null;
                    NetworkResponse response = error.networkResponse;
                    if(response != null && response.data != null) {
                        switch (response.statusCode) {
                            case 400:
                                json = new String(response.data);
                                Log.e("json", "json" + json);
                                listener.onError(json);
                                break;

                            case 401:
                                json = new String(response.data);
                                Log.e("json", "json" + json);
                                listener.onError(json);
                                break;

                            case 403:
                                json = new String(response.data);
                                Log.e("json", "json" + json);
                                listener.onError(json);
                                break;

                            case 404:
                                json = new String(response.data);
                                Log.e("json", "json" + json);
                                listener.onError(json);
                                break;

                            case 422:
                                json = new String(response.data);
                                Log.e("json", "json" + json);
                                listener.onError(json);
                                break;

                            case 500:
                                json = new String(response.data);
                                Log.e("json", "json" + json);
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
                public Map<String, String> getHeaders() throws AuthFailureError {
                    //Header params
                    Map<String, String>  params = new HashMap<String, String>();
                    params.put("x-api-key", "iyiGklK5onMLiL8ag29h4atrKJJjukJ8Aq6X6id6");
                    params.put("Content-Type", "application/json");
                    params.put("x-access-token", ""+ new SessionStores(context).getAccessToken());
                    return params;
                }

                @Override
                protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
                    try {
                        String jsonString = new String(response.data,
                                HttpHeaderParser.parseCharset(response.headers));
                        if(jsonString.contains("<") && jsonString.contains("</")) { //if response contains XML tags
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(response));
                        } else { //else JSON response
                            return Response.success(new JSONArray(jsonString),
                                    HttpHeaderParser.parseCacheHeaders(response));
                        }
                    } catch (JSONException je) {
                        return Response.error(new ParseError(je));
                    } catch (UnsupportedEncodingException e) {
                        return Response.error(new ParseError(e));
                    }
                }
            };
        }

        jsonArrayRequest.setRetryPolicy(new DefaultRetryPolicy(20 * 1000,0,1f));

        queue.add(jsonArrayRequest);

        return requestString;
    }

    public static enum RequestType {
        GET, POST, PUT, DELETE, GET_ARRAY
    }
}
