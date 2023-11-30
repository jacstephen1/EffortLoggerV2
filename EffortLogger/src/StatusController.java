/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.XYChart;
import javafx.stage.Stage;

public class StatusController implements Initializable{
	//Variables
	private Stage stage;
	private Scene scene;
	private Parent root;
	private ArrayList<String> projects = new ArrayList<String>();
	private ArrayList<Integer> projectsDefectCount = new ArrayList<Integer>();
	private XYChart.Series<String, Integer> defectSeries;
	
	//FXML Variables
	@FXML private BarChart<String, Integer> graph;
	
	public void switchToMain(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Read teams and info from text file
	public void read(String projectFile, String defectsFile) throws IOException
	{
		//INITIALIZE PROJECTS WITH COUNT OF 0
		BufferedReader bfr = null;
		try 
		{
			bfr = new BufferedReader(new FileReader(projectFile + ".txt")); //Opens bfr
			String str; //empty
			while ((str = bfr.readLine()) != null) //iterate
			{
				projects.add(str); //Put the project name into project list
				projectsDefectCount.add(0);
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
		
		//FIND DEFECT COUNT
		try 
		{
			bfr = new BufferedReader(new FileReader(defectsFile + ".txt")); //Opens bfr
			String defectsStr; //empty
			while ((defectsStr = bfr.readLine()) != null) //iterate
			{
				String search = defectsStr.substring(defectsStr.indexOf('-')+2, defectsStr.indexOf('|')-1);
				if (projects.contains(search))
				{
					for (int i = 0; i < projects.size(); i++)
					{
						if (projects.get(i).equals(search))
						{
							projectsDefectCount.set(i, projectsDefectCount.get(i) + 1);
						}
					}
				}
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
	}
	
	//Read info into charts
	public void readInfo()
	{
		//Set Series Name
		//defectSeries.setName("Projects");
		
		for(int i = 0; i < projects.size(); i++)
		{
			//Add data to data series
			defectSeries.getData().add(new XYChart.Data<String, Integer>(projects.get(i), projectsDefectCount.get(i)));
		}
		//Add data to chart
		graph.getData().add(defectSeries);
	}
	
	public void setGraphInfo()
	{
		defectSeries = new XYChart.Series<String, Integer>(); //New Data Series
		graph.getData().clear(); //Empty Graph
		readInfo(); //read team info
	}
	
	//Set the choice box to the list of projects found in the text file from the read function
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			read("projects", "defect_logs"); //Read the text file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		setGraphInfo();
	}
}
