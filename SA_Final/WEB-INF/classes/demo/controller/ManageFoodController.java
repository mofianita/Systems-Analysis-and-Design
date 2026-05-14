package demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import tools.JsonReader;
import demo.app.FoodHelper;
import demo.app.Food;

@WebServlet("/api/manage_food.do")
public class ManageFoodController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private FoodHelper fh = FoodHelper.getHelper();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
		/*管理員新增食品(沒有id)*/
		
		JsonReader jsr = new JsonReader(request);
		JSONObject jso = jsr.getObject();
		
		float[] categories = new float[6];
		
		String name = jso.getString("food_name");
		float calories = Float.parseFloat(jso.getString("calories"));
		categories[0] = Float.parseFloat(jso.getString("grains"));
		categories[1] = Float.parseFloat(jso.getString("meats_and_protein"));
		categories[2] = Float.parseFloat(jso.getString("vegetables"));
		categories[3] = Float.parseFloat(jso.getString("fruits"));	
		categories[4] = Float.parseFloat(jso.getString("milk_and_products"));	
		categories[5] = Float.parseFloat(jso.getString("fats"));	
		
		Food f = new Food(name, calories, categories);
		
		if(name.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        } else {
			JSONObject data = fh.create(f);
			
			JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 新增食品項目...");
            resp.put("response", data);
            
            jsr.response(resp, response);
		}
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
			/*For管理員檢視所有食品資訊*/
		
	        JsonReader jsr = new JsonReader(request);
	        String food_name = jsr.getParameter("food_name");
	        String food_id = jsr.getParameter("food_id");
	        
	        if(food_id.isEmpty()) {
	           
	            JSONObject query = fh.getByName(food_name);
	            
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "所有食品資料取得成功");
	            resp.put("response", query);
	    
	            jsr.response(resp, response);
	        }
	        else {
	            JSONObject query = fh.getByID(food_id);
	            
	            JSONObject resp = new JSONObject();
	            resp.put("status", "200");
	            resp.put("message", "食品資料取得成功");
	            resp.put("response", query);
	    
	            jsr.response(resp, response);
	        }
	    }
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*提供管理員刪除食品功能*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        String name = jso.getString("food_name");
        
        JSONObject query = fh.deleteByFoodName(name);
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "食品移除成功！");
        resp.put("response", query);
        
        jsr.response(resp, response);
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*提供管理員更改食品資訊*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        float[] categories = new float[6];
        
        int id = jso.getInt("food_id");
        String name = jso.getString("food_name");
        float calories = Float.parseFloat(jso.getString("calories"));
        categories[0] = Float.parseFloat(jso.getString("grains"));
		categories[1] = Float.parseFloat(jso.getString("meats_and_protein"));
		categories[2] = Float.parseFloat(jso.getString("vegetables"));
		categories[3] = Float.parseFloat(jso.getString("fruits"));	
		categories[4] = Float.parseFloat(jso.getString("milk_and_products"));	
		categories[5] = Float.parseFloat(jso.getString("fats"));
		int categories_id = jso.getInt("six_categories_id");
        
        Food f = new Food(id, name, calories, categories, categories_id);
        
        JSONObject data = fh.update(f);
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新食品資料...");
        resp.put("response", data);
        
        jsr.response(resp, response);
	}
}
