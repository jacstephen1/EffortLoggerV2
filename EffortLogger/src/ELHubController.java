/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

import javafx.stage.Stage;

public class ELHubController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	public void switchToMain(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToLoggerConsole(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("loggerConsole.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToLogViewer(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("logViewer.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToDefinitions(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("definitions.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
