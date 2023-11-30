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
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class DefectController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML public Text noDefect;
	@FXML public TextField fix;
	@FXML public TextField defectName;
	@FXML public TextField cause;
	@FXML public ComboBox<String> project;
	@FXML public ComboBox<String> defects;
	@FXML public ListView<String> injection;
	@FXML public ListView<String> removal;
	@FXML public ListView<String> category;
	@FXML public Button openButton;
	@FXML public Button closeButton;
	
	final String nosel = "No Selection";
	private int openClose = 0;
	private int index = 0;
	private boolean updating = false;
	private Log selectedLog = null;
	
	
	public void switchToELConsole(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("loggerConsole.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void openButton(ActionEvent event) throws IOException
	{
		openClose = 1;
		openButton.setDisable(true);
		closeButton.setDisable(false);
	}
	
	public void closeButton(ActionEvent event) throws IOException
	{
		openClose = 2;
		openButton.setDisable(false);
		closeButton.setDisable(true);
	}
	
	
	public void clearButton(ActionEvent event) throws IOException
	{
		updating = true;
		project.getSelectionModel().select(0);
		defects.getSelectionModel().select(0);
		injection.getSelectionModel().select(0);
		removal.getSelectionModel().select(0);
		category.getSelectionModel().select(0);
		fix.clear();
		defectName.clear();
		cause.clear();
		openClose = 0;
		openButton.setDisable(false);
		closeButton.setDisable(false);
		selectedLog = null;
		updating = false;
	}
	
	public void updateCurrentDefect(ActionEvent event) throws IOException
	{
		Log createdDefectLog = new Log("Defect", project.getValue());
		createdDefectLog.createDefectLog(index, category.getSelectionModel().getSelectedItem(), defectName.getText(), cause.getText(), 
				injection.getSelectionModel().getSelectedItem(), removal.getSelectionModel().getSelectedItem(), fix.getText(), openClose);
		DBUtils.updateDefectLog(createdDefectLog);
	}
	public void deleteCurrentDefect(ActionEvent event) throws IOException
	{
		if(selectedLog == null){
			
		}
		else
		{
			Log tempHigher = DBUtils.getLastDefectLog();
			DBUtils.deleteLastDefectLog();
			
			for(int i = ((tempHigher.getId()) - 1); i >= selectedLog.getId(); i--) 
			{
				Log tempLower = DBUtils.getDefectLogById(i);
				tempHigher.setId(i);
				DBUtils.updateDefectLog(tempHigher);
				tempHigher = tempLower;
			}
			selectedLog = null;
		}
		
	}
	
	public void createButton(ActionEvent event) throws IOException
	{
		if(injection.getSelectionModel().getSelectedItem() == null || nosel.equals(injection.getSelectionModel().getSelectedItem())
				|| removal.getSelectionModel().getSelectedItem() == null || nosel.equals(removal.getSelectionModel().getSelectedItem())
				|| category.getSelectionModel().getSelectedItem() == null || nosel.equals(category.getSelectionModel().getSelectedItem())) 
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("one or more of the selected itmes is blank or on the default setting. please choose the popper item.");
			alert.show();
		}
		else 
		{
			if(!nosel.equals(defects.getValue().trim())) 
			{
				Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
				alert.setTitle("Message");
				alert.setHeaderText("You have a defect selected to work on. Hitting the create defect button will make a copy. "
						+ "Press ok to continue and make the copy.");
				Optional<ButtonType> result = alert.showAndWait();
				if (result.get().equals(ButtonType.OK)) 
				{
					doingChecksAndMakeDefect();
					alert.close();
				}
				else if (result.get().equals(ButtonType.NO)) 
				{
				       alert.close();
				}
			}
			else 
			{
				doingChecksAndMakeDefect();
			}
		}
	}
	
	public void popProjectBox(ActionEvent event) throws IOException
	{
		noDefect.setVisible(false);
		updating = true;
		defects.getItems().clear();
		Log lastDefect = DBUtils.getLastDefectLog();
		if(lastDefect.getId() == 1)
		{
			defects.getItems().addAll(nosel);
			defects.getSelectionModel().select(0);
			noDefect.setVisible(true);
		}
		else
		{
			defects.getItems().addAll(nosel);
			for(int i = 2; i <= lastDefect.getId(); i++) {
				Log temp = DBUtils.getDefectLogById(i);
				if(temp.getProject().equals(project.getValue())) {
					defects.getItems().addAll(temp.getDefectName());
				}
			}
			defects.getSelectionModel().select(0);
			if(defects.getItems().size() == 1) {
				noDefect.setVisible(true);
			}
			updating = false;
		}
	}
	
	public void popDefectRest(ActionEvent event) throws IOException
	{
		if(updating == false) {
			noDefect.setVisible(false);
			Log lastDefect = DBUtils.getLastDefectLog();
			for(int i = 2; i <= lastDefect.getId(); i++) {
				Log temp = DBUtils.getDefectLogById(i);
				if(temp.getDefectName().equals(defects.getValue())) {
					index = i;
				}
			}
			selectedLog = DBUtils.getDefectLogById(index);
			fix.setText(selectedLog.getDefectFix());
			defectName.setText(selectedLog.getDefectName());
			cause.setText(selectedLog.getDefectSymptom());
			
			injection.getSelectionModel().select(selectedLog.getDefectInjection());
			removal.getSelectionModel().select(selectedLog.getDefectRemoval());
			category.getSelectionModel().select(selectedLog.getDefectCategory());
			
			injection.scrollTo(selectedLog.getDefectInjection());
			removal.scrollTo(selectedLog.getDefectRemoval());
			category.scrollTo(selectedLog.getDefectCategory());
			
			if(selectedLog.getOpenClose() == 1) {
				openButton(event);
			}
			else
			{
				closeButton(event);
			}
		}
	}
	
	public void initialize() {
		updating = true;
		try {
			readToCB(project, "projects");
			defects.getItems().addAll(nosel);
			noDefect.setVisible(false);
			readToLV(injection,"lifecycle");
			readToLV(removal,"lifecycle");
			readToLV(category,"defect");
			
			project.getSelectionModel().select(0);
			defects.getSelectionModel().select(0);
			injection.getSelectionModel().select(0);
			removal.getSelectionModel().select(0);
			category.getSelectionModel().select(0);
			
			openButton.setDisable(false);
			closeButton.setDisable(false);
		} catch (IOException e) {
			e.printStackTrace();
		}
		updating = false;
	}
	
	//Save txt files and inputs to a text area
			public void readToLV(ListView<String> lv, String name) throws IOException
			{
				BufferedReader bfr = null;
				try 
				{
					bfr = new BufferedReader(new FileReader(name + ".txt"));
					lv.getItems().clear();
					String str = nosel;
					lv.getItems().addAll(str);
					while ((str = bfr.readLine()) != null)
					{
						lv.getItems().addAll(str);
					}
				}
				catch (IOException e)
				{
				} 
				finally
				{
					try
					{
						bfr.close();
					} 
					catch (Exception ex)
					{	
					}
				}
			}
	
	//Save txt files and inputs to a text area
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
		}
		catch (IOException e) 
		{
		}
		finally
		{
			try
			{
				bfr.close();
			}
			catch (Exception ex)
			{
			}
		}
	}
		
	public void doingChecksAndMakeDefect() 
	{
		if(defectName.getText() == null || defectName.getText().trim().isEmpty() 
				|| cause.getText() == null || cause.getText().trim().isEmpty() 
				|| fix.getText() == null || fix.getText().trim().isEmpty()) 
		{
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("One or more of the text field were blank. Please enter the neccessary information");
			alert.show();
		}
		else
		{
			if (defectName.getText().contains("?") || defectName.getText().contains("'")
					|| defectName.getText().contains("=") || defectName.getText().contains("%"))
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Invalid characters in Defect Name field. You cannot use the following characters: ? ' = %");
				alert.show();
			}
			else if (cause.getText().contains("?") || cause.getText().contains("'") || cause.getText().contains("=") || cause.getText().contains("%"))
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Invalid characters in Cause field. You cannot use the following characters: ? ' = %");
				alert.show();
			}
			else if (fix.getText().contains("?") || fix.getText().contains("'") || fix.getText().contains("=") || fix.getText().contains("%"))
			{
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("Invalid characters in Fix field. You cannot use the following characters: ? ' = %");
				alert.show();
			}
			else
			{
				if(project.getValue() == null || nosel.equals(project.getValue()))
				{
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("No Project Selected. Please Enter a project for this defect");
					alert.show();
				}
				else 
				{
					if(openClose == 0) 
					{
						Alert alert = new Alert(Alert.AlertType.ERROR);
						alert.setContentText("Please Select if the Project is open or closed.");
						alert.show();
					}
					else
					{
						boolean dupe = false;
						Log lastDefect = DBUtils.getLastDefectLog();
						for(int i = 2; i <= lastDefect.getId(); i++) {
							Log temp = DBUtils.getDefectLogById(i);
							if(temp.getDefectName().equals(defectName.getText())) {
								dupe = true;
							}
						}
						if(dupe == true) {
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setContentText("This defect name is already in use. Please choose a new name");
							alert.show();
						}
						else
							{
							selectedLog = new Log("Defect", project.getValue());
							selectedLog.createDefectLog(DBUtils.getLastDefectLog().getId(), category.getSelectionModel().getSelectedItem(), defectName.getText(), 
									cause.getText(), injection.getSelectionModel().getSelectedItem(), removal.getSelectionModel().getSelectedItem(), 
									fix.getText(), openClose);
							DBUtils.createDBDefectEntry(selectedLog);
							}
						
					}
				}
			}
		}
	}
	
	
}
