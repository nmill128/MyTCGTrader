/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.CardPhotos;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author Erik
 */
@Stateless
public class CardPhotosFacade extends AbstractFacade<CardPhotos> {

    @PersistenceContext(unitName = "MyTCGTraderPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CardPhotosFacade() {
        super(CardPhotos.class);
    }
    
                // The following findPhotosByUserID method is added to the generated code.
    //returns a list of CardPhotos that have the passed cardID as their cardID in the db
    public List<CardPhotos> findPhotosByCardID(Integer cardID) {
        return (List<CardPhotos>) em.createNamedQuery("CardPhotos.findPhotosByCardId")
                .setParameter("cardId", cardID)
                .getResultList();
    }
    
}
