package com.pe.games.model;

public class Game implements Comparable<Game> {
	
	public static final int MY_LESSONS = -1;
	public static final int ALL = 0;
	
	public static final String DEFAULT_TITLE = "No name";
	public static final String DEFAULT_AREA = "Any";
	public static final String DEFAULT_EQUIPMENT = "None";
	public static final String DEFAULT_INSTRUCTIONS = "No instructions";
	public static final String DEFAULT_VARIATION = "None";
	
	protected int id;
	protected int categoryId;
	protected String title;
	protected String area;
	protected String equipment;
	protected String instructions;
	protected String variation;
	protected boolean myLesson;
	
	public Game() {
		this(0, 0, DEFAULT_TITLE, DEFAULT_AREA, 
				DEFAULT_EQUIPMENT, DEFAULT_INSTRUCTIONS, DEFAULT_VARIATION, false);
	}
	
	public Game(int id, int categoryId, String title, String area, 
			String equipment, String instructions, String variation, boolean myLesson)
	{
		this.id = id;
		this.categoryId = categoryId;
		this.title = title;
		this.area = area;
		this.equipment = equipment;
		this.instructions = instructions;
		this.variation = variation;
		this.myLesson = myLesson;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public boolean hasId() {
		return id != 0;
	}
	
	public int getCategoryId() {
		return categoryId;
	}
	
	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public boolean hasCategoryId() {
		return categoryId != 0;
	}
	
	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean hasTitle() {
		return (title != null) && !(title.equals("") || title.equals(" "));
	}
	
	public String getArea() {
		return area;
	}
	
	public void setArea(String area) {
		this.area = area;
	}
	
	public boolean hasArea() {
		return (area != null) && !(area.equals("") || area.equals(" "));
	}
	
	public String getEquipment() {
		return equipment;
	}
	
	public void setEquipment(String equipment) {
		this.equipment = equipment;
	}
	
	public boolean hasEquipment() {
		return (equipment != null) && !(equipment.equals("") || equipment.equals(" "));
	}
	
	public String getInstructions() {
		return instructions;
	}
	
	public void setInstructions(String instructions) {
		this.instructions = instructions;
	}
	
	public boolean hasInstructions() {
		return (instructions != null) && !(instructions.equals("") || instructions.equals(" "));
	}
	
	public String getVariation() {
		return variation;
	}
	
	public void setVariation(String variation) {
		this.variation = variation;
	}
	
	public boolean hasVariation() {
		return (variation != null) && !(variation.equals("") || variation.equals(" "));
	}
	
	public boolean isMyLesson() {
		return myLesson;
	}
	
	public void setMyLesson(boolean myLesson) {
		this.myLesson = myLesson;
	}

	public int compareTo(Game another) {
		return title.compareToIgnoreCase(another.getTitle());
	}

}