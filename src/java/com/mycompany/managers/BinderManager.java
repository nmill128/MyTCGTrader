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
import com.mycompany.entitypackage.Trades;
import com.mycompany.entitypackage.Tradecards;
import com.mycompany.jsfpackage.util.JsfUtil;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;
import java.util.Date;
import java.util.ResourceBundle;

@Named(value = "binderManager")
@SessionScoped
/**
 *
 * @author Erik
 */
public class BinderManager implements Serializable {

    private Users user;
    private List<Wants> wants;
    private List<Entry> entries;
    private int[] checks;
    private int[] otherChecks;
    private String offerUser;
    private List<Trades> currOffers;
    private Trades currentOffer;

    private Map<String, Object> checksValue;
    private Map<String, Object> otherChecksValue;

    public List<Trades> getCurrOffers(){
        setCurrOffers(tradesFacade.findTradesByUserId(getLoggedInUser().getId()));
        return this.currOffers;
    }
    
    public void setCurrOffers(List<Trades> offers){
        this.currOffers = offers;
    }
    
    public Trades getCurrentOffer() {
        return currentOffer;
    }

    public void setCurrentOffer(Trades t) {
        this.currentOffer = t;
        List<Tradecards> tradecards = tradecardsFacade.findTradecardsByTradeId(this.currentOffer.getId());
        List<Cards> userCards = new ArrayList();
        List<Cards> otherCards = new ArrayList();
        if(this.currentOffer.getCreatorId().getId().equals(user.getId())){
            setOfferUser(userFacade.find(this.currentOffer.getRecieverId().getId()).getUsername());
        } else {
            setOfferUser(userFacade.find(this.currentOffer.getCreatorId().getId()).getUsername());            
        }
        for(Tradecards tc : tradecards){
            Cards card = cardsFacade.find(tc.getCardID().getId());
            if(card.getUserId().getId().equals(user.getId())){
                userCards.add(card);
            } else {
                otherCards.add(card);
            }
        }
        this.currentOffer.setUserCards(userCards);
        this.currentOffer.setOtherCards(otherCards);       
    }
    
    public String viewCurrentOffer(Integer id){
        Trades trade = tradesFacade.find(id);
        setCurrentOffer(trade);
        return "CurrentOffer";
    }
    
    public String getOfferUser() {
        return offerUser;
    }

    public void setOfferUser(String u) {
        this.offerUser = u;
    }

    public String populateOfferUser() {
        Users oUser = (Users) userFacade.findByUsername(this.offerUser);
        if (oUser != null && !oUser.getId().equals(user.getId())) {
            return "CreateOfferUser";
        }
        return "CreateOffer";
    }

    public Map<String, Object> getChecksValue() {
        checksValue = new LinkedHashMap();
        List<Cards> cards = cardsFacade.findCardsByUserID(getLoggedInUser().getId());
        for (Cards card : cards) {
            checksValue.put(card.getCardName(), card.getId()); //label, value
        }
        return checksValue;
    }

    public Map<String, Object> getOtherChecksValue() {
        otherChecksValue = new LinkedHashMap();
        Users oUser = (Users) userFacade.findByUsername(this.offerUser);
        List<Cards> cards = cardsFacade.findCardsByUserID(oUser.getId());
        for (Cards card : cards) {
            otherChecksValue.put(card.getCardName(), card.getId()); //label, value
        }
        return otherChecksValue;
    }

    public String submitOffer() {
        System.out.println(Arrays.toString(checks));
        System.out.println(Arrays.toString(otherChecks));
        if (checks.length == 0 || otherChecks.length == 0) {
            return "CreateOfferUser";
        }
        Date now = new Date();
        Trades newTrade = new Trades();
        newTrade.setApproved(false);
        newTrade.setCompleted(false);
        newTrade.setParentOfferId(null);
        newTrade.setOfferTimestamp(now);
        newTrade.setCreatorId(getLoggedInUser());
        newTrade.setRecieverId(userFacade.findByUsername(offerUser));
        try {
            tradesFacade.create(newTrade);
            Trades tradeId = tradesFacade.findByDate(newTrade.getOfferTimestamp());
            for(int cardId : checks){
                Tradecards tc = new Tradecards();
                tc.setTradeID(tradeId);
                tc.setCardID(cardsFacade.find(cardId));
                tradecardsFacade.create(tc);
            }
            for(int cardId : otherChecks){
                Tradecards tc = new Tradecards();
                tc.setTradeID(tradeId);
                tc.setCardID(cardsFacade.find(cardId));
                tradecardsFacade.create(tc);
            }
            return "CreateOfferUser";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }
    
    public String acceptOffer(){
        this.currentOffer.setApproved(true);
        tradesFacade.edit(currentOffer);
        return "CurrentOffer";
    }

    public class Entry implements Serializable {

        private CardPhotos photo;
        private Cards card;

        public Entry(Cards card, CardPhotos photo) {
            this.card = card;
            this.photo = photo;
        }

        public Cards getCard() {
            return card;
        }

        public void setCard(Cards card) {
            this.card = card;
        }

        public CardPhotos getPhoto() {
            return photo;
        }

        public void setPhoto(CardPhotos photo) {
            this.photo = photo;
        }

        public String getFile() {
            if (photo == null) {
                return "/DefaultUserPhoto.png";
            } else {
                return photo.getFilename();
            }
        }
    }

    public int[] getChecks() {
        return checks;
    }

    public void setChecks(int[] checks) {
        this.checks = checks;
    }

    public int[] getOtherChecks() {
        return otherChecks;
    }

    public void setOtherChecks(int[] checks) {
        this.otherChecks = checks;
    }

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private com.mycompany.sessionBeanPackage.UsersFacade userFacade;

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private com.mycompany.sessionBeanPackage.WantsFacade wantsFacade;

    @EJB
    private com.mycompany.sessionBeanPackage.CardsFacade cardsFacade;

    @EJB
    private com.mycompany.sessionBeanPackage.CardPhotosFacade cardPhotosFacade;
    @EJB
    private com.mycompany.sessionBeanPackage.TradesFacade tradesFacade;
        @EJB
    private com.mycompany.sessionBeanPackage.TradecardsFacade tradecardsFacade;

    public BinderManager() {

    }

    public List<Entry> getEntries() {
        List<Cards> cards = cardsFacade.findCardsByUserID(getLoggedInUser().getId());
        List<Entry> entriesReturn = new ArrayList(0);
        for (Cards card : cards) {
            List<CardPhotos> photos = cardPhotosFacade.findPhotosByCardID(card.getId());
            CardPhotos photo;
            if (!photos.isEmpty()) {
                photo = photos.get(0);
            } else {
                photo = null;
            }
            entriesReturn.add(new Entry(card, photo));

        }
        setEntries(entriesReturn);
        return entriesReturn;
    }

    public void setEntries(List<Entry> entries) {
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

    public List<Wants> getWants() {
        return wantsFacade.findWantsByUserID(user.getId());
    }

    public void setWants() {
        this.wants = wantsFacade.findWantsByUserID(user.getId());;
    }

    public String viewMyBinder() {
        user = getLoggedInUser();
        wants = wantsFacade.findWantsByUserID(user.getId());
        return "MyBinder";
    }

}
