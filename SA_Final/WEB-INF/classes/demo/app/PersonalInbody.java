package demo.app;
import org.json.*;
import java.util.Date;
import java.sql.Timestamp;
public class PersonalInbody {
	private int member_id;
	private String gender;
	private int age;
	private float height;
	private float weight;
	private String self_activity;
	private float bmr;
	private float recom_calorie;
	private float categories_requirement[] = new float[6];
	private Timestamp last_update_time;
	public PersonalInbody(String gender, int age, float height,
							float weight, String self_activity,
							int member_id) {
		this.gender = gender;
		this.age = age;
		this.height = height;
		this.weight = weight;
		calBMR();
		this.self_activity = self_activity;
		this.member_id = member_id;
		this.recom_calorie = calRecomCalorie();
		calCategoriesRequirement();
		this.last_update_time = updateTime();
	}
	
	public int getMemberID() {
		return this.member_id;
	}
	public String getGender() {
		return this.gender;
	}
	public int getAge() {
		return this.age;
	}
	public float getHeight() {
		return this.height;
	}
	public float getWeight() {
		return this.weight;
	}
	public Timestamp getLastUpdateTime() {
		return this.last_update_time;
	}
	public String getSelfActivity() {
		return this.self_activity;
	}
	public float getBMR() {
		return this.bmr;
	}
	public float getRecomCalorie() {
		return this.recom_calorie;
	}
	public float[] getCategoriesRequirement() {
		return this.categories_requirement;
	}
	private void calBMR() {
		if (gender == "Male") {
			bmr = (float)((10 * weight) + (6.25 * height) - (5 * age) + 5); 
		}
		else {
			bmr = (float)((10 * weight) + (6.25 * height) - (5 * age) - 161); 
		}
	}
	private float calRecomCalorie() {
		float multiplier;
		if (this.self_activity == "lower") multiplier = 1.2f;
		else if (this.self_activity == "static") multiplier = 1.4f;
		else if (this.self_activity == "normal") multiplier = 1.6f;
		else if (this.self_activity == "higher") multiplier = 1.7f;
		else multiplier = 1.9f;
		return (this.bmr * multiplier) /*- 300*/;
	}
	private void calCategoriesRequirement() {
		float base[] = {1.5f, 3.0f, 1.5f, 3.0f, 2.0f, 4.0f};
		float mul = (float) (this.recom_calorie / 1200);
		for (int i = 0; i < this.categories_requirement.length; i++) {
			this.categories_requirement[i] = base[i] * mul;
		}
	}
	public JSONObject update() {
		JSONObject data = new JSONObject();
		return data;
	}
	public JSONObject getData() {
		JSONObject jso = new JSONObject();
		jso.put("member_id", member_id);
		jso.put("gender", gender);
		jso.put("age", age);
		jso.put("height", height);
		jso.put("weight", weight);
		jso.put("self_activity", self_activity);
		jso.put("bmr", bmr);
		jso.put("TDEE", recom_calorie);
		jso.put("grains", categories_requirement[0]);
		jso.put("meat_and_protein", categories_requirement[1]);
		jso.put("vegetables", categories_requirement[2]);
		jso.put("fruits", categories_requirement[3]);
		jso.put("milk_and_products", categories_requirement[4]);
		jso.put("fats", categories_requirement[5]);
		jso.put("last_update_time", last_update_time);
		return jso;
	}
	private Timestamp updateTime() {
		Date now = new Date();
		return new Timestamp(now.getTime());
	}
}
