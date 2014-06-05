package com.pe.games.screen;

import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

import com.pe.games.R;

public class MainScreen extends TabActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_screen);
        
        initTabs();
    }
    
    private void initTabs() {
    	setTitle(R.string.game_book);
    	
    	Resources res = getResources();
    	TabHost tabHost = getTabHost();
    	
    	Intent categoriesIntent = new Intent(this, CategoriesScreen.class);
    	TabSpec tabSpecCategories = tabHost
    			.newTabSpec(getString(R.string.categories))
    			.setIndicator(getString(R.string.categories),
    					res.getDrawable(R.drawable.tab_categories))
    			.setContent(categoriesIntent);
    	
    	Intent alphabeticalIntent = new Intent(this, AlphabeticalScreen.class);
    	TabSpec tabSpecAlphabetical = tabHost
    			.newTabSpec(getString(R.string.alphabetical))
    			.setIndicator(getString(R.string.alphabetical),
    					res.getDrawable(R.drawable.tab_alphabetical))
    			.setContent(alphabeticalIntent);
    	
    	Intent randomIntent = new Intent(this, RandomScreen.class);
    	TabSpec tabSpecRandom = tabHost
    			.newTabSpec(getString(R.string.random))
    			.setIndicator(getString(R.string.random), 
    					res.getDrawable(R.drawable.tab_random))
    			.setContent(randomIntent);
    	
    	Intent myLessonsIntent = new Intent(this, MyLessonsScreen.class);
    	TabSpec tabSpecMyLessons = tabHost
    			.newTabSpec(getString(R.string.my_lessons))
    			.setIndicator(getString(R.string.my_lessons), 
    					res.getDrawable(R.drawable.tab_my_lessons))
    			.setContent(myLessonsIntent);
    	
    	tabHost.addTab(tabSpecCategories);
    	tabHost.addTab(tabSpecAlphabetical);
    	tabHost.addTab(tabSpecRandom);
    	tabHost.addTab(tabSpecMyLessons);
    	
    	tabHost.setCurrentTab(0);
    }
    
}
