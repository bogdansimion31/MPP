package eng.ubb.brigadagrea.server.domain;

import java.util.Random;

/**
 * The type Client.
 */
public class Client extends Entity<Long> {
    private String name;
    private String surname;
    private int fidelityScore;

    /**
     * Instantiates a new Client.
     */
    public Client(){}

    /**
     * Instantiates a new Client.
     *
     * @param name    the name
     * @param surname the surname
     */
    public Client(String name, String surname){
        this.name = name;
        this.surname = surname;
        this.fidelityScore = 0;
    }

    /**
     * Gets name.
     *
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets name.
     *
     * @param name the name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets surname.
     *
     * @return the surname
     */
    public String getSurname() {
        return surname;
    }

    /**
     * Sets surname.
     *
     * @param surname the surname
     */
    public void setSurname(String surname) {
        this.surname = surname;
    }

    /**
     * Gets fidelity score.
     *
     * @return the fidelity score
     */
    public int getFidelityScore() {
        return fidelityScore;
    }

    /**
     * Sets fidelity score.
     *
     * @param fidelityScore the fidelity score
     */
    public void setFidelityScore(int fidelityScore) {
        this.fidelityScore = fidelityScore;
    }

    @Override
    public boolean equals(Object obj){
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;

        Client client = (Client) obj;
        if (!this.surname.equals(client.surname)) return false;
        if (!this.name.equals(client.name)) return false;

        return this.getId().equals(client.getId());
    }

    @Override
    public int hashCode() {
        int result = surname.hashCode();
        result = 31 * result + name.hashCode();
        result = 31 * result + getId().hashCode();

        return result;
    }

    @Override
    public String toString() {
        return super.toString() + " " + name + " " + surname + " " + fidelityScore;
    }
}
