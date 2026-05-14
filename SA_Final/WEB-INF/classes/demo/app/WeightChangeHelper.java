package demo.app;

import java.sql.*;
import java.util.ArrayList;
import java.sql.Timestamp;
import org.json.*;

import demo.util.DBMgr;

public class WeightChangeHelper {

	private static WeightChangeHelper wch;
	private Connection conn = null;
	/** PreparedStatement是用來執行預先編譯的 SQL 語句的介面 **/
	private PreparedStatement pres = null;

	private WeightChangeHelper() {

	}

	public static WeightChangeHelper getHelper() {
		/** Singleton檢查是否已經有WeightChangeHelper物件，若無則new一個，若有則直接回傳 */
		if (wch == null) {
			wch = new WeightChangeHelper();
		}
		return wch;
	}
	// For Data Analysis
	public ArrayList<WeightChange> getByID(String id) { // refers to member_id
		/** 新建一個 WeightChange 物件之 m 變數，用於紀錄每一位查詢回之會員資料 */
		WeightChange wc = null;
		/** 用於儲存所有檢索回之會員，以JSONArray方式儲存 */
		//JSONArray jsa = new JSONArray();
		/** 記錄實際執行之SQL指令 */
		String exexcute_sql = "";
		/** 紀錄程式開始執行時間 */
		//long start_time = System.nanoTime();
		/** 紀錄SQL總行數 */
		//int row = 0;
		/** 儲存JDBC檢索資料庫後回傳之結果，以 pointer 方式移動到下一筆資料 */
		ResultSet rs = null;
		ArrayList<WeightChange> out = new ArrayList<>();
		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();
			/** SQL指令 */
			String sql = "SELECT DATE(out_table.weight_change_date) AS date_part, " +
                    	"out_table.weight_change_weight" +
                    	"FROM ( " +
                    	"  SELECT weight_change_date, " +
                    	"         weight_change_weight " +
                    	"  FROM sa_v1.weight_change " +
                    	"  WHERE weight_change.member_id = ?" +
                    	"  ORDER BY weight_change_date ASC " +
                    	"  LIMIT 10 " +
                    	") AS out_table";

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql);
			pres.setString(1, id);
			/** 執行查詢之SQL指令並記錄其回傳之資料 */
			rs = pres.executeQuery();

			/** 紀錄真實執行的SQL指令，並印出 **/
			exexcute_sql = pres.toString();
			System.out.println(exexcute_sql);

			/** 透過 while 迴圈移動pointer，取得每一筆回傳資料 */
			while (rs.next()) {
				/** 每執行一次迴圈表示有一筆資料 */
				//row += 1;

				/** 將 ResultSet 之資料取出 */
				String weight_change_date = rs.getString("date_part");
				float weight_change_weight = rs.getFloat("weight_change_weight");

				/** 將每一筆會員資料產生一名新WeightChange物件 */
				wc = new WeightChange(weight_change_date, weight_change_weight);
				out.add(wc);
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
//		long end_time = System.nanoTime();
//		/** 紀錄程式執行時間 */
//		long duration = (end_time - start_time);
//
//		/** 將SQL指令、花費時間、影響行數與所有會員資料之JSONArray，封裝成JSONObject回傳 */
//		JSONObject response = new JSONObject();
//		response.put("sql", exexcute_sql);
//		response.put("row", row);
//		response.put("time", duration);
//		response.put("data", jsa);

		return out;
	}

	/*public JSONObject getUpdateTimes(WeightChange wc) {

		return jso;
	}*/

	public JSONObject create(WeightChange wc) {
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
			String sql = "INSERT INTO `sa_v1`.`weight_change`(`weight_change_date`, `weight_change_weight`, `member_id`)"
					+ " VALUES(?, ?, ?)"; /* 待改成我們的資料庫屬性 */

			/** 取得所需之參數 */
			Timestamp weight_change_date = wc.getDateTime();
			float weight_change_weight = wc.getWeight();
			int member_id = wc.getMemberID();

			/** 將參數回填至SQL指令當中 */
			pres = conn.prepareStatement(sql); 
			pres.setTimestamp(1, weight_change_date);
			pres.setFloat(2, weight_change_weight);
			pres.setInt(3, member_id);

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

}
