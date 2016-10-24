package com.eliqxir.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import android.location.Location;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;

public class Constant {

	
	public static String manualTipAmount = "null";	
	public static int tipPosition;	
	public static String textToShare = "I didn't need to stop at the store, I used ELIQXIR to get drinks brought to my door.";
	public static String twitter_consumer_key = "vLkzlSqjd0iQKeKfWN1YZT2OA";
	public static String twitter_secret_key = "mC4G62rMF0P8DXTNeUeHV345dJ6eASnr8SLlB4BcoZ6Y7w8kmz";
	public static double currentLattitue;
	public static double currentLongitude;
	public static String isVendorAvailable = "available";
	public static String isDriverAvailable = "available";
	public static double tipsPercent=0.0;
	public static double tipsAmount;	
	public static int selectedTabPosition = 2;
	public static String networkDisconected = "Network may be disconnected (or) too slow. Check your Network Settings.";
	public static String valueInvalid = "Value must not exceed more than two digits.";
	// public static String zipCode = "60611"; // Un Cork It
	// public static String zipCode = "60614"; // Milk & More
	// public static String zipCode = "60201"; // Evanston 1st Liquors
	// public static String zipCode = "60660"; // Independent Spirits, Inc.
	// public static String zipCode = "60612"; // Grand & Western Armanetti Fine
	// Wines & Liquors

	// public static String zipCode ="60610";
	public static String zipCode = "notAvailable";
	public static ArrayList<HashMap<String, String>> cartArray = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> subCategoriesHeader = new ArrayList<HashMap<String, String>>();
	public static ArrayList<HashMap<String, String>> subCategoriesArray = new ArrayList<HashMap<String, String>>();

	public static ArrayList<HashMap<String, String>> ParentaArray = new ArrayList<HashMap<String, String>>();

	public static HashMap<String, String> beerhashMapHeader = new HashMap<String, String>();
	public static HashMap<String, String> featuredhashMapHeader = new HashMap<String, String>();
	public static HashMap<String, String> winehashMapHeader = new HashMap<String, String>();
	public static HashMap<String, String> liquorhashMapHeader = new HashMap<String, String>();
	public static HashMap<String, String> mixerhashMapHeader = new HashMap<String, String>();
	
	public static String currentPager = "";

	public static String userAccessType = null;
	public static String visa = "^4[0-9x]{12}(?:[0-9x]{3})?$";
	public static String master = "^5[1-5][0-9x]{14}$";
	public static String amex = "^3[47][0-9x]{13}$";
	public static String diners = "^3(0[0-5]|[68][0-9])[0-9x]{11}$";
	public static String jcb = "^35(28|29|[3-8][0-9])[0-9x]{12}$";
	public static String maestro = "^(5[06-8]|6[0-9])[0-9x]{10,17}$";
	public static String discover = "^65[4-9][0-9]{13}|64[4-9][0-9]{13}|6011[0-9]{12}|(622(?:12[6-9]|1[3-9][0-9]|[2-8][0-9][0-9]|9[01][0-9]|92[0-5])[0-9]{10})$";

	public static String PHONE_REGEX = "\\([4-6]{1}[0-9]{2}\\) [0-9]{3}\\-[0-9]{4}$";
	// public static String PHONE_REGEX =
	// "\\(\\d{3}\\)\\d{3}\\-\\d{2}\\-\\d{2}";
	public static String priceRegex = "\\$[-0-9.,]+[-0-9.,a-zA-Z]*\b";

	public static boolean CheckCard(String cardNo, String cardToCheck) {
		Pattern sPattern = Pattern.compile(cardToCheck); // pattern
		// for
		// card

		boolean b = sPattern.matcher(cardNo).matches(); // card
		// validation
		Log.v("b >>", b + "");

		return b;
	}

	public static InputFilter filter = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (source.length() > 0) {
				if (!Character.isDigit(source.charAt(0)))
					return "";
				else {
					Log.e("Start Value",""+dstart);
					/* if ((dstart == 5) || (dstart == 9))
							return "-" + source;*/
					
					/*if (dstart == 3) {													
						return source + ") ";
					} else if (dstart == 0) {
						return "(" + source;
					} else if ((dstart == 5) || (dstart == 9))
						return "-" + source;
					else if (dstart >= 14)
						return "";*/
				}
			} else {
				//1-> 123-3454                (7number)  
				//2-> (123)456-4356      (10number) 
				//3-> +1-123-456-2345  (11 number)
			}
			return null;
		}
	};
	
	
	
	public static InputFilter priceFilter = new InputFilter() {

		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (source.length() > 0) {

				if (dstart == 0) {
					return "$" + source;
				} else {
					return source;
				}

			} else {

			}
			return null;
		}
	};	
	
	public static InputFilter filter_password = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2',
						'3', '4', '5', '6', '7', '8', '9', '0', '*', '#','!','%','&','^','$',
						'@'};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};
	
	public static InputFilter filter_email = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2',
						'3', '4', '5', '6', '7', '8', '9', '0','.','_',
						'@'};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};

	public static InputFilter filter_name = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2',
						'3', '4', '5', '6', '7', '8', '9', '0','.','_',' '};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};
	
	public static InputFilter filter_itemname = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','1', '2',
						'3', '4', '5', '6', '7', '8', '9', '0','-','@','.','_',' '};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};
	
	public static InputFilter filter_username = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z','.','_',' ','1', '2',
						'3', '4', '5', '6', '7', '8', '9', '0'};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};
	
	public static InputFilter filter_fname = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',' '};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};
	
	public static InputFilter filter_sizeValue = new InputFilter() {
		@Override
		public CharSequence filter(CharSequence source, int start, int end,
				Spanned dest, int dstart, int dend) {
			if (end > start) {
				char[] acceptedChars = new char[] { 'a', 'b', 'c', 'd',
						'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
						'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
						'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
						'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R',
						'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z', '1', '2',
						'3', '4', '5', '6', '7', '8', '9', '0',' '};
				for (int index = start; index < end; index++) {
					if (!new String(acceptedChars).contains(String
							.valueOf(source.charAt(index)))) {
						return "";
					}
				}
			}
			return null;
		}
	};

	public static double calculateDistance(double latitude, double longitude,
			String latitude1, String longitude1) {
		String distance = "";
		Location loc1 = new Location("");
		loc1.setLatitude(latitude);
		loc1.setLongitude(longitude);

		Location loc2 = new Location("");
		loc2.setLatitude(Double.parseDouble(latitude1));
		loc2.setLongitude(Double.parseDouble(longitude1));

		float distanceInMeters = loc1.distanceTo(loc2);
		double miles = distanceInMeters * 0.00062137119;
		System.out.println("Miles: " + miles);
		distance = String.format("%.1f", miles);

		return Double.parseDouble(distance);

	}
	
	/*//to check the entered value is alphanumeric
	public static boolean containAlphanumeric(final String str) {
		 byte counter = 0;
		 boolean checkdigit = false, checkchar = false;
		 for (int i = 0; i < str.length() && counter < 2; i++) {
		     // If we find a non-digit character we return false.
		     if (!checkdigit && Character.isDigit(str.charAt(i))) {
		  checkdigit = true;
		  counter++;
		     }
		     String a = String.valueOf(str.charAt(i));
		     Log.e("Displayed String is",a);
		     if (!checkchar && a.matches("[a-zA-Z]*")) {
		  checkchar = true;
		  counter++;
		     }
		 }
		 if (checkdigit && checkchar) {
			 Log.e("Both are true","Function is displayed");
		     return true;
		 }
		 return false;
		    }*/
}
