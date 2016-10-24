package activities.mswift.info.walaapp.wala.support;

import org.json.JSONObject;

/**
 * Created by karuppiah on 2/10/2016.
 */
public interface VolleyResponseDummy {
    void onError(String message);

    void onResponse(JSONObject response);
}
