package com.pe.games.screen;

import java.util.List;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListView;

import com.pe.games.R;
import com.pe.games.adapter.GameAdapter;
import com.pe.games.database.DatabaseHelper;
import com.pe.games.model.Game;

public class AlphabeticalScreen extends BaseScreen implements OnItemClickListener {
	
	protected ListView list;
	protected EditText search;
	protected List<Game> games;
	protected GameAdapter adapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.alphabetical_screen);
		games = dbManager.getGames(Game.ALL);
		adapter = new GameAdapter(this, games);
		initializeViews();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getParent().setTitle(R.string.game_book);
	}
	
	protected void initializeViews() {
		search = (EditText) findViewById(R.id.search);
		search.addTextChangedListener(filter);
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
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

}
