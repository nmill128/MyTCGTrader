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
        
    public List<Cards> findCardsByUserID(Integer userID) {
        return (List<Cards>) em.createNamedQuery("Cards.findCardsByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }
    
    
    public List<Cards> findByName(String name) {
        return em.createQuery("SELECT c FROM Cards c WHERE c.cardName LIKE :cardName").
                setParameter("cardName", "%" + name + "%").getResultList();
    }
    
    public List<Cards> findByOwnerName(String name) {
        return em.createQuery("SELECT c from Cards c, Users u WHERE u.username LIKE :username AND c.userId.id = u.id").
                setParameter("username", "%" + name + "%").getResultList();
    }
    
}
