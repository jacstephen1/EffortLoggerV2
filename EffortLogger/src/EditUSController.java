import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUSController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML
	private TextField a;
	@FXML
	private TextArea b;
	@FXML
	private TextField c;
	
	public void switchToEditPP(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("editPlanningPoker.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

    public void Save(ActionEvent actionEvent) {
		String name = a.getText();
		String weight = c.getText();
		String description = b.getText();
		String similar = null;

		boolean created = DBUtils.createUserStory(name,weight,description,null);
    }
}
