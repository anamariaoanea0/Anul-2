package domain;

import java.util.function.Predicate;

public class CriteriaUser {

    /**
     * Creates a predicate to filter users by their last name (case-insensitive).
     *
     * @param lastName the last name to search for
     * @return a Predicate<User> that evaluates to true if the user's last name matches the given lastName
     */
    public static Predicate<User> hasLastName(String lastName) {
        return u -> u.getLastName().equalsIgnoreCase(lastName);
    }

    /**
     * Creates a predicate to filter users by their first name (case-insensitive).
     *
     * @param firstName the first name to search for
     * @return a Predicate<User> that evaluates to true if the user's first name matches the given firstName
     */
    public static Predicate<User> hasFirstName(String firstName) {
        return u -> u.getFirstName().equalsIgnoreCase(firstName);
    }

}