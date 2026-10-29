package domain;

import java.util.Objects;

/**
 * Represents a friendship relationship between two users in the social network.
 */
public class Friendship extends Entity<Long>{
    private final Long idUser1;
    private final Long idUser2;

    /**
     * Constructs a new Friendship without a specified entity ID.
     * The provided user IDs are automatically sorted to maintain ascending order.
     *
     * @param idUser1 the ID of the first user
     * @param idUser2 the ID of the second user
     */
    public Friendship(Long idUser1, Long idUser2){
        this.idUser1 = Math.min(idUser1, idUser2);
        this.idUser2 = Math.max(idUser1, idUser2);
    }

    /**
     * Constructs a new Friendship with a specified entity ID.
     * The provided user IDs are automatically sorted to maintain ascending order.
     *
     * @param id the unique identifier of the friendship entity
     * @param idUser1 the ID of the first user
     * @param idUser2 the ID of the second user
     */
    public Friendship(Long id, Long idUser1, Long idUser2){
        super.setId(id);
        this.idUser1 = Math.min(idUser1, idUser2);
        this.idUser2 = Math.max(idUser1, idUser2);
    }

    /**
     * Retrieves the ID of the first user.
     * Due to the internal sorting, this will always be the smaller of the two IDs.
     *
     * @return the ID of the first user
     */
    public Long getIdUser1(){
        return idUser1;
    }

    /**
     * Retrieves the ID of the second user.
     * Due to the internal sorting, this will always be the larger of the two IDs.
     *
     * @return the ID of the second user
     */
    public Long getIdUser2(){
        return idUser2;
    }

    /**
     * Returns a string representation of the Friendship entity.
     *
     * @return a formatted string containing the friendship ID and the IDs of the two users involved
     */
    @Override
    public String toString(){
        return "Friendship: Id: " + getId() + ", First friend Id: " + getIdUser1() + ", Second friend Id: " + getIdUser2();
    }

    /**
     * Indicates whether some other object is "equal to" this friendship.
     * Two friendships are considered equal if they have the same entity ID and the same user IDs.
     *
     * @param o the reference object with which to compare
     * @return true if this object is the same as the obj argument, false otherwise
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Friendship that)) return false;
        if (!super.equals(o)) return false;
        return Objects.equals(getIdUser1(), that.getIdUser1()) &&
                Objects.equals(getIdUser2(), that.getIdUser2());
    }

    /**
     * Returns a hash code value for the Friendship entity.
     *
     * @return a hash code value for this object
     */
    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), getIdUser1(), getIdUser2());
    }
}


