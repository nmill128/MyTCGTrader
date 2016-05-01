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
import com.mycompany.entitypackage.UserPhotos;
import com.mycompany.entitypackage.Trades;
import com.mycompany.entitypackage.Tradecards;
import com.mycompany.entitypackage.Tradecomments;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import java.util.List;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Date;
import javax.mail.*;
import javax.mail.internet.*;
import java.util.*;

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
    private List<Trades> pastOffers;
    private Trades currentOffer;
    private List<Tradecomments> comments;
    private String commentMessage;

    private Map<String, Object> checksValue;
    private Map<String, Object> otherChecksValue;

    public List<Tradecomments> getComments() {
        setComments(tradecommentsFacade.findCommentsByTradeId(this.currentOffer.getId()));
        return this.comments;
    }

    public void setComments(List<Tradecomments> comments) {
        this.comments = comments;
    }

    public String getCommentMessage() {
        return this.commentMessage;
    }

    public void setCommentMessage(String cm) {
        this.commentMessage = cm;
    }

    public String createComment() {
        Tradecomments tc = new Tradecomments();
        tc.setCreatorId(getLoggedInUser());
        tc.setCreateDate(new Date());
        tc.setString(getCommentMessage());
        tc.setTradeID(this.currentOffer);
        tradecommentsFacade.create(tc);
        return "CurrentOffer";
    }

    public List<Trades> getCurrOffers() {
        setCurrOffers(tradesFacade.findCurrTradesByUserId(getLoggedInUser().getId()));
        return this.currOffers;
    }

    public void setCurrOffers(List<Trades> offers) {
        this.currOffers = offers;
    }

    public List<Trades> getPastOffers() {
        setPastOffers(tradesFacade.findPastTradesByUserId(getLoggedInUser().getId()));
        return this.pastOffers;
    }

    public void setPastOffers(List<Trades> offers) {
        this.pastOffers = offers;
    }

    public Trades getCurrentOffer() {
        return currentOffer;
    }

    public void setCurrentOffer(Trades t) {
        this.currentOffer = t;
        List<Tradecards> tradecards = tradecardsFacade.findTradecardsByTradeId(this.currentOffer.getId());
        List<Cards> userCards = new ArrayList();
        List<Cards> otherCards = new ArrayList();
        if (this.currentOffer.getCreatorId().getId().equals(user.getId())) {
            setOfferUser(userFacade.find(this.currentOffer.getRecieverId().getId()).getUsername());
        } else {
            setOfferUser(userFacade.find(this.currentOffer.getCreatorId().getId()).getUsername());
        }
        for (Tradecards tc : tradecards) {
            Cards card = cardsFacade.find(tc.getCardID().getId());
            if (card.getUserId().getId().equals(user.getId())) {
                userCards.add(card);
            } else {
                otherCards.add(card);
            }
        }
        this.currentOffer.setUserCards(userCards);
        this.currentOffer.setOtherCards(otherCards);
    }

    public String viewCurrentOffer(Integer id) {
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

    public String tradeWith() {
        this.offerUser = user.getUsername();
        populateOfferUser();
        return "CreateOfferUser";
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
            for (int cardId : checks) {
                Tradecards tc = new Tradecards();
                tc.setTradeID(tradeId);
                tc.setCardID(cardsFacade.find(cardId));
                tradecardsFacade.create(tc);
            }
            for (int cardId : otherChecks) {
                Tradecards tc = new Tradecards();
                tc.setTradeID(tradeId);
                tc.setCardID(cardsFacade.find(cardId));
                tradecardsFacade.create(tc);
            }
            String message = "A new Trade offer has been submitted from " + tradeId.getCreatorId().getUsername() + ". Please log on to MyTCGTrader to view the details of this offer.";
            String subject = "New Trade Offer from " + tradeId.getCreatorId().getUsername();
            sendMail(subject, message, tradeId.getRecieverId().getEmail());
            return "CreateOfferUser";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }

    }

    public String acceptOffer() {
        this.currentOffer.setApproved(true);
        tradesFacade.edit(currentOffer);
        String message = "Your trade offer has been accepted by " + currentOffer.getRecieverId().getUsername() + ". Please log on to MyTCGTrader to view the details of this offer and discuss the logistics of the trade via comments.";
        String subject = currentOffer.getRecieverId().getUsername() + " ACCEPTED your offer";
        sendMail(subject, message, currentOffer.getCreatorId().getEmail());
        return "CurrentOffer";
    }

    public String completeOffer() {
        this.currentOffer.setCompleted(true);
        tradesFacade.edit(currentOffer);
        return "CurrentOffer";
    }

    public String cancelOffer() {
        List<Tradecards> tradecards = tradecardsFacade.findTradecardsByTradeId(this.currentOffer.getId());
        for (Tradecards tc : tradecards) {
            tradecardsFacade.remove(tc);
        }
        String message = "Your trade offer has been canceled by " + currentOffer.getCreatorId().getUsername() + ". The offer no longer exists.";
        String subject = currentOffer.getCreatorId().getUsername() + "CANCELED thier offer";
        sendMail(subject, message, currentOffer.getRecieverId().getEmail());
        tradesFacade.remove(currentOffer);
        cancelOffer(this.currentOffer.getParentOfferId());
        currentOffer = null;
        return "CreateOffer";
    }

    public void cancelOffer(Trades offer) {
        if (offer != null) {
            List<Tradecards> tradecards = tradecardsFacade.findTradecardsByTradeId(offer.getId());
            for (Tradecards tc : tradecards) {
                tradecardsFacade.remove(tc);
            }
            tradesFacade.remove(offer);
            cancelOffer(offer.getParentOfferId());
        }
    }

    public String createCounterOffer() {
        if (this.currentOffer.getCreatorId().getId().equals(getLoggedInUser().getId())) {
            this.offerUser = this.currentOffer.getRecieverId().getUsername();
        } else {
            this.offerUser = this.currentOffer.getCreatorId().getUsername();
        }
        List<Tradecards> tradecards = tradecardsFacade.findTradecardsByTradeId(this.currentOffer.getId());
        int uCounter = 0;
        int oCounter = 0;
        for (Tradecards tc : tradecards) {
            if (tc.getCardID().getUserId().getId().equals(getLoggedInUser().getId())) {
                uCounter++;
            } else {
                oCounter++;
            }
        }
        int[] uChecks = new int[uCounter];
        int[] oChecks = new int[oCounter];
        uCounter = 0;
        oCounter = 0;
        for (Tradecards tc : tradecards) {
            if (tc.getCardID().getUserId().getId().equals(getLoggedInUser().getId())) {
                uChecks[uCounter] = tc.getCardID().getId();
                uCounter++;
            } else {
                oChecks[oCounter] = tc.getCardID().getId();
                oCounter++;
            }
        }
        setChecks(uChecks);
        setOtherChecks(oChecks);
        return "CounterOffer";
    }

    public String submitCounterOffer() {
        if (checks.length == 0 || otherChecks.length == 0) {
            return "CreateOfferUser";
        }
        Date now = new Date();
        Trades newTrade = new Trades();
        newTrade.setApproved(false);
        newTrade.setCompleted(false);
        newTrade.setParentOfferId(this.currentOffer);
        newTrade.setOfferTimestamp(now);
        newTrade.setCreatorId(getLoggedInUser());
        newTrade.setRecieverId(userFacade.findByUsername(offerUser));
        try {
            tradesFacade.create(newTrade);
            Trades tradeId = tradesFacade.findByDate(newTrade.getOfferTimestamp());
            for (int cardId : checks) {
                Tradecards tc = new Tradecards();
                tc.setTradeID(tradeId);
                tc.setCardID(cardsFacade.find(cardId));
                tradecardsFacade.create(tc);
            }
            for (int cardId : otherChecks) {
                Tradecards tc = new Tradecards();
                tc.setTradeID(tradeId);
                tc.setCardID(cardsFacade.find(cardId));
                tradecardsFacade.create(tc);
            }
            this.setCurrentOffer(tradeId);

            String message = "Your trade offer has been reviewed by " + currentOffer.getCreatorId().getUsername() + "and a counter offer has been submitted.  Please log on to MyTCGTrader to view the details of this new offer.";
            String subject = currentOffer.getCreatorId().getUsername() + "created a counter offer";
            sendMail(subject, message, currentOffer.getRecieverId().getEmail());
            return "CurrentOffer";
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
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
    private com.mycompany.sessionBeanPackage.UserPhotosFacade userPhotosFacade;
    @EJB
    private com.mycompany.sessionBeanPackage.TradesFacade tradesFacade;
    @EJB
    private com.mycompany.sessionBeanPackage.TradecardsFacade tradecardsFacade;
    @EJB
    private com.mycompany.sessionBeanPackage.TradecommentsFacade tradecommentsFacade;

    public BinderManager() {

    }

    public List<Entry> getEntries() {
        List<Cards> cards = cardsFacade.findCardsByUserID(getUser().getId());
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
        if (user == null) {
            this.user = getLoggedInUser();
        }
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

    public int sendMail(String subject, String message, String to) {
        try {
            Properties props = System.getProperties();
            // -- Attaching to default Session, or we could start a new one --
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.starttls.enable", "true");
            props.put("mail.smtp.host", "smtp.gmail.com");
            props.put("mail.smtp.auth", "true");
            props.put("mail.smtp.port", "587");
            Authenticator auth = new SMTPAuthenticator();
            Session session = Session.getInstance(props, auth);
            // -- Create a new message --
            Message msg = new MimeMessage(session);
            // -- Set the FROM and TO fields --
            msg.setFrom(new InternetAddress("mytcgtrader@gmail.com"));
            msg.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to, false));
            msg.setSubject(subject);
            msg.setText(message);
            // -- Set some other header information --
            msg.setHeader("MyMail", "Mr. XYZ");
            msg.setSentDate(new Date());
            // -- Send the message --
            Transport.send(msg);
            System.out.println("Message sent to" + to + " OK.");

            return 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception " + ex);
            return -1;
        }
    }

// Also include an inner class that is used for authentication purposes
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "mytcgtrader@gmail.com";           // specify your email id here (sender's email id)
            String password = "csd@VT(S16)";                                      // specify your password here
            return new PasswordAuthentication(username, password);
        }
    }

    public String viewUserBinder(Users user) {
        this.user = user;
        return "OtherBinder";
    }

    public String getUserPhotoFileName(Users u) {
        List<UserPhotos> userPhotos = this.userPhotosFacade.findPhotosByUserID(u.getId());
        if (!userPhotos.isEmpty()) {
            return userPhotos.get(0).getThumbnailName();
        }
        return "userPhotos/defaultUserPhoto.png";

    }

    public String getCardPhotoFileName(Cards c) {
        CardPhotos cp = this.cardPhotosFacade.findPhotosByCardID(c.getId()).get(0);
        return cp.getThumbnailName();
    }
}
