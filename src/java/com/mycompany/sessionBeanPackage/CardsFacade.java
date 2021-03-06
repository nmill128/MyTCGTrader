/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
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
    
////The following methods were added to the generated code
    
    //Finds a card by its Id
    public Cards findById(int id) {

        return (Cards) (em.createQuery("SELECT c FROM Cards c WHERE c.id = :id")
            .setParameter("id", id)
            .getSingleResult());        
    }
     
    //Returns a list of cards that have the passed in userID as the card's user's id
    public List<Cards> findCardsByUserID(Integer userID) {
        return (List<Cards>) em.createNamedQuery("Cards.findCardsByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }
    
    //Returns a list of cards that have the passed in name
    public List<Cards> findByName(String name) {
        return em.createQuery("SELECT c FROM Cards c WHERE c.cardName LIKE :cardName").
                setParameter("cardName", "%" + name + "%").getResultList();
    }
    
    //returns a list of cards that have their user's username as the string passed in
    public List<Cards> findByOwnerName(String name) {
        return em.createQuery("SELECT c from Cards c, Users u WHERE u.username LIKE :username AND c.userId.id = u.id").
                setParameter("username", "%" + name + "%").getResultList();
    }
    
    //returns a list of cards that have the cardname, username, edition, and value that is passed in
    public List<Cards> findByAdvancedCriteria(String cardName, String userName, int edition, String value, int valueType) {
        if (cardName.equals("") && userName.equals("") && edition != 0 && value.equals("")) {
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
        if (edition != 0) {
            if (!searchCriteria.equals("")) { searchCriteria = searchCriteria + " AND "; }
            switch (edition) {
                case 1:
                    searchCriteria = searchCriteria + "c.cardCondition LIKE 'Mint'";
                    break;
                case 2:
                    searchCriteria = searchCriteria + "c.cardCondition LIKE 'Lightly Played'";
                    break;
                case 3:
                    searchCriteria = searchCriteria + "c.cardCondition LIKE 'Moderately Played'";
                    break;
                case 4:
                    searchCriteria = searchCriteria + "c.cardCondition LIKE 'Heavily Played'";
                    break;
                case 5:
                    searchCriteria = searchCriteria + "c.cardCondition LIKE 'Damaged'";
                    break;
                default:
                    break;
            }
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
