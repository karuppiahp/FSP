package farmersfridge.farmersfridge.models;

import android.util.Log;

/**
 * Created by karuppiah on 6/21/2016.
 */
public class XmlParserModel {

    private String url;

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUrl() {
        return url;
    }

    public XmlParserModel(){
        this.url = "";
    }

    public XmlParserModel(String url){
        this.url = url;
    }

    public ApiCallParams xmlLinkParse(){

        /*String endpoint= "general/get_terms_and_conditions";
        String url = ApiUrlGenerator.getApiUrl(endpoint);*/
        Log.e("Url>>", "" + url);

        return new ApiCallParams(null, url, " ");
    }
}
