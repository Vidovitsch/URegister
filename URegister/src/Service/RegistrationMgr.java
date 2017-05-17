/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Service;

import Database.RegistrationDAO;
import Database.RegistrationDAOJPAImpl;
import Model.Registration;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author David
 */
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
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return registrations;
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
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
        
        return registrations;
    }
    
    public List<Registration> findByDateSpan(Date start, Date end) {
        List<Registration> registrations = new ArrayList();
        Date varDate = start;
        long diff = new Utility().getDateDiff(start, end, TimeUnit.DAYS);
        for (int i = 0; i < diff; i++) {
            registrations.addAll(findBySingleDate(varDate));
            varDate = new Utility().addDayToDate(varDate);
        }
        
        return registrations;
    }
}
