package demo.app;
import org.json.*;
import java.util.Date;
import java.sql.Timestamp;
public class WeightChange {
	private int member_id;
	private Timestamp date_time;
	private float weight;
	private String date_part;
	public WeightChange(float weight, int member_id){
		Date now = new Date();
		this.date_time = new Timestamp(now.getTime());
		this.member_id = member_id;
		this.weight = weight;
	}
	public WeightChange(String date, float weight) {
		this.date_part = date;
		this.weight = weight;
	}
	public int getMemberID() {
		return this.member_id;
	}
	public float getWeight() {
		return this.weight;
	}
	public Timestamp getDateTime() {
		return this.date_time;
	}
	public String getDatePart() {
		return this.date_part;
	}
}
