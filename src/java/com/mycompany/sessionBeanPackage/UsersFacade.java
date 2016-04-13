/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.Users;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Erik
 */
@Stateless
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }
    
        //-----------------------------------------------------
    //The following methods are added to the generated code
    //-----------------------------------------------------
    
    public Users getUser(int id) {
        return em.find(Users.class, id);
    }

    public Users findByUsername(String username) {
        if (em.createQuery("SELECT u FROM Users u WHERE u.username = :uname")
                .setParameter("uname", username)
                .getResultList().isEmpty()) {
            return null;
        }
        else {
            return (Users) (em.createQuery("SELECT u FROM Users u WHERE u.username = :uname")
                .setParameter("uname", username)
                .getSingleResult());        
        }
    }
    
    public void deleteUser(int id){
        
        Users user = em.find(Users.class, id);
        em.remove(user);
    }
    
}
