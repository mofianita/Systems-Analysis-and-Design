package demo.app;

import org.json.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DataAnalysis {
	private int member_id;
	private DefaultCategoryDataset dataset;
	private String date_time;
	private JFreeChart chart;
	private String chart_info_type;
	private String imageName;
	
	public DataAnalysis(int member_id, String chart_info_type) {
		this.member_id = member_id;
		this.chart_info_type = chart_info_type;
	}
	
	public int getMemberID() {
		return this.member_id;
	}
	
	public String getChartInfoType() {
		return this.chart_info_type;
	}
	
	public JSONObject createWeightChart() {
		JSONObject data = new JSONObject();
		this.dataset = createWeightDataset();
		this.chart = ChartFactory.createLineChart(
				"Weight Change",
				"TimeLine",
				"(kg)",
				this.dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);
		saveChart(this.chart, "statics/img/chart", this.chart_info_type);
		data = storeImgNameInDB(this.imageName);
		return data;
	}
	
	public JSONObject createCalorieChart(){
		JSONObject data = new JSONObject();
		this.dataset = createCalorieDataset();
		this.chart = ChartFactory.createLineChart(
				"Calorie Intake",
				"TimeLine",
				"(kcal)",
				this.dataset,
				PlotOrientation.VERTICAL,
				false,
				false,
				false);
		saveChart(this.chart, "statics/img/chart", this.chart_info_type);
		data = storeImgNameInDB(this.imageName);
		return data;
	}
	
	private DefaultCategoryDataset createWeightDataset() {
		DefaultCategoryDataset dcd = new DefaultCategoryDataset();
		// need chart_info_type, member_id
		// dcd.addValue(value, "", dateTime)
		return dcd;
	}
	
	private DefaultCategoryDataset createCalorieDataset() {
		DefaultCategoryDataset dcd = new DefaultCategoryDataset();
		return dcd;
	}
	
	private void saveChart(JFreeChart chart, String imageDir,
							String chart_info_type) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
			this.date_time = sdf.format(new Date());
			this.imageName = chart_info_type + "_" + this.date_time + ".png";
			String imgPath = imageDir + "/" + this.imageName;
			File outF = new File(imgPath);
			ChartUtilities.saveChartAsPNG(outF, chart, 600, 450);
		}
		catch (IOException e) {
			e.printStackTrace();
		}
	}
	private JSONObject storeImgNameInDB(String imageName) {
		JSONObject data = new JSONObject();
		return data;
	}
}
