package activities.mswift.info.walaapp.wala.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by karuppiah on 12/15/2015.
 */
public class SessionStores {

    private static SharedPreferences.Editor editorQuiz;
    public static boolean saveRegisterToken(String token, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("register", 0).edit();
        editor.putString("registerToken", token);
        return editor.commit();
    }

    public static String getRegisterToken(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "register", 0);
        return savedSession.getString("registerToken", null);
    }

    public static boolean saveInstallationId(String id, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("registerSuccess", 0).edit();
        editor.putString("installationId", id);
        return editor.commit();
    }

    public static String getInstallationId(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "registerSuccess", 0);
        return savedSession.getString("installationId", null);
    }

    public static boolean saveFbInstallationId(String id, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("facebookRegister", 0).edit();
        editor.putString("installationId", id);
        return editor.commit();
    }

    public static String getFbInstallationId(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "facebookRegister", 0);
        return savedSession.getString("installationId", null);
    }

    public static boolean saveAccessToken(String token, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("access", 0).edit();
        editor.putString("accessToken", token);
        return editor.commit();
    }

    public static String getAccessToken(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "access", 0);
        return savedSession.getString("accessToken", null);
    }

    public static boolean saveRefreshToken(String token, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("refresh", 0).edit();
        editor.putString("refreshToken", token);
        return editor.commit();
    }

    public static String getRefreshToken(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "refresh", 0);
        return savedSession.getString("refreshToken", null);
    }

    public static boolean saveTimeExpiresIn(String expiresIn, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("expiresIn", 0).edit();
        editor.putString("expiresIn", expiresIn);
        return editor.commit();
    }

    public static String getTimeExpiresIn(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "expiresIn", 0);
        return savedSession.getString("expiresIn", null);
    }

    public static boolean saveLoginDate(String date, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("loginDate", 0).edit();
        editor.putString("date", date);
        return editor.commit();
    }

    public static String getLoginDate(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "loginDate", 0);
        return savedSession.getString("date", null);
    }

    public static boolean saveUserEmail(String mailId, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("email", 0).edit();
        editor.putString("mailId", mailId);
        return editor.commit();
    }

    public static String getUserEmail(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "email", 0);
        return savedSession.getString("mailId", null);
    }

    public static boolean saveUserName(String userName, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userName", 0).edit();
        editor.putString("userName", userName);
        return editor.commit();
    }

    public static String getUserName(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "userName", 0);
        return savedSession.getString("userName", null);
    }

    public static boolean saveUserPwd(String userPwd, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("userPwd", 0).edit();
        editor.putString("userPwd", userPwd);
        return editor.commit();
    }

    public static String getUserPwd(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "userPwd", 0);
        return savedSession.getString("userPwd", null);
    }

    public static boolean saveRegViaFb(String fb, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("facebook", 0).edit();
        editor.putString("fbReg", fb);
        return editor.commit();
    }

    public static String getRegViaFb(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "facebook", 0);
        return savedSession.getString("fbReg", null);
    }

    public static boolean saveFbAccessToken(String token, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("facebook", 0).edit();
        editor.putString("fbToken", token);
        return editor.commit();
    }

    public static String getFbAccessToken(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "facebook", 0);
        return savedSession.getString("fbToken", null);
    }

    public static boolean saveFbPresent(String fbPresent, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("facebook", 0).edit();
        editor.putString("fbPresent", fbPresent);
        return editor.commit();
    }

    public static String getFbPresent(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "facebook", 0);
        return savedSession.getString("fbPresent", null);
    }

    public static boolean saveCountry(String country, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("countryField", 0).edit();
        editor.putString("country", country);
        return editor.commit();
    }

    public static String getCountry(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "countryField", 0);
        return savedSession.getString("country", null);
    }

    public static boolean saveCity(String city, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("cityField", 0).edit();
        editor.putString("city", city);
        return editor.commit();
    }

    public static String getCity(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "cityField", 0);
        return savedSession.getString("city", null);
    }

    public static boolean saveWalletNumber(String walletNumber, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("walletNumber", 0).edit();
        editor.putString("walletNumber", walletNumber);
        return editor.commit();
    }

    public static String getWalletNumber(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "walletNumber", 0);
        return savedSession.getString("walletNumber", null);
    }

    public static boolean saveAccNumberStep2(String accNumberStep2, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("accNumberStep2", 0).edit();
        editor.putString("accNumberStep2", accNumberStep2);
        return editor.commit();
    }

    public static String getAccNumberStep2(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "accNumberStep2", 0);
        return savedSession.getString("accNumberStep2", null);
    }

    public static boolean saveAccNumberStep3(String accNumberStep3, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("accNumberStep3", 0).edit();
        editor.putString("accNumberStep3", accNumberStep3);
        return editor.commit();
    }

    public static String getAccNumberStep3(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "accNumberStep3", 0);
        return savedSession.getString("accNumberStep3", null);
    }

    public static boolean saveSavingsWalletNumber(String walletNumber, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("SavingsWalletNumber", 0).edit();
        editor.putString("SavingsWalletNumber", walletNumber);
        return editor.commit();
    }

    public static String getSavingsWalletNumber(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "SavingsWalletNumber", 0);
        return savedSession.getString("SavingsWalletNumber", null);
    }

    public static boolean saveDebtWalletNumber(String walletNumber, Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences("DebtWalletNumber", 0).edit();
        editor.putString("DebtWalletNumber", walletNumber);
        return editor.commit();
    }

    public static String getDebtWalletNumber(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "DebtWalletNumber", 0);
        return savedSession.getString("DebtWalletNumber", null);
    }

    public static boolean saveQuizEntry(String entered, Context context){
        editorQuiz = context.getSharedPreferences("Quiz_ques", 0).edit();
        editorQuiz.putString("quizQuesEntry", entered);
        return editorQuiz.commit();
    }

    public static String getQuizEntry(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "Quiz_ques", 0);
        return savedSession.getString("quizQuesEntry", null);
    }

    public static boolean saveQuesDate(String entered, Context context){
        editorQuiz = context.getSharedPreferences("Quiz_ques", 0).edit();
        editorQuiz.putString("quizQuesDate", entered);
        return editorQuiz.commit();
    }

    public static String getQuesDate(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "Quiz_ques", 0);
        return savedSession.getString("quizQuesDate", null);
    }

    public static boolean saveFinancialStep1(String entered, Context context){
        editorQuiz = context.getSharedPreferences("financial", 0).edit();
        editorQuiz.putString("finStep1", entered);
        return editorQuiz.commit();
    }

    public static String getFinancialStep1(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "financial", 0);
        return savedSession.getString("finStep1", null);
    }

    public static boolean saveFinancialStep2(String entered, Context context){
        editorQuiz = context.getSharedPreferences("financial", 0).edit();
        editorQuiz.putString("finStep2", entered);
        return editorQuiz.commit();
    }

    public static String getFinancialStep2(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "financial", 0);
        return savedSession.getString("finStep2", null);
    }

    public static boolean saveFinancialStep3(String entered, Context context){
        editorQuiz = context.getSharedPreferences("financial", 0).edit();
        editorQuiz.putString("finStep3", entered);
        return editorQuiz.commit();
    }

    public static String getFinancialStep3(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "financial", 0);
        return savedSession.getString("finStep3", null);
    }

    public static boolean saveFinancialStep4(String entered, Context context){
        editorQuiz = context.getSharedPreferences("financial", 0).edit();
        editorQuiz.putString("finStep4", entered);
        return editorQuiz.commit();
    }

    public static String getFinancialStep4(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "financial", 0);
        return savedSession.getString("finStep4", null);
    }

    public static void clearQuizQues(Context context) {
        editorQuiz = context.getSharedPreferences("Quiz_ques", 0).edit();
        editorQuiz.clear().commit();
    }

    public static boolean saveTransOverlay(String entered, Context context){
        editorQuiz = context.getSharedPreferences("trans", 0).edit();
        editorQuiz.putString("overlay", entered);
        return editorQuiz.commit();
    }

    public static String getTransOverlay(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "trans", 0);
        return savedSession.getString("overlay", null);
    }

    public static boolean saveBalancesOverlay(String entered, Context context){
        editorQuiz = context.getSharedPreferences("balances", 0).edit();
        editorQuiz.putString("overlay", entered);
        return editorQuiz.commit();
    }

    public static String getBalancesOverlay(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "balances", 0);
        return savedSession.getString("overlay", null);
    }

    public static boolean saveGoalsOverlay(String entered, Context context){
        editorQuiz = context.getSharedPreferences("goals", 0).edit();
        editorQuiz.putString("overlay", entered);
        return editorQuiz.commit();
    }

    public static String getGoalsOverlay(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "goals", 0);
        return savedSession.getString("overlay", null);
    }

    public static boolean saveSavingsType(String type, Context context){
        editorQuiz = context.getSharedPreferences("savings", 0).edit();
        editorQuiz.putString("type", type);
        return editorQuiz.commit();
    }

    public static String getSavingsType(Context context){
        SharedPreferences savedSession = context.getSharedPreferences(
                "savings", 0);
        return savedSession.getString("type", null);
    }

    public static boolean saveCurrencyCode(String currencyCode, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("currencyCode", 0).edit();
        editor.putString("countryCode", currencyCode);
        return editor.commit();
    }

    public static String getCurrencyCode(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "currencyCode", 0);
        return savedSession.getString("countryCode", null);
    }

    public static boolean setReferralEmailId(String referralId, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("referral", 0).edit();
        editor.putString("referralEmailId", referralId);
        return editor.commit();
    }

    public static String getReferralEmailId(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "referral", 0);
        return savedSession.getString("referralEmailId", null);
    }

    public static boolean setAnswer(String answer, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("quiz", 0).edit();
        editor.putString("answer", answer);
        return editor.commit();
    }

    public static String getAnswer(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "quiz", 0);
        return savedSession.getString("answer", null);
    }

    public static boolean setQuizQues(String ques, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("quiz", 0).edit();
        editor.putString("ques", ques);
        return editor.commit();
    }

    public static String getQuizQues(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "quiz", 0);
        return savedSession.getString("ques", null);
    }

    public static boolean setQuizAns(String answer, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("quiz", 0).edit();
        editor.putString("quesAnswer", answer);
        return editor.commit();
    }

    public static String getQuizAns(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "quiz", 0);
        return savedSession.getString("quesAnswer", null);
    }

    public static boolean setPlayPoints(String points, Context context) {
        SharedPreferences.Editor editor = context.getSharedPreferences("quiz", 0).edit();
        editor.putString("points", points);
        return editor.commit();
    }

    public static String getPlayPoints(Context context) {
        SharedPreferences savedSession = context.getSharedPreferences(
                "quiz", 0);
        return savedSession.getString("points", null);
    }
}
