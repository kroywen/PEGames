package com.pe.games.screen;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.pe.games.R;
import com.pe.games.adapter.GameAdapter;
import com.pe.games.database.DatabaseHelper;
import com.pe.games.model.Category;
import com.pe.games.model.Game;

public class GamesScreen extends BaseScreen implements OnItemClickListener, OnClickListener {
	
	protected ListView list;
	protected EditText search;
	protected Button add;
	protected List<Game> games;
	protected GameAdapter adapter;
	protected int categoryId;
		
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.games_screen);
		getIntentData();
		games = dbManager.getGames(categoryId);
		adapter = new GameAdapter(this, games);
		initializeViews();
	}
	
	protected void getIntentData() {
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(DatabaseHelper.FIELD_CATEGORY_ID))
			categoryId = intent.getIntExtra(DatabaseHelper.FIELD_CATEGORY_ID, 0);
	}
	
	protected void initializeViews() {
		Category category = dbManager.getCategoryById(categoryId);
		if (category != null)
			this.setTitle(category.getTitle());
		
		search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(filter);
		
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);
		
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
	}
	
	@Override
	public void onResume() {
		super.onResume();
		games = dbManager.getGames(categoryId);
		if (adapter == null) {
			adapter = new GameAdapter(this, games);
			list.setAdapter(adapter);
		} else {
			adapter.setGames(games);
			adapter.notifyDataSetChanged();
		}
	}
	
	
	@Override
	protected void onDestroy() {
	    super.onDestroy();
	    search.removeTextChangedListener(filter);
	}
	
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.add_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add:
	    	Intent intent = new Intent(this, AddGameScreen.class);
	    	intent.putExtra(DatabaseHelper.FIELD_CATEGORY_ID, categoryId);
	    	startActivity(intent);
	    	return true;
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}
	*/
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		if (v.getId() == R.id.list) {
			AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) menuInfo;
			Game game = adapter.getItem(info.position);
			menu.setHeaderTitle(game.getTitle());
			String myLessons = game.isMyLesson() ? getString(R.string.remove_from_my_lessons) : getString(R.string.add_to_my_lessons);
			String[] menuItems = {myLessons, getString(R.string.edit), getString(R.string.delete)};
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
		switch (menuItemIndex) {
		case 0:
			if (game.isMyLesson()) {
				game.setMyLesson(false);
				dbManager.removeFromMyLessons(game);
			} else {
				game.setMyLesson(true);
				dbManager.addToMyLessons(game);
			}
			break;
		case 1:
			Intent intent = new Intent(this, EditGameScreen.class);
			intent.putExtra(DatabaseHelper.FIELD_ID, game.getId());
			startActivity(intent);
			break;
		case 2:
			dbManager.deleteGame(game);
			games = dbManager.getGames(categoryId);
			adapter.setGames(games);
			adapter.notifyDataSetChanged();
			break;
		}
		return true;
	}
	
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int gameId = adapter.getItem(position).getId();
		Intent intent = new Intent(this, GameScreen.class);
		intent.putExtra(DatabaseHelper.FIELD_ID, gameId);
		startActivity(intent);
	}
	
	private TextWatcher filter = new TextWatcher() {
		public void afterTextChanged(Editable s) {}
	    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
	    public void onTextChanged(CharSequence s, int start, int before, int count) {
	    	adapter.filterGames(s);
	    }
	};

	public void onClick(View v) {
		if (v.getId() == R.id.add) {
			Intent intent = new Intent(this, AddGameScreen.class);
			intent.putExtra(DatabaseHelper.FIELD_CATEGORY_ID, categoryId);
			startActivity(intent);
		}
	}

}
