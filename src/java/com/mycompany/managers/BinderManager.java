/*
 * Created By Erik Yeomans 4/15/2016
    MyTCGTrader
    The Bindermanager is the managing file for anything MyBinder, other peoples
    binders, or offers.  It also includes tools that are used in other files
    like getters for card photos etc.

    Displaying cards and wants on the binder pages, creating. displaying and 
    removing offers on the offer pages.  Creating and viewing comments on offer
    pages.
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

/**
 *
 * @author Erik
 */
@Named(value = "binderManager")
@SessionScoped
/**
 * The binder Manager class
 *
 * @author Erik
 */
public class BinderManager implements Serializable {

    //private variables
    private Users user;                         //current/logged on user
    private List<Wants> wants;                  //list of users wants
    private List<Entry> entries;                //list of users entries(cards and photos)
    private int[] checks;                       //values of checked cards in offers
    private int[] otherChecks;                  //values of checked cards in offers of the other users cards
    private String offerUser;                   //other user an offer deals with
    private List<Trades> currOffers;            //list current offers for display
    private List<Trades> pastOffers;            //list of past offers for display
    private Trades currentOffer;                //The current offer for viewing
    private List<Tradecomments> comments;       //List of comments for current offer
    private String commentMessage;              //Message to pass with new comment

    private Map<String, Object> checksValue;    //Map for creating select many cards
    private Map<String, Object> otherChecksValue; //Map for creating other users select many cards

    /**
     * The Entry helper class
     * Designed to hold both a card and card photo to easily send a list of
     * entries to iterate over on the front end
     *
     * @author Erik
     */
    public class Entry implements Serializable {

        //private variables
        private CardPhotos photo;
        private Cards card;

        /**
         *
         * @param card
         * @param photo
         */
        public Entry(Cards card, CardPhotos photo) {
            this.card = card;
            this.photo = photo;
        }

    //getter and setters for private variables

        /**
         *
         * @return
         */
        public Cards getCard() {
            return card;
        }

        /**
         *
         * @param card
         */
        public void setCard(Cards card) {
            this.card = card;
        }

        /**
         *
         * @return
         */
        public CardPhotos getPhoto() {
            return photo;
        }

        /**
         *
         * @param photo
         */
        public void setPhoto(CardPhotos photo) {
            this.photo = photo;
        }

        //Get file method to return photo filename for this card

        /**
         * returns the name of the file for the photo in the entity 
         * @return
         */
        public String getFile() {
            if (photo == null) {
                return "/DefaultUserPhoto.png";
            } else {
                return photo.getFilename();
            }
        }
    }

    /**
     * All of the Facade instance variables are annotated with the @EJB
     * annotation. This means that the GlassFish application server, at runtime,
     * will inject in this instance variable a reference to the @Stateless
     * session bean UserFacade.
     */
    @EJB
    private com.mycompany.sessionBeanPackage.UsersFacade userFacade;

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



    /**
     * constructor
     */
    public BinderManager() {

    }

    //getter and setter for entries

    /**
     * getter and setter for entries
     * @return
     */
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

    /**
     * sets entries
     * @param entries
     */
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


    /**
     *returns the logged in user and sets the user
     * @return
     */
    public Users getLoggedInUser() {
        return user = userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id"));
    }

    //getter and setter for comments
    //getter fetches comments from the database

    /**
     *
     * getter and setter for comments
     * getter fetches comments from the database
     * @return list tc
     */
    public List<Tradecomments> getComments() {
        setComments(tradecommentsFacade.findCommentsByTradeId(this.currentOffer.getId()));
        return this.comments;
    }

    /**
     * sets comments
     * @param comments
     */
    public void setComments(List<Tradecomments> comments) {
        this.comments = comments;
    }


    /**
     * getter and setter for comment message, used in creating new comments
     * @return
     */
    public String getCommentMessage() {
        return this.commentMessage;
    }

    /**
     *
     * @param cm
     */
    public void setCommentMessage(String cm) {
        this.commentMessage = cm;
    }
    

