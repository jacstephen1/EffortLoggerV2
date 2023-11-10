/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application{
	
	public static User user = new User();
	
	@Override
	public void start(Stage stage)
	{
		try 
		{
			Parent root = FXMLLoader.load(getClass().getResource("login.fxml"));
			Scene login = new Scene(root);
			stage.setScene(login);
			stage.show();
			stage.setTitle("EffortLogger V2");
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@Override
    public void stop() 
	{
		//Log out user at program close
		user.setInfo(-1, null, null, null);
	}
	
	public static void main(String[] args)
	{
		launch(args);
	}

}
