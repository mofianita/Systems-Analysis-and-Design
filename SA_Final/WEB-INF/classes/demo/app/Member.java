package demo.app;
import org.json.*;
public class Member {
	private int id;
	private String email;
	private String name;
	private String password;
	private MemberHelper Mh = MemberHelper.getHelper();
	public Member(String email, String name, String password) {
		this.email = email;
		this.name = name;
		this.password = password;
	}
	public Member(int id, String email, String name, String password) {
		this.id = id;
		this.email = email;
		this.name = name;
		this.password = password;
	}
	public int getID() {
		return this.id;
	}
	public String getEmail() {
		return this.email;
	}
	public String getName() {
		return this.name;
	}
	public String getPassword() {
		return this.password;
	}
	public JSONObject update() {
		JSONObject data = new JSONObject();
		data = Mh.update(this);
		return data;
	}
	public JSONObject getData() {
		JSONObject jso = new JSONObject();
		jso.put("id", getID());
		jso.put("name", getName());
		jso.put("email", getEmail());
		jso.put("password", getPassword());
		return jso;
	}
}
