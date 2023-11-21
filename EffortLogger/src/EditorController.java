/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.stage.Stage;

public class EditorController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML public ComboBox<String> project;
	@FXML public ComboBox<String> logEntry;
	@FXML public ComboBox<String> lifeCycle;
	@FXML public ComboBox<String> effort;
	@FXML public ComboBox<String> plan;
	
	public void switchToELConsole(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("loggerConsole.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void entryDelete(ActionEvent event) throws IOException
	{
		
	}
	
	public void entrySplit(ActionEvent event) throws IOException
	{
		
	}
	
	public void entryUpdate(ActionEvent event) throws IOException
	{
		
	}
	
	public void logDelete(ActionEvent event) throws IOException
	{
		
	}
	
	public void readToCB(ComboBox<String> cb, String name) throws IOException
	{
		BufferedReader bfr = null;
		  try 
		  {
			  bfr = new BufferedReader(new FileReader(name + ".txt"));
			  cb.getItems().clear();
			  String str;
			  while ((str = bfr.readLine()) != null)
			  {
				  cb.getItems().addAll(str);
			  }
		  } catch (IOException e) {
		  } finally {
		    try { bfr.close(); } catch (Exception ex) { }
		    }
	}
	
	public void initialize() {
		try {
			readToCB(project, "projects");
			readToCB(lifeCycle,"lifecycle");
			readToCB(effort,"lifecycle");
			readToCB(plan,"plans");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
