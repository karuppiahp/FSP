package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.CountryModel;
import activities.mswift.info.walaapp.wala.model.GenderModel;
import activities.mswift.info.walaapp.wala.utils.Constants;

/**
 * Created by karuppiah on 12/3/2015.
 */
public class SpinnerListAdapter extends BaseAdapter {

    private Context context;
    private String type;

    public SpinnerListAdapter(Context context, String type) {
        this.context = context;
        this.type = type;
    }

    @Override
    public int getCount() {
        if (type.equals("country")) {
            return Constants.COUNTRY_ARRAY.size();
        } else {
            return Constants.GENDER_ARRAY.size();
        }
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View v, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        v = inflater.inflate(R.layout.listview_text_item, null);
        TextView textView = (TextView) v.findViewById(R.id.txtForListItem);
        if (type.equals("country")) {
            CountryModel countryModel = Constants.COUNTRY_ARRAY.get(i);
            textView.setText(countryModel.getName());
        } else {
            GenderModel genderModel = Constants.GENDER_ARRAY.get(i);
            textView.setText(genderModel.getName());
        }
        return v;
    }
}
