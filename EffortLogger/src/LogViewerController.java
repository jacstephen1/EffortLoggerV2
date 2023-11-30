/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LogViewerController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	//Consistent with definitions class
	@FXML private ChoiceBox<String> project;
	@FXML private ChoiceBox<String> effort;
	@FXML private ChoiceBox<String> defect;
	@FXML private TextArea deliverable;
	@FXML private TextArea fix;
	@FXML private TextField date;
	@FXML private TextField startTime;
	@FXML private TextField endTime;
	@FXML private TextField timeElapsed;
	@FXML private TextField lifeCycleStep;
	@FXML private TextField effortCategory;
	@FXML private TextField defectName;
	@FXML private TextField injection;
	@FXML private TextField removal;
	@FXML private TextField defectCategory;
	@FXML private TextField openClose;
	
	private boolean updating = false;
	String nosel = "No Selection";
	
	public void switchToEL(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("effortLoggerHub.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void projectSelected(ActionEvent event) throws IOException
	{
		updating = true;
		effort.getItems().clear();
		defect.getItems().clear();
		Log lastEffort = DBUtils.getLastEffortLog();
		if(lastEffort.getId() == 1)
		{
			
			effort.getItems().addAll(nosel);
			effort.getSelectionModel().select(0);
			effort.setVisible(true);
		}
		else
		{
			effort.getItems().addAll(nosel);
			for(int i = 2; i <= lastEffort.getId(); i++) {
				Log temp = DBUtils.getEffortLogById(i);
				if(temp.getProject().equals(project.getValue())) {
					effort.getItems().addAll(temp.getDate()+temp.getStartTime()+temp.getEndTime());
				}
			}
			effort.getSelectionModel().select(0);
		}
		
		Log lastDefect = DBUtils.getLastDefectLog();
		if(lastDefect.getId() == 1)
		{
			defect.getItems().addAll(nosel);
			defect.getSelectionModel().select(0);
		}
		else
		{
			defect.getItems().addAll(nosel);
			for(int i = 2; i <= lastDefect.getId(); i++) {
				Log temp = DBUtils.getDefectLogById(i);
				if(temp.getProject().equals(project.getValue())) {
					defect.getItems().addAll(temp.getDefectName());
				}
			}
			defect.getSelectionModel().select(0);
			updating = false;
		}
	}
	
	public void effortSelected(ActionEvent event) throws IOException
	{
		
		if(updating == false) {
			date.clear();
			startTime.clear();
			endTime.clear();
			timeElapsed.clear();
			lifeCycleStep.clear();
			effortCategory.clear();
			deliverable.clear();
			int index = 0;
			Log lastEffort = DBUtils.getLastEffortLog();
			for(int i = 2; i <= lastEffort.getId(); i++) {
				Log temp = DBUtils.getEffortLogById(i);
				if((temp.getDate()+temp.getStartTime()+temp.getEndTime()).equals(effort.getValue())) {
					index = i;
				}
			}
			Log selectedLog = DBUtils.getEffortLogById(index);
			
			date.setText(selectedLog.getDate());
			startTime.setText(selectedLog.getStartTime());
			endTime.setText(selectedLog.getEndTime());
			timeElapsed.setText("this4");
			lifeCycleStep.setText(selectedLog.getLifeCycleStep());
			effortCategory.setText(selectedLog.getEffortCategory());
			deliverable.setText(selectedLog.getDeliverable());
			
		}
	}
	
	public void defectSelected(ActionEvent event) throws IOException
	{
		if(updating == false) 
		{
			defectName.clear();
			injection.clear();
			removal.clear();
			defectCategory.clear();
			openClose.clear();
			effortCategory.clear();
			deliverable.clear();
			int index = 0;
			Log lastDefect = DBUtils.getLastDefectLog();
			for(int i = 2; i <= lastDefect.getId(); i++) {
				Log temp = DBUtils.getDefectLogById(i);
				if(temp.getDefectName().equals(defect.getValue())) {
					index = i;
				}
			}
			Log selectedLog = DBUtils.getDefectLogById(index);
				
				
				
			defectName.setText(selectedLog.getDefectName());
			injection.setText(selectedLog.getDefectInjection());
			removal.setText(selectedLog.getDefectRemoval());
			defectCategory.setText(selectedLog.getDefectCategory());
			if(selectedLog.getOpenClose() == 1) 
			{
				openClose.setText("Open");
			}
			else if(selectedLog.getOpenClose() == 2)
			{
				openClose.setText("Closed");
			}
			else
			{
				openClose.setText(nosel);
			}
			fix.setText(selectedLog.getDefectFix());
		}
	}

	//Reads fro txt file to choicebox
	public void readToCB(ChoiceBox<String> cb, String fileName) throws IOException
	{
		BufferedReader bfr = null;
		  try 
		  {
			  bfr = new BufferedReader(new FileReader(fileName + ".txt"));
			  String str = nosel;
			  cb.getItems().add(str);
			  while ((str = bfr.readLine()) != null)
			  {
				  cb.getItems().add(str);
			  }
		  } catch (IOException e) {
		  } finally {
		    try { bfr.close(); } catch (Exception ex) { }
		    }
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		updating = true;
		try {
			readToCB(project, "projects");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
		updating = false;
	}
}
