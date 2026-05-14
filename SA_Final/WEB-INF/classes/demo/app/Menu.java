package demo.app;

import org.json.*;
import java.util.ArrayList;

public class Menu {
	private String label;//maybe no need
	private float recommend_calorie; // no use
	private float[] categories_requirement = new float[6];
	private int menu_id;
	// private float total_calorie; The menu's calorie, not sure to use or not
	// private float total_nutrients = new float[6];// The menu's
	private ArrayList<Food> breakfast = new ArrayList<Food>();
	private ArrayList<Food> lunch = new ArrayList<Food>();
	private ArrayList<Food> dinner = new ArrayList<Food>();
	public Menu(int menu_id, ArrayList<Food> breakfast, ArrayList<Food> lunch, ArrayList<Food> dinner){
		this.menu_id = menu_id;
		this.breakfast = breakfast;
		this.lunch = lunch;
		this.dinner = dinner;
	}
	
	public int getID() {
		return menu_id;
	}
	
	public String getLabel() {
		return this.label;
	}
	public float getRecommendCalorie() {
		return this.recommend_calorie;
	} // no use
	public float[] getCategoriesRequirement() {
		return this.categories_requirement;
	}
	public ArrayList<Food> getBreakfast() {
		return breakfast;
	}
	public ArrayList<Food> getLunch() {
		return lunch;
	}
	public ArrayList<Food> getDinner() {
		return dinner;
	}
	public JSONObject getData() {
		JSONObject data = new JSONObject();
		
		data.put("menu_id", menu_id);
		data.put("breakfast", breakfast);
		data.put("lunch", lunch);
		data.put("dinner", dinner);
		
		return data;
	}
}
