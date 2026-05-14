package demo.app;

import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.ArrayList;

import org.json.*;
import demo.util.DBMgr;
public class FoodIntakeHelper {

	private static FoodIntakeHelper fih;
	private FoodHelper fh = FoodHelper.getHelper();
	private Connection conn = null;
	/** PreparedStatement是用來執行預先編譯的 SQL 語句的介面 **/
	private PreparedStatement pres = null;

	private FoodIntakeHelper() {

	}

	public static FoodIntakeHelper getHelper() {
		/** Singleton檢查是否已經有MenuHelper物件，若無則new一個，若有則直接回傳 */
		if (fih == null) {
			fih = new FoodIntakeHelper();
		}
		return fih;
	}
	
	// 根據日期、member_id delete and edit
	public JSONObject deleteByIDDate(int id, String date) {
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();

			/** SQL指令 */
			String sql = "DELETE FROM daily_diet "
					+ "WHERE daily_diet.member_id = ? "
					+ "AND Date(daily_diet.daily_diet_date) = ?";

			/** pres已經編譯了一個指定的 SQL 語句 (sql)。接下來便可以使用這個 PreparedStatement 物件來執行 SQL 查詢 */
			pres = conn.prepareStatement(sql);
			/** 將整數值 (id) 設定到 SQL 語句中的第一個占位符 */
			pres.setInt(1, id);
			pres.setString(2, date);
			/** 執行刪除之SQL指令並記錄影響之行數 */
			row = pres.executeUpdate();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

		} catch (SQLException e) {
			/** 印出JDBC SQL指令錯誤 **/
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			/** 若錯誤則印出錯誤訊息 */
			e.printStackTrace();
		} finally {
			/** 關閉連線並釋放所有資料庫相關之資源(也可以不用rs) **/
			DBMgr.close(rs, pres, conn);
		}

		/** 紀錄程式結束執行時間 */
		long end_time = System.nanoTime();
		/** 紀錄程式執行時間 */
		long duration = (end_time - start_time);

