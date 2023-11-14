/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;

import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

public class User {
	private int id = -1;
	private String username = null;
	private String password = null;
	private String role = null;
	
	//Create User Info
	public void setInfo(int userId, String name, String pass, String compRole)
	{
		id = userId;
		username = name;
		password = pass;
		role = compRole;
		
		if (id < 0)
		{
			try {
				clearInfo("effort_logs");
				clearInfo("defect_logs");
				clearInfo("pp_projects");
				clearInfo("user_stories");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	//Create User Tables
	public void createTables()
	{
		if (id < 0)
		{
			return;
		}
		else
		{
			DBUtils.createNewUserTables(id);
		}
	}
	
	public void retrieveTables()
	{
		
		if (id < 0)
		{
			return;
		}
		else
		{
			DBUtils.retrieveUserTable("effort_logs", id);
			DBUtils.retrieveUserTable("defect_logs", id);
			DBUtils.retrieveUserTable("pp_projects", id);
			DBUtils.retrieveUserTable("user_stories", id);
		}
	}
	
	//Clear txt temp text files
	public void clearInfo(String name) throws IOException
	{
		BufferedWriter bf = new BufferedWriter(new FileWriter(new File(name + ".txt")));
		bf.write("No User Information Available");
		bf.flush();
		bf.close();
	}
	
	//Access Necessary User Info
	public int getId()
	{
		return id;
	}
	
	public String getUsername()
	{
		return username;
	}
	
	public String getRole()
	{
		return role;
	}
}
