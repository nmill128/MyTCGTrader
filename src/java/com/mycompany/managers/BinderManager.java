/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.managers;

import com.mycompany.entitypackage.Users;
import com.mycompany.entitypackage.Wants;
import com.mycompany.entitypackage.Cards;
import com.mycompany.entitypackage.CardPhotos;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;
import java.util.ArrayList;

@Named(value = "binderManager")
@SessionScoped
/**
 *
 * @author Erik
 */
public class BinderManager implements Serializable{
    
    private Users user;
    List<Wants> wants;
    List<Entry> entries; 
    
    public class Entry{
        private CardPhotos photo;
        private Cards card;
        
        public Entry(Cards card, CardPhotos photo){
            this.card = card;
            this.photo = photo;
        }
        
        public Cards getCard(){
            return card;
        }
        
        public void setCard(Cards card){
            this.card = card;
        }
        
        public CardPhotos getPhoto(){
            return photo;
        }
        
        public void setPhoto(CardPhotos photo){
            this.photo = photo;
        }
        
        public String getFile(){
            if(photo == null){
                return "/DefaultUserPhoto.png";
            } else {
                return photo.getFilename();
            }
        }
    }
    
    
      /**
    * The instance variable 'userFacade' is annotated with the @EJB annotation.
    * This means that the GlassFish application server, at runtime, will inject in
    * this instance variable a reference to the @Stateless session bean UserFacade.
    */  
    @EJB
    private com.mycompany.sessionBeanPackage.UsersFacade userFacade;
    
          /**
    * The instance variable 'userFacade' is annotated with the @EJB annotation.
    * This means that the GlassFish application server, at runtime, will inject in
    * this instance variable a reference to the @Stateless session bean UserFacade.
    */  
    @EJB
    private com.mycompany.sessionBeanPackage.WantsFacade wantsFacade;
    
        @EJB
    private com.mycompany.sessionBeanPackage.CardsFacade cardsFacade;
        
                @EJB
    private com.mycompany.sessionBeanPackage.CardPhotosFacade cardPhotosFacade;
    
    public BinderManager(){
        
    }
    
    public List<Entry> getEntries(){
        List<Cards> cards = cardsFacade.findCardsByUserID(user.getId());
        List<Entry> entriesReturn = new ArrayList(0);
        for(Cards card : cards){
            List<CardPhotos> photos = cardPhotosFacade.findPhotosByCardID(card.getId());
            CardPhotos photo;
            if(!photos.isEmpty()){
                photo = photos.get(0);
            } else {
                photo = null;
            }
            entriesReturn.add(new Entry(card, photo));
            
        }
        setEntries(entriesReturn);
        return entriesReturn;
    }
    
    public void setEntries(List<Entry> entries){
        this.entries = entries;
    }
      /**
    * @return the user
    */
    public Users getUser() {
      return user;
    }

    public Users getLoggedInUser() {
      return user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    /**
     * @param user the user to set
     */
    public void setUser(Users user) {
      this.user = user;
    }
    
    public List<Wants> getWants(){
        return wantsFacade.findWantsByUserID(user.getId());
    }
    
    public void setWants(){
        this.wants  = wantsFacade.findWantsByUserID(user.getId());;
    }
    
    public String viewMyBinder(){
        user = getLoggedInUser();
        wants = wantsFacade.findWantsByUserID(user.getId());
        return "MyBinder";
    }

    
}