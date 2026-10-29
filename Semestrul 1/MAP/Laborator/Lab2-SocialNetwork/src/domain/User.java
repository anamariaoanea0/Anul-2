package domain;

import java.util.Objects;

/**
 * Represents a user entity within the social network.
 */
public class User extends Entity<Long>{
    private String firstName;
    private String lastName;
    private int age;

    /**
     * Constructs a new User without a specified entity ID.
     * This is typically used when creating a new user before saving it to a repository,
     * where the ID will be automatically generated.
     *
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param age the user's age
     */
    public User(String firstName, String lastName, int age){
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    /**
     * Constructs a new User with a specified entity ID.
     * This is typically used when loading an existing user from a repository.
     *
     * @param id the unique identifier of the user
     * @param firstName the user's first name
     * @param lastName the user's last name
     * @param age the user's age
     */
    public User(Long id, String firstName, String lastName, int age){
        super.setId(id);
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
    }

    /**
     * Retrieves the first name of the user.
     *
     * @return the first name
     */
    public String getFirstName(){
        return firstName;
    }

    /**
     * Updates the first name of the user.
     *
     * @param newFirstName the new first name to be set
     */
    public void setFirstName(String newFirstName){
        this.firstName = newFirstName;
    }

    /**
     * Retrieves the last name of the user.
     *
     * @return the last name
     */
    public String getLastName(){
        return lastName;
    }

    /**
     * Updates the last name of the user.
     *
     * @param newLastName the new last name to be set
     */
    public void setLastName(String newLastName){
        this.lastName = newLastName;
    }

    /**
     * Retrieves the age of the user.
     *
     * @return the age in years
     */
    public int getAge(){
        return age;
    }

    /**
     * Updates the age of the user.
     *
     * @param newAge the new age to be set.
     */
    public void setAge(int newAge) {
        this.age = newAge;
    }

    /**
     * Returns a string representation of the User entity.
     *
     * @return a formatted string containing the user's ID, first name, last name, and age
     */
    @Override
    public String toString(){
        return "User: " + "Id: " + getId() + ", First name: " + getFirstName() + ", Last name: " + getLastName() + ", Age: " + getAge();
    }

    /**
     * Indicates whether some other object is "equal to" this user.
     * Two users are considered equal if they have the same entity ID, first name, last name, and age.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User user)) return false;
        if (!super.equals(o)) return false;

        return age == user.age &&
                Objects.equals(getFirstName(), user.getFirstName()) &&
                Objects.equals(getLastName(), user.getLastName());
    }

    /**
     * Returns a hash code value for the User entity.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getFirstName(), getLastName(), age);
    }
}
