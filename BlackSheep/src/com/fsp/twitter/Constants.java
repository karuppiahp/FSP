package com.fsp.twitter;

public class Constants {
	
	
//original
	public static final String CONSUMER_KEY = "135lhH63mRxS9SQIVtJ3A";
	public static final String CONSUMER_SECRET= "ogeeqZVy6lnCvbL9TB5M1bJsH4j4pPyzuOYWjiDis";
	//test
//	public static final String CONSUMER_KEY = "JLaf1cCWiw2hVfdhsX4oKoxny";
//	public static final String CONSUMER_SECRET= "4VqKMjvLEemNv3SeyQU69fCoTx5PXLHn0awCJusqgDwUGnMxuu";
	
	
	public static final String REQUEST_URL = "https://api.twitter.com/oauth/request_token";
	public static final String ACCESS_URL = "https://api.twitter.com/oauth/access_token";
	public static final String AUTHORIZE_URL = "https://api.twitter.com/oauth/authorize";
	
public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow-twitter";
//	public static final String	OAUTH_CALLBACK_SCHEME	= "x-oauthflow";
	public static final String	OAUTH_CALLBACK_HOST		= "callback";
	public static final String	OAUTH_CALLBACK_URL		= OAUTH_CALLBACK_SCHEME + "://" + OAUTH_CALLBACK_HOST;

}

