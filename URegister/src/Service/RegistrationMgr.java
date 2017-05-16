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
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;


/**
 *
 * @author David
 */
public class RegistrationMgr {

    private final EntityManagerFactory emf = Persistence.createEntityManagerFactory("bankPU");
    private RegistrationDAO regDAO;
    
    public void createRegistration(Registration reg) {
        EntityManager em = emf.createEntityManager();
        regDAO = new RegistrationDAOJPAImpl(em);
        em.getTransaction().begin();
        try {
            regDAO.create(createDummy());
            em.getTransaction().commit();
        } catch (Exception e) {
            em.getTransaction().rollback();
        } finally {
            em.close();
        }
    }
    
    //Dummy method
    private Registration createDummy() {
        Registration reg = new Registration();
        reg.setDate(new Date(12, 12, 1212));
        reg.setStart(new Time(12, 12, 12));
        reg.setEnd(new Time(13, 13, 13));
        reg.setContent("Test");
        
        return reg;
    }
}
