package farmersfridge.farmersfridge.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import farmersfridge.farmersfridge.R;
import farmersfridge.farmersfridge.models.SettingsMainModel;

/**
 * Created by karuppiah on 6/14/2016.
 */

/*
 * Adapter for Settings items it contains
 * name text
 */
public class SettingsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<SettingsMainModel> settingsArray = new ArrayList<>();
    @BindView(R.id.txtForSettingsItem)
    TextView txtForName;

    public SettingsAdapter(Context context, ArrayList<SettingsMainModel> settingsArray) {
        this.context = context;
        this.settingsArray = settingsArray;
    }

    @Override
    public int getCount() {
        return settingsArray.size();
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
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View v = inflater.inflate(R.layout.settings_list_items, null);
        ButterKnife.bind(this, v);
        txtForName.setText(settingsArray.get(i).getName());
        return v;
    }
}
