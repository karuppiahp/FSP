package activities.mswift.info.walaapp.wala.support;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

/**
 * Created by karuppiah on 1/13/2016.
 */
public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
    TextView txtViewForDate;
    int day, month, year;

    public DatePickerFragment(TextView txtViewForDate) {
        this.txtViewForDate = txtViewForDate;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Calendar cal;
        // Use the current date as the default date in the picker
        cal = Calendar.getInstance();
        day = cal.get(Calendar.DAY_OF_MONTH); //Current day
        month = cal.get(Calendar.MONTH);// current month
        year = cal.get(Calendar.YEAR) - 13;// current year

        DatePicker datePicker = new DatePicker(getActivity());

        // set date picker as current date
        DatePickerDialog dateDialog = new DatePickerDialog(getActivity(), this, year, month,
                day) {
            @Override
            public void onDateChanged(DatePicker view, int mYear, int monthOfYear, int dayOfMonth) {
                //    view.setMaxDate(new Date().getTime());
                if (mYear > year)
                    view.updateDate(year, month, day);

                if (monthOfYear > month && mYear == year)
                    view.updateDate(year, month, day);

                if (dayOfMonth > day && mYear == year && monthOfYear == month)
                    view.updateDate(year, month, day);

            }
        };
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        dateDialog.getDatePicker().setMaxDate(c.getTimeInMillis());
        return dateDialog;
    }

    public void onDateSet(DatePicker view, int selectedYear,
                          int selectedMonth, int selectedDay) {
        // Do something with the date chosen by the user
        String formattedDay = (String.valueOf(selectedDay));
        String formattedMonth = (String.valueOf(selectedMonth + 1));
        if (selectedDay < 10) {
            formattedDay = "0" + selectedDay;
        }
        if (Integer.parseInt(formattedMonth) < 10) {
            formattedMonth = "0" + (formattedMonth);
        }
        txtViewForDate.setText(formattedDay + "/" + formattedMonth
                + "/" + selectedYear);
    }
}
