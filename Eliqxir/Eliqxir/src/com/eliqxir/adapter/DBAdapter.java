package com.eliqxir.adapter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBAdapter {
		public static final String KEY_ID ="id";
		public static final String KEY_QUANTITY = "quantity";
		public static final String KEY_ORDER_NAME = "name";
		public static final String KEY_ORDER_DESC = "desc";
		public static final String KEY_ORDER_INITIAL_COST = "initialCost";
		public static final String KEY_ORDER_COST = "cost";
		public static final String KEY_ITEM_ID = "itemId";
		public static final String KEY_OPTION_ID = "optionId";
		public static final String KEY_SPINNER_ID = "spinnerId";
		public static final String KEY_ORDER_TOATAL = "totalQty";
		public static final String KEY_ITEM_NAME = "itemName";
		public static final String KEY_USER_ID = "userId";
		public static final String KEY_ITEM_TAX="tax";
		private static final String TAG = "DBAdapter";
		private static final String DATABASE_NAME = "Eliqxir";
		private static final String DATABASE_TABLE = "Cart";
		
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_CREATE ="create table Cart (id integer primary key autoincrement,quantity text not null,name text not null,desc text not null,initialCost text not null,cost text not null,itemId text not null,optionId text not null,spinnerId text not null,totalQty text not null,itemName text not null);";
		private final Context context;
		private DatabaseHelper DBHelper;
		private SQLiteDatabase db;
		
		public DBAdapter(Context ctx)
		{
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
		}
		
		private static class DatabaseHelper extends SQLiteOpenHelper
		{
		DatabaseHelper(Context context)
		{
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		@Override
		public void onCreate(SQLiteDatabase db)
		{
		try {
		db.execSQL(DATABASE_CREATE);
		} catch (SQLException e) {
		e.printStackTrace();
		}
		}
		
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
		{
		Log.w(TAG, "Upgrading database from version " + oldVersion + "to"
		+ newVersion + ", which will destroy all old data");
		db.execSQL("DROP TABLE IF EXISTS VideoName");
		
		onCreate(db);
		}
		}
		
		//---opens the database---
		public DBAdapter open() throws SQLException
		{
		db = DBHelper.getWritableDatabase();
		return this;
		}
		
		//---closes the database---
		public void close()
		{
		DBHelper.close();
		}
		
		//---insert a contact into the database---
		public long insertToCart(String qty, String orderName, String orderDesc, String initialCost, String orderCost, String itemId, String optionId, String spinnerId, String totalQty, String itemName)
		{
			long tableDetails = 0;
			
			ContentValues initialValues = new ContentValues();
			initialValues.put(KEY_QUANTITY, qty);
			initialValues.put(KEY_ORDER_NAME, orderName);
			initialValues.put(KEY_ORDER_DESC, orderDesc);
			initialValues.put(KEY_ORDER_INITIAL_COST, initialCost);
			initialValues.put(KEY_ORDER_COST, orderCost);
			initialValues.put(KEY_ITEM_ID, itemId);
			initialValues.put(KEY_OPTION_ID, optionId);
			initialValues.put(KEY_SPINNER_ID, spinnerId);
			initialValues.put(KEY_ORDER_TOATAL, totalQty);
			initialValues.put(KEY_ITEM_NAME, itemName);
		//	initialValues.put(KEY_ITEM_TAX, tax);
			tableDetails= db.insert(DATABASE_TABLE, null, initialValues);
			
			return tableDetails;
		}
		
		public int deleteRows()
	    {
	        return db.delete(DATABASE_TABLE, null, null);
	    }
		
		public Cursor getAllCarts()
		{			
			Cursor cur=db.rawQuery("SELECT * FROM " + DATABASE_TABLE,null);
			
			if  (cur.moveToFirst()) {
		        do {
		        	@SuppressWarnings("unused")
					String id = cur.getString(0);
		        	@SuppressWarnings("unused")
					String qty = cur.getString(1);
		        }while (cur.moveToNext());
		   //     cur.close();
		    }			
			return cur;
		}
		
		public boolean getCartItem(String itemId, String optionId, String spinnerId)
		{
			Cursor cur = db.rawQuery("SELECT * FROM " + DATABASE_TABLE+" WHERE "+KEY_ITEM_ID + "='"+ itemId+"' AND "+KEY_OPTION_ID+"='"+optionId+"' AND "+KEY_SPINNER_ID+"='"+spinnerId+"'",null);
			
			/*Cursor cur = db.query(DATABASE_TABLE, new String[] { KEY_ITEM_ID }, 
		            KEY_ITEM_ID + " = ? and "+ KEY_OPTION_ID + " = ? and " + KEY_SPINNER_ID + " = ?" , 
		            new String[] {itemId,optionId,spinnerId}, 
		            null, null, null, null);*/
			
			if(cur.getCount()>0){
				return true;
			}else{
				return false;
			}
			
		}
		
		public boolean updateCart(String itemId, String optionId, String spinnerId, String qty, String price)
		{
			ContentValues args = new ContentValues();
			args.put(KEY_QUANTITY, qty);
			args.put(KEY_ORDER_COST, price);
			return db.update(DATABASE_TABLE, args, KEY_ITEM_ID + "='"+ itemId+"' AND "+KEY_OPTION_ID+"='"+optionId+"' AND "+KEY_SPINNER_ID+"='"+spinnerId+"'", null) > 0;
		}
		
		public int deleteCartItem(String itemId, String optionId, String spinnerId)
	    {
	        return db.delete(DATABASE_TABLE, KEY_ITEM_ID + "='"+ itemId+"' AND "+KEY_OPTION_ID+"='"+optionId+"' AND "+KEY_SPINNER_ID+"='"+spinnerId+"'", null);
	    }
		
		}
