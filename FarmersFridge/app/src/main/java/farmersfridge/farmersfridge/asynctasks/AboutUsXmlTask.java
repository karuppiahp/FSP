package farmersfridge.farmersfridge.asynctasks;

import android.content.Context;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import farmersfridge.farmersfridge.about_us.AboutUs;
import farmersfridge.farmersfridge.fragment.FragmentMain;
import farmersfridge.farmersfridge.models.AboutUsParaModel;
import farmersfridge.farmersfridge.models.AboutUsSectionModel;
import farmersfridge.farmersfridge.models.AboutUsSubHeaderModel;
import farmersfridge.farmersfridge.models.ApiCallParams;
import farmersfridge.farmersfridge.support.ServerResponse;
import farmersfridge.farmersfridge.support.VolleyResponseListener;
import farmersfridge.farmersfridge.utils.Utils;

/**
 * Created by karuppiah on 9/1/2016.
 */
public class AboutUsXmlTask {
    private Context context;
    private String result = "", heading = "", headingSub = "", header = "", paraContent = "";
    private JSONObject jObject = null;
    private ApiCallParams apiCallParams;
    private AboutUs aboutUs;

    public AboutUsXmlTask(Context context, AboutUs aboutUs, ApiCallParams apiCallParams) {
        this.context = context;
        this.apiCallParams = apiCallParams;
        this.aboutUs = aboutUs;
        ResponseTask();
    }

    public void ResponseTask() {
        new ServerResponse(apiCallParams.getUrl()).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, context, new VolleyResponseListener() {
            @Override
            public void onError(String message) { //Error response
                try {
                    ((FragmentMain) context).setProgressBarGone();
                    jObject = new JSONObject(message.toString());
                    if (jObject != null) {
                        if(jObject.has("message")) {
                            Utils.ShowAlert(context, jObject.getString("message"));
                        }
                    } else {
                        Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }

            /*
             * In Success Response the values are fetched and stored as dynamic by separates as
             *  1. Heading
             *  2. Section array -> Array items separate as heading, subheading
             *  2.a. subheading(JSON Object) -> if sub heading has array it separates as header and the paragraph
             *  2.b. subheading(JSON Array) -> it separates heading and paragraph
             */
            @Override
            public void onResponse(String response) { //Success Response
                result = response.toString();
                try {
                    jObject = new JSONObject(result);
                    ArrayList<AboutUsSubHeaderModel> subHeaderArrayList = new ArrayList<>();
                    if (jObject != null) {
                        JSONObject txtObj = jObject.getJSONObject("text");
                        heading = txtObj.getString("heading");
                        JSONArray sectionArray = txtObj.getJSONArray("section");
                        for(int i=0; i<sectionArray.length(); i++) {
                            headingSub = sectionArray.getJSONObject(i).getString("heading");
                            if(sectionArray.getJSONObject(i).has("subheading")) {
                                Object subHeading = sectionArray.getJSONObject(i).get("subheading");
                                if(subHeading instanceof JSONObject) { // Object
                                    JSONObject subHeadingObj = sectionArray.getJSONObject(i).getJSONObject("subheading");
                                    if(subHeadingObj.has("header")) {
                                        header = subHeadingObj.getString("header");
                                    } else {
                                        header = "";
                                    }
                                    JSONArray paraArray = subHeadingObj.getJSONArray("p");
                                    ArrayList<AboutUsParaModel> paraArrayList = new ArrayList<>();
                                    for(int j=0; j<paraArray.length(); j++) {
                                        paraContent = paraArray.get(j).toString();
                                        AboutUsParaModel paraModel = new AboutUsParaModel();
                                        paraModel.setHeader(header);
                                        paraModel.setContent(paraContent);
                                        paraArrayList.add(paraModel);
                                        header = "";
                                    }

                                    AboutUsSubHeaderModel subHeaderModel = new AboutUsSubHeaderModel();
                                    subHeaderModel.setSubHeader(headingSub);
                                    subHeaderModel.setParrayList(paraArrayList);
                                    subHeaderArrayList.add(subHeaderModel);
                                } else if(subHeading instanceof JSONArray) { //Array
                                    JSONArray subHeadingArray = sectionArray.getJSONObject(i).getJSONArray("subheading");
                                    ArrayList<AboutUsParaModel> paraArrayList = new ArrayList<>();
                                    for(int j=0; j<subHeadingArray.length(); j++) {
                                        header = subHeadingArray.getJSONObject(j).getString("header");
                                        paraContent = subHeadingArray.getJSONObject(j).getString("p");
                                        AboutUsParaModel paraModel = new AboutUsParaModel();
                                        paraModel.setHeader(header);
                                        paraModel.setContent(paraContent);
                                        paraArrayList.add(paraModel);
                                    }

                                    AboutUsSubHeaderModel subHeaderModel = new AboutUsSubHeaderModel();
                                    subHeaderModel.setSubHeader(headingSub);
                                    subHeaderModel.setParrayList(paraArrayList);
                                    subHeaderArrayList.add(subHeaderModel);
                                    headingSub = "";
                                }
                            }
                        }

                        AboutUsSectionModel sectionModel = new AboutUsSectionModel();
                        sectionModel.setHeader(heading);
                        sectionModel.setSubHeaderList(subHeaderArrayList);

                        aboutUs.setViews(sectionModel.getHeader(), sectionModel.getSubHeaderList());
                    }

                    ((FragmentMain) context).setProgressBarGone();
                } catch (JSONException e) {
                    ((FragmentMain) context).setProgressBarGone();
                    e.printStackTrace();
                }
            }
        });
    }
}
