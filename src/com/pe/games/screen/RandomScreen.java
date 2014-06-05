package com.pe.games.screen;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.webkit.WebView;
import android.widget.Button;

import com.pe.games.R;
import com.pe.games.model.Game;
import com.pe.games.util.TemplateParser;

public class RandomScreen extends BaseScreen implements SensorEventListener, OnClickListener {
	
	protected Button refresh;
	protected Button add;
	protected WebView webView;
	protected Game game;
	protected TemplateParser templateParser;
	
	protected SensorManager sensorManager;
	protected long lastUpdate = -1;
	protected float x, y, z;
	protected float last_x, last_y, last_z;
	protected static final int SHAKE_THRESHOLD = 800;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.random_screen);
		game = dbManager.getRandomGame();
		templateParser = new TemplateParser(this, game);
		initializeViews();
		refreshGame();
	}
	
	@Override
	public void onPause() {
		super.onPause();
		if (sensorManager != null) {
			sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
			sensorManager = null;
	    }
	}
	
	@Override
	public void onResume() {
		super.onResume();
		if (game != null)
			getParent().setTitle(game.getTitle());
		sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
		if (sensorManager != null) {
			boolean accelSupported = sensorManager.registerListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_NORMAL);
		 	if (!accelSupported)
		 		sensorManager.unregisterListener(this, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
		}
	}
	
	protected void initializeViews() {
		refresh = (Button) findViewById(R.id.refresh);
		refresh.setOnClickListener(this);
		
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);
		add.setText(game.isMyLesson() ? R.string.remove : R.string.add);
		
		webView = (WebView) findViewById(R.id.webView);
		webView.setBackgroundColor(0);
	}
	
	protected void refreshGame() {
		game = dbManager.getRandomGame();
		if (game != null)
			getParent().setTitle(game.getTitle());
		templateParser.setGame(game);
		webView.loadData(templateParser.getPreparedHtml(), "text/html", "UTF-8");
		add.setText(game.isMyLesson() ? R.string.remove : R.string.add);
	}
	
	/*
	public boolean onCreateOptionsMenu(Menu menu) {
	    MenuInflater inflater = getMenuInflater();
	    inflater.inflate(R.menu.random_menu, menu);
	    return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    case R.id.refresh:
	    	refreshGame();
	    	return true;
	    case R.id.add:
	    	dbManager.addToMyLessons(game);
	    	return true;
	    default:
	    	return super.onOptionsItemSelected(item);
	    }
	}
	*/
	
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.refresh:
			refreshGame();
			break;
		case R.id.add:
			if (game.isMyLesson()) {
				game.setMyLesson(false);
				dbManager.removeFromMyLessons(game);
				add.setText(R.string.add);
			} else {
				game.setMyLesson(true);
				dbManager.addToMyLessons(game);
				add.setText(R.string.remove);
			}
			break;
		}
	}

	public void onAccuracyChanged(Sensor arg0, int arg1) {}

	public void onSensorChanged(SensorEvent event) {
		if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
		    long curTime = System.currentTimeMillis();
		    if ((curTime - lastUpdate) > 100) {
		    	long diffTime = (curTime - lastUpdate);
		    	lastUpdate = curTime;
	 
		    	float[] values = event.values;
		    	x = values[SensorManager.DATA_X];
				y = values[SensorManager.DATA_Y];
				z = values[SensorManager.DATA_Z];
	 
				float speed = Math.abs(x+y+z - last_x - last_y - last_z) / diffTime * 10000;
				if (speed > SHAKE_THRESHOLD) {
				    refreshGame();
				}
				
				last_x = x;
				last_y = y;
				last_z = z;
		    }
		}
	}

}
