package demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import tools.JsonReader;
import demo.app.PersonalInbodyHelper;

@WebServlet("/api/TDEE.do")
public class TDEEController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private PersonalInbodyHelper pih = PersonalInbodyHelper.getHelper();
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		/*for會員：檢視一日總熱量消耗(TDEE)*/
		
		JsonReader jsr = new JsonReader(request);
		String member_id = jsr.getParameter("id");
		
		JSONObject query = pih.getByID(member_id);
		
		JSONObject resp = new JSONObject();
        resp.put("status", "200");
        resp.put("message", "個人基礎數值取得成功");
        resp.put("response", query);

        jsr.response(resp, response);
		
		
	}
}
