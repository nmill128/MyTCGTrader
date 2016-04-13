/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.sessionBeanPackage;

import com.mycompany.entitypackage.CardPhotos;
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
    
    public List<CardPhotos> findPhotosByCardID(Integer cardID) {
        return (List<CardPhotos>) em.createNamedQuery("CardPhotos.findPhotosByCardId")
                .setParameter("cardId", cardID)
                .getResultList();
    }
    
}
