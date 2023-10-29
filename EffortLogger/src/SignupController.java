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
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class SignupController{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private TextField user;
	@FXML private TextField password;
	@FXML private TextField confirmPassword;
	@FXML private TextField companyCode;
	@FXML private TextField companyRole;
	@FXML private Button signUpButton;
	
	//Signup user when signup is clicked
	public void Signup(ActionEvent event) throws IOException
	{
		//Check that data was entered
		if (!user.getText().trim().isEmpty() 
				|| !password.getText().trim().isEmpty()
				|| !confirmPassword.getText().trim().isEmpty()
				|| !companyCode.getText().trim().isEmpty()
				|| !companyRole.getText().trim().isEmpty())
		{
			//Check input length
			if (user.getText().trim().length() >= 32 
					|| (password.getText().trim().length() >= 32) 
					|| (confirmPassword.getText().trim().length() >= 32)
					|| (companyCode.getText().trim().length() >= 32)
					|| (companyRole.getText().trim().length() >= 32))
			{
				System.out.println("Input too long");
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setContentText("The max input length is 32 characters");
				alert.show();
			}
			else
			{
				//Check company code and check passwords and the same
				if (companyCode.getText().equals("000") && password.getText().trim().equals(confirmPassword.getText().trim()))
				{
					DBUtils.signUpUser(event, user.getText().trim(), password.getText().trim(), companyRole.getText().trim());
				}
				else
				{
					System.out.println("company code is invalid or passwords don't match");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("Provided credentials are invalid");
					alert.show();
				}
			}
		}
		else
		{
			System.out.println("Fields not filled");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("Please fill in all info to signup");
			alert.show();
		}
	}
	
	//Switch back to login page
	public void switchToLogin(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("login.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
}
