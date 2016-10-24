package activities.mswift.info.walaapp.wala.utils;

import java.util.ArrayList;

import activities.mswift.info.walaapp.wala.model.CountryModel;
import activities.mswift.info.walaapp.wala.model.GenderModel;
import activities.mswift.info.walaapp.wala.model.GetLotteryModel;
import activities.mswift.info.walaapp.wala.model.QuizQuestionModel;
import activities.mswift.info.walaapp.wala.model.UpdateAnswerModel;

/**
 * Created by karuppiah on 12/3/2015.
 */
public class Constants {

    public static String EMAIL_ADDRESS = "";
    public static String PHONE_NUMBER = "";
    public static String USER_NAME = "";
    public static String PASSWORD = "";
    public static String FIRST_NAME = "";
    public static String MIDDLE_NAME = "";
    public static String LAST_NAME = "";
    public static String COUNTRY = "";
    public static String CITY = "";
    public static String DOB = "";
    public static String GENDER = "";
    public static String COUNTRY_ID = "";

    public static String CURRENCY_CODE="";
    public static String COUNTRY_CODE="";

    public static String TAB = "";
    public static boolean firstTime = false;

    public static boolean userRegister = false;
    public static boolean USER_LOGIN = false;
    public static String TRANSACTION_TYPE = "HOME";
    public static String WALLETNUMBER = "";

    public static int VIEW_PAGER_COUNT = 0;
    public static int WEEKLY_MONTHLY = 0;

    public static String GAMES_PAGE = "";
//    public static String ANSWER="";
    public static String FB_ACCESS_TOKEN = "";
    public static boolean FB_BUTTON_CLICKS = false;
    public static double WALLET_BALANCE = 0;
    public static double WALLET_BALANCE_SAVINGS = 0;
    public static double WALLET_BALANCE_DEBT = 0;

    public static String FINANCIAL_TYPE = "";

    public static ArrayList<CountryModel> COUNTRY_ARRAY = new ArrayList<CountryModel>();
    public static ArrayList<GenderModel> GENDER_ARRAY = new ArrayList<GenderModel>();
    public static ArrayList<QuizQuestionModel> QUES_ARRAY = new ArrayList<QuizQuestionModel>();
    public static ArrayList<UpdateAnswerModel> ANSWER_POINT_ARRAY = new ArrayList<UpdateAnswerModel>();
    public static ArrayList<GetLotteryModel> LOTTERY_MODEL_ARRAY = new ArrayList<GetLotteryModel>();

/*
    public static String BEARER_KEY = "Bearer 8cb8844a8ce8762dee3021b99b55b1c5";
*/
    /*Need For Production Bearer key by TIPSGO team */
    public static String BEARER_KEY = "Bearer b8f1ggmpHbU1h5yN5XxH0Yfub84a";

    /*Need For DEVELOPMENT Bearer key by TIPSGO team */
 //   public static String BEARER_KEY = "Bearer _Go20d6m8j0A7hxIp5NRhZ5s_Cqa";

   // public static String BASIC_KEY = "Basic aVNkY28ycG9sOEpvUUdmQWFSbTFlSGZ1M2ZrYTpXekEwRnFpclJJOHJSalh4bm9iUHQxcG9kV01h";

    /*Need For Production Basic Key by TIPSGO team */
    public static String BASIC_KEY = "Basic ajVSNUg3YldqVEdGRlJzZDl2V2tmSnhLYWN3YTpwaW9JNHlwZjBfYVdsRDFWRWlYWllBMjRWVGdh";

    /*Need For DEVELOPMENT Basic Key by TIPSGO team */
 //   public static String BASIC_KEY = "Basic OFdnSG9BQzhYa2taZVA3RHQxTjRmVWVWRDZZYTo4a0x2b3Z4WG1ZYzZmVGVmV1JJTWFsRklkOEVh";

    /*Need for Production LoginActivity FB param BasicKey*/
    public static String BASIC_KEY_FB = "ajVSNUg3YldqVEdGRlJzZDl2V2tmSnhLYWN3YTpwaW9JNHlwZjBfYVdsRDFWRWlYWllBMjRWVGdh";

    /*Need for DEVELOPMENT LoginActivity FB param BasicKey*/
 //   public static String BASIC_KEY_FB = "OFdnSG9BQzhYa2taZVA3RHQxTjRmVWVWRDZZYTo4a0x2b3Z4WG1ZYzZmVGVmV1JJTWFsRklkOEVh";

    /*Need for Production Token API of TipsGo*/
    public static String TOKEN_API = "https://api.sit.modjadji.org:8243/token";

    /*Need for DEVELOPMENT Token API of TipsGo*/
    //   public static String TOKEN_API = "https://api.dev.modjadji.org:8243/token";

    /*Need for Production Token API Logout of TipsGo*/
    public static String TOKEN_API_LOGOUT = "https://api.sit.modjadji.org:8243/revoke";

