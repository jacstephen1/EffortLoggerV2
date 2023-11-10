/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.util.Iterator;
import java.util.ResourceBundle;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DefinitionsController implements Initializable{

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML public TextArea projects;
	@FXML public TextArea lifecycle;
	@FXML public TextArea effort;
	@FXML public TextArea plans;
	@FXML public TextArea deliverables;
	@FXML public TextArea interuptions;
	@FXML public TextArea defect;
	
	public void switchToEL(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("effortLoggerHub.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}

	public void saveDefs(ActionEvent event) throws IOException
	{
		saveTA(projects, "projects");
		saveTA(lifecycle, "lifecycle");
		saveTA(effort, "effort");
		saveTA(plans, "plans");
		saveTA(deliverables, "deliverables");
		saveTA(interuptions, "interuptions");
		saveTA(defect, "defect");
	}
	
	//Save text area to a txt file
	public void saveTA(TextArea ta, String name) throws IOException
	{
		ObservableList<CharSequence> paragraph = ta.getParagraphs();
	    Iterator<CharSequence>  iter = paragraph.iterator();
	        BufferedWriter bf = new BufferedWriter(new FileWriter(new File(name + ".txt")));
	        while(iter.hasNext())
	        {
	            CharSequence seq = iter.next();
	            bf.append(seq);
	            bf.newLine();
	        }
	        bf.flush();
	        bf.close();
	}
	
	//Save txt files and inputs to a text area
	public void readToTA(TextArea ta, String name) throws IOException
	{
		BufferedReader bfr = null;
		  try 
		  {
			  bfr = new BufferedReader(new FileReader(name + ".txt"));
			  ta.setText("");
			  String str;
			  while ((str = bfr.readLine()) != null)
			  {
				  ta.appendText(str+"\n");
			  }
		  } catch (IOException e) {
		  } finally {
		    try { bfr.close(); } catch (Exception ex) { }
		    }
	}
	
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		
		System.getProperty("." + File.separator + "defintions");
		
		try {
			readToTA(projects, "projects");
			readToTA(lifecycle, "lifecycle");
			readToTA(effort, "effort");
			readToTA(plans, "plans");
			readToTA(deliverables, "deliverables");
			readToTA(interuptions, "interuptions");
			readToTA(defect, "defect");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
