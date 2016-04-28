/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public List<Tradecomments> findCommentsByTradeId(Integer tradeID) {
        return (List<Tradecomments>) em.createNamedQuery("Tradecomments.findCommentsByTradeId")
                .setParameter("tradeID", tradeID)
                .getResultList();
    }
    
}
