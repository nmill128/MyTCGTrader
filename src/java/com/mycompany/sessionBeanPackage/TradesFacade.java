/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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

    public Trades findByDate(Date date) {
        return (Trades) em.createNamedQuery("Trades.findByOfferTimestamp")
                .setParameter("offerTimestamp", new Timestamp(date.getTime()))
                .getResultList().get(0);
    }

    public List<Trades> findCurrTradesByUserId(Integer id) {
        return (List<Trades>) em.createNamedQuery("Trades.findCurrTradesByUserId")
                .setParameter("creatorId", id)
                .setParameter("recieverId",id)
                .getResultList();
    }
    public List<Trades> findPastTradesByUserId(Integer id) {
        return (List<Trades>) em.createNamedQuery("Trades.findPastTradesByUserId")
                .setParameter("creatorId", id)
                .setParameter("recieverId",id)
                .getResultList();
    }

}
