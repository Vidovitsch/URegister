package Model;

import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@Entity
@Table(name = "Registration")
@NamedQueries({
    @NamedQuery(name = "Account.findByDate", query = "SELECT a FROM Registration AS a WHERE a.date = :date"),
    @NamedQuery(name = "Account.findAll", query = "SELECT a FROM Registration AS a")
})
public class Registration implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    private Date date;
    private Time start;
    private Time end;
    private Time workedTime;
    private String content;

    public Registration() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Time getWorkedTime() {
        return workedTime;
    }

    public void setWorkedTime(Time workedTime) {
        this.workedTime = workedTime;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Time getStart() {
        return start;
    }

    public void setStart(Time start) {
        this.start = start;
    }

    public Time getEnd() {
        return end;
    }

    public void setEnd(Time end) {
        this.end = end;
    }

    public String getContent() {
        return this.content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public String toString() {
        return "Date: " + date + "\nPeriod: " + start + " - " + end + "                                                                 Time worked: " + workedTime;
    }
}
