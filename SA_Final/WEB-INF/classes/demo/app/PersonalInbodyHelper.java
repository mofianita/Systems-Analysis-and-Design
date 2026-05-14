package demo.app;

import java.sql.*;
import java.sql.Timestamp;
import org.json.*;

import demo.util.DBMgr;

public class PersonalInbodyHelper {

	private static PersonalInbodyHelper pih;
	private Connection conn = null;
	/** PreparedStatement是用來執行預先編譯的 SQL 語句的介面 **/
	private PreparedStatement pres = null;

	private PersonalInbodyHelper() {

	}

	public static PersonalInbodyHelper getHelper() {
		/** Singleton檢查是否已經有PersonalInbodyHelper物件，若無則new一個，若有則直接回傳 */
		if (pih == null) {
			pih = new PersonalInbodyHelper();
		}
		return pih;
	}

	// 因為刪除member時會把personal_inbody、six_categories的資料刪除
	// 刪除都寫在member那邊

	// Personal_Inbody似乎不會有getAll

	public JSONObject getByID(String id) {					//取member_id
		/** 新建一個 PersonalInbody 物件之 pi 變數，用於紀錄每一位查詢回之身體數值資料 */
		PersonalInbody pi = null;
		/** 用於儲存所有檢索回之身體數值，以JSONArray方式儲存 */
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
			String sql = "SELECT * FROM sa_v1.personal_inbody WHERE member_id = ? LIMIT 1";

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			/** 正確來說資料庫只會有一筆該身體數值編號之資料，因此其實可以不用使用 while 迴圈 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int member_id = rs.getInt("member_id"); /* 待改成我們的資料庫屬性 */
				int age = rs.getInt("age");
				String gender = rs.getString("gender");
				float height = rs.getFloat("height");
				float weight = rs.getFloat("personal_inbody_weight");
				String self_activity = rs.getString("self_activity");

				/** 將每一筆身體數值資料產生一名新PersonalInbody物件 */
				pi = new PersonalInbody(gender, age, height, weight, self_activity, member_id);
				/** 取出該身體數值之資料並封裝至 JSONsonArray 內 */
				jsa.put(pi.getData()); /* getData：PersonsalInbody裡面的方法 */
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

		/** 將SQL指令、花費時間、影響行數與所有身體數值資料之JSONArray，封裝成JSONObject回傳 */
		JSONObject response = new JSONObject();
		response.put("sql", exexcute_sql);
		response.put("row", row);
		response.put("time", duration);
		response.put("data", jsa);

		return response;
	}

	/*public JSONObject getByRecordDate(Date record_date) {

		return jso;
	}*/

	/*public JSONObject getUpdateTimes(PersonalInbody pi) {

		return jso;
	}*/

	public JSONObject create(PersonalInbody pi) {
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
			String sql1 = "INSERT INTO sa_v1.six_categories("
					+ "six_categories_label, grains, meats_and_protein, vegetables, "
					+ "fruits, milk_and_products, fats )"
					+ "VALUES(?, ?, ?, ?, ?, ?, ?)";
			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql1);
			pres.setString(1, "personal_inbody");
			float categories_require[] = pi.getCategoriesRequirement();
			for (int i = 2; i <= 7; i++) {
				pres.setFloat(i, categories_require[i-2]);
			}
			row = pres.executeUpdate();
			// Get six_categories' PK as personal_inbody's FK
			ResultSet generated_key = pres.getGeneratedKeys(); // Get the last auto-generated key
			int last_insert_key = 0;
			if(generated_key.next()) {
				last_insert_key = generated_key.getInt(1);
			}
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			/** SQL指令 */
			String sql2 = "INSERT INTO sa_v1.personal_inbody(member_id, personal_inbody_date, "
					+ "age, gender, height, personal_inbody_weight, self_activity, "
					+ "basal_metabolic_rate, total_daily_energy_expenditure, "
					+ "six_nutrients_id)"
					+ " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?)"; /* 待改成我們的資料庫屬性 */
			/** 將參數回填至SQL指令當中 */
			/* 取得所需之參數 */
			int member_id = pi.getMemberID(); 			/* PersonalInbody的方法getID */
			Timestamp record_date = pi.getLastUpdateTime();						/*要補*/
			int age = pi.getAge();				
			String gender = pi.getGender();
			float height = pi.getHeight();
			float weight = pi.getWeight();
			String self_activity = pi.getSelfActivity();
			float BMR = pi.getBMR();
			float recom_calories_intake = pi.getRecomCalorie();
			int nutrients_id = last_insert_key;

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql2); 
			pres.setInt(1, member_id);
			pres.setTimestamp(2,record_date);
			pres.setInt(3, age);
			pres.setString(4, gender);
			pres.setFloat(5, height);
			pres.setFloat(6, weight);
			pres.setString(7, self_activity);
			pres.setFloat(8, BMR);
			pres.setFloat(9, recom_calories_intake);
			pres.setInt(10, nutrients_id);
			

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

	public JSONObject update(PersonalInbody pi) {	/*有點不確定*/
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
			String sql = "UPDATE personal_inbody "
				+ "SET personal_inbody_date = ?, age =?, "
				+ "gender = ?, height = ?, personal_inbody_weight = ?, "
				+ "self_activity = ?, basal_metabolic_rate = ?, "
				+ "total_daily_energy_expenditure = ? "
				+ "WHERE member_id = ?";
			Timestamp record_date = pi.getLastUpdateTime();	
			int age = pi.getAge();					
			String gender = pi.getGender();
			float height = pi.getHeight();
			float weight = pi.getWeight();
			String self_activity = pi.getSelfActivity();
			float BMR = pi.getBMR();
			float recom_calories_intake = pi.getRecomCalorie();
			int member_id = pi.getMemberID();
			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setTimestamp(1, record_date); 			/* 以下12行牽扯到資料庫欄位 */
			pres.setInt(2, age);
			pres.setString(3, gender);
			pres.setFloat(4, height);
			pres.setFloat(5, weight);
			pres.setString(6, self_activity);
			pres.setFloat(7, BMR);
			pres.setFloat(8, recom_calories_intake);
			pres.setInt(9, member_id);
			/** 執行更新之SQL指令並記錄影響之行數 */
			row = pres.executeUpdate();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			
			sql = "UPDATE six_categories "
				+ "SET grains = ?, meats_and_protein = ?, vegetables = ?, "
				+ "fruits = ?, milk_and_products = ?, fats = ? "
				+ "WHERE six_categories.six_categories_id = "
				+ "(SELECT personal_inbody.six_categories_id FROM personal_inbody "
				+ "WHERE personal_inbody.member_id = ?)";
			pres = conn.prepareStatement(sql);
			for(int i = 1; i <= 6; i++) {
				pres.setFloat(i, pi.getCategoriesRequirement()[i-1]);
			}
			pres.setInt(7, member_id);
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

	/*public void setUpdateTimes(PersonalInbody pi) {

	}*/
}
