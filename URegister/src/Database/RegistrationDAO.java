/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package Database;

import Model.Registration;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * @author David
 */
public interface RegistrationDAO {

    /**
     * The account is persisted.
     * If a account with the same id allready exists an EntityExistsException is thrown
     * @param reg
     */
    void create(Registration reg);
    
    /**
     * Merge the state of the given account into persistant context.
     * If the account did not exist an IllegalArgumentException is thrown
     * @param reg
     */
    void update(Registration reg);
    
    /**
     * Remove the entity instance
     * @param reg
     */
    void delete(Registration reg);
    
    /**
     * Searches for entities within a given date
     * @param date
     * @return a list of entities
     */
    List<Registration> findByDate(Date date);
    
    List<Registration> findAll();
}
