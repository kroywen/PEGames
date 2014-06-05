package com.pe.games.database;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.pe.games.model.Category;
import com.pe.games.model.Game;

public class DatabaseManager {
	
	protected static DatabaseManager instance;
	protected DatabaseHelper dbHelper;
	protected Context context;
	protected SQLiteDatabase db;
	
	public DatabaseManager(Context context) {
		this.context = context;
		dbHelper = new DatabaseHelper(context);
		db = dbHelper.getWritableDatabase();
	}
	
	public static DatabaseManager getInstance(Context context) {
		if (instance == null)
			instance = new DatabaseManager(context);
		return instance;
	}
	
	public List<Category> getCategories() {
		List<Category> categories = null;
		try {
			Cursor c = db.query(DatabaseHelper.TABLE_CATEGORIES, 
					null, null, null, null, null, null);
			if (c.moveToFirst()) {
				categories = new LinkedList<Category>();
				do {
					categories.add(new Category(
						c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_ID)),
						c.getString(c.getColumnIndex(DatabaseHelper.FIELD_TITLE))
					));
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return categories;
	}
	
	public Category getCategoryById(int id) {
		Category category = null;
		try {
			Cursor c = db.query(DatabaseHelper.TABLE_CATEGORIES, 
					null, DatabaseHelper.FIELD_ID+"="+id, null, null, null, null);
			if (c.moveToFirst()) {
				category = new Category(
					c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_ID)),
					c.getString(c.getColumnIndex(DatabaseHelper.FIELD_TITLE))
				);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return category;
	}
	
	public long addCategory(Category category) {
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.FIELD_TITLE, category.getTitle());
		return db.insert(DatabaseHelper.TABLE_CATEGORIES, null, values);
	}
	
	public void updateCategory(Category category) {
		if (category == null)
			return;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.FIELD_TITLE, category.getTitle());
		db.update(DatabaseHelper.TABLE_CATEGORIES, values, DatabaseHelper.FIELD_ID+"="+category.getId(), null);
	}

	public void deleteCategory(Category category) {
		if (category == null)
			return;
		db.delete(DatabaseHelper.TABLE_CATEGORIES, DatabaseHelper.FIELD_ID+"="+category.getId(), null);
	}
	
	public void deleteGamesByCategoryId(int categoryId) {
		db.delete(DatabaseHelper.TABLE_GAMES, DatabaseHelper.FIELD_CATEGORY_ID+"="+categoryId, null);
	}
	
	public List<Game> getGames(int categoryId) {
		List<Game> games = null;
		try {
			String where = (categoryId == Game.ALL) ? null : 
				(categoryId == Game.MY_LESSONS) ? DatabaseHelper.FIELD_MY_LESSONS+"=1" : 
					DatabaseHelper.FIELD_CATEGORY_ID+"="+categoryId;
			Cursor c = db.query(DatabaseHelper.TABLE_GAMES, 
					null, where, null, null, null, null);
			if (c.moveToFirst()) {
				games = new LinkedList<Game>();
				do {
					games.add(new Game(
						c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_ID)),
						c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_CATEGORY_ID)),
						c.getString(c.getColumnIndex(DatabaseHelper.FIELD_TITLE)),
						c.getString(c.getColumnIndex(DatabaseHelper.FIELD_AREA)),
						c.getString(c.getColumnIndex(DatabaseHelper.FIELD_EQUIPMENT)),
						c.getString(c.getColumnIndex(DatabaseHelper.FIELD_INSTRUCTIONS)),
						c.getString(c.getColumnIndex(DatabaseHelper.FIELD_VARIATION)),
						c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_MY_LESSONS)) == 1 ? true : false
					));
				} while (c.moveToNext());
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return games;
	}
	
	public Game getGameById(int id) {
		Game game = null;
		try {
			Cursor c = db.query(DatabaseHelper.TABLE_GAMES, 
					null, DatabaseHelper.FIELD_ID+"="+id, null, null, null, null);
			if (c.moveToFirst()) {
				game = new Game(
					c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_ID)),
					c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_CATEGORY_ID)),
					c.getString(c.getColumnIndex(DatabaseHelper.FIELD_TITLE)),
					c.getString(c.getColumnIndex(DatabaseHelper.FIELD_AREA)),
					c.getString(c.getColumnIndex(DatabaseHelper.FIELD_EQUIPMENT)),
					c.getString(c.getColumnIndex(DatabaseHelper.FIELD_INSTRUCTIONS)),
					c.getString(c.getColumnIndex(DatabaseHelper.FIELD_VARIATION)),
					c.getInt(c.getColumnIndex(DatabaseHelper.FIELD_MY_LESSONS)) == 1 ? true : false
				);
			}
			c.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return game;
	}
	
	public void addToMyLessons(Game game) {
		if (game == null)
			return;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.FIELD_MY_LESSONS, 1);
		db.update(DatabaseHelper.TABLE_GAMES, values, DatabaseHelper.FIELD_ID+"="+game.getId(), null);
	}
	
	public void removeFromMyLessons(Game game) {
		if (game == null)
			return;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.FIELD_MY_LESSONS, 0);
		db.update(DatabaseHelper.TABLE_GAMES, values, DatabaseHelper.FIELD_ID+"="+game.getId(), null);
	}
	
	public Game getRandomGame() {
		Game game = null;
		List<Game> games = getGames(Game.ALL);
		if (games != null && !games.isEmpty()) {
			Random random = new Random();
			int index = random.nextInt(games.size());
			game = games.get(index);
		}
		return game;
	}
	
	public boolean addGame(Game game) {
		if (game == null || game.getTitle() == null || game.getTitle().equals(""))
			return false;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.FIELD_CATEGORY_ID, game.getCategoryId());
		values.put(DatabaseHelper.FIELD_TITLE, game.getTitle());
		values.put(DatabaseHelper.FIELD_AREA, game.getArea());
		values.put(DatabaseHelper.FIELD_EQUIPMENT, game.getEquipment());
		values.put(DatabaseHelper.FIELD_INSTRUCTIONS, game.getInstructions());
		values.put(DatabaseHelper.FIELD_VARIATION, game.getVariation());
		long result = db.insert(DatabaseHelper.TABLE_GAMES, null, values);
		return result != -1L;
	}
	
	public void deleteGame(Game game) {
		if (game == null)
			return;
		db.delete(DatabaseHelper.TABLE_GAMES, DatabaseHelper.FIELD_ID+"="+game.getId(), null);
	}
	
	public boolean updateGame(Game game) {
		if (game == null)
			return false;
		ContentValues values = new ContentValues();
		values.put(DatabaseHelper.FIELD_CATEGORY_ID, game.getCategoryId());
		values.put(DatabaseHelper.FIELD_TITLE, game.getTitle());
		values.put(DatabaseHelper.FIELD_AREA, game.getArea());
		values.put(DatabaseHelper.FIELD_EQUIPMENT, game.getEquipment());
		values.put(DatabaseHelper.FIELD_INSTRUCTIONS, game.getInstructions());
		values.put(DatabaseHelper.FIELD_VARIATION, game.getVariation());
		long result = db.update(DatabaseHelper.TABLE_GAMES, values, DatabaseHelper.FIELD_ID+"="+game.getId(), null);
		return result != -1L;
	}

}