		/** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		/** 在response中放入了三個屬性：sql、row、和 time，同時將execute_sql、row、duration存入 */
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);

		return response;
	}
	
	public JSONObject getByID(String id) {
		FoodIntake fi = null;
		JSONArray jsa = new JSONArray();
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;
		try {
			conn = DBMgr.getConnection();
			String sql = "SELECT Date(daily_diet.daily_diet_date) AS daily_date, "
					+ "SUM(daily_diet.total_calories) AS Calorie FROM daily_diet "
					+ "WHERE daily_diet.member_id = ? "
					+ "GROUP BY Date(daily_diet.daily_diet_date) "
					+ "ORDER BY Date(daily_diet.daily_diet_date) DESC";
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			rs = pres.executeQuery();
			
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				Date date = rs.getDate("daily_date");
				String date_format = date.toString();
				float calorie = rs.getFloat("Calorie");
				fi = new FoodIntake(date_format, calorie);
				jsa.put(fi.getRecordData());
			}
			
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
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}
	
	public JSONObject getByIDDate(String id, String daily_diet_date) {
		/** 新建一個 FoodIntake 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
		FoodIntake fi = null;
		/** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
		JSONArray jsa = new JSONArray();
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT * FROM food f "
					+ "JOIN food_linking_daily_diet fldd ON f.food_id = fldd.food_id "
					+ "JOIN daily_diet dd ON fldd.daily_diet_id = dd.daily_diet_id "
					+ "WHERE dd.member_id = ? AND DATE(dd.daily_diet_date) = ?";

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			pres.setString(2, daily_diet_date);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			/** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
			ArrayList<Food> breakfast = new ArrayList<>();
			ArrayList<Food> lunch = new ArrayList<>();
			ArrayList<Food> dinner = new ArrayList<>();
			int member_id = Integer.parseInt(id);
			String date = "";
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				if(date.isEmpty()) {
					Timestamp a = rs.getTimestamp("daily_diet_date"); // Not initialized
					String all = a.toString();
					date = all.substring(0, 10); // get yyyy-MM-dd part
				}
				int set_label_id = rs.getInt("set_label_id");
				String food_name = rs.getString("food_name");
				if(set_label_id == 1) breakfast.add(new Food(food_name));
				else if(set_label_id == 2) lunch.add(new Food(food_name));
				else if(set_label_id == 3) dinner.add(new Food(food_name));
			}
			fi = new FoodIntake(member_id, breakfast, lunch, dinner, date);
			jsa.put(fi.getData()); /* getData：FoodIntake裡面的方法 */

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
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}

	public JSONObject create(FoodIntake fi) {
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			conn.setAutoCommit(false);
			/** SQL指令 */ // INSERT daily_diet table
			String sql1 = "INSERT INTO sa_v1.daily_diet(member_id, daily_diet_date, set_label_id, total_calories)"
					+ " VALUES(?, ?, ?, ?)";
			/** 取得所需之參數 */
			int member_id = fi.getMemberID(); 
			Timestamp daily_diet_date = fi.getDailyDietDate();
			/** 將參數回填至SQL指令當中 */
			// Breakfast
			if(fi.getBreakfast().size() > 0) {// There are breakfasts in ArrayList
				pres = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				pres.setInt(1, member_id);
				pres.setTimestamp(2, daily_diet_date);
				pres.setInt(3, 1);
				pres.setFloat(4, fi.getBCalorie());
				row = pres.executeUpdate();
				ResultSet generated_key = pres.getGeneratedKeys(); // Get the last auto-generated key
				int last_insert_key_b = 0; // breakfast
				if(generated_key.next()) {
					last_insert_key_b = generated_key.getInt(1);
				}
				String sql2 = "INSERT INTO sa_v1.food_linking_daily_diet(food_id, "
						+ "daily_diet_id) "
						+ "VALUES(?, ?)";
				for(int i = 0; i < fi.getBreakfast().size(); i++) {
					pres = conn.prepareStatement(sql2);
					String food_name = fi.getBreakfast().get(i).getFoodName();
					int id = retFoodIDData(food_name); // return food_id based on food_name
					pres.setInt(1, id);
					pres.setInt(2, last_insert_key_b);
					row = pres.executeUpdate();
				}
			}
			
			// Lunch
			if(fi.getLunch().size() > 0) { // There are lunches in ArrayList
				pres = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				pres.setInt(1, member_id);
				pres.setTimestamp(2, daily_diet_date);
				pres.setInt(3, 2);
				pres.setFloat(4, fi.getLCalorie());
				row = pres.executeUpdate();
				ResultSet generated_key = pres.getGeneratedKeys();
				int last_insert_key_l = 0;
				if(generated_key.next()) {
					last_insert_key_l = generated_key.getInt(1);
				}
				String sql2 = "INSERT INTO sa_v1.food_linking_daily_diet(food_id, "
						+ "daily_diet_id) "
						+ "VALUES(?, ?)";
				for(int i = 0; i < fi.getLunch().size(); i++) {
					pres = conn.prepareStatement(sql2);
					String food_name = fi.getLunch().get(i).getFoodName();
					int id = retFoodIDData(food_name); // return food_id based on food_name
					pres.setInt(1, id);
					pres.setInt(2, last_insert_key_l);
					row = pres.executeUpdate();
				}
			}
			//Dinner
			if(fi.getDinner().size() > 0) { // There are dinners in ArrayList
				pres = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
				pres.setInt(1, member_id);
				pres.setTimestamp(2, daily_diet_date);
				pres.setInt(3, 3);
				pres.setFloat(4, fi.getDCalorie());
				row = pres.executeUpdate();
				ResultSet generated_key = pres.getGeneratedKeys();
				int last_insert_key_d = 0;
				if(generated_key.next()) {
					last_insert_key_d = generated_key.getInt(1);
				}
				String sql2 = "INSERT INTO sa_v1.food_linking_daily_diet(food_id, "
						+ "daily_diet_id) "
						+ "VALUES(?, ?)";
				for(int i = 0; i < fi.getDinner().size(); i++) {
					pres = conn.prepareStatement(sql2);
					String food_name = fi.getDinner().get(i).getFoodName();
					int id = retFoodIDData(food_name); // return food_id based on food_name
					pres.setInt(1, id);
					pres.setInt(2, last_insert_key_d);
					row = pres.executeUpdate();
				}
			}
			conn.commit();
		} catch (SQLException e) {
			/** 印出JDBC SQL指令錯誤 **/
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
			try {
		        conn.rollback();
		    } catch (SQLException rollbackException) {
		        rollbackException.printStackTrace();
		    }
		} catch (Exception e) {
			/** 若錯誤則印出錯誤訊息 */
			e.printStackTrace();
		} finally {
			/** 關閉連線並釋放所有資料庫相關之資源 **/
			DBMgr.close(pres, conn);
		}

		/** 紀錄程式結束執行時間 */
		long end_time = System.nanoTime();
		/** 紀錄程式執行時間 */
		long duration = (end_time - start_time);

		/** 將SQL指令、花費時間與影響行數，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("time", duration);
		response.put("row", row);

		return response;
	}

	public JSONObject update(FoodIntake fi) {
		/** 紀錄回傳之資料 */
		// 因為更新後的資料可能會改變很多且幾乎不相關(食物數量變化、種類變化)
		// 因此會先刪除原本資料，再新增新資料
		JSONObject delete = deleteByIDDate(fi.getMemberID(), fi.getDateFormat());
		JSONObject create = create(fi);
		return create;
	}

	private int retFoodIDData(String food_name) {
		int id = 0;
		JSONObject response = fh.getByName(food_name);
		if (response.has("data")) {
		    JSONArray dataArray = response.getJSONArray("data");

		    // Check if there is at least one item in the array
		    if (dataArray.length() > 0) {
		        // Get the first item in the array (assuming it's the only one based on your LIMIT 1 query)
		        JSONObject foodData = dataArray.getJSONObject(0);

		        // Check if "calories" key is present in the food data
		        if (foodData.has("food_id")) {
		            // Extract the "calories" value
		            id = (int) foodData.getInt("food_id");
		        }
		    }
		    
		}
		return id;
	}
}
