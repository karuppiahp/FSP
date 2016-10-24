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
 * Created by karuppiah on 6/21/2016.
 */
public class FaqAdapter extends BaseAdapter {
    Context context;
    @BindView(R.id.txtForQues)
    TextView txtForQues;
    @BindView(R.id.txtForAns)
    TextView txtForAns;
    private ArrayList<SettingsMainModel> faqArray = new ArrayList<>();

    public FaqAdapter(Context context, ArrayList<SettingsMainModel> faqArray) {
        this.context = context;
        this.faqArray = faqArray;
    }

    @Override
    public int getCount() {
        return faqArray.size();
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
        view = inflater.inflate(R.layout.faq_list_item, null);
        ButterKnife.bind(this, view);
        //Faq values fetched from settings model class
        SettingsMainModel settingsModel = faqArray.get(i);
        txtForQues.setText(settingsModel.getQues());
        txtForAns.setText(settingsModel.getAns());
        return view;
    }
}
