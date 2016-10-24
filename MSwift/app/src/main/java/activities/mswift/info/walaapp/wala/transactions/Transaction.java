package activities.mswift.info.walaapp.wala.transactions;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Arrays;
import java.util.Map;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.asyntasks.AddValue;
import activities.mswift.info.walaapp.wala.asyntasks.PayOffLoan;
import activities.mswift.info.walaapp.wala.asyntasks.UseValue;

/**
 * Created by kruno on 11.02.16..
 */
public class Transaction {

    private Context context;
    private String cathegorry;
    public int arrayNumber;
    private boolean mapping;
    String[] transactionTip;
    String[] transactionName;
    String[] transactionMapping;
    TypedArray transactionIcon;

    public Transaction(Context context) {
        this.context = context;
    }

    public Transaction(Context context, int arrayNumber) {
        this.context = context;
        this.arrayNumber = arrayNumber;
        mapping = true;
        init();
    }

    public Transaction(Context context, String apiCathegory) {
        this.context = context;
        this.cathegorry = apiCathegory;
        mapping = false;
        init();
    }

    private void init() {
        if (mapping) {
            transactionIcon = context.getResources().obtainTypedArray(R.array.transactionImage);
            transactionTip = context.getResources().getStringArray(R.array.transactionTip);
        }
        transactionName = context.getResources().getStringArray(R.array.transactionName);
        transactionMapping = context.getResources().getStringArray(R.array.transactionMapping);
    }

    public TypedArray getTransactionIcon() {
        return transactionIcon;
    }

    public String getTransactionTip() {
        String tempTip = transactionTip[arrayNumber];
        return tempTip;
    }

    public String getTransactionName() {
        String tempName = transactionName[arrayNumber];
        return tempName;
    }

    public String getTransactionMapping() {
        String tempMapping;
        if (mapping) {
            tempMapping = transactionMapping[arrayNumber];
        } else {
            tempMapping = transactionName[Arrays.asList(transactionMapping).indexOf(cathegorry)];
        }

        return tempMapping;
    }

    public int getArrayNumber() {
        int tmpI;
        tmpI = Arrays.asList(transactionMapping).indexOf(cathegorry);
        return tmpI;
    }

    public String formatNegativeValue(String s) {

        String tmpAmount = s;
        tmpAmount = tmpAmount.startsWith("-") ? tmpAmount.substring(1) : tmpAmount;
        Log.d("string prefix", tmpAmount);

        return tmpAmount;
    }

    public void addTransactionToVallet(Map<String, String> params, FragmentManager fragmentManager, Map<String, String> params1) {

        //checking wheater increase or decrease value in specific wallet
        if (arrayNumber < 11) {
            new UseValue(context, params, fragmentManager, params1, arrayNumber);
        } else if (arrayNumber == 12 || arrayNumber == 11) {
            new AddValue(context, params, fragmentManager, arrayNumber);
        } else if (arrayNumber == 13) {
            new PayOffLoan(context, params, fragmentManager, params1, arrayNumber);
        }
    }

    public JSONObject transactionDeleteJson(String transactionId) {

        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TransactionId", "" + transactionId);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public JSONObject transactionUpdateJson(String id, String date, String amount, String reason, String subcathegory) {
        JSONObject jsonObject = new JSONObject();
        try {
            jsonObject.put("TransactionId", id);
            jsonObject.put("Date", date);
            jsonObject.put("Amount", amount);
            jsonObject.put("Reason", reason);
            jsonObject.put("SubCategoryCode", subcathegory);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public Bundle getTransactionBundle(String subCathegoryMapping, String iD, String description, String tranAmount, String dateposted, String subCathegory, int index) {

        Bundle bundle = new Bundle();
        bundle.putString("SubCategoryMapping", subCathegoryMapping);
        bundle.putString("ID", iD);
        bundle.putString("Description", description);
        bundle.putString("Tranamount", tranAmount);
        bundle.putString("DatePosted", dateposted);
        bundle.putString("SubCathegory", subCathegory);
        bundle.putInt("index", index);

        return bundle;
    }
}
