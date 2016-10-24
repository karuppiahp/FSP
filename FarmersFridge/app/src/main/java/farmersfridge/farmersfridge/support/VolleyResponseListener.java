package farmersfridge.farmersfridge.support;

/**
 * Created by karuppiah on 6/13/2016.
 */
public interface VolleyResponseListener {
    void onError(String message);

    void onResponse(String response);
}
