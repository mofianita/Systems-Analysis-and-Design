package demo.app;
import org.json.*;

public class Food {
	private int id;
	private String food_name;
	private float calories;
	private int categories_id;
	private float[] categories = new float[6];
	public Food(String food_name, float calories, float[] categories) {
		this.food_name = food_name;
		this.calories = calories;
		for (int i = 0; i < categories.length; i++) {
			this.categories[i] = categories[i];
		}
	}
	public Food(int id, String food_name, float calories, 
				float[] six_categories, int categories_id) {
		this.id = id;
		this.food_name = food_name;
		this.calories = calories;
		for (int i = 0; i < six_categories.length; i++) {
			this.categories[i] = six_categories[i];
		}
		this.categories_id = categories_id;
	}
	public Food(String food_name) { // This is for DailyDietUse.
		this.food_name = food_name;
	}
	public int getID() {
		return this.id;
	}
	public String getFoodName() {
		return this.food_name;
	}
	public float getCalories() {
		return this.calories;
	}
	public int getCategoriesId() {
		return categories_id;
	}
	public float[] getCategories() {
		return this.categories;
	}
	public JSONObject getData() {
		JSONObject jso = new JSONObject();
		jso.put("food_id", id);
		jso.put("food_name", food_name);
		jso.put("calories", calories);
		// jso.put("categories", categories); // ver1
		jso.put("grains", categories[0]);
		jso.put("meats_and_protein", categories[1]);
		jso.put("vegetables", categories[2]);
		jso.put("fruits", categories[3]);
		jso.put("milk_and_products", categories[4]);
		jso.put("fats", categories[5]);
		jso.put("six_categories_id", categories_id);
		return jso;
	}
}
