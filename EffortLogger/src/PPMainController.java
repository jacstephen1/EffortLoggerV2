/*
 * Author: Charlie Baird
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

public class PPMainController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML private Button editPPButton;
	@FXML private ListView<UserStory> UserStories;
	
	//PlanningPoker Label Testing
	@FXML private ListView<LegacyProject> projectList;
	@FXML private TextArea projectInfo;
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<LegacyProject> legacyProjects = DBUtils.GetLegacyProjectsFromDB(Main.user.getId());
		projectList.getItems().addAll(legacyProjects);

		UserStories.setMouseTransparent( true );
		UserStories.setFocusTraversable( false );

//		List<UserStory> stories = DBUtils.GetUserStoriesFromDB();
//		UserStories.getItems().addAll(stories);
	}
	
	public void switchToMain(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("main.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public static String editId = null;

	public void Edit(ActionEvent event) throws IOException
	{
		if (projectList.getSelectionModel().getSelectedItem() != null)
		{
			editId = projectList.getSelectionModel().getSelectedItem().id;

			root = FXMLLoader.load(getClass().getResource("editPlanningPoker.fxml"));
			stage = (Stage)((Node)event.getSource()).getScene().getWindow();
			scene = new Scene(root);
			stage.setScene(scene);
			stage.show();
		}
	}

	public void New(ActionEvent event) throws IOException
	{
		editId = null;

		root = FXMLLoader.load(getClass().getResource("editPlanningPoker.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void projectListViewUpdate(MouseEvent event) throws IOException
	{
		LegacyProject selected = projectList.getSelectionModel().getSelectedItem();
		if (selected != null)
		{
			projectInfo.setText(selected.description);
			UserStories.getItems().clear();

			UserStory story = DBUtils.GetUserStoryById(Main.user.getId(), id);
			UserStories.getItems().add(story);
			String ids = selected.user_story_id;
			String[] split = ids.split(" ");

			ArrayList<UserStory> stories = new ArrayList<>();

			for (String id : split)
			{
				UserStory story = DBUtils.GetUserStoryById(id);
				stories.add(story);
			}

			stories.sort(new UserStoryComparable());

			UserStories.getItems().addAll(stories);
		}
	}

//	private final FileLock lock = new FileLock();

	public void Save(ActionEvent event)
	{
//		Task task = new Task<Void>() {
//			@Override public Void call() {
//
//				System.out.println("Save clicked");
//
//				// Claim lock on file
//				lock.WaitToClaim(200);
//
//				// Write to the file
//				WriteToFile("planningpoker.txt");
//
//				// Release lock on file
//				lock.Unlock();
//
//				return null;
//			}
//		};
//
//		new Thread(task).start();
	}
//
//	private void WriteToFile(String path)
//	{
//		System.out.println("Saving to file... ");
//
//		// Arbitrary cooldown
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			throw new RuntimeException(e);
//		}
//
//		BufferedWriter writer = null;
//		try {
//			String content = "Content";
//
//			File file = new File(path);
//
//			// Init file
//			if (file.exists())
//			{
//				file.delete();
//			}
//
//			file.createNewFile();
//
//			FileWriter fileWriter = new FileWriter(file);
//			writer = new BufferedWriter(fileWriter);
//			writer.write(content);
//			writer.close();
//			fileWriter.close();
//
//		} catch (IOException ioe) {
//			ioe.printStackTrace();
//		}
//
//		System.out.println("Done " + (new Date().toString()));
//	}
}

//class FileLock
//{
//
//	// Main boolean to indicate file is being used
//	private boolean locked = false;
//
//	// Unlocks the file, file is no longer being used and is open to being locked
//	public void Unlock()
//	{
//		locked = false;
//	}
//
//	// Attempt to claim the lock, if unlocked, succeed and return
//	// Otherwise, sleep until the lock is available
//	public void WaitToClaim(int cooldown)
//	{
//		if (!locked)
//		{
//			locked = true;
//			return;
//		}
//
//		while (true)
//		{
//			try {
//				Thread.sleep(cooldown);
//			} catch (InterruptedException e) {
//				throw new RuntimeException(e);
//			}
//
//			if (!locked)
//			{
//				locked = true;
//				return;
//			}
//		}
//
//	}
//}