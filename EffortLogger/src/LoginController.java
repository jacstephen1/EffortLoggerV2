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

public class LoginController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private TextField user;
	@FXML private TextField password;
	@FXML private Button loginButton;
	
	public void Login(ActionEvent event) throws IOException
	{
		if ((user.getText().equals("user")) && (password.getText().equals("pass")))
		{
			root = FXMLLoader.load(getClass().getResource("main.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}
}
