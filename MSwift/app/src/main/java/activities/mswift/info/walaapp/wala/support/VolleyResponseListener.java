package activities.mswift.info.walaapp.wala.support;

/**
 * Created by karuppiah on 12/4/2015.
 */
public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(String response);
}
