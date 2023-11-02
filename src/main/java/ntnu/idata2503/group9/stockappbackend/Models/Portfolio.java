package ntnu.idata2503.group9.stockappbackend.Models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

@Entity
@Table(name = "portfolio")
public class Portfolio {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long Pid;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    /**
     * Constructor for portfolio.
     * @param user the user you want to set that owns the portfolio.
     */
    public Portfolio(User user) {
        setUser(user);
    }

    /**
     * Sets the user.
     * @param user the user yo want to set
     */
    public void setUser(User user) {
        this.user = user;
    }

    /**
     * Return user.
     * @return user.
     */
    public User getUser() {
        return this.user;
    }

    /**
     * Returns portfolio id.
     * @return pid.
     */
    public long getPid() {
        return this.Pid;
    }

    /**
     * Checks if the portfolio is valid
     * @return boolean statement. True if portfolio is valid, false if not.
     */
    public boolean isValid() {
        return this.user != null;
    }

}