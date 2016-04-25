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
    
    public List<Cards> findByAdvancedCriteria(String cardName, String userName, String edition, String value, int valueType) {
        if (cardName.equals("") && userName.equals("") && edition.equals("") && value.equals("")) {
            System.out.println("Blank search");
            return em.createQuery("SELECT c FROM Cards c").getResultList();
        }
        String searchCriteria = "";
        if (!cardName.equals("")) {
            searchCriteria = searchCriteria + "c.cardName LIKE '" + cardName + "'";
        }
        if (!userName.equals("")) {
            if (!searchCriteria.equals("")) { searchCriteria = searchCriteria + " AND "; }
            searchCriteria = searchCriteria + "u.username LIKE '" + userName + "' AND c.userId.id = u.id";
        }
        if (!edition.equals("")) {
            if (!searchCriteria.equals("")) { searchCriteria = searchCriteria + " AND "; }
            searchCriteria = searchCriteria + "c.edition LIKE '" + edition + "'";
        }
        if (!value.equals("")) {
            if (!searchCriteria.equals("")) { searchCriteria = searchCriteria + " AND "; }
            switch (valueType) {
                case 0:
                    searchCriteria = searchCriteria + "c.cardValue = " + value;
                    break;
                case 1:
                    searchCriteria = searchCriteria + "c.cardValue < " + value;
                    break;
                case 2:
                    searchCriteria = searchCriteria + "c.cardValue > " + value;
                    break;
                default:
                    break;
            }
        }
        System.out.println(searchCriteria);
        return em.createQuery("SELECT DISTINCT c FROM Cards c, Users u WHERE " + searchCriteria).getResultList();
    }
    
}
