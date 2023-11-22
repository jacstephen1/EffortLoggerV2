/*
 * Author: Charlie Baird
 */
public class LegacyProject {
	public String id;
	public String title;
	public String description;
	public String user_story_id;
	
	public LegacyProject(String id, String title, String description, String user_story_id) {
		this.id = id;
		this.title = title;
		this.description = description;
		this.user_story_id = user_story_id;
	}
	
	@Override
	public String toString()
	{
		return this.title;
	}
}
