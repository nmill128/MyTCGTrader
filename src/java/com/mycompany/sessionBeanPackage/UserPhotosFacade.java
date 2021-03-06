/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.UserPhotos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Erik
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
    
            // The following findPhotosByUserID method is added to the generated code.
    
    //finds a photo by the user Id
    public List<UserPhotos> findPhotosByUserID(Integer userID) {
        return (List<UserPhotos>) em.createNamedQuery("UserPhotos.findPhotosByUserId")
                .setParameter("userId", userID)
                .getResultList();
    }
    
}
