package com.pe.games.util;

import java.io.InputStream;

import android.content.Context;

import com.pe.games.model.Game;

public class TemplateParser {
	
	public static final String TEMPLATE_FILENAME = "DisplayTemplate.html";
	
	public static final String TAG_NAME = "{name}";
	public static final String TAG_AREA = "{area}";
	public static final String TAG_EQUIPMENT = "{equipment}";
	public static final String TAG_INSTRUCTIONS = "{instructions}";
	public static final String TAG_VARIATION = "{variation}";
	public static final String TAG_BODY = "{body}";
	
	protected String AREA_TEMPLATE = "<div class=\"textContent\">" +
			"<div class=\"textTitle\" align=\"center\">Area</div>" +
			"<div class=\"textBody\"><p align=\"center\">{area}</p></div><div>";
	protected String EQUIPMENT_TEMPLATE = "<div class=\"textContent\">" +
			"<div class=\"textTitle\" align=\"center\">Equipment</div>" +
			"<div class=\"textBody\"><p align=\"center\">{equipment}</p></div><div>";
	protected String INSTRUCTIONS_TEMPLATE = "<div class=\"textContent\">" +
			"<div class=\"textTitle\" align=\"center\">Instructions</div>" +
			"<div class=\"textBody\"><p>{instructions}</p></div><div>";
	protected String VARIATION_TEMPLATE = "<div class=\"textContent\">" +
			"<div class=\"textTitle\" align=\"center\">Variation</div>" +
			"<div class=\"textBody\"><p>{variation}</p></div><div>";
	
	protected Game game;
	protected Context context;
	protected String template;
	protected String preparedHtml;
	
	public TemplateParser() {
		this(null, null);
	}
	
	public TemplateParser(Context context) {
		this(context, null);
	}
	
	public TemplateParser(Context context, Game game) {
		this.context = context;
		this.game = game;
		loadTemplate();
		parseTemplate();
	}
	
	protected void loadTemplate() {
		try {
			InputStream is = context.getAssets().open(TEMPLATE_FILENAME);
			int size = is.available();
			byte[] buffer = new byte[size];
			is.read(buffer);
			is.close();
			template = new String(buffer);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	protected void parseTemplate() {
		try {
			String body = "";
			if (game.hasArea())
				body += AREA_TEMPLATE.replace(TAG_AREA, game.getArea());
			if (game.hasEquipment())
				body += EQUIPMENT_TEMPLATE.replace(TAG_EQUIPMENT, game.getEquipment());
			if (game.hasInstructions())
				body += INSTRUCTIONS_TEMPLATE.replace(TAG_INSTRUCTIONS, game.getInstructions());
			if (game.hasVariation())
				body += VARIATION_TEMPLATE.replace(TAG_VARIATION, game.getVariation());
			preparedHtml = template.replace(TAG_NAME, game.getTitle()).replace(TAG_BODY, body);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public String getPreparedHtml() {
		return preparedHtml;
	}
	
	public void setGame(Game game) {
		this.game = game;
		parseTemplate();
	}

}
