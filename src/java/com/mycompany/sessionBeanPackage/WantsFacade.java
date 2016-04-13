/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.UserPhotos;
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
    
    public List<Wants> findWantsByUserID(Integer userID) {
        return (List<Wants>) em.createNamedQuery("Wants.findWantsByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }
    
}
