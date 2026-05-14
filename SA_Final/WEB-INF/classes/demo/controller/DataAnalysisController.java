package demo.controller;

import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import org.json.*;

import tools.JsonReader;
import demo.app.PersonalInbodyHelper;
import demo.app.DataAnalysisHelper;

@WebServlet("/api/data_analysis.do")
public class DataAnalysisController extends HttpServlet{
	
	private static final long serialVersionUID = 1L;
	
	private DataAnalysisHelper dah = DataAnalysisHelper.getHelper();
	private PersonalInbodyHelper pih = PersonalInbodyHelper.getHelper();

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException{
		/*for會員：查看體重變化圖表*/
		/*for會員：查看熱量攝取圖表*/
		
		JsonReader jsr = new JsonReader(request);
		String member_id = jsr.getParameter("member_id");
		
		//JSONObject query = dah.getByID("member_id");
		
		JSONObject resp = new JSONObject();
		resp.put("status", "200");
        resp.put("message", "兩圖表取得成功");
        //resp.put("response", query);
        
        jsr.response(resp, response);
	}
}
