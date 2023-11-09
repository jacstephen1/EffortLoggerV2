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
import javafx.stage.Stage;

public class LogViewerController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	//Consistent with definitions class
	@FXML private ChoiceBox<String> projects;
	@FXML private ChoiceBox<String> effortLogList;
	@FXML private ChoiceBox<String> defectLogList;
	
	public void switchToEL(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("effortLoggerHub.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	//Reads fro txt file to choicebox
	public void readToCB(ChoiceBox<String> cb, String fileName) throws IOException
	{
		BufferedReader bfr = null;
		  try 
		  {
			  bfr = new BufferedReader(new FileReader(fileName + ".txt"));
			  String str;
			  while ((str = bfr.readLine()) != null)
			  {
				  System.out.println(str);
				  cb.getItems().add(str);
			  }
		  } catch (IOException e) {
		  } finally {
		    try { bfr.close(); } catch (Exception ex) { }
		    }
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			readToCB(projects, "projects");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			System.out.println("error");
			e.printStackTrace();
		}
	}
}
