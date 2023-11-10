/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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
	}
	
	//Create User Tables
	public void createTables()
	{
		if (id == -1)
		{
			return;
		}
		else
		{	
			//SQL Database Variable Prep
			Connection connection = null;
			PreparedStatement psCreate = null;
			
			try {
				//Attempt to connect to SQL database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", "ELDatabasePassword1!");
				
				//Make Unique Effort Logs Table
				psCreate = connection.prepareStatement("CREATE TABLE effort_logs_" + id + " ("
						+ "project VARCHAR(255), "
						+ "start_time VARCHAR(255), "
						+ "end_time VARCHAR(255), "
						+ "date VARCHAR(255), "
						+ "lifecyle TEXT, "
						+ "category TEXT, "
						+ "plan TEXT, "
						+ "deliverable TEXT, "
						+ "interruption TEXT, "
						+ "defect TEXT, "
						+ "other TEXT"
						+ ");");
				psCreate.executeUpdate();
				
				
				//Make Unique Defect Logs Table
				psCreate = connection.prepareStatement("CREATE TABLE defect_logs_" + id + " ("
						+ "project VARCHAR(255), "
						+ "name VARCHAR(255), "
						+ "inject TEXT, "
						+ "remove TEXT, "
						+ "symptoms TEXT, "
						+ "category TEXT, "
						+ "fix TEXT, "
						+ "PRIMARY KEY (name)"
						+ ");");
				psCreate.executeUpdate();
				
				
				//Make Unique Planning Poker Project Table
				psCreate = connection.prepareStatement("CREATE TABLE pp_projects_" + id + " ("
						+ "name VARCHAR(255), "
						+ "description TEXT, "
						+ "personal_us_list TEXT, "
						+ "historic_us_list TEXT, "
						+ "PRIMARY KEY (name)"
						+ ");");
				psCreate.executeUpdate();
				
				
				//Make Unique User Stories Table
				psCreate = connection.prepareStatement("CREATE TABLE user_stories_" + id + " ("
						+ "name VARCHAR(255), "
						+ "weight INT, "
						+ "description TEXT, "
						+ "similar_us_list TEXT, "
						+ "PRIMARY KEY (name)"
						+ ");");
				psCreate.executeUpdate();
				
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				//Close SQL connection
				if (connection != null)
				{
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
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
