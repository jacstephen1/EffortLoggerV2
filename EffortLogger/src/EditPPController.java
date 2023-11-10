/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;

public class EditPPController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private Button editUSButton;
	@FXML private Button editHUSButton;
	@FXML private ListView<String> listHUS;
	@FXML private ListView<UserStory> listUS;

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

		List<UserStory> stories = DBUtils.GetUserStoriesFromDB();
		listUS.getItems().addAll(stories);


	}
	
	public void switchToPPMain(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("planningPoker.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void switchToUS(ActionEvent event) throws IOException
	{
		if ((event.getSource().equals(editUSButton) && listUS.getSelectionModel().getSelectedItem() != null) 
				|| (event.getSource().equals(editHUSButton) && listHUS.getSelectionModel().getSelectedItem() != null)
				|| (!event.getSource().equals(editUSButton)))
		{
			root = FXMLLoader.load(getClass().getResource("editUserStory.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void testSemaphores(ActionEvent event)
	{
		System.out.println("Test");
	}
}
