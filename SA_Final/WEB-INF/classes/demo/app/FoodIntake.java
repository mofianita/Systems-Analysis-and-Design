package demo.app;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONObject;

public class FoodIntake {
	private int member_id;
	private ArrayList<Food> breakfast = new ArrayList<>();
	private ArrayList<Food> lunch = new ArrayList<>();
	private ArrayList<Food> dinner = new ArrayList<>();
	private float b_calorie = 0;
	private float l_calorie = 0;
	private float d_calorie = 0;
	private Timestamp daily_diet_date;
	
	private String date_format;
	private float t_calorie;
	private FoodHelper fh = FoodHelper.getHelper();
	
	/*Create Use Constructor*/
	public FoodIntake(int member_id, ArrayList<Food> breakfast,
						ArrayList<Food> lunch, ArrayList<Food> dinner) {
		this.member_id = member_id;
		LocalDateTime now = LocalDateTime.now();
		DateTimeFormatter patt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
		String formatted = now.format(patt);
		this.daily_diet_date = Timestamp.valueOf(formatted);
		for (int i = 0; i < breakfast.size(); i++) {
			this.breakfast.add(breakfast.get(i));
		}
		calBreakCalorie();
		for (int i = 0; i < lunch.size(); i++) {
			this.lunch.add(lunch.get(i));
		}
		calLunchCalorie();
		for (int i = 0; i < dinner.size(); i++) {
			this.dinner.add(dinner.get(i));
		}
		calDinnerCalorie();
	}
	// update/ get detail daily_diet
	public FoodIntake(int member_id, ArrayList<Food> breakfast,
						ArrayList<Food> lunch, ArrayList<Food> dinner, 
						String date_format) {
		this.member_id = member_id;
		this.date_format = date_format;
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		try {
			Date parseDate = sdf.parse(date_format);
			this.daily_diet_date = new Timestamp(parseDate.getTime());
		}
		catch (java.text.ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		for (int i = 0; i < breakfast.size(); i++) {
			this.breakfast.add(breakfast.get(i));
		}
		calBreakCalorie();
		for (int i = 0; i < lunch.size(); i++) {
			this.lunch.add(lunch.get(i));
		}
		calLunchCalorie();
		for (int i = 0; i < dinner.size(); i++) {
			this.dinner.add(dinner.get(i));
		}
		calDinnerCalorie();
	} // For update use, need parameter date data
	public FoodIntake(String date, float t_calorie) {
		this.date_format = date;
		this.t_calorie = t_calorie;
	}
	
	public int getMemberID() {
		return this.member_id;
	}
	public ArrayList<Food> getBreakfast() {
		return this.breakfast;
	}
	public ArrayList<Food> getLunch() {
		return this.lunch;
	}
	public ArrayList<Food> getDinner() {
		return this.dinner;
	}
	public float getBCalorie() {
		return this.b_calorie;
	}
	public float getLCalorie() {
		return this.l_calorie;
	}
	public float getDCalorie() {
		return this.d_calorie;
	}
	public Timestamp getDailyDietDate() {
		return this.daily_diet_date;
	}
	
	public String getDateFormat() {
		return this.date_format;
	}
	public float getTCalorie() {
		return this.t_calorie;
	}
	private void calBreakCalorie() {
		for (int i = 0; i < this.breakfast.size(); i++) {
			String name = this.breakfast.get(i).getFoodName();
			float food_calorie = retCalorieData(name);
			b_calorie += food_calorie;
		}
	}
	private void calLunchCalorie() {
		for (int i = 0; i < this.lunch.size(); i++) {
			String name = this.lunch.get(i).getFoodName();
			float food_calorie = retCalorieData(name);
			l_calorie += food_calorie;
		}
	}
	private void calDinnerCalorie() {
		for (int i = 0; i < this.dinner.size(); i++) {
			String name = this.dinner.get(i).getFoodName();
			float food_calorie = retCalorieData(name);
			d_calorie += food_calorie;
		}
	}
	private float retCalorieData(String food_name) {
		float food_calorie = 0;
		JSONObject response = fh.getByName(food_name);
		if (response.has("data")) {
		    JSONArray dataArray = response.getJSONArray("data");

		    // Check if there is at least one item in the array
		    if (dataArray.length() > 0) {
		        // Get the first item in the array (assuming it's the only one based on your LIMIT 1 query)
		        JSONObject foodData = dataArray.getJSONObject(0);

		        // Check if "calories" key is present in the food data
		        if (foodData.has("calories")) {
		            // Extract the "calories" value
		            food_calorie = (float) foodData.getFloat("calories");
		        }
		    }
		    
		}
		return food_calorie;
	}
	public JSONObject update() {
		JSONObject data = new JSONObject();
		
		return data;
	}
	public JSONObject getData() {
		JSONObject jso = new JSONObject();
		jso.put("member_id", getMemberID());
		jso.put("date", getDateFormat());
		for(int i = 1; i <= 5; i++) {
			String pointer = "breakfast" + i;
			if(i <= getBreakfast().size()) 
				jso.put(pointer, getBreakfast().get(i-1).getFoodName());
			else
				break;
		}
		for(int i = 1; i <= 5; i++) {
			String pointer = "lunch" + i;
			if(i <= getLunch().size()) 
				jso.put(pointer, getLunch().get(i-1).getFoodName());
			else
				break;
		}
		for(int i = 1; i <= 5; i++) {
			String pointer = "dinner" + i;
			if(i <= getDinner().size()) 
				jso.put(pointer, getDinner().get(i-1).getFoodName());
			else
				break;
		}
		jso.put("calories", getBCalorie() + getLCalorie() + getDCalorie());
		return jso;
	}
	public JSONObject getRecordData() {
		JSONObject jso = new JSONObject();
		jso.put("date", date_format);
		jso.put("calories", t_calorie);
		return jso;
	}
}
