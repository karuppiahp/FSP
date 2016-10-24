package com.gradapp.au.support;

import android.app.DatePickerDialog;
import android.content.Context;
import android.widget.DatePicker;

public class DatePickerDialogWithMaxMinRange extends DatePickerDialog {

	int maxYear = 2005;
	int maxMonth = 11;
	int maxDay = 31;

	int minYear = 1925;
	int minMonth = 0;
	int minDay = 1;

	public DatePickerDialogWithMaxMinRange(Context context,
			OnDateSetListener callBack, int minYear, int minMonth, int minDay,
			int maxYear, int maxMonth, int maxDay) {
		super(context, callBack, maxYear, maxMonth, maxDay);
		this.minDay = minDay;
		this.minMonth = minMonth;
		this.minYear = minYear;
		this.maxDay = maxDay;
		this.maxMonth = maxMonth;
		this.maxYear = maxYear;
	}

	@Override
	public void onDateChanged(DatePicker view, int year, int monthOfYear,
			int dayOfMonth) {
		super.onDateChanged(view, year, monthOfYear, dayOfMonth);

		if (year > maxYear || monthOfYear > maxMonth && year == maxYear
				|| dayOfMonth > maxDay && year == maxYear
				&& monthOfYear == maxMonth) {
			view.updateDate(maxYear, maxMonth, maxDay);
		} else if (year < minYear || monthOfYear < minMonth && year == minYear
				|| dayOfMonth < minDay && year == minYear
				&& monthOfYear == minMonth) {
			view.updateDate(minYear, minMonth, minDay);
		}
	}
}
