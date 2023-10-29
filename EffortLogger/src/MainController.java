/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

public class MainController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML Label welcomeLabel;
	@FXML Button logoutButton;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		// TODO Auto-generated method stub
		// HANDLE USER FILE ACCESS AND LOADING
		welcomeLabel.setText("Welcome " + Main.user.getUsername() + "!");
	}
	
	public void setUser(String username)
	{
		welcomeLabel.setText("Welcome " + username + "!");
	}
	
	public void logout(ActionEvent event) throws IOException
	{
		Main.user.setInfo(-1, null, null, null);
		
		root = FXMLLoader.load(getClass().getResource("login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToEL(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("effortLoggerHub.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToPP(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("planningPoker.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToMain(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
