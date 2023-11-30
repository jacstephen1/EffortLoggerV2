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
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditorController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML public ComboBox<String> project;
	@FXML public ComboBox<String> logEntry;
	@FXML public ComboBox<String> lifeCycle;
	@FXML public ComboBox<String> effort;
	@FXML public ComboBox<String> deliverable;
	@FXML public TextField date;
	@FXML public TextField startTime;
	@FXML public TextField stopTime;
	
	private String nosel = "No Selection";
	private Log selectedLog;
	private boolean updatePoject = false;
	private boolean updating = false;
	
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
		if(selectedLog == null){
			
		}
		else
		{	
			
			Log tempHigher = DBUtils.getLastEffortLog();
			DBUtils.deleteLastEffortLog();
			
			for(int i = ((tempHigher.getId()) - 1); i >= selectedLog.getId(); i--) 
			{
				Log tempLower = DBUtils.getEffortLogById(i);
				tempHigher.setId(i);
				DBUtils.updateLogEntryById(tempHigher);
				tempHigher = tempLower;
			}
			selectedLog = null;
			
			
		}
	}
	
	public void entrySplit(ActionEvent event) throws IOException
	{
		String tempStart = selectedLog.getStartTime();
		String[] starts = tempStart.split(":");
		
		String tempEnd = selectedLog.getEndTime();
		String[] ends = tempEnd.split(":");
		
		int[] startsI = {Integer.parseInt(starts[0]),Integer.parseInt(starts[1]),Integer.parseInt(starts[2])};
		int[] endsI = {Integer.parseInt(ends[0]),Integer.parseInt(ends[1]),Integer.parseInt(ends[2])};
		
		
		int[] half = {endsI[0]-startsI[0],endsI[1]-startsI[1],endsI[2]-startsI[2]};
		
		half[0] = half[0]/2;
		
		if(half[0]%2 == 1) {
			half[1] = half[1]+60;
		}
		
		if(half[1]<0) {
			half[0] = half[0]-1;
			half[1] = half[1]+60;
		}
		
		half[1] = half[1]/2;
		
		if(half[1]%2 == 1) {
			half[2] = half[2]+60;
		}
		
		if (half[2]<0) {
			if(half[1]<0) {
				half[0] = half[0]-1;
				half[1] = half[1]+60;
			}
			half[1] = half[1]-1;
			half[2] = half[2]+60;
		}
		half[1] = half[1]/2;
		
		Log createdEfflortLogFirst = new Log("Defect", project.getValue());
		createdEfflortLogFirst.createEffortLog(selectedLog.getId(), startTime.getText(), (startsI[0] + half[0])+":"+ (startsI[1] + half[1]) +":"+ (startsI[2] + half[2]) , selectedLog.getDate(), 
				lifeCycle.getSelectionModel().getSelectedItem(), effort.getSelectionModel().getSelectedItem(), "none", deliverable.getSelectionModel().getSelectedItem(), "none", "none", "none");
		DBUtils.updateLogEntryById(createdEfflortLogFirst);
		
		
		Log createdEfflortLogSecond = new Log("Defect", project.getValue());
		createdEfflortLogSecond.createEffortLog(selectedLog.getId(), (endsI[0] - half[0])+":"+ (endsI[1] - half[1]) +":"+ (endsI[2] - half[2]), selectedLog.getEndTime(), date.getText(), 
				lifeCycle.getSelectionModel().getSelectedItem(), effort.getSelectionModel().getSelectedItem(), "none", deliverable.getSelectionModel().getSelectedItem(), "none", "none", "none");
		DBUtils.createDBStartLogEntry(createdEfflortLogSecond);
		
		selectedLog = null;
	}
	
	public void entryUpdate(ActionEvent event) throws IOException
	{
		Log createdEfflortLog = new Log("Defect", project.getValue());
		createdEfflortLog.createEffortLog(selectedLog.getId(), startTime.getText(), stopTime.getText(), date.getText(), 
				lifeCycle.getSelectionModel().getSelectedItem(), effort.getSelectionModel().getSelectedItem(), "none", deliverable.getSelectionModel().getSelectedItem(), "none", "none", "none");
		DBUtils.updateLogEntryById(createdEfflortLog);
	}
	
	public void clear(ActionEvent event) throws IOException
	{
		updating = true;
		project.getSelectionModel().select(0);
		logEntry.getSelectionModel().select(0);
		lifeCycle.getSelectionModel().select(0);
		effort.getSelectionModel().select(0);
		deliverable.getSelectionModel().select(0);
		date.clear();
		startTime.clear();
		stopTime.clear();
		selectedLog = null;
		updating = false;
	}
	
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
	
	public void initialize() {
		updating = true;
		try {
			readToCB(project, "projects");
			project.getSelectionModel().select(0);
		} catch (IOException e) {
			e.printStackTrace();
		}
		updating = false;
	}
	
	public void projectSelected(ActionEvent event) throws IOException
	{
		updating = true;
		logEntry.getItems().clear();
		Log lastEffort = DBUtils.getLastEffortLog();
		if(lastEffort.getId() == 1)
		{
			logEntry.getItems().addAll(nosel);
			logEntry.getSelectionModel().select(0);
		}
		else
		{
			logEntry.getItems().addAll(nosel);
			for(int i = 2; i <= lastEffort.getId(); i++) {
				Log temp = DBUtils.getEffortLogById(i);
				if(temp.getProject().equals(project.getValue())) {
					logEntry.getItems().addAll(temp.getDate() + "   " + temp.getStartTime() + "   " + temp.getEndTime());
				}
			}
			logEntry.getSelectionModel().select(0);
		}
		updating = false;
	}
	
	public void entrySelected(ActionEvent event) throws IOException
	{
		if(updating == false) {
			int index = 0;
			Log lastEffort = DBUtils.getLastEffortLog();
			for(int i = 2; i <= lastEffort.getId(); i++) {
				Log temp = DBUtils.getEffortLogById(i);
				if((temp.getDate() + "   " + temp.getStartTime() + "   " + temp.getEndTime()).equals(logEntry.getValue())) {
					index = i;
				}
			}
			selectedLog = DBUtils.getEffortLogById(index);
			date.setText(selectedLog.getDate());
			startTime.setText(selectedLog.getStartTime());
			stopTime.setText(selectedLog.getEndTime());
			
			readToCB(lifeCycle,"lifecycle");
			readToCB(effort,"lifecycle");
			readToCB(deliverable,"deliverables");
			lifeCycle.getSelectionModel().select(selectedLog.getLifeCycleStep());
			effort.getSelectionModel().select(selectedLog.getEffortCategory());
			deliverable.getSelectionModel().select(selectedLog.getDeliverable());
			
		}

	}

}