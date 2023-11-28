/*
 * Author: Charlie Baird
 */

import java.util.Comparator;

public class UserStory {
    public String id;
    public String name;
    public String description;
    public String weight;
    public String similar;

    public UserStory(String id, String name, String description, String weight, String similar) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.weight = weight;
        this.similar = similar;
    }

    @Override
    public String toString()
    {
        return this.name;
    }
}

class UserStoryComparable implements Comparator<UserStory>
{

    @Override
    public int compare(UserStory o1, UserStory o2) {
        return Integer.valueOf(o2.weight) - Integer.valueOf(o1.weight);
    }
}
