package com.pe.games.screen;

import java.util.List;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.pe.games.R;
import com.pe.games.adapter.CategoryAdapter;
import com.pe.games.database.DatabaseHelper;
import com.pe.games.model.Category;

public class CategoriesScreen extends BaseScreen implements OnItemClickListener, OnClickListener {
	
	protected ListView list;
	protected Button add;
	protected CategoryAdapter adapter;
	protected List<Category> categories;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.categories_screen);
		categories = dbManager.getCategories();
		adapter = new CategoryAdapter(this, categories);
		initializeViews();
	}
	
	@Override
	public void onResume() {
		super.onResume();
		getParent().setTitle(R.string.game_book);
	}
	
	protected void initializeViews() {
		add = (Button) findViewById(R.id.add);
		add.setOnClickListener(this);
		
		list = (ListView) findViewById(R.id.list);
		list.setAdapter(adapter);
		list.setOnItemClickListener(this);
		registerForContextMenu(list);
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
	    case R.id.edit:
	    	return true;
	    case R.id.add:
	    	showAddCategoryDialog();
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
			Category category = adapter.getItem(info.position);
			menu.setHeaderTitle(category.getTitle());
			String[] menuItems = {getString(R.string.edit), getString(R.string.delete)};
			for (int i = 0; i < menuItems.length; i++) {
				menu.add(Menu.NONE, i, i, menuItems[i]);
			}
		}
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
		Category category = adapter.getItem(info.position);
		int menuItemIndex = item.getItemId();
		switch (menuItemIndex) {
		case 0:
			showEditCategoryDialog(category);
			break;
		case 1:
			dbManager.deleteCategory(category);
			categories = dbManager.getCategories();
			adapter.setCategories(categories);
			adapter.notifyDataSetChanged();
			break;
		}
		return true;
	}
	
	protected void showEditCategoryDialog(final Category category) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setPadding(10, 10, 10, 10);
		
		final EditText input = new EditText(this);
		input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		input.setHint(R.string.category_name);
		if (category != null)
			input.setText(category.getTitle());
		
		ll.addView(input);
		
		builder
			.setView(ll)
			.setPositiveButton(R.string.edit, new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialog, int which) {
					String title = input.getText().toString();
					if (category != null && title != null && !title.equals("")) {
						category.setTitle(title);
						dbManager.updateCategory(category);
						categories = dbManager.getCategories();
						adapter.setCategories(categories);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
					}
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create()
			.show();
	}
	
	protected void showAddCategoryDialog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		
		LinearLayout ll = new LinearLayout(this);
		ll.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		ll.setPadding(10, 10, 10, 10);
		
		final EditText input = new EditText(this);
		input.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT));
		input.setHint(R.string.category_name);
		
		ll.addView(input);
		
		builder
			.setView(ll)
			.setPositiveButton(R.string.add, new DialogInterface.OnClickListener() {	
				public void onClick(DialogInterface dialog, int which) {
					String title = input.getText().toString();
					if (title != null && !title.equals("")) {
						Category category = new Category(title);
						int categoryId = (int) dbManager.addCategory(category);
						categories = dbManager.getCategories();
						adapter.setCategories(categories);
						adapter.notifyDataSetChanged();
						dialog.dismiss();
						
						Intent intent = new Intent(CategoriesScreen.this, GamesScreen.class);
						intent.putExtra(DatabaseHelper.FIELD_CATEGORY_ID, categoryId);
						startActivity(intent);
					}
				}
			})
			.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					dialog.dismiss();
				}
			})
			.create()
			.show();
	}

	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		int categoryId = adapter.getItem(position).getId();
		Intent intent = new Intent(this, GamesScreen.class);
		intent.putExtra(DatabaseHelper.FIELD_CATEGORY_ID, categoryId);
		startActivity(intent);
	}

	public void onClick(View v) {
		if (v.getId() == R.id.add) {
			showAddCategoryDialog();
		}
	}
	

}
