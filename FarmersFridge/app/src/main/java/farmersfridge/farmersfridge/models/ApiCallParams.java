package farmersfridge.farmersfridge.models;

import java.util.HashMap;

/**
 * Created by karuppiah on 6/15/2016.
 */
public class ApiCallParams {

    private HashMap<String, String> params;
    private HashMap<String, Object> paramsObj;
    private String url;
    private String tag;

    public String getTag() {
        return tag;
    }

    public HashMap<String, String> getParams() {
        return params;
    }

    public String getUrl() {
        return url;
    }

    public ApiCallParams(HashMap<String, String> params, String url, String tag) {

        this.params = params;
        this.url = url;
        this.tag = tag;
    }

    public ApiCallParams(HashMap<String, Object> paramsObj, String url, String tag, String name) {

        this.paramsObj = paramsObj;
        this.url = url;
        this.tag = tag;
    }
}
