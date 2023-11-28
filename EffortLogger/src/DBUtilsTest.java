import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class DBUtilsTest {

    // Create new user story, query all user stories, delete created user story.
    @Test
    void UserStoriesTest() {
        ArrayList<UserStory> stories =  DBUtils.GetUserStoriesFromDB();
        int length = stories.size();
        String id = DBUtils.createUserStory("JUnitTest", "100", "A description", "", null);
        stories =  DBUtils.GetUserStoriesFromDB();
        assertTrue(stories.size() > length);

        // Cleanup
        DBUtils.DeleteUserStory(id);
        stories =  DBUtils.GetUserStoriesFromDB();
        assertEquals(length, stories.size());
    }

    // Create new user story, create new project linked to that user story, query all projects, delete created project.
    @Test
    void LegacyProjectsTest() {
        ArrayList<LegacyProject> projects = DBUtils.GetLegacyProjectsFromDB();
        int length = projects.size();
        String storyId = DBUtils.createUserStory("JUnitTest", "100", "A description", "", null);
        UserStory story = DBUtils.GetUserStoryById(storyId);
        String projectId = DBUtils.createProject("JUnitTest", "A description", story, null);
        projects = DBUtils.GetLegacyProjectsFromDB();
        assertTrue(projects.size() > length);

        // Cleanup
        DBUtils.DeleteUserStory(storyId);
        DBUtils.DeleteLegacyProject(projectId);
        projects = DBUtils.GetLegacyProjectsFromDB();
        assertEquals(length, projects.size());
    }

    // Mutation test of user stories and legacy projects
    @Test
    void MutationTest() {
        // Create
        String storyId = DBUtils.createUserStory("JUnitTest", "100", "A description", "", null);
        UserStory story = DBUtils.GetUserStoryById(storyId);
        String projectId = DBUtils.createProject("JUnitTest", "A description", story, null);
        LegacyProject project = DBUtils.GetLegacyProjectById(projectId);

        // Query current
        assertEquals(story.name, "JUnitTest");
        assertEquals(project.title, "JUnitTest");

        // Mutate
        DBUtils.createUserStory("JUnitTestEdit", "100", "A description", "", storyId);
        story = DBUtils.GetUserStoryById(storyId);
        DBUtils.createProject("JUnitTestEdit", "A description", story, projectId);
        project = DBUtils.GetLegacyProjectById(projectId);

        assertEquals(story.name, "JUnitTestEdit");
        assertEquals(project.title, "JUnitTestEdit");

        // Cleanup
        DBUtils.DeleteUserStory(storyId);
        DBUtils.DeleteLegacyProject(projectId);
    }
}