    /**
     * create new comment
     * @return
     */
    public String createComment() {
        Tradecomments tc = new Tradecomments();
        tc.setCreatorId(getLoggedInUser());
        tc.setCreateDate(new Date());
        tc.setString(getCommentMessage());
        tc.setTradeID(this.currentOffer);
        tradecommentsFacade.create(tc);
        return "CurrentOffer";
    }

    //getter and setter for current and past offers
    //getters fetch the current/past offers from the database

    /**
     *
     * getter and setter for current and past offers
     * getters fetch the current/past offers from the database
     * @return
     */
    public List<Trades> getCurrOffers() {
        setCurrOffers(tradesFacade.findCurrTradesByUserId(getLoggedInUser().getId()));
        return this.currOffers;
    }

    /**
     *
     * @param offers
     */
    public void setCurrOffers(List<Trades> offers) {
        this.currOffers = offers;
    }

    /**
     *
     * @return
     */
    public List<Trades> getPastOffers() {
        setPastOffers(tradesFacade.findPastTradesByUserId(getLoggedInUser().getId()));
        return this.pastOffers;
    }

    /**
     *
     * @param offers
     */
    public void setPastOffers(List<Trades> offers) {
        this.pastOffers = offers;
    }

    //getter and setter for current offer

    /**
     *
     * @return
     */
    public Trades getCurrentOffer() {
        return currentOffer;
    }


    /**
     * setter prepares the select many maps with cards to be displayed by fetching them from the database
     * @param t
     */
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


    /**
     * prepares the page to view a current offer **redirects
     * @param id
     * @return
     */
    public String viewCurrentOffer(Integer id) {
        Trades trade = tradesFacade.find(id);
        setCurrentOffer(trade);
        return "CurrentOffer";
    }


    /**
     * getter and setters for offer user
     * @return
     */
    public String getOfferUser() {
        return offerUser;
    }

    /**
     *
     * @param u
     */
    public void setOfferUser(String u) {
        this.offerUser = u;
    }


    /**
     * **redirect, using the offer user, prepares the next page to create an offer
     * @return
     */
    public String populateOfferUser() {
        Users oUser = (Users) userFacade.findByUsername(this.offerUser);
        if (oUser != null && !oUser.getId().equals(user.getId())) {
            return "CreateOfferUser";
        }
        return "CreateOffer";
    }


    /**
     *  prepares to create an offer with this user
     * @return
     */
    public String tradeWith() {
        this.offerUser = user.getUsername();
        populateOfferUser();
        return "CreateOfferUser";
    }


    /**
     * getters for checks/otherchecks value.  prepares the map, fetches the cards
     * @return
     */
    public Map<String, Object> getChecksValue() {
        checksValue = new LinkedHashMap();
        List<Cards> cards = cardsFacade.findCardsByUserID(getLoggedInUser().getId());
        for (Cards card : cards) {
            checksValue.put(card.getCardName(), card.getId()); //label, value
        }
        return checksValue;
    }

    /**
     *
     * @return
     */
    public Map<String, Object> getOtherChecksValue() {
        otherChecksValue = new LinkedHashMap();
        Users oUser = (Users) userFacade.findByUsername(this.offerUser);
        List<Cards> cards = cardsFacade.findCardsByUserID(oUser.getId());
        for (Cards card : cards) {
            otherChecksValue.put(card.getCardName(), card.getId()); //label, value
        }
        return otherChecksValue;
    }

 
    /**
     * creates the offer being made
     * pulls the values from checks and creates a new trade in the database
     * the creates the appropriote tradecards for the many to one relationship
     * **sends email 
     * @return redirect
     */
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

    /**
     * accepts an offer thats currently available
     * sends an email
     * @return
     */
    public String acceptOffer() {
        this.currentOffer.setApproved(true);
        tradesFacade.edit(currentOffer);
        String message = "Your trade offer has been accepted by " + currentOffer.getRecieverId().getUsername() + ". Please log on to MyTCGTrader to view the details of this offer and discuss the logistics of the trade via comments.";
        String subject = currentOffer.getRecieverId().getUsername() + " ACCEPTED your offer";
        sendMail(subject, message, currentOffer.getCreatorId().getEmail());
        return "CurrentOffer";
    }



