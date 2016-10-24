package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.MyAccountModel;

/**
 * Created by karuppiah on 12/12/2015.
 */
public class MyAccountAdapter extends BaseAdapter {

    Context context;
    ArrayList<MyAccountModel> myAccArray = new ArrayList<>();

    public MyAccountAdapter(Context context, ArrayList<MyAccountModel> myAccArray) {
        this.context = context;
        this.myAccArray = myAccArray;
    }

    @Override
    public int getCount() {
        return myAccArray.size();
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
        view = inflater.inflate(R.layout.more_list_items, null);
        int density = context.getResources().getDisplayMetrics().densityDpi;
        RelativeLayout moreListItem = (RelativeLayout) view.findViewById(R.id.moreListItem);
        RelativeLayout.LayoutParams paramsMore = (RelativeLayout.LayoutParams) moreListItem.getLayoutParams();

        if (density == DisplayMetrics.DENSITY_HIGH) {
            paramsMore.setMargins(12, 10, 12, 12);
            moreListItem.setLayoutParams(paramsMore);
        }
        ImageView imgForIcon = (ImageView) view.findViewById(R.id.imgForListicon);
        TextView txtView = (TextView) view.findViewById(R.id.txtForListItem);
        TypedArray imgs = context.getResources().obtainTypedArray(R.array.myAccIcon);
        imgForIcon.setImageResource(imgs.getResourceId(i, -1));
        MyAccountModel myAccModel = myAccArray.get(i);
        txtView.setText(myAccModel.getName());
        return view;
    }
}
