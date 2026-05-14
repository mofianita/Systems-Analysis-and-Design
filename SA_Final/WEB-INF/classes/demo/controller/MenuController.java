package demo.controller;

import java.util.Date;
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import tools.JsonReader;
import demo.app.MenuHelper;

@WebServlet("/api/menu.do")
public class MenuController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private MenuHelper mh = MenuHelper.getHelper();

	public void doGet(HttpServletRequest request, HttpServletResponse response1)
			throws ServletException, IOException{
		/*for會員：檢視推薦菜單*/
		
		JsonReader jsr = new JsonReader(request);
		int member_id = Integer.parseInt(jsr.getParameter("member_id"));
		
		float TDEE = mh.getTDEEByID(member_id);
		
		JSONObject query = mh.getMenuByTDEE(TDEE);
		
		JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "個人基礎數值取得成功");
        resp.put("response", query);

        jsr.response(resp, response1);		
	}
}