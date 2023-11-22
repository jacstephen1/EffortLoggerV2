import java.io.IOException;
import java.util.List;
import java.util.Objects;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class EditUSController {

	private Stage stage;
	private Scene scene;
	private Parent root;

	@FXML private ListView<UserStory> listUS;
	@FXML
	private TextField a;
	@FXML
	private TextArea b;
	@FXML
	private TextField c;
	private String id = null;

	@FXML
	public void initialize() {

		List<UserStory> stories = DBUtils.GetUserStoriesFromDB(Main.user.getId());
		stories.removeIf(story -> Objects.equals(((UserStory) story).id, EditPPController.editId));
		listUS.getItems().addAll(stories);
		listUS.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);

		if (EditPPController.editId != null)
		{
			UserStory story = DBUtils.GetUserStoryById(Main.user.getId(), EditPPController.editId);
			a.setText(story.name);
			b.setText(story.description);
			c.setText(story.weight);
			id = story.id;

			if (story.similar != null)
			{
				String[] similarIds = story.similar.split(",");

				for (int i=0; i<listUS.getItems().size(); i++)
				{
					UserStory istory = listUS.getItems().get(i);
					for (String id : similarIds)
					{
						if (id.equals(istory.id))
						{
							listUS.getSelectionModel().select(i);
						}
					}
				}
			}

		}

	}
	
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
		ObservableList<UserStory> list = listUS.getSelectionModel().getSelectedItems();
		String similar = list.isEmpty() ? null : "";
        for (UserStory userStory : list) {
            similar += userStory.id + ",";
        }
		if (similar != null && !similar.isEmpty())
		{
			similar = similar.substring(0, similar.length()-1);
		}


		// will update or create
		boolean created = DBUtils.createUserStory(Main.user.getId(), name,weight,description,similar, id);
    }
}