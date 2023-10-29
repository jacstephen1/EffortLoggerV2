/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LoginController{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private TextField user;
	@FXML private TextField password;
	@FXML private Button loginButton;
	@FXML private Button signUpButton;
	
	//Attempt to login user with entered data
	public void Login(ActionEvent event) throws IOException
	{
		DBUtils.loginUser(event, user.getText(), password.getText());
	}
	
	//Switch to signup page
	public void Signup(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("signup.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
