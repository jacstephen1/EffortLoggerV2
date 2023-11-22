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
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.ChoiceBox;
import javafx.stage.Stage;

public class StatusController implements Initializable{
	//Variables
	private Stage stage;
	private Scene scene;
	private Parent root;
	private ArrayList<String> teams = new ArrayList<String>();
	private XYChart.Series<Number, Number> defectSeries;
	
	//FXML Variables
	@FXML private ChoiceBox<String> teamSelector;
	@FXML private LineChart<Number, Number> graph;
	
	public void switchToMain(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	//Read teams and info from text file
	public void read(String fileName) throws IOException
	{
		BufferedReader bfr = null;
		try 
		{
			bfr = new BufferedReader(new FileReader(fileName + ".txt")); //Opens bfr
			String str; //empty
			while ((str = bfr.readLine()) != null) //iterate
			{
				teams.add(str.substring(0, str.indexOf('|'))); //Put the team name into teams list
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
	}
	
	//Read info into charts
	public void readTeamInfo(String fileName, String teamName)
	{
		//Set Series Name
		defectSeries.setName(teamName);
		
		//Iterator through team list in file to find team
		BufferedReader bfr = null;
		try 
		{
			bfr = new BufferedReader(new FileReader(fileName + ".txt"));
			String str;
			while ((str = bfr.readLine()) != null)
			{
				if (str.substring(0, str.indexOf('|')).equals(teamName)) //Found team
				{
					String[] data = str.substring(str.indexOf('|')+1).split(" "); //Create a data array of info (+1 to make sure it doesn't include |)
					for (String s : data) //Iterate through data
					{
						if (s.indexOf("-") != -1 && !s.substring(0, s.indexOf("-")).equals("") && !s.substring(s.indexOf("-")+1).equals(""))
						{
							//Add data to data series
							defectSeries.getData().add(new Data<Number, Number>(Integer.parseInt(s.substring(0, s.indexOf("-"))), Integer.parseInt(s.substring(s.indexOf("-")+1))));
						}
					}
				}
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
		
		//Add data to chart
		graph.getData().add(defectSeries);
	}
	
	public void setGraphInfo()
	{
		defectSeries = new XYChart.Series<Number, Number>(); //New Data Series
		graph.getData().clear(); //Empty Graph
		readTeamInfo("teamStatus", teamSelector.getSelectionModel().getSelectedItem()); //read team info
	}
	
	//Set the choice box to the list of projects found in the text file from the read function
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			read("teamStatus"); //Read the text file
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		teamSelector.getItems().addAll(teams); //Add teams to list
	}
}
