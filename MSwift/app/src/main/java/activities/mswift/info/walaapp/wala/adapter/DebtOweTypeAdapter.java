package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.DebtOweTypeModel;

/**
 * Created by karuppiah on 1/9/2016.
 */
public class DebtOweTypeAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<DebtOweTypeModel> typeArray = new ArrayList<>();

    public DebtOweTypeAdapter(Context context, ArrayList<DebtOweTypeModel> typeArray) {
        this.context = context;
        this.typeArray = typeArray;
    }

    @Override
    public int getCount() {
        return typeArray.size();
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
        DebtOweTypeModel typeModel = typeArray.get(i);
        textView.setText(typeModel.getType());
        return v;
    }
}
