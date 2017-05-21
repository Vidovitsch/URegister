package Service;

import Database.RegistrationDAO;
import Database.RegistrationDAOJPAImpl;
import Model.Registration;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class RegistrationMgr {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("URegisterPU");
    private RegistrationDAO regDAO;
    
    public void createRegistration(Registration reg) {
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            regDAO.create(reg);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(RegistrationMgr.class.getName()).log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public void updateRegistration(Registration reg) {
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            regDAO.update(reg);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(RegistrationMgr.class.getName()).log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public List<Registration> findBySingleDate(Date date) {
        List<Registration> registrations = null;
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            registrations = regDAO.findByDate(date);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(RegistrationMgr.class.getName()).log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return registrations;
    }
    
    public void remove(Registration reg) {
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            regDAO.delete(reg);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(RegistrationMgr.class.getName()).log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    public List<Registration> findAll() {
        List<Registration> registrations = null;
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            registrations = regDAO.findAll();
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(RegistrationMgr.class.getName()).log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return registrations;
    }
    
    public List<Registration> findByDateSpan(Date start, Date end) {
        List<Registration> registrations = null;
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            registrations = regDAO.findByDateSpan(start, end);
            em.getTransaction().commit();
        } catch (Exception e) {
            Logger.getLogger(RegistrationMgr.class.getName()).log(Level.SEVERE, null, e);
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return registrations;
    }
    
    public void saveSalary(String salary) {
        new Utility().saveSalary(salary);
    }
    
    public String loadSalary() {
        return new Utility().loadSalary();
    }
}
