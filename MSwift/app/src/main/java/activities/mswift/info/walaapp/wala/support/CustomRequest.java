package activities.mswift.info.walaapp.wala.support;

/**
 * Created by karuppiah on 1/7/2016.
 */

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by NGUYEN on 1/7/2016.
 */
public class CustomRequest extends JsonRequest<JSONObject> {

    // Since we're extending a Request class
    // we just use its constructor
    public CustomRequest(int method, String url, String jsonRequest, final String authorizationKey, final String installationId,
                         Response.Listener<JSONObject> listener, Response.ErrorListener errorListener) {
        super(method, url, jsonRequest, listener, errorListener);
        headers.clear();
        Log.e("Url???", "" + url);
        Log.e("jsonRequest???", "" + jsonRequest);
        // Change this value with latest user access token
        headers.put("Authorization", authorizationKey);
        headers.put("AppId", "WalaAndroidApp");
        headers.put("DeviceId", "132432345332456578798");
        headers.put("InstallationId", installationId);
        headers.put("Content-Type", "application/json; charset=UTF-8");
    }

    private Map<String, String> headers = new HashMap<>();

    /**
     * Custom class!
     */
    public void setCookies(List<String> cookies) {
        StringBuilder sb = new StringBuilder();
        for (String cookie : cookies) {
            sb.append(cookie).append("; ");
        }
        headers.put("Cookie", sb.toString());
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
    }

    @Override
    protected Response<JSONObject> parseNetworkResponse(NetworkResponse response) {
        try {
            String jsonString = new String(response.data,
                    HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
            return Response.success(new JSONObject(jsonString),
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException je) {
            return Response.error(new ParseError(je));
        }
    }
}


