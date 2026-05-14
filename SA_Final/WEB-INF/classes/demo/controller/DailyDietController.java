package demo.controller;

import java.io.*;
import java.util.ArrayList;

import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import demo.app.Food;
import demo.app.FoodIntake;

import demo.app.FoodIntakeHelper;
import tools.JsonReader;

@WebServlet("/api/daily_diet.do")
public class DailyDietController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private FoodIntakeHelper fih =  FoodIntakeHelper.getHelper();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {	
			/*會員新增攝取的食物資料*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();

        int member_id = Integer.parseInt(jso.getString("id")); //6
        
        ArrayList<Food> breakfast = new ArrayList<>();
        ArrayList<Food> lunch = new ArrayList<>();
        ArrayList<Food> dinner = new ArrayList<>();
        
        for(int i = 1; i <= 5; i++) {
        	String pointer = "breakfast" + i;
        	if(jso.getString(pointer) == "") break;
        	breakfast.add(new Food(jso.getString(pointer)));
        }
        for(int i = 1; i <= 5; i++) {
        	String pointer = "lunch" + i;
        	if(jso.getString(pointer) == "") break;
        	lunch.add(new Food(jso.getString(pointer)));
        }
        
        for(int i = 1; i <= 5; i++) {
        	String pointer = "dinner" + i;
        	if(jso.getString(pointer) == "") break;
        	dinner.add(new Food(jso.getString(pointer)));
        }
        
        if(member_id == 0) {	//確保member_id不為空
        	String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
        	jsr.response(resp, response);
        }
        else {
        	FoodIntake fi = new FoodIntake(member_id, breakfast, lunch, dinner);
        	JSONObject data = fih.create(fi);
        	JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 紀錄攝取食品...");
            resp.put("response", data);
            
            jsr.response(resp, response);

        }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
			/*For會員檢視所有攝取食品*/
		
		JsonReader jsr = new JsonReader(request);
		String member_id = jsr.getParameter("id");	//無法用getString
		String date = jsr.getParameter("date");
		
		if(!date.isEmpty() && !member_id.isEmpty()) {
			JSONObject query = fih.getByIDDate(member_id, date);
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "所有攝取食品資料取得成功");
			resp.put("response", query);
			jsr.response(resp, response);
		}
		else if(date.isEmpty() && !member_id.isEmpty()) {
			JSONObject query = fih.getByID(member_id);
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
			resp.put("message", "所有會員攝取資料取得成功");
			resp.put("response", query);
			jsr.response(resp, response);
		}
		
			
	}
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*提供會員刪除攝取的食物(以天為單位)*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        int id = Integer.parseInt(jso.getString("id"));		/*member_id*/
        String date = jso.getString("date");	
        
        JSONObject query = fih.deleteByIDDate(id,date);
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "食物移除成功(以天為單位)！");
        resp.put("response", query);
        
        jsr.response(resp, response);
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*提供會員更改攝取食品*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        String id = jso.getString("id");	//member_id
        int id_int = Integer.parseInt(id);
        
        ArrayList<Food> breakfast = new ArrayList<>();
        ArrayList<Food> lunch = new ArrayList<>();
        ArrayList<Food> dinner = new ArrayList<>();
        
        for(int i = 1; i <= 5; i++) {
        	String pointer = "breakfast" + i;
        	if(jso.getString(pointer) == "") break;
        	breakfast.add(new Food(jso.getString(pointer)));
        }
        for(int i = 1; i <= 5; i++) {
        	String pointer = "lunch" + i;
        	if(jso.getString(pointer) == "") break;
        	lunch.add(new Food(jso.getString(pointer)));
        }
        
        for(int i = 1; i <= 5; i++) {
        	String pointer = "dinner" + i;
        	if(jso.getString(pointer) == "") break;
        	dinner.add(new Food(jso.getString(pointer)));
        }
        
        String time = jso.getString("date");
        FoodIntake fi = new FoodIntake(id_int, breakfast, lunch, dinner, time); // time format: String, yyyy-MM-dd
        
        JSONObject data = fih.update(fi);	//繞過FoodIntake，直接call FoodIntake的update
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新攝取食品資料...");
        resp.put("response", data);
        
        jsr.response(resp, response);
	}
}
