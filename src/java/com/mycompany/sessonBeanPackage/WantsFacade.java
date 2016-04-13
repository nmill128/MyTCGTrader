/*
 * Created by Nicholas Miller on 2016.04.12  * 
 * Copyright © 2016 Nicholas Miller. All rights reserved. * 
 */
package com.mycompany.sessonBeanPackage;

import com.mycompany.entitypackage.Wants;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author nmiller
 */
@Stateless
public class WantsFacade extends AbstractFacade<Wants> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public WantsFacade() {
        super(Wants.class);
    }
    
}
