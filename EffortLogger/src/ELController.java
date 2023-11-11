/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.shape.Rectangle;
import javafx.scene.paint.Color;
import javafx.stage.Stage;


public class ELController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML public Label clockText;
	@FXML public Rectangle recRG;
	@FXML public ComboBox<String> project;
	@FXML public ComboBox<String> lifeCycle;
	@FXML public ComboBox<String> effort;
	@FXML public ComboBox<String> deliverable;
	
	final String running = "Clock is running";
	final String stopped = "Clock is running";
	private int Clock = 0;
	
	public void switchToEL(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("effortLoggerHub.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void startPress(ActionEvent event) throws IOException
	{
		if(Clock == 1) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Message");
			alert.setHeaderText("Clock is already running.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.OK)) { 
			       alert.close();
			}else if (result.get().equals(ButtonType.NO)) {
			       alert.close();
			}
		}else {
			recRG.setFill(Color.GREEN);
			clockText.setText(running);
			//add the database the start time and all the selected elements
			if(project.getValue() == null || lifeCycle.getValue() == null || effort.getValue() == null || deliverable.getValue() == null){
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Message");
				alert.setHeaderText("One or more of the selections is blank.");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get().equals(ButtonType.OK)) { 
				       alert.close();
				}else if (result.get().equals(ButtonType.NO)) {
				       alert.close();
				}
			}
			Clock = 1;
		}
	}
	
	public void stopPress(ActionEvent event) throws IOException
	{
		if(Clock == 0) {
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Message");
			alert.setHeaderText("Clock is Stopped.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.OK)) { 
			       alert.close();
			}else if (result.get().equals(ButtonType.NO)) {
			       alert.close();
			}
		}else {
			recRG.setFill(Color.RED);
			clockText.setText(stopped);
			//add the database the end time and all the selected elements
			if(project.getValue() == null || lifeCycle.getValue() == null || effort.getValue() == null || deliverable.getValue() == null){
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Message");
				alert.setHeaderText("One or more of the selections is blank.");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get().equals(ButtonType.OK)) { 
				       alert.close();
				}else if (result.get().equals(ButtonType.NO)) {
				       alert.close();
				}
			}
			Clock= 0;
		}
	}
	
	public void switchToDefect(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("defectConsole.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToEditor(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("logEditor.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
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
		recRG.setFill(Color.RED);
		clockText.setText(stopped);
		Clock = 0;
		try {
			readToCB(project, "projects");
			readToCB(lifeCycle, "lifecycle");
			readToCB(effort, "effort");
			readToCB(deliverable, "deliverables");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
