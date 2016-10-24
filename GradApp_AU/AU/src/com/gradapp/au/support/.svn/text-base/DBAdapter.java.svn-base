package com.gradapp.au.support;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter {
	public static final String KEY_ID = "id";
	public static final String KEY_HAMBURGER_ID = "hamburger_id";
	public static final String KEY_ROLE_ID = "role_id";
	public static final String KEY_HAMBURGER_NAME = "hamburger_name";
	public static final String KEY_SCHOOL_ID = "school_id";
	public static final String KEY_UNIV_ID = "univ_id";
	public static final String KEY_STATIC_PAGE_TITLE = "title";
	public static final String KEY_STATIC_PAGE_CONTENT = "content";
	public static final String KEY_MYSCHEDULE_DATE = "scheduleDate";
	public static final String KEY_MYSCHEDULE_ID = "eventId";
	public static final String KEY_MYSCHEDULE_NAME = "eventName";
	public static final String KEY_MYSCHEDULE_TIME = "eventTime";
	public static final String KEY_MYSCHEDULE_DESC_NAME = "eventName";
	public static final String KEY_MYSCHEDULE_DESC_LOC = "eventLocation";
	public static final String KEY_MYSCHEDULE_DESC = "eventDesc";
	public static final String KEY_MYSCHEDULE_DESC_TIMING = "eventTiming";
	public static final String KEY_NOTIFICATION_ID = "notifyId";
	public static final String KEY_NOTIFICATION_TITLE = "title";
	public static final String KEY_NOTIFICATION_CONTENT = "content";
	public static final String KEY_NOTIFICATION_COLOR = "color";
	public static final String KEY_NOTIFICATION_DAY = "day";
	public static final String KEY_NOTIFICATION_MONTH = "month";
	public static final String KEY_NOTIFICATION_TIMING = "timing";
	public static final String KEY_FAQ_QUESTION = "question";
	public static final String KEY_FAQ_ANSWER = "answer";
	public static final String KEY_IMAGE_PATH = "imagePath";

	private static final String DATABASE_NAME = "CBU";
	private static final String DATABASE_TABLE = "HamburgerNames";
	private static final String DATABASE_TABLE_STATIC_PAGES = "StaticPages";
	private static final String DATABASE_TABLE_MYSCHEDULES = "MySchedules";
	private static final String DATABASE_TABLE_MYSCHEDULES_DESC = "MySchedule_Desc";
	private static final String DATABASE_TABLE_NOTIFICATION = "Notification";
	private static final String DATABASE_TABLE_FAQ = "FAQ";
	private static final String DATABASE_TABLE_IMAGEPATH = "ImagePath";

	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_CREATE = "create table HamburgerNames (id integer primary key autoincrement,hamburger_id text not null,hamburger_name text not null);";
	private static final String DATABASE_CREATE_STATIC_PAGES = "create table StaticPages (id integer primary key autoincrement,hamburger_id text not null,title text not null,content text not null);";
	private static final String DATABASE_CREATE_MYSCHEDULES = "create table MySchedules (id integer primary key autoincrement,scheduleDate text not null,eventId text not null,eventName text not null,eventTime text not null);";
	private static final String DATABASE_CREATE_MYSCHEDULES_DESC = "create table MySchedule_Desc (id integer primary key autoincrement,eventId text not null,eventName text not null,eventLocation text not null,eventDesc text not null,eventTime text not null,eventTiming text not null);";
	private static final String DATABASE_CREATE_NOTIFICATION = "create table Notification (id integer primary key autoincrement,notifyId text not null,title text not null,content text not null,color text not null,day text not null,month text not null,timing text not null);";
	private static final String DATABASE_CREATE_FAQ = "create table FAQ (id integer primary key autoincrement,question text not null,answer text not null);";
	private static final String DATABASE_CREATE_IMAGEPATH = "create table ImagePath (id integer primary key autoincrement,imagePath text not null);";
	private final Context context;
	private DatabaseHelper DBHelper;
	private SQLiteDatabase db;

	public DBAdapter(Context ctx) {
		this.context = ctx;
		DBHelper = new DatabaseHelper(context);
	}

	private static class DatabaseHelper extends SQLiteOpenHelper {
		DatabaseHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			try {
				//tables
				db.execSQL(DATABASE_CREATE);
				db.execSQL(DATABASE_CREATE_STATIC_PAGES);
				db.execSQL(DATABASE_CREATE_MYSCHEDULES);
				db.execSQL(DATABASE_CREATE_MYSCHEDULES_DESC);
				db.execSQL(DATABASE_CREATE_NOTIFICATION);
				db.execSQL(DATABASE_CREATE_FAQ);
				db.execSQL(DATABASE_CREATE_IMAGEPATH);
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS HamburgerNames");
			onCreate(db);
		}
	}

	// ---opens the database---
	public DBAdapter open() throws SQLException {
		db = DBHelper.getWritableDatabase();
		return this;
	}

	// ---closes the database---
	public void close() {
		DBHelper.close();
	}

	// ---insert hamburger names into the database---
	public long insertToHambrgr(String hambgrId, String hambgrName) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_HAMBURGER_ID, hambgrId);
		initialValues.put(KEY_HAMBURGER_NAME, hambgrName);
		tableDetails = db.insert(DATABASE_TABLE, null, initialValues);

		return tableDetails;
	}

	// ---insert static pages content into the database---
	public long insertToStaticPages(String hambgrId, String title,
			String content) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_HAMBURGER_ID, hambgrId);
		initialValues.put(KEY_STATIC_PAGE_TITLE, title);
		initialValues.put(KEY_STATIC_PAGE_CONTENT, content);
		tableDetails = db.insert(DATABASE_TABLE_STATIC_PAGES, null,
				initialValues);

		return tableDetails;
	}

	// ---insert MySchedule events into the database---
	public long insertToMySchedules(String date, String eventId,
			String eventName, String eventTime) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MYSCHEDULE_DATE, date);
		initialValues.put(KEY_MYSCHEDULE_ID, eventId);
		initialValues.put(KEY_MYSCHEDULE_NAME, eventName);
		initialValues.put(KEY_MYSCHEDULE_TIME, eventTime);
		tableDetails = db.insert(DATABASE_TABLE_MYSCHEDULES, null,
				initialValues);

		return tableDetails;
	}

	// ---insert MySchedule event details into the database---
	public long insertToMyScheduleDetails(String eventId, String eventName,
			String eventDesc, String eventLoc, String eventTime,
			String eventTiming) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_MYSCHEDULE_ID, eventId);
		initialValues.put(KEY_MYSCHEDULE_DESC_NAME, eventName);
		initialValues.put(KEY_MYSCHEDULE_DESC, eventDesc);
		initialValues.put(KEY_MYSCHEDULE_DESC_LOC, eventLoc);
		initialValues.put(KEY_MYSCHEDULE_TIME, eventTime);
		initialValues.put(KEY_MYSCHEDULE_DESC_TIMING, eventTiming);
		tableDetails = db.insert(DATABASE_TABLE_MYSCHEDULES_DESC, null,
				initialValues);

		return tableDetails;
	}

	// ---insert Notifications into the database---
	public long insertToNotification(String notifyId, String title,
			String content, String color, String day, String month,
			String timing) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_NOTIFICATION_ID, notifyId);
		initialValues.put(KEY_NOTIFICATION_TITLE, title);
		initialValues.put(KEY_NOTIFICATION_CONTENT, content);
		initialValues.put(KEY_NOTIFICATION_COLOR, color);
		initialValues.put(KEY_NOTIFICATION_DAY, day);
		initialValues.put(KEY_NOTIFICATION_MONTH, month);
		initialValues.put(KEY_NOTIFICATION_TIMING, timing);
		tableDetails = db.insert(DATABASE_TABLE_NOTIFICATION, null,
				initialValues);

		return tableDetails;
	}
	
	// ---insert FAQ into the database---
	public long insertToFAQ(String ques, String ans) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_FAQ_QUESTION, ques);
		initialValues.put(KEY_FAQ_ANSWER, ans);
		tableDetails = db.insert(DATABASE_TABLE_FAQ, null,
				initialValues);

		return tableDetails;
	}
	
	// ---insert ImagePaths into the database---
	public long insertToImagePath(String path) {
		long tableDetails = 0;

		ContentValues initialValues = new ContentValues();
		initialValues.put(KEY_IMAGE_PATH, path);
		tableDetails = db.insert(DATABASE_TABLE_IMAGEPATH, null,
				initialValues);

		return tableDetails;
	}

	// ---delete Hamburger Names from the database---
	public int deleteRows() {
		return db.delete(DATABASE_TABLE, null, null);
	}

	// ---delete MySchedules from the database---
	public int deleteMySchedules() {
		return db.delete(DATABASE_TABLE_MYSCHEDULES, null, null);
	}

	// ---delete Notifications from the database---
	public int deleteNotifications() {
		return db.delete(DATABASE_TABLE_NOTIFICATION, null, null);
	}
	
	// ---delete FAQ from the database---
	public int deleteFaq() {
		return db.delete(DATABASE_TABLE_FAQ, null, null);
	}

	// ---Fetch All Hamburger names from the database---
	public Cursor getAllHambrgrNames() {

		Cursor cur = db.rawQuery("SELECT * FROM " + DATABASE_TABLE, null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
			// cur.close();
		}

		return cur;
	}

	// ---Fetch All Static Pages from the database---
	public Cursor getAllStaticPages() {

		Cursor cur = db.rawQuery(
				"SELECT * FROM " + DATABASE_TABLE_STATIC_PAGES, null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
		}

		return cur;
	}

	// ---Fetch All MySchedules from the database---
	public Cursor getAllMySchedules() {

		Cursor cur = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_MYSCHEDULES,
				null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
		}
		return cur;
	}

	// ---Fetch particular event details with condition of date from the database---
	public Cursor getMySchedulesItem(String date) {

		Cursor cur = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_MYSCHEDULES
				+ " WHERE " + KEY_MYSCHEDULE_DATE + "='" + date + "'", null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
		}
		return cur;
	}

	// ---Fetch particular event details with condition of eventId from the database---
	public Cursor getMySchedulesDesc(String eventId) {

		Cursor cur = db.rawQuery("SELECT * FROM "
				+ DATABASE_TABLE_MYSCHEDULES_DESC + " WHERE "
				+ KEY_MYSCHEDULE_ID + "='" + eventId + "'", null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
		}
		return cur;
	}

	// ---Fetch All Notifications from the database---
	public Cursor getAllNotifications() {

		Cursor cur = db.rawQuery(
				"SELECT * FROM " + DATABASE_TABLE_NOTIFICATION, null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
		}
		return cur;
	}

	// ---Fetch All FAQ from the database---
	public Cursor getAllFAQ() {

		Cursor cur = db.rawQuery(
				"SELECT * FROM " + DATABASE_TABLE_FAQ, null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String hambgrId = cur.getString(1);
			} while (cur.moveToNext());
		}
		return cur;
	}
	
	// ---Fetch All ImagePaths from the database---
	public Cursor getAllImagePaths() {

		Cursor cur = db.rawQuery("SELECT * FROM " + DATABASE_TABLE_IMAGEPATH,
				null);

		if (cur.moveToFirst()) {
			do {
				@SuppressWarnings("unused")
				String id = cur.getString(0);
				@SuppressWarnings("unused")
				String path = cur.getString(1);
			} while (cur.moveToNext());
		}

		return cur;
	}
	
	// ---delete ImagePaths from the database---
	public int deleteImagePath(String path) {
		return db.delete(DATABASE_TABLE_IMAGEPATH, KEY_IMAGE_PATH + "='"
				+ path + "'", null);
	}
	
	// ---Update Static Pages into the database---
	public boolean updateStaticPages(String hambrgrId, String title,
			String content) {
		ContentValues args = new ContentValues();
		args.put(KEY_STATIC_PAGE_TITLE, title);
		args.put(KEY_STATIC_PAGE_CONTENT, content);
		return db.update(DATABASE_TABLE_STATIC_PAGES, args, KEY_HAMBURGER_ID
				+ "='" + hambrgrId + "'", null) > 0;
	}

	// ---Update MySchedule desc into the database---
	public boolean updateMySchedulesDesc(String eventId, String eventName,
			String eventDesc, String eventLoc, String eventTime,
			String eventTiming) {
		ContentValues args = new ContentValues();
		args.put(KEY_MYSCHEDULE_DESC_NAME, eventName);
		args.put(KEY_MYSCHEDULE_DESC, eventDesc);
		args.put(KEY_MYSCHEDULE_DESC_LOC, eventLoc);
		args.put(KEY_MYSCHEDULE_TIME, eventTime);
		args.put(KEY_MYSCHEDULE_DESC_TIMING, eventTiming);
		return db.update(DATABASE_TABLE_MYSCHEDULES_DESC, args,
				KEY_MYSCHEDULE_ID + "='" + eventId + "'", null) > 0;
	}

}
