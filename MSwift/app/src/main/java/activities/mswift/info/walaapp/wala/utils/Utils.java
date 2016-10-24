package activities.mswift.info.walaapp.wala.utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import activities.mswift.info.walaapp.R;
import activities.mswift.info.walaapp.wala.adapter.FaqListAdapter;
import activities.mswift.info.walaapp.wala.application.MSwiftApplication;
import activities.mswift.info.walaapp.wala.model.AboutUsModel;
import activities.mswift.info.walaapp.wala.model.AboutUsOurTeamModel;
import activities.mswift.info.walaapp.wala.model.CountryModel;
import activities.mswift.info.walaapp.wala.model.DebtOweTypeModel;
import activities.mswift.info.walaapp.wala.model.EmploymentStatusModel;
import activities.mswift.info.walaapp.wala.model.FinancialInfo5Model;
import activities.mswift.info.walaapp.wala.model.FinancialInfo5ModelBackend;
import activities.mswift.info.walaapp.wala.model.GenderModel;
import activities.mswift.info.walaapp.wala.model.IncomeFrequencyModel;
import activities.mswift.info.walaapp.wala.model.IncomeTypeModel;
import activities.mswift.info.walaapp.wala.model.MyAccountModel;
import activities.mswift.info.walaapp.wala.model.OtherModel;
import activities.mswift.info.walaapp.wala.model.RecentTransactionsModel;
import activities.mswift.info.walaapp.wala.model.SavingsTypeModel;
import activities.mswift.info.walaapp.wala.model.TodoListModel;
import activities.mswift.info.walaapp.wala.signup.ConfirmOTPActivity;
import activities.mswift.info.walaapp.wala.signup.IntroFragments;
import activities.mswift.info.walaapp.wala.signup.SignUpActivity;
import activities.mswift.info.walaapp.wala.signup.SignUpStep2Activity;

/**
 * Created by karuppiah on 11/24/2015.
 */
public class Utils {

    private static String[] todoListNameArray;
    private static String[] genderArray;
    private static String[] myAccNameArray;
    private static String[] otherNameArray;
    private static String[] headerArray;
    private static String[] savingsTypeArray;
    private static String[] debtOweTypeArray;
    private static String[] empStatusArray;
    private static String[] incomeTypeArray;
    private static String[] incomeFreqArray;
    private static String[] faqQuesArray;
    private static String[] faqAnsArray;
    private static String[] ourTeamTitleArray;
    private static String[] ourTeamContentArray;
    private static List<FaqListAdapter.Item> data = new ArrayList<>();
    private static boolean isValid;
    private static boolean isValidDate;
    private static SimpleDateFormat utcFormat;
    private static Date utcDate;

    public static List<TodoListModel> getTodoNameValues(Context context) {
        List<TodoListModel> todoNameArray = new ArrayList<TodoListModel>();
        todoListNameArray = context.getResources().getStringArray(
                R.array.toDoListArray);

        for (int i = 0; i < todoListNameArray.length; i++) {
            TodoListModel scheduleEvents = new TodoListModel();
            scheduleEvents.setName(todoListNameArray[i]);
            todoNameArray.add(scheduleEvents);
        }
        return todoNameArray;
    }

    public static boolean isOnline() {
        ConnectivityManager cm = (ConnectivityManager) MSwiftApplication
                .getGlobalContext().getSystemService(
                        Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);
        if (matcher.matches()) {
            isValid = true;
        }
        return isValid;
    }

    public static boolean isPhoneNumberValid(String number) {
        return android.util.Patterns.PHONE.matcher(number).matches();
    }

    public static void ShowAlert(Context context, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(context);
        adb.setMessage(message);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }

    public static void ShowAlertActivity(final Activity activity, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setMessage(message);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intentToSignUp = new Intent(activity, SignUpStep2Activity.class);
                activity.startActivity(intentToSignUp);
                dialog.cancel();
                activity.finish();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }

