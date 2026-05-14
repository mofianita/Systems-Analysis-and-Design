package demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;
import demo.app.Member;
import demo.app.MemberHelper;
import tools.JsonReader;

@WebServlet("/api/member.do")
public class MemberController extends HttpServlet{	
	/*確保序列化的版本一致性*/
	private static final long serialVersionUID = 1L;
	
	private MemberHelper Mh =  MemberHelper.getHelper();
	
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*For註冊：新增會員資料到資料庫*/
		
		/*創建一個JsonReader物件，用於解析HTTP請求的JSON格式資料*/
		JsonReader jsr = new JsonReader(request);
		/*使用JsonReader解析JSON格式資料，並將其轉換為JSONObject物件*/
        JSONObject jso = jsr.getObject();
        
        String name = jso.getString("name");
        String email = jso.getString("email");
        String password = jso.getString("password");
        
        Member M = new Member(email, name, password);
        
        if(name.isEmpty() || email.isEmpty() || password.isEmpty()) {
            /** 以字串組出JSON格式之資料 */
            String resp = "{\"status\": \'400\', \"message\": \'欄位不能有空值\', \'response\': \'\'}";
            /** 透過JsonReader物件回傳到前端（以字串方式） */
            jsr.response(resp, response);
        }
        else if (!Mh.checkDuplicate(M)) {
        	
        	/*將會員資料新增至資料庫，並取得回傳的資料*/
        	JSONObject data = Mh.create(M);
        	
        	/*創建一個新的JSON物件，用於封裝回應資料*/
        	JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "成功! 註冊會員資料...");
            resp.put("response", data);
            
            /*使用JsonReader回傳成功的回應*/
            jsr.response(resp, response);
        }
        else {
        	String resp = "{\"status\": \'400\', \"message\": \'新增帳號失敗，此E-Mail帳號重複！\', \'response\': \'\'}";
        	/*使用JsonReader回傳錯誤回應*/
        	jsr.response(resp, response);
        }
	}
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*For登入與管理員檢視會員*/
		/*若提供Email，回傳特定Member資料。若沒有Email，回傳所有Member*/
		
		JsonReader jsr = new JsonReader(request);
		String Email = jsr.getParameter("email");
		String id = jsr.getParameter("id");
		
		if (Email.isEmpty() && id.isEmpty()) {
			JSONObject query = Mh.getAll();
			
			JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "所有會員資料取得成功");
            resp.put("response", query);
            
            jsr.response(resp, response);
		}
		else if(!Email.isEmpty() && id.isEmpty()) {
			JSONObject query = Mh.getByMail(Email);
			
			JSONObject resp = new JSONObject();
            resp.put("status", "200");
            resp.put("message", "登入會員資料取得成功");
            resp.put("response", query);
            
            jsr.response(resp, response);
		}
		else if(!id.isEmpty()) {
			JSONObject query = Mh.getByID(id);
			JSONObject resp = new JSONObject();
			resp.put("status", "200");
            resp.put("message", "這個會員資料取得成功");
            resp.put("response", query);
            
            jsr.response(resp, response);
		}
	}
	
	public void doDelete(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*提供管理員刪除會員功能*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        int id = Integer.parseInt(jso.getString("id"));
        
        JSONObject query = Mh.deleteByID(id);
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "會員移除成功！");
        resp.put("response", query);
        
        jsr.response(resp, response);
	}
	
	public void doPut(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
		/*提供會員更改自身數據*/
		
		JsonReader jsr = new JsonReader(request);
        JSONObject jso = jsr.getObject();
        
        int id = Integer.parseInt(jso.getString("id"));
        String email = jso.getString("email");
        String password = jso.getString("password");
        String name = jso.getString("name");
        
        Member m = new Member(id, email, name, password);
        
        JSONObject data = m.update();
        
        JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "成功! 更新會員資料...");
        resp.put("response", data);
        
        jsr.response(resp, response);
	}
}