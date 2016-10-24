package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.IncomeFrequencyModel;

/**
 * Created by karuppiah on 1/23/2016.
 */
public class IncomeFreqAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<IncomeFrequencyModel> incomeFreqArray = new ArrayList<>();

    public IncomeFreqAdapter(Context context, ArrayList<IncomeFrequencyModel> incomeFreqArray) {
        this.context = context;
        this.incomeFreqArray = incomeFreqArray;
    }

    @Override
    public int getCount() {
        return incomeFreqArray.size();
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
        IncomeFrequencyModel incomeFreqModel = incomeFreqArray.get(i);
        textView.setText(incomeFreqModel.getFreq());
        return v;
    }
}