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
