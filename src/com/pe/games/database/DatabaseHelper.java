package com.pe.games.database;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.pe.games.R;

public class DatabaseHelper extends SQLiteOpenHelper {
	
	public static final String DATABASE_NAME = "pe_games";
	public static final int DATABASE_VERSION = 1; 
	
	public static final String TABLE_CATEGORIES = "categories";
	public static final String TABLE_GAMES = "games";
	
	public static final String FIELD_ID = "id";
	public static final String FIELD_TITLE = "title";
	public static final String FIELD_CATEGORY_ID = "category_id";
	public static final String FIELD_AREA = "area";
	public static final String FIELD_EQUIPMENT = "equipment";
	public static final String FIELD_INSTRUCTIONS = "instructions";
	public static final String FIELD_VARIATION = "variation";
	public static final String FIELD_MY_LESSONS = "my_lessons";
	
	public static final String CREATE_TABLE_CATEGORIES = 
			"create table if not exists " + TABLE_CATEGORIES + " (" +
			FIELD_ID + " integer primary key autoincrement, " +
			FIELD_TITLE + " text not null);";
	public static final String DROP_TABLE_CATEGORIES = 
			"drop table if exists " + TABLE_CATEGORIES;
	
	public static final String CREATE_TABLE_GAMES = 
			"create table if not exists " + TABLE_GAMES + " (" +
			FIELD_ID + " integer primary key autoincrement, " +
			FIELD_CATEGORY_ID + " integer not null, " +
			FIELD_TITLE + " text not null, " +
			FIELD_AREA + " text, " +
			FIELD_EQUIPMENT + " text, " +
			FIELD_INSTRUCTIONS + " text, " +
			FIELD_VARIATION + " text, " +
			FIELD_MY_LESSONS + " integer default 0);";
	public static final String DROP_TABLE_GAMES = 
			"drop table if exists " + TABLE_GAMES;
	
	protected Context context;
	
	public DatabaseHelper(Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.context = context;
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		createTables(db);
		importCsvToDatabase(db);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		dropTables(db);
		onCreate(db);
	}
	
	protected void createTables(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CATEGORIES);
		db.execSQL(CREATE_TABLE_GAMES);
	}
	
	protected void dropTables(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE_CATEGORIES);
		db.execSQL(CREATE_TABLE_GAMES);
	}
	
	public void importCsvToDatabase(SQLiteDatabase db) {
		String[] files = context.getResources().getStringArray(R.array.files);
		if (files != null && files.length > 0) {
			for (int i=0; i<files.length; i++) {
				String categoryTitle = files[i].substring(0, files[i].length()-4).replace('_', ' ');
				ContentValues categoryValues = new ContentValues();
				categoryValues.put(FIELD_TITLE, categoryTitle);
				db.insert(TABLE_CATEGORIES, null, categoryValues);
				try {
			        BufferedReader br = new BufferedReader(new InputStreamReader(
			        		context.getAssets().open(files[i])));
			        String reader = "";
			        while ((reader = br.readLine()) != null) {
			            String[] data = reader.split("___");
			            if (data.length == 5) {
			            	ContentValues gameValues = new ContentValues();
			            	gameValues.put(FIELD_CATEGORY_ID, i+1);
			            	gameValues.put(FIELD_TITLE, data[0]);
			            	gameValues.put(FIELD_AREA, data[1]);
			            	gameValues.put(FIELD_EQUIPMENT, data[2]);
			            	gameValues.put(FIELD_INSTRUCTIONS, data[3]);
			            	gameValues.put(FIELD_VARIATION, data[4]);
				            db.insert(TABLE_GAMES, null, gameValues);
			            }
			        }
			        br.close();
			    } catch (IOException e) {
			        e.printStackTrace();
			    }
			}
			
		}
	}
	

}
