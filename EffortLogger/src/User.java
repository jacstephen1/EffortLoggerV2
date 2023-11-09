/*
 * Author: Jacob Wetherell
 * ASU ID: 1224869945
 * CSE 360 EffortLogger 2.0 Development
 */

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
