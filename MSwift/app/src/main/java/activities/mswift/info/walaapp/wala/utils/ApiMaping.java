package activities.mswift.info.walaapp.wala.utils;

/**
 * Created by kruno on 25.01.16..
 */
public class ApiMaping {

    // our categories
    private static final String HOME = "HOME";
    private static final String GROCERIES = "GROCERIES";
    private static final String TRANSPORTATION = "TRANSPORTATION";
    private static final String HEALTH = "HEALTH";
    private static final String EDUCATION = "EDUCATION";
    private static final String UTILITIES = "UTILITIES";
    private static final String SHOPPING = "SHOPPING";
    private static final String DININGOUT = "DINING OUT";
    private static final String ENTERTAINMENT = "ENTERTAINMENT";
    private static final String TRAVEL = "TRAVEL";
    private static final String CREDIT = "CREDIT";
    private static final String DEBT = "DEBT";

    //api categories just a test
    private static final String CHILDCARE = "ChildCare";
    private static final String CHILDSUPPORTPAYMENTS = "ChildSupportPayments";
    private static final String CLOTHINGFOOTWARE = "ClothingFootware";

    public static String apiCoding(String s){

        if(s.equals(HOME)){
            s = CHILDCARE;
        }
        if(s.equals(GROCERIES)){
            s = CHILDSUPPORTPAYMENTS;
        }
        if (s.equals(TRANSPORTATION)){
            s = CLOTHINGFOOTWARE;
        }
        return s;
    }

    public static String apiDecoding(String s){

        if(s.equals(CHILDCARE)){
            s = HOME;
        }
        if(s.equals(CHILDSUPPORTPAYMENTS)){
            s = GROCERIES;
        }
        if (s.equals(CLOTHINGFOOTWARE)){
            s = TRANSPORTATION;
        }
        return s;
    }

}
