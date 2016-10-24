package activities.mswift.info.walaapp.wala.utils;

/**
 * Created by karuppiah on 4/12/2016.
 */
public class FinancialInfoUtils {

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

    public static String empStatusBackend(String statusChoose) {
        String status = "";
        if (statusChoose.equals("Full-Time Employed")) {
            status = "full_time_employed";
        } else if (statusChoose.equals("Part-Time Employed")) {
            status = "part_time_employed";
        } else {
            status = "unemployed";
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

    public static String incomeTypeBackend(String typesChoose) {
        String status = "";
        if (typesChoose.equals("Salary")) {
            status = "salary";
        } else if (typesChoose.equals("Hourly Wage")) {
            status = "hourly_wage";
        } else {
            status = "other";
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

    public static String frequencyBackend(String freqChoose) {
        String status = "";
        if (freqChoose.equals("Monthly")) {
            status = "monthly";
        } else if (freqChoose.equals("Bi-Monthly")) {
            status = "bi_monthly";
        } else if (freqChoose.equals("Weekly")) {
            status = "weekly";
        } else {
            status = "everyday";
        }
        return status;
    }
}
