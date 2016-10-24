package activities.mswift.info.walaapp.wala.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.transactions.Transaction;

/**
 * Created by kruno on 11.02.16..
 */
public class ManageTransactionAdapter extends BaseAdapter {

    private JSONArray dataArray;
    private static SimpleDateFormat sdf;
    private static Date ddObj;
    Context c;

    private static LayoutInflater inflater = null;

    public ManageTransactionAdapter(JSONArray jsonArray, Context c) {
        this.dataArray = jsonArray;
        this.c = c;

        inflater = (LayoutInflater) this.c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return this.dataArray.length();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ListCell cell;
        //set up convert view if its null
        if (convertView == null) {
            convertView = inflater.inflate(R.layout.transaction_manage_item, null);
            cell = new ListCell();

            cell.transactionName = (TextView) convertView.findViewById(R.id.transaction_manage_item_type);
            cell.transactionDescription = (TextView) convertView.findViewById(R.id.transaction_manage_item_description);
            cell.transactionAmount = (TextView) convertView.findViewById(R.id.transaction_manage_item_amount);
            cell.transactionDate = (TextView) convertView.findViewById(R.id.transaction_manage_item_date);
            cell.transactionId = (TextView) convertView.findViewById(R.id.transaction_manage_item_id);
            cell.transactionSubCathegory = (TextView) convertView.findViewById(R.id.transaction_manage_item_subCathegory);
            cell.datePosted = (TextView) convertView.findViewById(R.id.transaction_manage_item_datePosted);

            convertView.setTag(cell);
        } else {
            cell = (ListCell) convertView.getTag();
        }

        //change the data of cell here
        try {
            JSONObject jsonObject = this.dataArray.getJSONObject(position);

            String subcat = jsonObject.getString("SubCategory");
            String date = jsonObject.getString("DatePosted");

            Transaction transaction = new Transaction(c, subcat);
            cell.transactionSubCathegory.setText(subcat);
            cell.transactionName.setText(transaction.getTransactionMapping());
            if (jsonObject.has("Description")) {
                cell.transactionDescription.setText(jsonObject.getString("Description"));
            } else {
                cell.transactionDescription.setText("-");
            }
            cell.transactionAmount.setText(jsonObject.getString("TranAmount"));
            cell.transactionDate.setText(ddMMMMYYYYFormat(date));
            cell.transactionId.setText(jsonObject.getString("ID"));
            cell.datePosted.setText(date);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return convertView;
    }

    private Context getApplicationContext() {
        return null;
    }

    private class ListCell {
        private TextView transactionName;
        private TextView transactionDate;
        private TextView transactionAmount;
        private TextView transactionDescription;
        private TextView transactionId;
        private TextView transactionSubCathegory;
        private TextView datePosted;
    }

    public static String ddMMMMYYYYFormat(String ipDate) {
        String s = "";
        try {
            sdf = new SimpleDateFormat("yyyy-MM-dd");
            ddObj = sdf.parse(ipDate);
            s = new SimpleDateFormat("dd/MM/yyyy").format(ddObj);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return s;
    }
}
