package demo.app;

import java.sql.*;
import org.json.*;
import demo.util.DBMgr;

public class FoodHelper {

	private static FoodHelper fh;
	private Connection conn = null;
	/** PreparedStatement是用來執行預先編譯的 SQL 語句的介面 **/
	private PreparedStatement pres = null;

	private FoodHelper() {

	}

	public static FoodHelper getHelper() {
		/** Singleton檢查是否已經有VoucherHelper物件，若無則new一個，若有則直接回傳 */
		if (fh == null) {
			fh = new FoodHelper();
		}
		return fh;
	}

	public JSONObject deleteByFoodName(String food_name) {
		String execute_sql = "";
		long start_time = System.nanoTime();
		ResultSet rs = null;
		int row = 0;
		try {
			conn = DBMgr.getConnection();
			String sql1 = "DELETE FROM food WHERE food.food_name = ?";
			pres = conn.prepareStatement(sql1);
			pres.setString(1, food_name);
			row = pres.executeUpdate();
			execute_sql = pres.toString();
			System.out.println(execute_sql);
		} catch (SQLException e) {
			System.err.format("SQL State: %s\n%s\n%s", e.getErrorCode(), e.getSQLState(), e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBMgr.close(rs, pres, conn);
		}
		JSONObject response = new JSONObject();
		long end_time = System.nanoTime();
		/** 紀錄程式執行時間 */
		long duration = (end_time - start_time);
		response.put("time", duration);
		return response;
	}

	public JSONObject getByID(String food_id) {
		/** 新建一個 Food 物件之 m 變數，用於紀錄每一位查詢回之食物資料 */
		Food f = null;
		/** 用於儲存所有檢索回之食物，以JSONArray方式儲存 */
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
			String sql = "SELECT *" 
					+ " FROM sa_v1.food "
					+ " JOIN sa_v1.six_categories ON food.six_categories_id = six_categories.six_categories_id "
					+ " WHERE food_id = ? LIMIT 1"; /* 待改 */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, food_id);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			/** 正確來說資料庫只會有一筆該食物編號之資料，因此其實可以不用使用 while 迴圈 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int id = rs.getInt("food_id");
				String food_name = rs.getString("food_name");
				float calories = rs.getFloat("calories");
				float categories[] = new float[6];
				categories[0] = rs.getFloat("grains");
				categories[1] = rs.getFloat("meats_and_protein");
				categories[2] = rs.getFloat("vegetables");
				categories[3] = rs.getFloat("fruits");
				categories[4] = rs.getFloat("milk_and_products");
				categories[5] = rs.getFloat("fats");
				int categories_id = rs.getInt("six_categories_id");

				/** 將每一筆食物資料產生一名新Food物件 */
				f = new Food(id, food_name, calories, categories,categories_id); /* 待改成我們的資料庫屬性 */
				/** 取出該名食物之資料並封裝至 JSONsonArray 內 */
				jsa.put(f.getData()); 	/* getData：Food裡面的方法 */
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

		/** 將SQL指令、花費時間、影響行數與所有食物資料之JSONArray，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}

	public JSONObject getByName(String name) {
		/** 新建一個 Food 物件之 m 變數，用於紀錄每一位查詢回之食物資料 */
		Food f = null;
		/** 用於儲存所有檢索回之食物，以JSONArray方式儲存 */
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
			String sql = "SELECT *" 
					+ " FROM sa_v1.food "
					+ " JOIN sa_v1.six_categories ON food.six_categories_id = six_categories.six_categories_id "
					+ " WHERE food_name = ? LIMIT 1"; /* 待改 */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, name);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			/** 正確來說資料庫只會有一筆該食物編號之資料，因此其實可以不用使用 while 迴圈 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int id = rs.getInt("food_id");
				String food_name = rs.getString("food_name");
				float calories = rs.getFloat("calories");
				float categories[] = new float[6];
				categories[0] = rs.getFloat("grains");
				categories[1] = rs.getFloat("meats_and_protein");
				categories[2] = rs.getFloat("vegetables");
				categories[3] = rs.getFloat("fruits");
				categories[4] = rs.getFloat("milk_and_products");
				categories[5] = rs.getFloat("fats");
				int categories_id = rs.getInt("six_categories_id");

				/** 將每一筆食物資料產生一名新Food物件 */
				f = new Food(id, food_name, calories, categories,categories_id); /* 待改成我們的資料庫屬性 */
				/** 取出該名食物之資料並封裝至 JSONsonArray 內 */
				jsa.put(f.getData()); 	/* getData：Food裡面的方法 */
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

		/** 將SQL指令、花費時間、影響行數與所有食物資料之JSONArray，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}

	/*public JSONObject getUpdateTimes(Food f) {

		return jso;
	}*/

	public JSONObject create(Food f) {
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql1 = "INSERT INTO sa_v1.six_categories (" +
							"six_categories_label, grains, meats_and_protein, vegetables, fruits, milk_and_products, fats)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?)";
			pres = conn.prepareStatement(sql1, Statement.RETURN_GENERATED_KEYS);
			pres.setString(1, "food");
			float nutri_type[] = f.getCategories();
			for (int i = 2; i <= 7; i++) {
				pres.setFloat(i, nutri_type[i-2]);
			}
			row = pres.executeUpdate();
			// Get six_categories's PK as food's FK
			ResultSet generated_key = pres.getGeneratedKeys(); // Get the last auto-generated key
			int last_insert_key = 0;
			if(generated_key.next()) {
				last_insert_key = generated_key.getInt(1);
			}
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			String sql2 = "INSERT INTO `sa_v1`.`food`(`food_name`, `calories`, `six_categories_id`)"
					+ " VALUES(?, ?, ?)";

			/** 取得所需之參數 */
			String name = f.getFoodName();
			float calories = f.getCalories();
			int categories_id = last_insert_key;
			

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql2);
			pres.setString(1, name);
			pres.setFloat(2,calories);
			pres.setInt(3,categories_id);

			/** 執行新增之SQL指令並記錄影響之行數 */
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
	
	public JSONObject update(Food f) {
		/** 紀錄回傳之資料 */
		JSONArray jsa = new JSONArray();
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		int row = 0;

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			/*
			 * String sql =
			 * "Update `missa`.`members` SET `name` = ? ,`password` = ? , `modified` = ? WHERE `email` = ?"
			 * ;
			 */
			String sql1 = "UPDATE six_categories "
					+ "SET six_categories.grains = ? , six_categories.meats_and_protein = ? , "
					+ "six_categories.vegetables = ? , six_categories.fruits = ? , "
					+ "six_categories.milk_and_products = ? , six_categories.fats = ? "
					+ "WHERE six_categories.six_categories_id = ?";
			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql1);
			for(int i = 1; i <=6; i++) {
				pres.setFloat(i, f.getCategories()[i-1]);
			}
			pres.setInt(7, f.getCategoriesId());
			/** 執行更新之SQL指令並記錄影響之行數 */
			row = pres.executeUpdate();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			/*sql2 statement*/
			String sql2 = "Update food SET food.food_name = ? , food.calories = ?"
						+ "WHERE food.food_id = ?";
			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql2);
			pres.setString(1, f.getFoodName());
			pres.setFloat(2, f.getCalories());
			pres.setInt(3, f.getID());
			/** 執行更新之SQL指令並記錄影響之行數 */
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
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	} 
	
	/*public void setUpdateTimes(Food f) {

	}*/
}
