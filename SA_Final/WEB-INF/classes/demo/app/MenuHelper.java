package demo.app;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.*;

import demo.util.DBMgr;

public class MenuHelper {

	private static MenuHelper mh;
	private Connection conn = null;
	/** PreparedStatement是用來執行預先編譯的 SQL 語句的介面 **/
	private PreparedStatement pres = null;

	private MenuHelper() {

	}

	public static MenuHelper getHelper() {
		/** Singleton檢查是否已經有MenuHelper物件，若無則new一個，若有則直接回傳 */
		if (mh == null) {
			mh = new MenuHelper();
		}
		return mh;
	}
	
	public float getTDEEByID(int id) {			/*for MenuController:利用member_id，查詢對應的TDEE*/
		
		String exexcute_sql = "";
		ResultSet rs = null;
		
		float TDEE = 0;
		
		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT"
					   + "	pi.total_daily_energy_expenditure"
					   + "FROM"
					   + "	personal_inbody pi"
					   + "WHERE"
					   + "	member_id = ? LIMIT 1";

			/** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
			pres = conn.prepareStatement(sql);
			pres.setInt(1, id);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			
			TDEE = rs.getFloat("total_daily_energy_expenditure");
			
		}catch (SQLException e) {
			/** 印出JDBC SQL指令錯誤 **/
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/** 若錯誤則印出錯誤訊息 */
			e.printStackTrace();
		} finally {
			/** 關閉連線並釋放所有資料庫相關之資源 **/
			DBMgr.close(rs, pres, conn);
		}
		
		return TDEE;
	}
	
	public JSONObject getMenuByTDEE(float TDEE) {	/*利用TDEE，搜出對應的menu_id，再搜出食品品項*/
		/** 新建一個 menu 物件之 m 變數，用於紀錄每一筆菜單對應到的食物名 */
		Menu m = null;
		/** 用於儲存所有檢索回之菜單，以JSONArray方式儲存 */
		JSONArray jsa = new JSONArray();
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;
		
		//int total_menu_calories = 0;
		ArrayList<Food> breakfast = new ArrayList<>();
		ArrayList<Food> lunch = new ArrayList<>();
		ArrayList<Food> dinner = new ArrayList<>(); 

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT"
					   + "	m.menu_id,"
					   + "	flm.food_id,"
					   + "	f.food_name,"
					   + "	flm.set_label_id"
					   + "  f.calories"
					   + "FROM"
					   + "	menu m"
					   + "JOIN"
					   + "    food_linking_menu flm ON m.menu_id = flm.menu_id"
					   + "JOIN"
					   + "    food f ON flm.food_id = f.food_id";

			/** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
			pres = conn.prepareStatement(sql);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			
			int  menu_id = -1;
			float[] total_menu_calories = {};
			
			/** 透過 while 迴圈移動pointer，找到該IDEE對應到的menu */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;
				
				/** 將 ResultSet 之資料取出 */
				int id = rs.getInt("menu_id");
				String food_name = rs.getString("food_name");
				int calories = rs.getInt("calories");
				
				total_menu_calories[id] += calories;
				
				//total_menu_calories += calories; 
				
				/*if(total_menu_calories >= TDEE){
					total_menu_calories -= calories;
					menu_id = id;
					break;
				}*/
			}
			
			for(int i=1;i<=5;i++) {
				if(TDEE>total_menu_calories[i]){
					menu_id =i;
				}
			}
				
			rs = pres.executeQuery();
			
			while(rs.next()) {
				int temp_menu_id = rs.getInt("menu_id");
				int label_id = rs.getInt("set_label_id");
				String temp_food_name = rs.getString("food_name");
				Food f = new Food(temp_food_name);
				
				if(temp_menu_id == menu_id){
					if(label_id == 1){
						breakfast.add(f);
					}
					else if(label_id == 2){
						lunch.add(f);
					}
					else if(label_id == 3){
						dinner.add(f);
					}
				}
			}
			
			m = new Menu(menu_id, breakfast, lunch, dinner);
			jsa.put(m.getData());
			
		} catch (SQLException e) {
			/** 印出JDBC SQL指令錯誤 **/
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/** 若錯誤則印出錯誤訊息 */
			e.printStackTrace();
		} finally {
			/** 關閉連線並釋放所有資料庫相關之資源 **/
			DBMgr.close(rs, pres, conn);
		}

		/** 紀錄程式結束執行時間 */
		long end_time = System.nanoTime();
		/** 紀錄程式執行時間 */
		long duration = (end_time - start_time);

		/** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);	//這個row沒意義
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}
}
