/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class DBUtils {
	
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
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", "ELDatabasePassword1!");
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
				connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/effortlogger_db", "root", "ELDatabasePassword1!");
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
			}
		}
	}
}
