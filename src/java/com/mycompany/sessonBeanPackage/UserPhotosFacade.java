/*
 * Created by Nicholas Miller on 2016.04.12  * 
 * Copyright © 2016 Nicholas Miller. All rights reserved. * 
 */
package com.mycompany.sessonBeanPackage;

import com.mycompany.entitypackage.UserPhotos;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author nmiller
 */
@Stateless
public class UserPhotosFacade extends AbstractFacade<UserPhotos> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UserPhotosFacade() {
        super(UserPhotos.class);
    }
    
}
