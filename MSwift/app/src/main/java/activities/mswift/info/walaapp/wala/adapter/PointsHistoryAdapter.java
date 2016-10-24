package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.model.PointsHistoryModel;

/**
 * Created by karuppiah on 4/15/2016.
 */
public class PointsHistoryAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private static SimpleDateFormat sdf;
    private static Date ddObj;
    Context c;
    private LayoutInflater inflater;
    private List<PointsHistoryModel> pointsItems;


    public PointsHistoryAdapter(Context c, List<PointsHistoryModel> pointsItems) {
        this.c = c;
        this.pointsItems = pointsItems;
    }

    @Override
    public int getCount() {
        return this.pointsItems.size();
    }

    @Override
    public Object getItem(int position) {

        return pointsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)

    {

        if (inflater == null)
            inflater = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.points_history_items, null);

        TextView usage = (TextView) convertView.findViewById(R.id.txtFordailyQuiz);
        TextView points = (TextView) convertView.findViewById(R.id.txtFordailyQuizpnt);
        TextView datePnt = (TextView) convertView.findViewById(R.id.dailyquizdate);

        PointsHistoryModel p = pointsItems.get(position);
        usage.setText(p.getUsageDetail());
        points.setText(p.getPointsDetail() + " Points");
        String setdatePoint = p.getDatePoint();
        datePnt.setText(ddMMMMYYYYFormat(setdatePoint));
        return convertView;
    }


    public static String ddMMMMYYYYFormat(String ipDate) {
        String s = "";
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            ddObj = sdf.parse(String.valueOf(ipDate));
            s = new SimpleDateFormat("dd/MM/yyyy").format(ddObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }
}
