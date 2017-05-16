package domain;

import java.util.ArrayList;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


/**
 *
 * @author David
 */
@Entity
@Table(name="Registratie")
public class Registratie {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    @Column(unique = true)
    private String date;
    @OneToMany(mappedBy="Registratie")
    private ArrayList<TimeDiff> times;
    
    public Registratie() { }
    
    public Long getId() {
        return this.id;
    }
    
    public void setId(Long id) {
        this.id = id;
    }
    
    public String getDate() {
        return this.date;
    }
    
    public void setDate(String date) {
        this.date = date;
    }
    
    public ArrayList<TimeDiff> getTimes() {
        return this.times;
    }
    
    public void setTimes(ArrayList<TimeDiff> times) {
        this.times = times;
    }
}
