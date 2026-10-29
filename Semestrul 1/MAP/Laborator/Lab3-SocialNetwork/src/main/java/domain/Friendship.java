package domain;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

/**
 * Represents a friendship relationship between two users in the social network.
 */
public class Friendship extends Entity<Tuple<Long, Long>>{
    private LocalDateTime date;

    public Friendship(Long idUser1, Long idUser2, LocalDateTime date){
        if(idUser1 != null && idUser2 != null)
        {
            long minId = Math.min(idUser1, idUser2);
            long maxId = Math.max(idUser1, idUser2);
            super.setId(new Tuple<>(minId, maxId));
        }
        else{
            super.setId(new Tuple<>(idUser1, idUser2));
        }
        this.date = date;
    }

    public Friendship(Long idUser1, Long idUser2) {
        this(idUser1, idUser2, LocalDateTime.now().truncatedTo(ChronoUnit.MINUTES));
    }

    /**
     * Retrieves the ID of the first user.
     * Due to the internal sorting, this will always be the smaller of the two IDs.
     *
     * @return the ID of the first user
     */
    public Long getIdUser1() {
        if (super.getId() == null) {
            return null;
        }
        return super.getId().getLeft();
    }

    /**
     * Retrieves the ID of the second user.
     * Due to the internal sorting, this will always be the larger of the two IDs.
     *
     * @return the ID of the second user
     */
    public Long getIdUser2() {
        if (super.getId() == null) {
            return null;
        }
        return super.getId().getRight();
    }

    public LocalDateTime getDate(){
        return date;
    }

    public void setDate(LocalDateTime date){
        this.date = date;
    }

    /**
     * Returns a string representation of the Friendship entity.
     *
     * @return a formatted string containing the IDs of the two users involved and date the friendship began
     */
    @Override
    public String toString() {
        return "Friendship: User1 ID=" + getIdUser1() + ", User2 ID=" + getIdUser2() + ", Date=" + date;
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