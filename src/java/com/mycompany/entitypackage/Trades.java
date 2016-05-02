/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */

package com.mycompany.entitypackage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * generated entity class for Trades
 * @author Erik
 */
@Entity
@Table(name = "Trades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trades.findAll", query = "SELECT t FROM Trades t"),
    @NamedQuery(name = "Trades.findById", query = "SELECT t FROM Trades t WHERE t.id = :id"),
    @NamedQuery(name = "Trades.findByOfferTimestamp", query = "SELECT t FROM Trades t WHERE t.offerTimestamp = :offerTimestamp"),
    @NamedQuery(name = "Trades.findByApproved", query = "SELECT t FROM Trades t WHERE t.approved = :approved"),
    @NamedQuery(name = "Trades.findCurrTradesByUserId", query = "SELECT t FROM Trades t WHERE (t.creatorId.id = :creatorId OR t.recieverId.id = :recieverId) AND t.completed=false"),
    @NamedQuery(name = "Trades.findPastTradesByUserId", query = "SELECT t FROM Trades t WHERE (t.creatorId.id = :creatorId OR t.recieverId.id = :recieverId) AND t.completed=true"),
    @NamedQuery(name = "Trades.findByCompleted", query = "SELECT t FROM Trades t WHERE t.completed = :completed")})
public class Trades implements Serializable {
    //private variables and their db counter parts
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "offer_timestamp")
    @Temporal(TemporalType.TIMESTAMP)
    private Date offerTimestamp;
    @Basic(optional = false)
    @NotNull
    @Column(name = "approved")
    private boolean approved;
    @Basic(optional = false)
    @NotNull
    @Column(name = "completed")
    private boolean completed;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "tradeID")
    private Collection<Tradecomments> tradecommentsCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "parentOfferId")
    private Collection<Trades> tradesCollection;
    @JoinColumn(name = "parent_offer_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Trades parentOfferId;
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users creatorId;
    @JoinColumn(name = "reciever_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users recieverId;
    @Transient 
    private List<Cards> userCards;
    @Transient 
    private List<Cards> otherCards;

    /**
     * Constructor
     */
    public Trades() {
    }

    /**
     * Constructs with id
     * @param id
     */
    public Trades(Integer id) {
        this.id = id;
    }

    /**
     * constructs with params
     * @param id
     * @param offerTimestamp
     * @param approved
     * @param completed
     */
    public Trades(Integer id, Date offerTimestamp, boolean approved, boolean completed) {
        this.id = id;
        this.offerTimestamp = offerTimestamp;
        this.approved = approved;
        this.completed = completed;
    }

    /**
     * gets id
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * sets id
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * gets offer time stamp
     * @return
     */
    public Date getOfferTimestamp() {
        return offerTimestamp;
    }

    /**
     * sets offer time stamp
     * also rounds up the miliseconds because mysql also does that
     * @param offerTimestamp
     */
    public void setOfferTimestamp(Date offerTimestamp) {
        long time = offerTimestamp.getTime();
        this.offerTimestamp = new Date(1000 * (time/ 1000));
    }

    /**
     * gets wether approved or not
     * @return
     */
    public boolean getApproved() {
        return approved;
    }

    /**
     * sets approved
     * @param approved
     */
    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    /**
     * gets wether completed or not
     * @return
     */
    public boolean getCompleted() {
        return completed;
    }

    /**
     * sets completed 
     * @param completed
     */
    public void setCompleted(boolean completed) {
        this.completed = completed;
    }
    
    /**
     * gets list of tradecomments
     * @return
     */
    @XmlTransient
    public Collection<Tradecomments> getTradecommentsCollection() {
        return tradecommentsCollection;
    }

    /**
     * sets trade comments
     * @param tradecommentsCollection
     */
    public void setTradecommentsCollection(Collection<Tradecomments> tradecommentsCollection) {
        this.tradecommentsCollection = tradecommentsCollection;
    }

    /**
     * gets trade collection
     * @return
     */
    @XmlTransient
    public Collection<Trades> getTradesCollection() {
        return tradesCollection;
    }

    /**
     * sets trade collection
     * @param tradesCollection
     */
    public void setTradesCollection(Collection<Trades> tradesCollection) {
        this.tradesCollection = tradesCollection;
    }

    /**
     * gets parent offer id
     * @return
     */
    public Trades getParentOfferId() {
        return parentOfferId;
    }

    /**
     * sets parent offer id
     * @param parentOfferId
     */
    public void setParentOfferId(Trades parentOfferId) {
        this.parentOfferId = parentOfferId;
    }
    
    /**
     * get creator id 
     * @return
     */
    public Users getCreatorId() {
        return creatorId;
    }

    /**
     * sets creator id 
     * @param creatorId
     */
    public void setCreatorId(Users creatorId) {
        this.creatorId = creatorId;
    }
    
    /**
     * gets reciever id
     * @return
     */
    public Users getRecieverId() {
        return recieverId;
    }

    /**
     * sets reciever id 
     * @param recieverId
     */
    public void setRecieverId(Users recieverId) {
        this.recieverId = recieverId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Trades)) {
            return false;
        }
        Trades other = (Trades) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.Trades[ id=" + id + " ]";
    }
    
    /**
     * gets user cards
     * @return
     */
    public List<Cards> getUserCards(){
        return this.userCards;
    }
    
    /**
     * sets user cards
     * @param cards
     */
    public void setUserCards(List<Cards> cards){
        this.userCards = cards;
    }
    
    /**
     * gets other user cards
     * @return
     */
    public List<Cards> getOtherCards(){
        return this.otherCards;
    }
    
    /**
     * sets other user cards
     * @param cards
     */
    public void setOtherCards(List<Cards> cards){
        this.otherCards = cards;
    }
    
}
