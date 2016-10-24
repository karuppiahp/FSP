package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.EmploymentStatusModel;

/**
 * Created by karuppiah on 1/23/2016.
 */
public class EmpStatusAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<EmploymentStatusModel> empStatusArray = new ArrayList<>();

    public EmpStatusAdapter(Context context, ArrayList<EmploymentStatusModel> empStatusArray) {
        this.context = context;
        this.empStatusArray = empStatusArray;
    }

    @Override
    public int getCount() {
        return empStatusArray.size();
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
        EmploymentStatusModel empStatusModel = empStatusArray.get(i);
        textView.setText(empStatusModel.getStatus());
        return v;
    }
}
