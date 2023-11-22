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
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DBUtils {
	
	public final static String DB_PASSWORD = "ELDatabasePassword1!";
	
	//Handles scene changes for login and signup to incorporate username and events
	public static void changeScene(ActionEvent event, String fxmlFile, String username)
	{
		Parent root = null;
		if (username != null)
		{
			try {
				//Load new fxml
				FXMLLoader loader = new FXMLLoader(DBUtils.class.getResource(fxmlFile)); 
				root = loader.load();
				MainController loggedIn = loader.getController();
				loggedIn.setUser(username);
			} catch (IOException e) {
				e.printStackTrace();
			}
		} 
		else 
		{
			try {
				root = FXMLLoader.load(DBUtils.class.getResource(fxmlFile));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		stage.setScene(new Scene(root));
		stage.show();
	}
	
	//CREATE NEW USER
	public static void signUpUser(ActionEvent event, String username, String password, String user_role)
	{
		if (username.contains("?") || username.contains("'") || username.contains("=") || username.contains("%")
				|| password.contains("?") || password.contains("'") || password.contains("=") || password.contains("%"))
		{
			System.out.println("Invalid characters in username or password field");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("You cannot use the following characters: ? ' = %");
			alert.show();
		}
		else
		{
			//SQL Database Prep
			Connection connection = null;
			PreparedStatement psInsert = null;
			PreparedStatement psCheckID = null;
			PreparedStatement psCheckUserExists = null;
			ResultSet resultSet = null;
			
			//Encrypt data for storage
			password = AES.encrypt(password);
			
			try {
				//Attempt to connect to SQL database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
				psCheckUserExists = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
				psCheckUserExists.setString(1, username);
				resultSet = psCheckUserExists.executeQuery();
				
				if (resultSet.isBeforeFirst()) //Check username taken
				{
					System.out.println("User already exists");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("You cannot use this username");
					alert.show();
				}
				else
				{
					//If username not taken make new user
					psInsert = connection.prepareStatement("INSERT INTO users (username, password, user_role) VALUES (?, ?, ?)");
					psInsert.setString(1, username);
					psInsert.setString(2, password);
					psInsert.setString(3, user_role);
					psInsert.executeUpdate();
					
					//Find user in SQL query for retrieving user data
					psCheckID = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
					psCheckID.setString(1, username);
					ResultSet resultID = psCheckID.executeQuery();
					resultID.next();
					
					Main.user.setInfo(resultID.getInt("user_id"), username, password,resultID.getString("user_role"));
					Main.user.createTables();
					Main.user.retrieveTables();
									
					changeScene(event, "main.fxml", username);
				} 
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				//Close SQL connection
				if (resultSet != null)
				{
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (psCheckUserExists != null)
				{
					try {
						psCheckUserExists.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (psInsert != null)
				{
					try {
						psInsert.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
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
	
	//Login existing user
	public static void loginUser(ActionEvent event, String username, String password)
	{
		if (username.contains("?") || username.contains("'") || username.contains("=") || username.contains("%")
				|| password.contains("?") || password.contains("'") || password.contains("=") || password.contains("%"))
		{
			System.out.println("Invalid characters in username or password field");
			Alert alert = new Alert(Alert.AlertType.ERROR);
			alert.setContentText("You cannot use the following characters: ? ' = %");
			alert.show();
		}
		else
		{
			//SQL Database prep
			Connection connection = null;
			PreparedStatement preparedStatement = null;
			PreparedStatement psCheckID = null;
			ResultSet resultSet = null;
			
			//Encrypt data to check already encrypted data
			password = AES.encrypt(password);
					
			try {
				//Connect to SQL Database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
				preparedStatement = connection.prepareStatement("SELECT password FROM users WHERE username = ?");
				preparedStatement.setString(1,  username);
				resultSet = preparedStatement.executeQuery();
				
				if (!resultSet.isBeforeFirst()) //Check if user is not in the database
				{
					System.out.println("User does not exist");
					Alert alert = new Alert(Alert.AlertType.ERROR);
					alert.setContentText("Provided credentials are invalid");
					alert.show();
				}
				else
				{
					while (resultSet.next())
					{
						String retrievedPassword = resultSet.getString("password");
						if (retrievedPassword.equals(password)) //CHECK IS PASSWORD IS CORRECT
						{
							//Find user in SQL query for retrieving user data
							psCheckID = connection.prepareStatement("SELECT * FROM users WHERE username = ?");
							psCheckID.setString(1, username);
							ResultSet resultID = psCheckID.executeQuery();
							resultID.next();
							
							Main.user.setInfo(resultID.getInt("user_id"), username, password,resultID.getString("user_role"));
							Main.user.retrieveTables();
							
							changeScene(event, "main.fxml", username);
						}
						else
						{
							System.out.println("Password did not match");
							Alert alert = new Alert(Alert.AlertType.ERROR);
							alert.setContentText("Provided credentials are invalid");
							alert.show();
						}
					}
				}
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				//Close SQL Connection
				if (resultSet != null)
				{
					try {
						resultSet.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (connection != null)
				{
					try {
						connection.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (preparedStatement != null)
				{
					try {
						preparedStatement.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				
				if (psCheckID != null)
				{
					try {
						psCheckID.close();
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	//Retrieve user tables from SQL and upload to temporary text files for data retrieval
	public static void retrieveUserTable(String table, int id)
	{
		//Connect to SQL Database
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet resultSet = null;
		//Find results for user id in specified table
		//Attempt to connect to SQL database
		try {
			//Attempt to connect to SQL database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
			//Find table
			ps = connection.prepareStatement("SELECT * FROM " + table + "_" + id);
			resultSet = ps.executeQuery();
			
			//Open File Writer
		    BufferedWriter bf;
			try {
				bf = new BufferedWriter(new FileWriter(new File(table + ".txt")));
				
				//Create text file with information for pulling
				while (resultSet.next())
				{
					switch (table)
					{
					case "effort_logs":
						bf.append(resultSet.getInt("id") + " | " 
								+ resultSet.getString("project") + " | " 
								+ resultSet.getString("start_time") + " | " 
								+ resultSet.getString("end_time") + " | " 
								+ resultSet.getString("date") + " | "
								+ resultSet.getString("lifecycle") + " | "
								+ resultSet.getString("category") + " | "
								+ resultSet.getString("plan") + " | "
								+ resultSet.getString("deliverable") + " | "
								+ resultSet.getString("interruption") + " | "
								+ resultSet.getString("defect") + " | "
								+ resultSet.getString("other"));
						bf.newLine();
						break;
					case "defect_logs":
						bf.append(resultSet.getString("project") + " | " 
								+ resultSet.getString("name") + " | " 
								+ resultSet.getString("inject") + " | " 
								+ resultSet.getString("remove") + " | "
								+ resultSet.getString("symptoms") + " | "
								+ resultSet.getString("category") + " | "
								+ resultSet.getString("fix"));
						bf.newLine();
						break;
					case "pp_projects":
						bf.append(resultSet.getString("id") + " | " 
								+ resultSet.getString("name") + " | " 
								+ resultSet.getString("description") + " | " 
								+ resultSet.getString("user_story_id"));
						bf.newLine();
						break;
					case "user_stories":
						bf.append(resultSet.getString("id") + " | " 
								+ resultSet.getString("name") + " | " 
								+ resultSet.getInt("weight") + " | " 
								+ resultSet.getString("description")  + " | " 
								+ resultSet.getString("similar_stories"));
						bf.newLine();
						break;
					}
				}
				bf.flush();
				bf.close();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
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
			
			if (ps != null)
			{
				try {
					ps.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			
			if (resultSet != null)
			{
				try {
					resultSet.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	public static void createNewUserTables(int id)
	{
		//SQL Database Variable Prep
		Connection connection = null;
		PreparedStatement psCreate = null;
		
		try {
			//Attempt to connect to SQL database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
			
			//Make Unique Effort Logs Table
			psCreate = connection.prepareStatement("CREATE TABLE effort_logs_" + id + " ("
					+ "id INT AUTO_INCREMENT, "
					+ "project VARCHAR(255), "
					+ "start_time VARCHAR(255), "
					+ "end_time VARCHAR(255), "
					+ "date VARCHAR(255), "
					+ "lifecycle TEXT, "
					+ "category TEXT, "
					+ "plan TEXT, "
					+ "deliverable TEXT, "
					+ "interruption TEXT, "
					+ "defect TEXT, "
					+ "other TEXT, "
					+ "PRIMARY KEY (id)"
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
					+ "id VARCHAR(255), "
					+ "name VARCHAR(255), "
					+ "description TEXT, "
					+ "user_story_id TEXT, "
					+ "PRIMARY KEY (id)"
					+ ");");
			psCreate.executeUpdate();
			
			
			//Make Unique User Stories Table
			psCreate = connection.prepareStatement("CREATE TABLE user_stories_" + id + " ("
					+ "id VARCHAR(255), "
					+ "name VARCHAR(255), "
					+ "weight INT, "
					+ "description TEXT, "
					+ "similar_stories TEXT, "
					+ "PRIMARY KEY (id)"
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
	
	// Create or update user story in the DB, based off whether sid is NULL
	public static boolean createUserStory(int id, String name, String weight, String description, String similar, String sid)
	{

		if (name == null || name == "" || weight == null || weight == "" || description == null || description == "")
		{
			return false;
		}

		//SQL Database Prep
		Connection connection = null;
		PreparedStatement psInsert = null;

		// If given id is null, create
		if (sid == null)
		{
			sid = String.valueOf(System.currentTimeMillis());

			try {
				//Connect to SQL Database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
				psInsert = connection.prepareStatement("INSERT INTO user_stories_" + id + " (id, name, weight, description, similar_stories) VALUES (?, ?, ?, ?, ?)");
				psInsert.setString(1, sid);
				psInsert.setString(2, name);
				psInsert.setString(3, weight);
				psInsert.setString(4, description);
				psInsert.setString(5, similar);
				int result = psInsert.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}

		// Otherwise, update
		else
		{
			try {
				//Connect to SQL Database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
				// UPDATE user_stories SET name = ?, weight = ?, description = ?, similar_stories = ? WHERE id = ?
				psInsert = connection.prepareStatement("UPDATE user_stories_" + id + " SET name = ?, weight = ?, description = ?, similar_stories = ? WHERE id = ?");
				psInsert.setString(1, name);
				psInsert.setString(2, weight);
				psInsert.setString(3, description);
				psInsert.setString(4, similar);
				psInsert.setString(5, sid);
				int result = psInsert.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
	}

	// Get user stories from DB in list form
	public static ArrayList<UserStory> GetUserStoriesFromDB(int id)
	{
		ArrayList<UserStory> list = new ArrayList<>();

		//SQL Database Prep
		Connection connection = null;
		PreparedStatement psInsert = null;
		ResultSet results = null;

		try {
			//Connect to SQL Database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
			psInsert = connection.prepareStatement("SELECT * FROM user_stories_" + id);
			results = psInsert.executeQuery();

			while (results.next())
			{
				String sid = results.getString(1);
				String name = results.getString(2);
				String description = results.getString(4);
				String weight = results.getString(3);
				String similar = results.getString(5);

				UserStory story = new UserStory(sid, name, description, weight, similar);
				list.add(story);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	// Get specific user story by ID
	public static UserStory GetUserStoryById(int id, String sid)
	{
		//SQL Database Prep
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;

		try {
			//Connect to SQL Database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
			ps = connection.prepareStatement("SELECT * FROM user_stories_" + id + " WHERE id = ?");
			ps.setString(1,  sid);
			results = ps.executeQuery();

			results.next();

			String name = results.getString(2);
			String description = results.getString(4);
			String weight = results.getString(3);
			String similar = results.getString(5);

			UserStory story = new UserStory(sid, name, description, weight, similar);
			return story;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// Get specific legacy project by unique ID
	public static LegacyProject GetLegacyProjectById(int id, String sid)
	{
		//SQL Database Prep
		Connection connection = null;
		PreparedStatement ps = null;
		ResultSet results = null;

		try {
			//Connect to SQL Database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
			ps = connection.prepareStatement("SELECT * FROM pp_projects_" + id + " WHERE ID = ?");
			ps.setString(1,  sid);
			results = ps.executeQuery();

			results.next();

			String name = results.getString(2);
			String description = results.getString(3);
			String story_id = results.getString(4);

			LegacyProject project = new LegacyProject(sid, name, description, story_id);
			return project;

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	// Gets list of all LegacyProjects in the DB
	public static List<LegacyProject> GetLegacyProjectsFromDB(int id)
	{
		ArrayList<LegacyProject> list = new ArrayList<>();

		//SQL Database Prep
		Connection connection = null;
		PreparedStatement psInsert = null;
		ResultSet results = null;

		try {
			//Connect to SQL Database
			connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
			psInsert = connection.prepareStatement("SELECT * FROM pp_projects_" + id);
			results = psInsert.executeQuery();

			while (results.next())
			{
				String sid = results.getString(1);
				String name = results.getString(2);
				String description = results.getString(3);
				String story_id = results.getString(4);

				LegacyProject project = new LegacyProject(sid, name, description, story_id);
				list.add(project);
			}

		} catch (SQLException e) {
			throw new RuntimeException(e);
		}

		return list;
	}

	// Creates or updates a planning poker project, based on whether sid in NULL
	public static boolean createProject(int id, String name, String description, UserStory story, String sid)
	{

		if (name == null || name == "" || story == null || description == null || description == "")
		{
			return false;
		}

		//SQL Database Prep
		Connection connection = null;
		PreparedStatement psInsert = null;

		// If given id is null, create
		if (sid == null)
		{
			sid = String.valueOf(System.currentTimeMillis());

			try {
				//Connect to SQL Database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
				psInsert = connection.prepareStatement("INSERT INTO pp_projects_" + id + " (id, name, description, user_story_id) VALUES (?, ?, ?, ?)");
				psInsert.setString(1, sid);
				psInsert.setString(2, name);
				psInsert.setString(3, description);
				psInsert.setString(4, story.id);
				int result = psInsert.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}

		// Otherwise, update
		else
		{
			try {
				//Connect to SQL Database
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", DB_PASSWORD);
				// UPDATE user_stories SET name = ?, weight = ?, description = ?, similar_stories = ? WHERE id = ?
				psInsert = connection.prepareStatement("UPDATE pp_projects_" + id + " SET name = ?, description = ?, user_story_id = ? WHERE id = ?");
				psInsert.setString(1, name);
				psInsert.setString(2, description);
				psInsert.setString(3, story.id);
				psInsert.setString(4, sid);
				int result = psInsert.executeUpdate();
			} catch (SQLException e) {
				e.printStackTrace();
				return false;
			}

			return true;
		}
	}
}
