package com.pe.games.adapter;

import java.util.Collections;
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
import com.pe.games.model.Game;

public class GameAdapter extends BaseAdapter {

	protected LayoutInflater inflater;
	protected List<Game> games;
	protected List<Game> filteredGames;
	
	public GameAdapter(Context context, List<Game> games) {
		inflater = (LayoutInflater) context.getSystemService(Service.LAYOUT_INFLATER_SERVICE);
		this.games = (games != null) ? games : new LinkedList<Game>();
		filteredGames = new LinkedList<Game>();
		if (games != null && !games.isEmpty())
			filteredGames.addAll(games);
		Collections.sort(filteredGames);
	}

	public int getCount() {
		return filteredGames.size();
	}

	public Game getItem(int position) {
		return filteredGames.get(position);
	}

	public long getItemId(int position) {
		return filteredGames.get(position).getId();
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = null;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.game_list_item, null);
			holder = new ViewHolder();
			holder.title = (TextView) convertView.findViewById(R.id.title);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		Game game = filteredGames.get(position);
		holder.title.setText(game.getTitle());
		
		return convertView;
	}
	
	public void setGames(List<Game> games) {
		this.games = (games != null) ? games : new LinkedList<Game>();
		filteredGames.clear();
		if (games != null && !games.isEmpty())
			filteredGames.addAll(games);
		Collections.sort(filteredGames);
	}
	
	public void filterGames(CharSequence s) {
		filteredGames.clear();
		for (int i=0; i<games.size(); i++) {
			if (games.get(i).getTitle().contains(s))
				filteredGames.add(games.get(i));
		}
		Collections.sort(filteredGames);
		notifyDataSetChanged();
	}
	
	class ViewHolder {
		TextView title;
	}

}
