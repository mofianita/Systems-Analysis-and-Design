package demo.app;
import java.sql.*;
import org.json.*;

import demo.util.DBMgr;
public class DataAnalysisHelper {
	private static DataAnalysisHelper dah;
	private Connection conn = null;
	private PreparedStatement pres = null;
	private DataAnalysisHelper() {
		
	}
	public static DataAnalysisHelper getHelper() {
		if(dah == null) {
			dah = new DataAnalysisHelper();
		}
		return dah;
	}
	public JSONObject createImgRef(String file_name) {
		String execute_sql = "";
		long start_time = System.nanoTime();
		int row = 0;
		ResultSet rs = null;
		try {
			/** 取得資料庫之連線 */
			conn = DBMgr.getConnection();

			/** SQL指令 */
			String sql = "";

			/** pres已經編譯了一個指定的 SQL 語句 (sql)。接下來便可以使用這個 PreparedStatement 物件來執行 SQL 查詢 */
			pres = conn.prepareStatement(sql);
			/** 將整數值 (id) 設定到 SQL 語句中的第一個占位符 */
			pres.setString(1, file_name);
			/** 執行刪除之SQL指令並記錄影響之行數 */
			row = pres.executeUpdate();

			/** 紀錄真實執行的SQL指令，並印出 **/
			execute_sql = pres.toString();
			System.out.println(execute_sql);

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
		response.put("sql", execute_sql);
		response.put("row", row);
		response.put("time", duration);

		return response;
	}
}

