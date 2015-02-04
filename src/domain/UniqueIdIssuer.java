/*
 * Fontys Hogescholen ICT - Software Development
 * Modelling with Automata in Professional Practice
 * Project developer: Arjan Boschman
 * Startdate: October 2014
 */
package domain;

/**
 * Utility class for returning unique integers.
 *
 * @author Arjan
 */
public class UniqueIdIssuer {

    private final int startingId;
    private int number;

    /**
     * Constructs a new UniqueIdIssuer that will give out id's starting at zero.
     */
    public UniqueIdIssuer() {
        this(0);
    }

    /**
     * Constructs a new UniqueIdIssuer that will give out id's starting at the
     * given number.
     *
     * @param number The first number to be issued by this instance of
     * UniqueIdIssuer.
     */
    public UniqueIdIssuer(int number) {
        this.startingId = number;
        this.number = number;
    }

    /**
     * @return Returns the starting id, or the first integer returned by this
     * object.
     */
    public int getStartingId() {
        return startingId;
    }

    /**
     * Give out a unique id. This will increment the internal number by one, so
     * this method will never return the same number twice.
     *
     * @return A unique integer.
     */
    public int getUniqueId() {
        return number++;
    }

    /**
     * Peeks at the id that will next be issued, without actually changing it.
     *
     * @return The next ID in line.
     */
    public int peekAtNextID() {
        return number;
    }

    /**
     * Checks if this UniqueIdIssuer has already issued id's in the past.
     *
     * @return True if at least one unique id has been issued in the past.
     */
    public boolean hasIssuedIds() {
        return number != startingId;
    }
}
