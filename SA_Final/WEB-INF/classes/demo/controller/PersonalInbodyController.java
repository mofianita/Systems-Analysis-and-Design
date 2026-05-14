package demo.controller;

import java.util.Date;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import tools.JsonReader;
import demo.app.PersonalInbodyHelper;
import demo.app.PersonalInbody;


@WebServlet("/api/personal_inbody.do")
public class PersonalInbodyController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private PersonalInbodyHelper pih = PersonalInbodyHelper.getHelper();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	throws ServletException, IOException{
		/*for會員：初始化基礎數值(initialize)*/
		
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		
		String gender = jso.getString("gender");
		int age = Integer.parseInt(jso.getString("age"));
		float height = Float.parseFloat(jso.getString("height"));
		float weight = Float.parseFloat(jso.getString("weight"));
		String self_activity = jso.getString("self_activtiy");
		int member_id = Integer.parseInt(jso.getString("member_id"));
		
		PersonalInbody pi = new PersonalInbody(gender, age, height, weight, self_activity, member_id);
		
		if(gender.isEmpty() || age == 0 || height == 0 || 
				weight == 0 || self_activity.isEmpty()) {
			
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            jsr.response(resp, response);
		}else {
			JSONObject data = pih.create(pi);
			
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
            resp.put("message", "成功! 初始化基本數值...");
            resp.put("response", data);
            
            jsr.response(resp, response);
		}
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*for會員：查看基礎數值+基礎代謝率*/
		
		JsonReader jsr = new JsonReader(request);
		String member_id = jsr.getParameter("member_id");
		
		JSONObject query = pih.getByID(member_id);
		
		JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "個人基礎數值取得成功");
        resp.put("response", query);

        jsr.response(resp, response);
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*for會員：修改基本數值*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        String gender = jso.getString("gender");
        int age = Integer.parseInt(jso.getString("age"));
        float height = Float.parseFloat(jso.getString("height"));
        float weight = Float.parseFloat(jso.getString("weight"));
        String self_activity = jso.getString("self_activity");
        int member_id = Integer.parseInt(jso.getString("member_id"));
        
        PersonalInbody pi = new PersonalInbody(gender, age, height, weight, self_activity, member_id);
        
        JSONObject data = pih.update(pi);	/*繞過pi，直接call pih的update*/
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新基本數值...");
        resp.put("response", data);
        
        jsr.response(resp, response);
	}
}
