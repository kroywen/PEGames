package com.pe.games.adapter;

import java.util.LinkedList;
import java.util.List;

import android.app.Service;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.pe.games.R;
import com.pe.games.model.Category;

public class CategoryAdapter extends BaseAdapter {
	
	protected LayoutInflater inflater;
	protected List<Category> categories;
	
	public CategoryAdapter(Context context, List<Category> categories) {
		inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.categories = (categories != null) ? categories : new LinkedList<Category>();
	}

	public int getCount() {
		return categories.size();
	}

	public Category getItem(int position) {
		return categories.get(position);
	}

	public long getItemId(int position) {
		return categories.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.category_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Category category = categories.get(position);
		holder.title.setText(category.getTitle());
		
		return convertView;
	}
	
	public void setCategories(List<Category> categories) {
		this.categories = categories;
	}
	
	class ViewHolder {
		TextView title;
	}

}