    /*Need for DEVELOPMENT Token API Logout of TipsGo*/
    //   public static String TOKEN_API_LOGOUT = "https://api.dev.modjadji.org:8243/revoke";

    public static final String API_KEY = "android_sdk-2716bdba3c44be20ec6a01d69ced9de326f59962";
    public static final String API_ID = "u7k0ore3";

   /* public static final String MIXPANEL_TOKEN = "e74e41608ad0a6d5d8cf527d99dad1dd";*/

    public static final String MIXPANEL_TOKEN = "4d0c3cab7ab31e17e6957c2290b0e38e";



    public static String PREVIOUS_DATE = "";
    public static String AMOUNT = "";
    public static String INCOME_TYPE = "";
    public static String INCOME_FREQ = "";

    public static String GOALS_MESSAGE = "";

    public static String OTP_VALUE="";

    public static boolean SIGNUP_RESPONSE = false;

    public static String ADD_VALUE="api/wallet/addValue";
    public static String CHANGE_PSWD="api/member/ChangePassword";
    public static String CONFIRM_OTP="api/member/ConfirmOTP";
    public static String CREATEDEPT_WALLET="api/wallet/CreateWallet";
    public static String FBLOGIN="api/member/login";
    public static String FB_REGISTER="api/member/RegisterMember";
    public static String INCOME_UPDATE="api/cashflow/UpdateMemberBudgetItem";
    public static String EMPLOYEMNTLIFE_UPDATE="api/member/UpdateLifeStageInfo";
    public static String CREATE_ACCOUNT="api/wallet/CreateAccount";
    public static String MONTHLY_EXPENSES="api/cashflow/UpdateMemberBudgetList";
    public static String GETCONTACT_INFO="api/member/GetContactInfo";
    public static String GETDEPT="api/wallet/GetAccountInfo?accountNumber=";
    public static String GETMEMBER_BUDGET="api/cashflow/GetMemberBudget?category=SalaryIncomeAfterTax";
    public static String GETMONTHLY_EXPENSES="api/cashflow/GetMemberBudget";
    public static String GET_PERSONALINFO="api/member/GetPersonalInfo?member=";
    public static String GET_SAVINGS="api/wallet/GetAccountInfo?accountNumber=";
    public static String GET_TRANSACTION="api/cashflow/GetTransactionList?";
    public static String GET_WALLETINFO="api/wallet/GetWalletInfo?walletCode=";
    public static String GETWALLET_LIST="api/wallet/GetWalletList?pageSize=0&pageNum=0";
    public static String GETACCOUNT_LIST="api/wallet/GetAccountList?pageSize=0&pageNum=0";
    public static String USEVALUE="api/wallet/UseValue";
    public static String SIGNUP_VALIDATE="api/member/ValidateRegisterMember";
    public static String SIGNUP_REGISTER="api/member/RegisterMember";
    public static String TRANSACTION_DELETE="api/wallet/DeleteValue";
    public static String EDITPERSONAL_INFO="api/member/UpdatePersonalInfo";
    public static String UPDATE_TRANSACTION="api/wallet/UpdateValue";
    public static String WALLETTRANSACTION_LIST="api/wallet/GetWalletTransactions?";
    public static String GETLIFESTYLE="api/member/GetLifeStageInfo";
    public static String RECOVERY="api/member/RecoveryMember";

    public static String GET_USER_DETAILS = "usersApi/getUser";
    public static String ADDUSER_BACKEND="usersApi/addUser";
    public static String BUY_TICKET="lotteriesApi/purchaseLottery";
    public static String DROP_TICKET="lotteriesApi/returnLottery";
    public static String INCOMEUPDATE_BACK="FinancialprofilesApi/addIncomeEstimate";
    public static String ADDSAVINGS_BACK="FinancialprofilesApi/addSavings";
    public static String ADDDEPT_BACK="FinancialprofilesApi/addDebts";
    public static String ADDEXPENSE_BACK="financialprofilesApi/addExpenseEstimates";
    public static String CURRENTGOAL_BACK="GoalsApi/getCurrentGoal";
    public static String CURRENTLOTTERY="lotteriesApi/getCurrentLottery";
    public static String GENERATEGOALS="GoalsApi/generateGoal";
    public static String GOALSHISTORY="goalsApi/goalsHistory";
    public static String POINTS_HISTORY="PointsApi/pointshistory";
    public static String QUIZ_QUESTION="QuizzesApi/question";
    public static String ADDTRANS_BACK="transactionsApi/addTransaction";
    public static String DELETETRANS_BACK="transactionsApi/deleteTransaction";
    public static String EDITTRANS_BACK="transactionsApi/editTransaction";
    public static String UPDATE_ANSWER="QuizzesApi/updateanswer";
    public static String UPDATEGOAL_RESULT="GoalsApi/updateGoalResult";
    public static String UPDATEUSER_BACK="usersApi/editUser";
    public static String FORGOT_PSWD="users/forgotpassword";

}
