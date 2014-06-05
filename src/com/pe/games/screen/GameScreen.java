package com.pe.games.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.pe.games.R;
import com.pe.games.database.DatabaseHelper;
import com.pe.games.model.Game;
import com.pe.games.util.TemplateParser;

public class GameScreen extends BaseScreen implements OnClickListener {
	
	protected Button add;
	protected WebView webView;
	protected Game game;
	protected int gameId;
	protected TemplateParser templateParser;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.game_screen);
		getIntentData();
		game = dbManager.getGameById(gameId);
		templateParser = new TemplateParser(this, game);
		initializeViews();
	}
	
	protected void getIntentData() {
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(DatabaseHelper.FIELD_ID))
			gameId = intent.getIntExtra(DatabaseHelper.FIELD_ID, 0);
	}
	
	protected void initializeViews() {
		if (game != null)
			setTitle(game.getTitle());
		
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);
		add.setText(game.isMyLesson() ? R.string.remove_from_my_lessons : R.string.add_to_my_lessons);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.loadData(templateParser.getPreparedHtml(), "text/html", "UTF-8");
		webView.setBackgroundColor(0);
	}
	
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.main_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.add:
	    	dbManager.addToMyLessons(game);
	    	return true;
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}
	*/

	public void onClick(View v) {
		if (v.getId() == R.id.add) {
			if (game.isMyLesson()) {
				game.setMyLesson(false);
				dbManager.removeFromMyLessons(game);
				add.setText(R.string.add_to_my_lessons);
			} else {
				game.setMyLesson(true);
				dbManager.addToMyLessons(game);
				add.setText(R.string.remove_from_my_lessons);
			}
		}
	}

}