    /**
     * moves an offer to completed, the offer will no go to past offers
     * @return
     */
    public String completeOffer() {
        this.currentOffer.setCompleted(true);
        tradesFacade.edit(currentOffer);
        return "CurrentOffer";
    }

    //**redirects to createoffer
    //cancels the currentoffer by removing it from the db
    //also recursivly removes all parent offers
    //**sends email

    /**
     *
     * @return
     */
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

    //recursive offer cancel helper
    //deletes offer sent from db and recursivly deletes its parent offer

    /**
     *
     * @param offer
     */
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

    //**redirect, prepares a counter offer by preparing the checked cards from
    //the last offer with tradecards from the database, then sets the select many
    //values to it

    /**
     *
     * @return
     */
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

    //**redirect, actually creates the counter offer into the database
    //very similar to creating a normal offer
    //**sends email

    /**
     *
     * @return
     */
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

    //getter and setters for checks and other checks

    /**
     *
     * @return
     */
    public int[] getChecks() {
        return checks;
    }

    /**
     *
     * @param checks
     */
    public void setChecks(int[] checks) {
        this.checks = checks;
    }

    /**
     *
     * @return
     */
    public int[] getOtherChecks() {
        return otherChecks;
    }

    /**
     *
     * @param checks
     */
    public void setOtherChecks(int[] checks) {
        this.otherChecks = checks;
    }

    /**
     * @param user the user to set
     */
    public void setUser(Users user) {
        this.user = user;
    }

    //getter and setters for wants
    //getter fetches wants from the db

    /**
     *
     * @return
     */
    public List<Wants> getWants() {
        return wantsFacade.findWantsByUserID(user.getId());
    }

    /**
     *
     */
    public void setWants() {
        this.wants = wantsFacade.findWantsByUserID(user.getId());;
    }

    //**redirect, prepares to view my binder

    /**
     *
     * @return
     */
    public String viewMyBinder() {
        user = getLoggedInUser();
        wants = wantsFacade.findWantsByUserID(user.getId());
        return "MyBinder";
    }

    //Send mail helper function, hard coded with our smtp information
    //takes in subject, message and to for the email

    /**
     *
     * @param subject
     * @param message
     * @param to
     * @return
     */
    public int sendMail(String subject, String message, String to) {
        try {
            if (false) {
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
            }
            return 0;

        } catch (Exception ex) {
            ex.printStackTrace();
            System.out.println("Exception " + ex);
            return -1;
        }
    }

// Also include an inner class that is used for smtp authentication purposes
    private class SMTPAuthenticator extends javax.mail.Authenticator {

        @Override
        public PasswordAuthentication getPasswordAuthentication() {
            String username = "mytcgtrader@gmail.com";           
            String password = "csd@VT(S16)";                            
            return new PasswordAuthentication(username, password);
        }
    }

    //**Redirect, takes us to a users binder

    /**
     *
     * @param user
     * @return
     */
    public String viewUserBinder(Users user) {
        if(user.getId().equals(this.getLoggedInUser().getId())){
            return "MyBinder";
        }
        this.user = user;
        return "OtherBinder";
    }

    //Helper for getting the file name of a user photo

    /**
     *
     * @param u
     * @return
     */
    public String getUserPhotoFileName(Users u) {
        List<UserPhotos> userPhotos = this.userPhotosFacade.findPhotosByUserID(u.getId());
        if (!userPhotos.isEmpty()) {
            return userPhotos.get(0).getThumbnailName();
        }
        return "userPhotos/defaultUserPhoto.png";

    }

    //helper for getting the file name of a cards photo

    /**
     *
     * @param c
     * @return
     */
    public String getCardPhotoFileName(Cards c) {
        CardPhotos cp = this.cardPhotosFacade.findPhotosByCardID(c.getId()).get(0);
        return cp.getThumbnailName();
    }
}
