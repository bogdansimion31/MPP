package eng.ubb.brigadagrea.server.domain;

/**
 * The type Entity.
 *
 * @param <ID> the type parameter
 */
public class Entity<ID> {
    private ID id;

    /**
     * Gets id.
     *
     * @return the id
     */
    public ID getId() {
        return id;
    }

    /**
     * Sets id.
     *
     * @param id the id
     */
    public void setId(ID id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return id + " ";
    }
}
