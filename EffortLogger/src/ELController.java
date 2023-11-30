/*
 * Authors: Jacob Wetherell & Jon-Paul Sullivan
 * ASU IDs: 1224869945 and 1220788725
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Optional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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


public class ELController 
{

	//set up for changing scenes
	private Stage stage;
	private Scene scene;
	private Parent root;
	
	//setting up the current fxml elements
	@FXML public Label clockText;
	@FXML public Rectangle recRG;
	@FXML public ComboBox<String> project;
	@FXML public ComboBox<String> lifeCycle;
	@FXML public ComboBox<String> effort;
	@FXML public ComboBox<String> deliverable;
	
	//set up some strings for the "clock" (changing the text on the clock label) and the combo boxes
	final String running = "Clock is running";
	final String stopped = "Clock is stopped";
	final String nosel = "No Selection";
	
	//set up the clock int
	private int Clock = 0;
	
	public void switchToEL(ActionEvent event) throws IOException
	{
		//switch the scene
		root = FXMLLoader.load(getClass().getResource("effortLoggerHub.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToDefect(ActionEvent event) throws IOException
	{
		//switch the scene
		root = FXMLLoader.load(getClass().getResource("defectConsole.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToEditor(ActionEvent event) throws IOException
	{
		//switch the scene
		root = FXMLLoader.load(getClass().getResource("logEditor.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void initialize() 
	{
		//we do this when the scene first loads
		//trying to initialize the combo boxes with all there options
		try 
		{
			readToCB(project, "projects");
			readToCB(lifeCycle, "lifecycle");
			readToCB(effort, "effort");
			readToCB(deliverable, "deliverables");
		} 
		catch (IOException e) //catching in case we error
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//make a log with the info from the last entry in the database (handled by dbutils)
		Log lastLog = DBUtils.getLastEffortLog();
		
		//if the last entry is 1 (aka the blank one) then we start the clock as off and we set the combo boxes as default
		if(lastLog.getId() == 1) {
			//set up the ui elements
			recRG.setFill(Color.RED);
			clockText.setText(stopped);
			
			//set up the clock
			Clock = 0;
			
			//set up the default values for the combo box
			project.getSelectionModel().select(0);
			lifeCycle.getSelectionModel().select(0);
			effort.getSelectionModel().select(0);
			deliverable.getSelectionModel().select(0);
		}
		else if((lastLog.getEndTime() != null) && (lastLog.getId() != 1)) //if its not the blank entry and the end time is full start the clock of and with default
		{
			
			//set up the ui elements
			recRG.setFill(Color.RED);
			clockText.setText(stopped);
			
			//set up the clock
			Clock = 0;
			
			//set up the default values for the combo box
			project.getSelectionModel().select(0);
			lifeCycle.getSelectionModel().select(0);
			effort.getSelectionModel().select(0);
			deliverable.getSelectionModel().select(0);
		}
		else
		{
			
			//set up the ui elements
			recRG.setFill(Color.GREEN);
			clockText.setText(running);
			
			//set the clock
			Clock = 1;
			
			//set up the previous selections for the combo box
			project.getSelectionModel().select(lastLog.getProject());
			lifeCycle.getSelectionModel().select(lastLog.getLifeCycleStep());
			effort.getSelectionModel().select(lastLog.getEffortCategory());
			deliverable.getSelectionModel().select(lastLog.getDeliverable());
		}
		
	}
	
	public void startPress(ActionEvent event) throws IOException
	{
		
		//if the clock is running already we alert the user
		if(Clock == 1)
		{
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Message");
			alert.setHeaderText("Clock is already running.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.OK)) { 
			       alert.close();
			}
		}
		else //the clock is stopped so we start it
		{
			
			//if any of the values are not selected or on the default, alert the user
			if(project.getValue() == null || lifeCycle.getValue() == null || effort.getValue() == null || deliverable.getValue() == null || nosel.equals(project.getValue()) || nosel.equals(lifeCycle.getValue()) || nosel.equals(effort.getValue()) || nosel.equals(deliverable.getValue()) )
			{
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Message");
				alert.setHeaderText("One or more of the selections is blank. Create entry with blank parts now?"
						+ "They must be filled before the log can be completed?");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get().equals(ButtonType.OK)) { 
					
					//make the log anyways
					createStartLog();
					
					//close alert
					alert.close();
				}
				else if (result.get().equals(ButtonType.NO)) 
				{
					
					//close alert
				    alert.close();
				}
			}
			else //all the values are filled so we make the log
			{
				createStartLog();
				
			}
		}
	}
	
	public void stopPress(ActionEvent event) throws IOException
	{
		//if the clock is already stopped we alert the user
		if(Clock == 0)
		{
			Alert alert = new Alert(Alert.AlertType.WARNING);
			alert.setTitle("Message");
			alert.setHeaderText("Clock is already stopped.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.OK)) { 
			       alert.close();
			}
		}
		else //the clock is running so we stop it
		{
			//if any of the values are null or not selected, then we alert the user
			if(project.getValue() == null || lifeCycle.getValue() == null || effort.getValue() == null || deliverable.getValue() == null 
					|| nosel.equals(project.getValue()) || nosel.equals(lifeCycle.getValue()) || nosel.equals(effort.getValue()) || nosel.equals(deliverable.getValue()) )
			{
				Alert alert = new Alert(Alert.AlertType.WARNING);
				alert.setTitle("Message");
				alert.setHeaderText("One or more of the selections is blank. Pressing ok will make an entry with blank sections now.");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get().equals(ButtonType.OK)) 
				{
					alert.close();
				}
			}
			else
			{
				createEndLog();
				clockOff();
			}
			
		}
	}
	
	//function used for populating the combo boxes
	public void readToCB(ComboBox<String> cb, String name) throws IOException
	{
		BufferedReader bfr = null;
		  try 
		  {
			  bfr = new BufferedReader(new FileReader(name + ".txt"));
			  cb.getItems().clear();
			  String str = nosel;
			  cb.getItems().addAll(str);
			  while ((str = bfr.readLine()) != null)
			  {
				  cb.getItems().addAll(str);
			  }
		  } catch (IOException e) {
		  } finally {
		    try { bfr.close(); } catch (Exception ex) { }
		    }
	}
	
	//
	public void createStartLog() 
	{
		
		//creating a log and a date and time object
		Log startLog = new Log("effort", project.getValue());
		LocalTime theTime = LocalTime.now();
		LocalDate theDate = LocalDate.now();
		
		int hour = theTime.getHour();
		int min = theTime.getMinute();
		int sec = theTime.getSecond();
		
		System.out.println(hour);
		System.out.println(min);
		System.out.println(sec);
		
		String time = hour + ":" + min + ":" + sec;
		
		//creating an effort log for the log
		startLog.createEffortLog(0, time , null, theDate.toString(), lifeCycle.getValue(), effort.getValue(),"none", deliverable.getValue(), "none", "none","none");
		
		//dbutils to makes the entry from the log
		DBUtils.createDBStartLogEntry(startLog);
		
		clockOn();
	}
	
	public void createEndLog() 
	{
		
		//create two logs for the old and new stuffs. create a date and time object
		Log startLog = DBUtils.getLastEffortLog();
		Log currentLog = new Log("effort", project.getValue());
		LocalTime theTime = LocalTime.now();
		LocalDate theDate = LocalDate.now();
		
		int hour = theTime.getHour();
		int min = theTime.getMinute();
		int sec = theTime.getSecond();
		
		System.out.println(hour);
		System.out.println(min);
		System.out.println(sec);
		
		String time = hour + ":" + min + ":" + sec;
		
		//creating an effort log for the log
		currentLog.createEffortLog(startLog.getId(), startLog.getStartTime(), time, theDate.toString(), lifeCycle.getValue(), effort.getValue(), "none", deliverable.getValue(), "none", "none","none");
		if((currentLog.getProject().equals(startLog.getProject())) || (currentLog.getLifeCycleStep().equals(startLog.getLifeCycleStep())) 
				|| (currentLog.getEffortCategory().equals(startLog.getEffortCategory())) || (currentLog.getDeliverable().equals(startLog.getDeliverable())) ) 
		{
			checkDate(startLog, currentLog);
		}
		else
		{
			Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
			alert.setTitle("Message");
			alert.setHeaderText("One or more of the selections has changed between your start and stop. Press ok to continue with the changes.");
			Optional<ButtonType> result = alert.showAndWait();
			if (result.get().equals(ButtonType.OK)) 
			{
				checkDate(startLog, currentLog);
				
			}
			else if (result.get().equals(ButtonType.NO)) 
			{
			       alert.close();
			}
		}
	}
	
	public void checkDate(Log oldLog, Log newLog) 
	{
		if(oldLog.getDate().equals(newLog.getDate()))
		{
			//dbutils to makes the entry from the log
			DBUtils.updateLogEntryById(newLog);
			clockOff();
		}
		else 
		{
			Alert alert1 = new Alert(Alert.AlertType.WARNING);
			alert1.setTitle("Message");
			alert1.setHeaderText("The start date and the end date are not the same day. Both are being recoreded.");
			Optional<ButtonType> result1 = alert1.showAndWait();
			if (result1.get().equals(ButtonType.OK)) 
			{
				String temp = oldLog.getDate() + " - " + newLog.getDate();
				newLog.setDate(temp);
				//dbutils to makes the entry from the log
				DBUtils.updateLogEntryById(newLog);
				clockOff();
			}
		}
	}
	
	public void clockOff() {
		//set the color and text of the ui element
		recRG.setFill(Color.RED);
		clockText.setText(stopped);
		
		//set the clock
		Clock = 0;
	}
	
	public void clockOn() {
		//set the color and text of the ui element
		recRG.setFill(Color.GREEN);
		clockText.setText(running);
		
		//set the clock
		Clock = 1;
	}
}