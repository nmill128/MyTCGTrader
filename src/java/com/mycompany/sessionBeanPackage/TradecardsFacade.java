/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.Tradecards;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Erik
 */
@Stateless
public class TradecardsFacade extends AbstractFacade<Tradecards> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TradecardsFacade() {
        super(Tradecards.class);
    }
    
////The Following was added to the generated code
    
    //returns a list of Tradecards that have the tradeId as the one passed
    public List<Tradecards> findTradecardsByTradeId(Integer id) {
        return (List<Tradecards>) em.createNamedQuery("Tradecards.findByTradeId")
                .setParameter("tradeId", id)
                .getResultList();
    }
    
}
