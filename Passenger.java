
enum Gender {
    MALE, FEMALE, OTHER
}

public class Passenger {
    private int id;
    private String name;
    private int age;
    private Gender gender;
    private String phone;

    public Passenger(int id, String name, int age, Gender gender, String phone) {
        this.id = id;
        this.name = name;
        this.age = age;
        this.gender = gender;
        this.phone = phone;
    }
    public int getId() { return id; }
    public String getName() { return name; }
    @Override
    public String toString() {
        return "ID: " + id + " | Name: " + name + " | Age: " + age + " | Gender: " + gender + " | Phone: " + phone;
    }
}
