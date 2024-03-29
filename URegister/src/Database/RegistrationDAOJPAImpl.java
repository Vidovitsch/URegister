package Database;

import Model.Registration;
import java.sql.Date;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.persistence.TemporalType;
import javax.persistence.criteria.CriteriaQuery;

public class RegistrationDAOJPAImpl implements RegistrationDAO {

    private EntityManager em;
    
    public RegistrationDAOJPAImpl(EntityManager em) {
        this.em = em;
    }
    
    @Override
    public void create(Registration reg) {
        em.persist(reg);
    }

    @Override
    public void update(Registration reg) {
        em.merge(reg);
    }

    @Override
    public void delete(Registration reg) {
        em.remove(em.merge(reg));
    }

    @Override
    public List<Registration> findByDate(Date date) {
        Query q = em.createNamedQuery("Account.findByDate", Registration.class);
        q.setParameter("date", date);
        return (List<Registration>) q.getResultList();
    }
    
    @Override
    public List<Registration> findAll() {
       CriteriaQuery cq = em.getCriteriaBuilder().createQuery();
        cq.select(cq.from(Registration.class));
        return em.createQuery(cq).getResultList();
    }

    @Override
    public List<Registration> findByDateSpan(Date start, Date end) {
        String query = "SELECT a FROM Registration AS a WHERE a.date >= '" + start + "' AND a.date <= '" + end + "'";
        System.out.println(query);
        Query q = em.createQuery(query);
        return (List<Registration>) q.getResultList();
    }
}
