/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.Tradecomments;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

/**
 *
 * @author Erik
 */
@Stateless
public class TradecommentsFacade extends AbstractFacade<Tradecomments> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public TradecommentsFacade() {
        super(Tradecomments.class);
    }
    
////The following was added to the generated code
    
    //returns a list of trade comments that have the tradeId as the one passed
    public List<Tradecomments> findCommentsByTradeId(Integer tradeID) {
        return (List<Tradecomments>) em.createNamedQuery("Tradecomments.findCommentsByTradeId")
                .setParameter("tradeID", tradeID)
                .getResultList();
    }
    
}
