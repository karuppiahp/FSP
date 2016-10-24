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
import activities.mswift.info.walaapp.wala.model.GoalsHistoryModel;
import activities.mswift.info.walaapp.wala.utils.Utils;

/**
 * Created by karuppiah on 4/28/2016.
 */
public class GoalsHistoryAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private static SimpleDateFormat sdf;
    private static Date ddObj;
    Context c;
    private LayoutInflater inflater;
    private List<GoalsHistoryModel> goalsItems;


    public GoalsHistoryAdapter(Context c, List<GoalsHistoryModel> goalsItems) {
        this.c = c;
        this.goalsItems = goalsItems;
    }

    @Override
    public int getCount() {
        return goalsItems.size();
    }

    @Override
    public Object getItem(int position) {
        return goalsItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) c
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.goals_list_item, null);

        TextView goalsDetail = (TextView) convertView.findViewById(R.id.goalsDetail);
        TextView goalsPoint = (TextView) convertView.findViewById(R.id.goalsPoint);
        TextView goalsDate = (TextView) convertView.findViewById(R.id.goalsDate);

        GoalsHistoryModel goalsModel = goalsItems.get(position);
        String typeGoals = goalsModel.getGoalsType();
        String dateGoal = goalsModel.getGoalsDate();

        goalsDetail.setText(Utils.goalNames(typeGoals, goalsModel.getGoalsTarget(), "history"));
        goalsPoint.setText(goalsModel.getGoalsPoints() + " Points");
        goalsDate.setText(ddMMMMYYYYFormat(dateGoal));

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
