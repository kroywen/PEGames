package com.pe.games.screen;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.pe.games.R;
import com.pe.games.adapter.GameAdapter;
import com.pe.games.database.DatabaseHelper;
import com.pe.games.model.Game;

public class MyLessonsScreen extends BaseScreen implements OnItemClickListener {
	
	protected ListView list;
	protected List<Game> games;
	protected GameAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.my_lessons_screen);
		initializeViews();
	}
	
	protected void initializeViews() {
		list = (ListView) findViewById(R.id.list);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getParent().setTitle(R.string.my_lessons);
		games = dbManager.getGames(Game.MY_LESSONS);
		if (adapter == null) {
			adapter = new GameAdapter(this, games);
			list.setAdapter(adapter);
		} else {
			adapter.setGames(games);
			adapter.notifyDataSetChanged();
		}
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int gameId = adapter.getItem(position).getId();
		Intent intent = new Intent(this, GameScreen.class);
		intent.putExtra(DatabaseHelper.FIELD_ID, gameId);
		startActivity(intent);
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Game game = adapter.getItem(info.position);
			menu.setHeaderTitle(game.getTitle());
			String[] menuItems = {getString(R.string.remove)};
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Game game = adapter.getItem(info.position);
		int menuItemIndex = item.getItemId();
		if (menuItemIndex == 0) {
			dbManager.removeFromMyLessons(game);
			games = dbManager.getGames(Game.MY_LESSONS);
			adapter.setGames(games);
			adapter.notifyDataSetChanged();
		}
		return true;
	}

}
