/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.text.Text;
import javafx.scene.control.TextField;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class DefectController {

	private Stage stage;
	private Scene scene;
	private Parent root;
	
	@FXML public Text noDefect;
	@FXML public TextField fix;
	@FXML public TextField defectName;
	@FXML public TextField cause;
	@FXML public ComboBox<String> project;
	@FXML public ComboBox<String> defects;
	@FXML public ListView<String> injection;
	@FXML public ListView<String> removal;
	@FXML public ListView<String> category;
	
	public void switchToELConsole(ActionEvent event) throws IOException
	{
		root = FXMLLoader.load(getClass().getResource("loggerConsole.fxml"));
		stage = (Stage)((Node)event.getSource()).getScene().getWindow();
		scene = new Scene(root);
		stage.setScene(scene);
		stage.show();
	}
	
	public void popDefectBox(ActionEvent event) throws IOException
	{
		//replace false with a while the database table still has elements 
		while (false)
		 {
			//set the string with the defectName
			String str;
			//and this will add it to the combobox 
			defects.getItems().addAll(str);
		 }
	}
	
	public void popDefectRest(ActionEvent event) throws IOException
	{
		int index;
		int i = 0;
		//change the nulls out for strings you get from the table with the right defectName
		fix.setText(null);
		defectName.setText(null);
		cause.setText(null);
		//get the string to equal the injection life cycle step
		String strInject;
		//change null to table isnt empty
		BufferedReader bfr = null;
		try 
		{
			bfr = new BufferedReader(new FileReader("lifecycle.txt"));
			String str;
			while ((str = bfr.readLine()) != null)
			{
				if (str == strInject) {
					index = i;
				}
				i++;
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
		injection.scrollTo(index);
        injection.getSelectionModel().select(index);
        index = 0;
        i = 0;
        try 
		{
			bfr = new BufferedReader(new FileReader("lifecycle.txt"));
			String str;
			while ((str = bfr.readLine()) != null)
			{
				if (str == strInject) {
					index = i;
				}
				i++;
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
        removal.scrollTo(index);
        removal.getSelectionModel().select(index);
        index = 0;
        i = 0;
        try 
		{
			bfr = new BufferedReader(new FileReader("defect.txt"));
			String str;
			while ((str = bfr.readLine()) != null)
			{
				if (str == strInject) {
					index = i;
				}
				i++;
			}
		} catch (IOException e) {
		} finally {
			try { bfr.close(); } catch (Exception ex) { }
		}
        category.scrollTo(index);
        category.getSelectionModel().select(index);
	}
	
	//Save txt files and inputs to a text area
			public void readToLV(ListView<String> lv, String name) throws IOException
			{
				BufferedReader bfr = null;
				  try 
				  {
					  bfr = new BufferedReader(new FileReader(name + ".txt"));
					  lv.getItems().clear();
					  String str;
					  while ((str = bfr.readLine()) != null)
					  {
						  lv.getItems().addAll(str);
					  }
				  } catch (IOException e) {
				  } finally {
				    try { bfr.close(); } catch (Exception ex) { }
				    }
			}
	
	//Save txt files and inputs to a text area
		public void readToCB(ComboBox<String> cb, String name) throws IOException
		{
			BufferedReader bfr = null;
			  try 
			  {
				  bfr = new BufferedReader(new FileReader(name + ".txt"));
				  cb.getItems().clear();
				  String str;
				  while ((str = bfr.readLine()) != null)
				  {
					  cb.getItems().addAll(str);
				  }
			  } catch (IOException e) {
			  } finally {
			    try { bfr.close(); } catch (Exception ex) { }
			    }
		}
		
		public void initialize() {
			try {
				readToCB(project, "projects");
				noDefect.setVisible(false);
				readToLV(injection,"lifecycle");
				readToLV(removal,"lifecycle");
				readToLV(category,"defect");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	
}
