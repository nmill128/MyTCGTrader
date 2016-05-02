/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.Wants;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Erik
 */
@Stateless
public class WantsFacade extends AbstractFacade<Wants> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WantsFacade() {
        super(Wants.class);
    }
 
////The following was added to the generated code
    
    //returns a list of wants that have the user id as the passed
    public List<Wants> findWantsByUserID(Integer userID) {
        return (List<Wants>) em.createNamedQuery("Wants.findWantsByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }
    
}
