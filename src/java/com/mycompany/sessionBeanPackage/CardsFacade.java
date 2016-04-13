/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.Cards;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

/**
 *
 * @author Erik
 */
@Stateless
public class CardsFacade extends AbstractFacade<Cards> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CardsFacade() {
        super(Cards.class);
    }
    
    public Cards findById(int id) {

            return (Cards) (em.createQuery("SELECT c FROM Cards c WHERE c.id = :id")
                .setParameter("id", id)
                .getSingleResult());        
    }
    
    public List<Cards> findByName(String name) {
        Query query = em.createQuery("SELECT c FROM Cards c");
        //query.setParameter(":name", name);
        System.out.println("Searching for " + name);
        return query.getResultList();
    }
    
}
