
public class User {
	private int id = -1;
	private String username = null;
	private String password = null;
	private String role = null;
	
	public void setInfo(int userId, String name, String pass, String compRole)
	{
		id = userId;
		username = name;
		password = pass;
		role = compRole;
	}
	
	public int getId()
	{
		return id;
	}
	
	public String getUsername()
	{
		return AES.decrypt(username);
	}
	
	public String getRole()
	{
		return role;
	}
}
