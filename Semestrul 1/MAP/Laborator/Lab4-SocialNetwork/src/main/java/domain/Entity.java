package domain;

import java.util.Objects;

/**
 * Basic generic class for all entities in the application.
 *
 * @param <ID> the type of the entity's identifier.
 */
public class Entity<ID> {
    private ID id;

    /**
     * Gets the idetifier of the entity.
     *
     * @return the id.
     */
    public ID getId(){
        return id;
    }

    /**
     * Sets the identifier of the entity.
     *
     * @param newId the new id.
     */
    public void setId(ID newId){
        this.id = newId;
    }

    /**
     * Indicates whether some other object is "equal to" this entity.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Entity<?> entity)) return false;
        return Objects.equals(getId(), entity.getId());
    }

    /**
     * Returns a hash code value for the entity.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
