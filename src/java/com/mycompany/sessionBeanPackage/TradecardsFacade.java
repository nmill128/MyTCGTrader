/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
    
    public List<Tradecards> findTradecardsByTradeId(Integer id) {
        return (List<Tradecards>) em.createNamedQuery("Tradecards.findByTradeId")
                .setParameter("tradeId", id)
                .getResultList();
    }
    
}
