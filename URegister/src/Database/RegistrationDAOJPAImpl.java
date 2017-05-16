/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import Model.Registration;
import java.sql.Date;
import java.util.ArrayList;
import javax.persistence.EntityManager;


/**
 *
 * @author David
 */
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
        em.remove(reg);
    }

    @Override
    public ArrayList<Registration> findByDate(Date date) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public ArrayList<Registration> findByDateSpan(Date start, Date end) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
