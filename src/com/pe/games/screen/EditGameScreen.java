package com.pe.games.screen;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.pe.games.R;
import com.pe.games.database.DatabaseHelper;
import com.pe.games.model.Game;

public class EditGameScreen extends BaseScreen implements OnClickListener {
	
	protected Button save;
	
	protected EditText title;
	protected EditText area;
	protected EditText equipment;
	protected EditText instructions;
	protected EditText variation;
	
	protected Game game;
	protected int gameId;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_game_screen_new);
		getIntentData();
		initializeViews();
		populateFields();
	}
	
	protected void getIntentData() {
		Intent intent = getIntent();
		if (intent != null && intent.hasExtra(DatabaseHelper.FIELD_ID)) {
			gameId = intent.getIntExtra(DatabaseHelper.FIELD_ID, 0);
			game = dbManager.getGameById(gameId);
		}
	}
	
	protected void initializeViews() {
		save = (Button) findViewById(R.id.save);
		save.setOnClickListener(this);
		
		title = (EditText) findViewById(R.id.title);
		area = (EditText) findViewById(R.id.area);
		equipment = (EditText) findViewById(R.id.equipment);
		instructions = (EditText) findViewById(R.id.instructions);
		variation = (EditText) findViewById(R.id.variation);
	}
	
	protected void populateFields() {
		if (game != null) {
			title.setText(game.getTitle());
			area.setText(game.getArea());
			equipment.setText(game.getEquipment());
			instructions.setText(game.getInstructions());
			variation.setText(game.getVariation());
		}
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
	    	saveGame();
	    	return true;
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}
	*/
	
	protected void saveGame() {
		if (game != null) {
			game.setTitle(title.getText().toString());
			game.setArea(area.getText().toString());
			game.setEquipment(equipment.getText().toString());
			game.setInstructions(instructions.getText().toString());
			game.setVariation(variation.getText().toString());
			
			boolean saved = dbManager.updateGame(game);
			if (saved)
				finish();
			else
				Toast.makeText(this, R.string.save_error, Toast.LENGTH_SHORT).show();
		}
	}

	public void onClick(View v) {
		if (v.getId() == R.id.save) {
			saveGame();
		}
	}

}
