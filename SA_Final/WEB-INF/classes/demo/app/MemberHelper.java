package demo.app;

import java.sql.*;
import org.json.*;
import java.util.Date;

import demo.util.DBMgr;

public class MemberHelper {

	private static MemberHelper Mh;
	private Connection conn = null;
	/** PreparedStatement是用來執行預先編譯的 SQL 語句的介面 **/
	private PreparedStatement pres = null;

	private MemberHelper() {

	}

	public static MemberHelper getHelper() {
		/** Singleton檢查是否已經有MemberHelper物件，若無則new一個，若有則直接回傳 */
		if (Mh == null) {
			Mh = new MemberHelper();
		}
		return Mh;
	}

	public JSONObject deleteByID(int id) {
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
			String sql = "DELETE FROM member WHERE member.member_id = ?";

			/** pres已經編譯了一個指定的 SQL 語句 (sql)。接下來便可以使用這個 PreparedStatement 物件來執行 SQL 查詢 */
			pres = conn.prepareStatement(sql);
			/** 將整數值 (id) 設定到 SQL 語句中的第一個占位符 */
			pres.setInt(1, id);
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

	public JSONObject getAll() {
		/** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
		Member M = null;
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
			String sql = "SELECT * FROM sa_v1.member"; /* 待改 */

			/** 將參數回填至SQL指令當中，若無則不用只需要執行 prepareStatement */
			pres = conn.prepareStatement(sql);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int member_id = rs.getInt("member_id");
				String name = rs.getString("member_name");
				String email = rs.getString("member_email");
				String password = rs.getString("member_password");
				// int login_times = rs.getInt("login_times");

				/** 將每一筆會員資料產生一名新Member物件 */
				M = new Member(member_id, email, name, password/* , login_times */); /* 待改成我們自己的資料庫屬性 */
				/** 取出該名會員之資料並封裝至 JSONsonArray 內 */
				jsa.put(M.getData());
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

	public JSONObject getByID(String id) {
		/** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
		Member M = null;
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
			String sql = "SELECT * FROM `sa_v1`.`member` WHERE `member_id` = ? LIMIT 1"; /* 待改 */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			/** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int member_id = rs.getInt("member_id"); /* 待改成我們的資料庫屬性 */
				String name = rs.getString("member_name");
				String email = rs.getString("member_email");
				String password = rs.getString("member_password");
				/* int login_times = rs.getInt("login_times"); */
				/* String status = rs.getString("status"); */

				/** 將每一筆會員資料產生一名新Member物件 */
				M = new Member(member_id, email, name, password/* , login_times, status */); /* 待改成我們的資料庫屬性 */
				/** 取出該名會員之資料並封裝至 JSONsonArray 內 */
				jsa.put(M.getData()); /* getData：Member裡面的方法 */
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

	public boolean checkDuplicate(Member M) {
		/** 紀錄SQL總行數，若為「-1」代表資料庫檢索尚未完成 */
		int row = -1;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;

		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT count(*) FROM `sa_v1`.`member` WHERE `member_email` = ?"; /* 待改 */

			/** 取得所需之參數 */
			String email = M.getEmail(); /* Member的方法getEmail */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, email);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 讓指標移往最後一列，取得目前有幾行在資料庫內 */
			rs.next();
			row = rs.getInt("count(*)");
			System.out.print(row);

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

		/**
		 * 判斷是否已經有一筆該電子郵件信箱之資料 若無重複則回傳False，否則回傳True
		 */
		return (row == 0) ? false : true;
	}

	public JSONObject create(Member M) {
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
			 * "INSERT INTO `missa`.`members`(`name`, `email`, `password`, `modified`, `created`, `login_times`, `status`)"
			 * + " VALUES(?, ?, ?, ?, ?, ?, ?)";
			 */
			String sql = "INSERT INTO `sa_v1`.`member`(`member_name`, `member_email`, `member_password`)"
					+ " VALUES(?, ?, ?)"; /* 待改成我們的資料庫路徑 */

			/** 取得所需之參數 */
			String name = M.getName(); /* Member的方法getName */
			String email = M.getEmail(); /* Member的方法getEmail */
			String password = M.getPassword(); /* Member的方法getPassword */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
			pres.setString(1, name);
			pres.setString(2, email);
			pres.setString(3, password);

			/** 執行新增之SQL指令並記錄影響之行數 */
			row = pres.executeUpdate();

			ResultSet generated_key1 = pres.getGeneratedKeys(); // Get the last auto-generated key
			int last_insert_key_member = 0;
			if(generated_key1.next()) {
				last_insert_key_member = generated_key1.getInt(1);
			}
			
			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			sql = "INSERT INTO sa_v1.six_categories ("
					+ "six_categories_label, grains, meats_and_protein, vegetables, fruits, milk_and_products, fats)"
					+ " VALUES('personal_inbody', 0, 0, 0, 0, 0, 0)";
			pres = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);

			row = pres.executeUpdate();
			
			ResultSet generated_key2 = pres.getGeneratedKeys(); // Get the last auto-generated key
			int last_insert_key_six_category = 0;
			if(generated_key2.next()) {
				last_insert_key_six_category = generated_key2.getInt(1);
			}
			
			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);
			
			sql = "INSERT INTO `sa_v1`.`personal_inbody`(member_id, "
					+ "personal_inbody_date, age, gender, height, "
					+ "personal_inbody_weight, self_activity, "
					+ "basal_metabolic_rate, total_daily_energy_expenditure, "
					+ "six_categories_id) "
					+ " VALUES(?, ?, 0, 'undefined', 0, 0, 'undefined', 0, 0, ?)";
			pres = conn.prepareStatement(sql);
			pres.setInt(1, last_insert_key_member);
			pres.setTimestamp(2, new Timestamp(new Date().getTime()));
			pres.setInt(3, last_insert_key_six_category);
			
			row = pres.executeUpdate();
			
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

	public JSONObject update(Member M) {
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
			String sql = "Update `sa_v1`.`member` SET `member_name` = ? ,`member_password` = ? WHERE `member_id` = ?";

			/** 取得所需之參數 */
			String name = M.getName(); /* Member的方法getName */
			int id = M.getID(); /* Member的方法getEmailName */
			String password = M.getPassword(); /* Member的方法getPassword */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, name); /* 以下4行牽扯到資料庫欄位 */
			pres.setString(2, password);
			pres.setInt(3, id);
			/* pres.setTimestamp(4, Timestamp.valueOf(LocalDateTime.now())); */
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

	public JSONObject getByName(String name) {
		/** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
		Member M = null;
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
			String sql = "SELECT * FROM `sa_v1`.`member` WHERE `member_name` = ?"; /* 待改 */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, name);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int id = rs.getInt("member_id"); /* 待改成我們的資料庫屬性 */
				String member_name = rs.getString("member_name");
				String email = rs.getString("member_email");
				String password = rs.getString("member_password");
				/* int login_times = rs.getInt("login_times"); */
				/* String status = rs.getString("status"); */

				/** 將每一筆會員資料產生一名新Member物件 */
				/* M = new Member(id, email, password, member_name, login_times, status); */
				M = new Member(id, email, member_name, password);
				/** 取出該名會員之資料並封裝至 JSONsonArray 內 */
				jsa.put(M.getData()); /* getData：Member裡面的方法 */
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

	public JSONObject getByMail(String mail) {
		/** 新建一個 Member 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
		Member M = null;
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
			String sql = "SELECT * FROM `sa_v1`.`member` WHERE `member_email` = ? LIMIT 1"; /* 待改 */

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, mail);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			/** 正確來說資料庫只會有一筆該會員編號之資料，因此其實可以不用使用 while 迴圈 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				row += 1;

				/** 將 ResultSet 之資料取出 */
				int id = rs.getInt("member_id"); /* 待改成我們的資料庫屬性 */
				String name = rs.getString("member_name");
				String member_email = rs.getString("member_email");
				String password = rs.getString("member_password");
				/* int login_times = rs.getInt("login_times"); */
				/* String status = rs.getString("status"); */

				/** 將每一筆會員資料產生一名新Member物件 */
				/* M = new Member(id, member_email, password, name, login_times, status); */ /* 待改成我們的資料庫屬性 */
				M = new Member(id, member_email, name, password);
				/** 取出該名會員之資料並封裝至 JSONsonArray 內 */
				jsa.put(M.getData()); /* getData：Member裡面的方法 */
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
}
