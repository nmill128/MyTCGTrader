/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.Trades;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Date;
import java.util.List;
import java.sql.Timestamp;

/**
 *
 * @author Erik
 */
@Stateless
public class TradesFacade extends AbstractFacade<Trades> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TradesFacade() {
        super(Trades.class);
    }
  
////The following was added to the generated code

    //Finds a trade by its date (sql Timestamp)
    //Used because originally this is the only way to get a new offer when you dont
    //have its id
    public Trades findByDate(Date date) {
        return (Trades) em.createNamedQuery("Trades.findByOfferTimestamp")
                .setParameter("offerTimestamp", new Timestamp(date.getTime()))
                .getResultList().get(0);
    }

    //Finds a list of current Trades by user id
    //the id passed can be eithe the creator or reciever and a current trade
    //has not been completed
    public List<Trades> findCurrTradesByUserId(Integer id) {
        return (List<Trades>) em.createNamedQuery("Trades.findCurrTradesByUserId")
                .setParameter("creatorId", id)
                .setParameter("recieverId",id)
                .getResultList();
    }
    
    //Finds a list of past trades by user id
    //the id passed can be either the creator or reciever and a past trade has 
    //been completed
    public List<Trades> findPastTradesByUserId(Integer id) {
        return (List<Trades>) em.createNamedQuery("Trades.findPastTradesByUserId")
                .setParameter("creatorId", id)
                .setParameter("recieverId",id)
                .getResultList();
    }

}
