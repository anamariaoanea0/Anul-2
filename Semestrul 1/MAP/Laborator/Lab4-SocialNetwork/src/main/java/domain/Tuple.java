package domain;

import java.util.Objects;

/**
 * Generic class representing an ordered pair of two elements.
 *
 * @param <E1> the type of the first element
 * @param <E2> the type of the second element
 */
public class Tuple<E1, E2>{
    private E1 e1;
    private E2 e2;

    /**
     * Constructs a new Tuple with the specified elements.
     *
     * @param e1 the first element of the tuple
     * @param e2 the second element of the tuple
     */
    public Tuple(E1 e1, E2 e2){
        this.e1 = e1;
        this.e2 = e2;
    }

    /**
     * Retrieves the first element of the tuple.
     *
     * @return the first element
     */
    public E1 getLeft(){
        return e1;
    }

    /**
     * Sets the first element of the tuple.
     *
     * @param e1 the new first element to be set
     */
    public void setLeft(E1 e1){
        this.e1 = e1;
    }

    /**
     * Retrieves the second element of the tuple.
     *
     * @return the second element
     */
    public E2 getRight(){
        return e2;
    }

    /**
     * Sets the second element of the tuple.
     *
     * @param e2 the new second element to be set
     */
    public void setRight(E2 e2){
        this.e2 = e2;
    }

    /**
     * Compares this tuple to the specified object for equality.
     * The result is true if and only if the argument is not null and is a Tuple object that
     * contains the same elements as this tuple.
     *
     * @param o the object to compare this tuple against
     * @return true if the given object represents a Tuple equivalent to this tuple, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tuple<?, ?> tuple)) return false;
        return Objects.equals(e1, tuple.e1) && Objects.equals(e2, tuple.e2);
    }

    /**
     * Returns a hash code value for this tuple.
     *
     * @return a hash code value generated based on both elements
     */
    @Override
    public int hashCode(){
        return Objects.hash(e1, e2);
    }

    /**
     * Returns a string representation of the tuple.
     *
     * @return a string in the format "(e1, e2)"
     */
    @Override
    public String toString() {
        return "(" + e1 + ", " + e2 + ")";
    }
}