    public static void ShowAlertSignupActivity(final Activity activityFrom, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activityFrom);
        adb.setMessage(message);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intentToSignUp = new Intent(activityFrom, SignUpActivity.class);
                activityFrom.startActivity(intentToSignUp);
                dialog.cancel();
                activityFrom.finish();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }


    public static void hideKeyboard(Activity activity) {
        // Check if no view has focus:
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager inputManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            inputManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public static void IntroActivity(final Activity activityFrom, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activityFrom);
        adb.setMessage(message);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intentToSignUp = new Intent(activityFrom, IntroFragments.class);
                activityFrom.startActivity(intentToSignUp);
                dialog.cancel();
                activityFrom.finish();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }

    public static void OtpActivity(final Activity activityFrom, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activityFrom);
        adb.setMessage(message);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                Intent intentToOTP = new Intent(activityFrom, ConfirmOTPActivity.class);
                activityFrom.startActivity(intentToOTP);
                dialog.cancel();
                activityFrom.finish();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }

    public static void OtpLoginActivity(final Activity activityFrom, String message) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activityFrom);
        adb.setMessage(message);
        adb.setPositiveButton("OK", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        AlertDialog alert = adb.create();
        alert.show();
    }

    public static void setListViewHeightBasedOnChildren(
            ListView listView) {
        // TODO Auto-generated method stub
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View mView = listAdapter.getView(i, null, listView);

            ViewGroup.LayoutParams lp = mView.getLayoutParams();
            if (lp == null) {
                lp = new ViewGroup.LayoutParams(-2, -2);
            }
            lp.height = ViewGroup.LayoutParams.WRAP_CONTENT;
            lp.width = ViewGroup.LayoutParams.MATCH_PARENT;
            mView.setLayoutParams(lp);
            mView.measure(
                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED),

                    MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));

            totalHeight += mView.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static void updateTransactionTable(Context context, LinearLayout transactionTableLay, List<RecentTransactionsModel> transactionArray) {
        Log.i("transaction array size", "" + transactionArray.size());
        for (int i = 0; i < transactionArray.size(); i++) {
            LayoutInflater inflator = LayoutInflater.from(context);
            View v = inflator.inflate(R.layout.recent_transactions_table, null);
            TextView textForCategory = (TextView) v.findViewById(R.id.txtForCategory);
            TextView textForDesc = (TextView) v.findViewById(R.id.txtForDescription);
            TextView textForAmnt = (TextView) v.findViewById(R.id.txtForAmount);

            v.setPadding(0, 15, 0, 15);
            if (i % 2 == 0) {
                v.setBackgroundColor(context.getResources().getColor(R.color.whiteBg));
            }

            RecentTransactionsModel transactionModel = transactionArray.get(i);
            textForCategory.setText(transactionModel.getCategory());
            textForDesc.setText(transactionModel.getDescription());
            textForAmnt.setText(transactionModel.getAmount());

            transactionTableLay.addView(v);
        }
    }

    public static ArrayList<GenderModel> getGenderValues(Context context) {
        ArrayList<GenderModel> genderNameArray = new ArrayList<GenderModel>();
        genderArray = context.getResources().getStringArray(
                R.array.gender);

        for (int i = 0; i < genderArray.length; i++) {
            GenderModel genders = new GenderModel();
            genders.setName(genderArray[i]);
            genderNameArray.add(genders);
        }
        return genderNameArray;
    }

    public static ArrayList<MyAccountModel> getMyAccValues(Context context) {
        ArrayList<MyAccountModel> nameArray = new ArrayList<MyAccountModel>();
        myAccNameArray = context.getResources().getStringArray(
                R.array.myAcc);

        for (int i = 0; i < myAccNameArray.length; i++) {
            MyAccountModel names = new MyAccountModel();
            names.setName(myAccNameArray[i]);
            nameArray.add(names);
        }
        return nameArray;
    }

    public static ArrayList<OtherModel> getOtherValues(Context context) {
        ArrayList<OtherModel> nameArray = new ArrayList<OtherModel>();
        otherNameArray = context.getResources().getStringArray(
                R.array.other);

        for (int i = 0; i < otherNameArray.length; i++) {
            OtherModel names = new OtherModel();
            names.setName(otherNameArray[i]);
            nameArray.add(names);
        }
        return nameArray;
    }

    public static ArrayList<AboutUsModel> getAboutUsHeaders(Context context) {
        ArrayList<AboutUsModel> nameArray = new ArrayList<AboutUsModel>();
        headerArray = context.getResources().getStringArray(
                R.array.aboutUsHeader);

        for (int i = 0; i < headerArray.length; i++) {
            AboutUsModel names = new AboutUsModel();
            names.setName(headerArray[i]);
            nameArray.add(names);
        }
        return nameArray;
    }

    public static void setInfo5Values(String subCategory, String amnt, ArrayList<FinancialInfo5Model> infoArrayList) {
        FinancialInfo5Model infoModel = new FinancialInfo5Model();
        infoModel.setSubCategory(subCategory);
        infoModel.setAmount(amnt);
        infoArrayList.add(infoModel);
    }

    public static void setInfo5ValuesBackend(String subCategory, String amnt, ArrayList<FinancialInfo5ModelBackend> infoArrayList) {
        FinancialInfo5ModelBackend infoModel = new FinancialInfo5ModelBackend();
        infoModel.setSubCategory(subCategory);
        infoModel.setAmount(amnt);
        infoArrayList.add(infoModel);
    }

    public static ArrayList<SavingsTypeModel> getSavingsTypes(Context context) {
        ArrayList<SavingsTypeModel> typeArray = new ArrayList<SavingsTypeModel>();
        savingsTypeArray = context.getResources().getStringArray(
                R.array.savingsType);

        for (int i = 0; i < savingsTypeArray.length; i++) {
            SavingsTypeModel types = new SavingsTypeModel();
            types.setType(savingsTypeArray[i]);
            typeArray.add(types);
        }
        return typeArray;
    }

    public static ArrayList<DebtOweTypeModel> getDebtOweTypes(Context context) {
        ArrayList<DebtOweTypeModel> typeArray = new ArrayList<DebtOweTypeModel>();
        debtOweTypeArray = context.getResources().getStringArray(
                R.array.debtOweTypes);

        for (int i = 0; i < debtOweTypeArray.length; i++) {
            DebtOweTypeModel types = new DebtOweTypeModel();
            types.setType(debtOweTypeArray[i]);
            typeArray.add(types);
        }
        return typeArray;
    }

    public static ArrayList<EmploymentStatusModel> getEmpStatus(Context context) {
        ArrayList<EmploymentStatusModel> statusArray = new ArrayList<EmploymentStatusModel>();
        empStatusArray = context.getResources().getStringArray(
                R.array.empStatus);

        for (int i = 0; i < empStatusArray.length; i++) {
            EmploymentStatusModel types = new EmploymentStatusModel();
            types.setStatus(empStatusArray[i]);
            statusArray.add(types);
        }
        return statusArray;
    }

    public static ArrayList<AboutUsOurTeamModel> getOurTeamList(Context context) {
        ArrayList<AboutUsOurTeamModel> teamArray = new ArrayList<AboutUsOurTeamModel>();
        ourTeamTitleArray = context.getResources().getStringArray(
                R.array.ourTeamTitle);
        ourTeamContentArray = context.getResources().getStringArray(
                R.array.ourTeamContent);

        for (int i = 0; i < ourTeamTitleArray.length; i++) {
            AboutUsOurTeamModel teamModel = new AboutUsOurTeamModel();
            teamModel.setTitle(ourTeamTitleArray[i]);
            teamModel.setContnet(ourTeamContentArray[i]);
            teamArray.add(teamModel);
        }
        return teamArray;
    }

    public static ArrayList<IncomeTypeModel> getIncomeType(Context context) {
        ArrayList<IncomeTypeModel> typeArray = new ArrayList<IncomeTypeModel>();
        incomeTypeArray = context.getResources().getStringArray(
                R.array.incomeType);

        for (int i = 0; i < incomeTypeArray.length; i++) {
            IncomeTypeModel types = new IncomeTypeModel();
            types.setType(incomeTypeArray[i]);
            typeArray.add(types);
        }
        return typeArray;
    }

    public static ArrayList<IncomeFrequencyModel> getIncomeFreq(Context context) {
        ArrayList<IncomeFrequencyModel> freqArray = new ArrayList<IncomeFrequencyModel>();
        incomeFreqArray = context.getResources().getStringArray(
                R.array.incomeFreq);

        for (int i = 0; i < incomeFreqArray.length; i++) {
            IncomeFrequencyModel types = new IncomeFrequencyModel();
            types.setFreq(incomeFreqArray[i]);
            freqArray.add(types);
        }
        return freqArray;
    }

    public static List<FaqListAdapter.Item> getFaqList(Context context) {
        faqQuesArray = context.getResources().getStringArray(
                R.array.faqQues);
        faqAnsArray = context.getResources().getStringArray(
                R.array.faqAns);

        for (int i = 0; i < faqQuesArray.length; i++) {
            FaqListAdapter.Item q1 = new FaqListAdapter.Item(FaqListAdapter.HEADER, faqQuesArray[i]);
            q1.invisibleChildren = new ArrayList<>();
            q1.invisibleChildren.add(new FaqListAdapter.Item(FaqListAdapter.CHILD, faqAnsArray[i]));
            data.add(q1);
        }
        return data;
    }

    public static boolean isValidMobile(String phone2) {
        boolean check;
        String regex = "^(999|998|997|996|995|994|993|992|991|990|979|978|977|976|975|974|973|972|971|970|969|968|967|966|965|964|963|962|961|960|899|898|897|896|895|894|893|892|891|890|889|888|887|886|885|884|883|882|881|880|879|878|877|876|875|874|873|872|871|870|859|858|857|856|855|854|853|852|851|850|839|838|837|836|835|834|833|832|831|830|809|808|807|806|805|804|803|802|801|800|699|698|697|696|695|694|693|692|691|690|689|688|687|686|685|684|683|682|681|680|679|678|677|676|675|674|673|672|671|670|599|598|597|596|595|594|593|592|591|590|509|508|507|506|505|504|503|502|501|500|429|428|427|426|425|424|423|422|421|420|389|388|387|386|385|384|383|382|381|380|379|378|377|376|375|374|373|372|371|370|359|358|357|356|355|354|353|352|351|350|299|298|297|296|295|294|293|292|291|290|289|288|287|286|285|284|283|282|281|280|269|268|267|266|265|264|263|262|261|260|259|258|257|256|255|254|253|252|251|250|249|248|247|246|245|244|243|242|241|240|239|238|237|236|235|234|233|232|231|230|229|228|227|226|225|224|223|222|221|220|219|218|217|216|215|214|213|212|211|210|98|95|94|93|92|91|90|86|84|82|81|66|65|64|63|62|61|60|58|57|56|55|54|53|52|51|49|48|47|46|45|44|43|41|40|39|36|34|33|32|31|30|27|20|7|1)[0-9]{8,14}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(phone2);

        if (matcher.matches()) {
            if (phone2.length() < 6 || phone2.length() > 13) {
                check = false;

            } else {
                check = true;
            }
        } else {
            check = false;
        }

        return check;
    }

    public static boolean isValidMobile(String phoneNumber, String countryCode) {
        boolean isValid = false;

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swissNumberProto = phoneUtil.parse(phoneNumber, countryCode.toUpperCase());

            isValid = phoneUtil.isValidNumber(swissNumberProto);
            if (isValid) {
                String tmpPhone = phoneUtil.format(swissNumberProto, PhoneNumberUtil.PhoneNumberFormat.E164);
                if (tmpPhone.startsWith("+")) {
                    tmpPhone = tmpPhone.substring(1);
                    Constants.PHONE_NUMBER = tmpPhone;
                } else {
                    Constants.PHONE_NUMBER = tmpPhone;
                }
            }
        } catch (NumberParseException e) {
        }

        return isValid;
    }

    private static final String PASSWORD_PATTERN =
            "^(?=.*?[A-Z])(?=.*?[a-z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{6,}$";

    public static boolean PasswordValidator(String pass) {
        Pattern pattern = Pattern.compile(PASSWORD_PATTERN);
        Matcher matcher = pattern.matcher(pass);

        if (matcher.matches()) {
            return true;
        } else {
            return false;
        }
    }

    public static String calculateDate(int d) {

        String ds = "";
        if (d < 10) {
            ds = "0" + String.valueOf(d);
        } else {
            return String.valueOf(d);
        }
        return ds;
    }

    public static void getCountry() {

        String[] locales = Locale.getISOCountries();
        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            CountryModel countryModel = new CountryModel();
            countryModel.setId(obj.getCountry());
            countryModel.setName(obj.getDisplayCountry());
            countryModel.setCountryCode(obj.getISO3Country());
            //    countrylist.add(countryModel);
            Constants.COUNTRY_ARRAY.add(countryModel);
        }
        //sort by name
        Collections.sort(Constants.COUNTRY_ARRAY, new Comparator<CountryModel>() {
            @Override
            public int compare(CountryModel lhs, CountryModel rhs) {
                return lhs.getName().compareTo(rhs.getName());
            }
        });
    }

    public static Map<String, String> getCountryCodes() {
        Map<String, String> countries = new HashMap<>();
        for (String iso : Locale.getISOCountries()) {
            Locale li = new Locale("", iso);
            countries.put(li.getDisplayCountry(), iso);
        }
        return countries;
    }

    /* According to TipsGo the values are mentioned here
      4 - Attending Full Time
      8 - Working
      512 - Unemployed
      1 - Not Working (mapping with Unemployed as new change)
     */
    public static String empStatus(String statusChoose) {
        String status = "";
        if (statusChoose.equals("Full-Time Employed")) {
            status = "4";
        } else if (statusChoose.equals("Part-Time Employed")) {
            status = "8";
        } else {
            status = "1";
        }
        return status;
    }

    /* According to TipsGo the values are mentioned here
    using http://api.tipsgo.com:8280/tipsgo/1.0.0/api/cashflow/GetSubCategoryList this API the sub category SalaryIncomeAfterTax is mentioned
      {
    "TotalRecord": 2,
    "ListOfObjects": [
        {
            "ID": 4055,
            "Code": "OvertimeIncome",
            "Name": "Salary income - Overtime",
            "Category": "SalaryIncomeaftertax",
            "Description": "",
            "Order": 0,
            "Active": true,
            "Frequency": "Yearly",
            "Annual": 0,
            "Monthly": 0,
            "MatchingExpressions": "overtime",
            "BillerCodes": ""
        },
        {
            "ID": 4065,
            "Code": "RegularWages",
            "Name": "Salary income - Regular wages",
            "Category": "SalaryIncomeaftertax",
            "Description": "",
            "Order": 0,
            "Active": true,
            "Frequency": "Yearly",
            "Annual": 2,
            "Monthly": 3,
            "MatchingExpressions": "SALARY",
            "BillerCodes": ""
        }
    ],
    "Status": "000000",
    "Message": "Request completed successfully."
}
     */
    /*
      New API changes
      Salary - Regular Salary
      Hourly Wage - Regular Hourly Wage
      Other - Other
     */
    public static String incomeType(String typesChoose) {
        String status = "";
        if (typesChoose.equals("Salary")) {
            status = "RegularSalary";
        } else if (typesChoose.equals("Hourly Wage")) {
            status = "RegularHourlyWage";
        } else {
            status = "OtherIncome";
        }
        return status;
    }

    /* According to TipsGo the values are mentioned here
      Monthly - Monthly
      Bi-Monthly - Fortnightly
      Weekly - Weekly
      Other - Daily
     */
    public static String frequency(String freqChoose) {
        String status = "";
        if (freqChoose.equals("Monthly")) {
            status = "Monthly";
        } else if (freqChoose.equals("Bi-Monthly")) {
            status = "Fortnightly";
        } else if (freqChoose.equals("Weekly")) {
            status = "Weekly";
        } else {
            status = "Daily";
        }
        return status;
    }

    public static String frequencyRevert(String freqChoose) {
        String status = "";
        if (freqChoose.equals("Monthly")) {
            status = "Monthly";
        } else if (freqChoose.equals("Fortnightly")) {
            status = "Bi-Monthly";
        } else if (freqChoose.equals("Weekly")) {
            status = "Weekly";
        } else {
            status = "Everyday";
        }
        return status;
    }


    public static String dateTimeFormatChange(String ipDF, String opDF, String ip) {
        String op = "";
        try {
            // input work
            utcFormat = new SimpleDateFormat(ipDF);
            utcDate = utcFormat.parse(ip);
            // Output work
            utcFormat = new SimpleDateFormat(opDF);
            op = utcFormat.format(utcDate);
        } catch (ParseException e) {
            e.printStackTrace();
            Log.e("changeTimeZone", "" + e.getMessage());
        }
        return op;
    }

    @SuppressLint("SimpleDateFormat")
    public static String getCurrentDate() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        String formattedDate = df.format(c.getTime());
        return formattedDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean calculateDaysBtwn(String dateSaved) {
        String postTime = dateSaved;
        boolean dateMore = false;
        SimpleDateFormat dateFormatter;
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd");

        try {
            Date date = dateFormatter.parse(postTime);
            long then = date.getTime();
            long now = new Date().getTime();


            long seconds = (now - then) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            if (days > 0) {
                dateMore = true;
            } else {
                dateMore = false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dateMore;
    }

    @SuppressLint("SimpleDateFormat")
    public static boolean calculateExpiresTime(String dateSaved, Context context) {
        String postTime = dateSaved;
        boolean timeHas = false;
        SimpleDateFormat dateFormatter;
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        try {
            Date date = dateFormatter.parse(postTime);
            long then = date.getTime();
            long now = new Date().getTime();


            long seconds = (now - then) / 1000;
            long minutes = seconds / 60;
            long hours = minutes / 60;
            long days = hours / 24;

            Log.e("Session time??", "" + SessionStores.getTimeExpiresIn(context));
            Log.e("difference seconds??", "" + seconds);
            if (days > 0) {
                timeHas = false;
            } else {
                if (seconds < Long.parseLong(SessionStores.getTimeExpiresIn(context))) {
                    timeHas = true;
                } else {
                    timeHas = false;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return timeHas;
    }

    @SuppressLint("SimpleDateFormat")
    public static String calculateTimeLeft(String dateSaved, Context context) {
        double h, m, s;
        h = (Integer.parseInt(dateSaved) % 86400) / 3600;
        m = ((Integer.parseInt(dateSaved) % 86400) % 3600) / 60;
        s = ((Integer.parseInt(dateSaved) % 86400) % 3600) % 60;

        return ((int) h < 10 ? "0" + String.valueOf((int) h) : String.valueOf((int) h)) + ":" + ((int) m < 10 ? "0" + String.valueOf((int) m) : String.valueOf((int) m))
                + ":" + ((int) s < 10 ? "0" + String.valueOf((int) s) : String.valueOf((int) s));
    }

    public static boolean calculateRecentDayUpdate(String oldDate, String newDate) {
        boolean dateNew = false;
        SimpleDateFormat dateFormatter;
        dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        try {
            Date oldDt = dateFormatter.parse(oldDate);
            Date newDt = dateFormatter.parse(newDate);

            if (newDt.after(oldDt)) {
                dateNew = true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return dateNew;
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateConvert(String date) {
        String formattedDate = "";
        SimpleDateFormat dfOld = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df = new SimpleDateFormat("MM/dd/yyyy");
        try {
            Date dateFormat = dfOld.parse(date);
            formattedDate = df.format(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    @SuppressLint("SimpleDateFormat")
    public static String dateConvertPersonalInfo(String date) {
        String formattedDate = "";
        SimpleDateFormat dfOld = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        try {
            Date dateFormat = dfOld.parse(date);
            formattedDate = df.format(dateFormat);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    public static String goalNames(String name, String goalValue, String type) {
        String goalName = "";
        if (name.equalsIgnoreCase("quizzes")) {
            goalName = "Play the daily quiz " + goalValue + " times this week.";
        } else if (name.equalsIgnoreCase("correct%")) {
            goalName = "Answer " + goalValue + "% of quizzes correctly this week.";
        } else if (name.equalsIgnoreCase("all wants")) {
            goalName = "Don't spend more than " + goalValue + " on your Want Expenses.";
        } else if (name.equalsIgnoreCase("Savings")) {
            goalName = "Hit the monthly goal by reaching the remaining Savings in the final week.";
        } else if (name.equalsIgnoreCase("Data")) {
            goalName = "Enter " + goalValue + " days of transactions this week.";
        } else if (name.equalsIgnoreCase("Entered")) {
            goalName = "Login and enter your transactions each day for " + goalValue + " days this week.";
        } else if (name.equalsIgnoreCase("")) {
            if (type.equals("history")) {
                goalName = "Completed financial profile.";
            } else {
                goalName = "Complete 4 financial weekly goals.";
            }
        } else {
            goalName = "Don't spend more than " + goalValue + " on " + name;
        }
        return goalName;
    }

    public static String goalReachedMain(String name) {
        String goalsRechedTxt = "";
        if (name.equalsIgnoreCase("quizzes")) {
            goalsRechedTxt = "PLAY NOW IN GAMES";
        } else if (name.equalsIgnoreCase("correct%")) {
            goalsRechedTxt = "PLAY NOW IN GAMES";
        } else if (name.equalsIgnoreCase("all wants")) {
            goalsRechedTxt = "IMPROVE YOUR FINANCES";
        } else if (name.equalsIgnoreCase("Savings")) {
            goalsRechedTxt = "IMPROVE YOUR FINANCES";
        } else if (name.equalsIgnoreCase("Data")) {
            goalsRechedTxt = "MANAGE YOUR TRANSACTIONS";
        } else if (name.equalsIgnoreCase("Entered")) {
            goalsRechedTxt = "MANAGE YOUR TRANSACTIONS";
        } else if (name.equalsIgnoreCase("")) {
            goalsRechedTxt = "MANAGE GOALS";
        } else {
            goalsRechedTxt = "IMPROVE YOUR FINANCES";
        }
        return goalsRechedTxt;
    }

    public static String goalReached(String name) {
        String goalsRechedTxt = "";
        if (name.equalsIgnoreCase("quizzes")) {
            goalsRechedTxt = "PLAYED GAMES";
        } else if (name.equalsIgnoreCase("correct%")) {
            goalsRechedTxt = "PLAYED GAMES";
        } else if (name.equalsIgnoreCase("all wants")) {
            goalsRechedTxt = "SPEND AMOUNT";
        } else if (name.equalsIgnoreCase("Savings")) {
            goalsRechedTxt = "SAVINGS REMAINING";
        } else if (name.equalsIgnoreCase("Data")) {
            goalsRechedTxt = "DAYS OF TRANSACTION";
        } else if (name.equalsIgnoreCase("Entered")) {
            goalsRechedTxt = "ENTERED EACH TRANSACTION";
        } else if (name.equalsIgnoreCase("")) {
            goalsRechedTxt = "WEEKLY GOALS";
        } else {
            goalsRechedTxt = "SPEND AMOUNT";
        }
        return goalsRechedTxt;
    }
}
