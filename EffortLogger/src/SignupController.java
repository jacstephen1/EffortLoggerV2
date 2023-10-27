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

public class SignupController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private TextField user;
	@FXML private TextField password;
	@FXML private TextField companyCode;
	@FXML private Button signUpButton;
	
	public void Signup(ActionEvent event) throws IOException
	{
		if (companyCode.getText().equals("000"))
		{
			root = FXMLLoader.load(getClass().getResource("login.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}
