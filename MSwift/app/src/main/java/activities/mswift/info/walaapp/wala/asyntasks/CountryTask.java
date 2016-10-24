package activities.mswift.info.walaapp.wala.asyntasks;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import activities.mswift.info.walaapp.wala.model.CountryModel;
import activities.mswift.info.walaapp.wala.support.ServerResponse;
import activities.mswift.info.walaapp.wala.support.VolleyResponseListener;
import activities.mswift.info.walaapp.wala.utils.Constants;
import activities.mswift.info.walaapp.wala.utils.UrlGenerator;

/**
 * Created by karuppiah on 12/3/2015.
 */
public class CountryTask extends AsyncTask<Void, Void, Boolean> {
    private Context context;
    private String url, result;
    private JSONObject jObject = null;

    public CountryTask(Context context) {
        this.context = context;
    }

    @Override
    protected Boolean doInBackground(Void... voids) {

        new ServerResponse(UrlGenerator.getCountries()).getJSONObjectfromURL(ServerResponse.RequestType.GET, null, null, context, "", new VolleyResponseListener() {
            @Override
            public void onError(String message) {
                Toast.makeText(context, "" + message, Toast.LENGTH_LONG).show();
            }

            @Override
            public void onResponse(String response) {
                result = response.toString();

                try {
                    jObject = new JSONObject(result);
                    JSONArray jsonArray = jObject.getJSONArray("country");
                    Log.e("array length", "" + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        String countryId = jsonArray.getJSONObject(i).getString("country_id");
                        String countryName = jsonArray.getJSONObject(i).getString("name");
                        CountryModel countryModel = new CountryModel(); // Model class
                        countryModel.setId(countryId);
                        countryModel.setName(countryName);
                        Constants.COUNTRY_ARRAY.add(countryModel);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        return null;
    }
}
