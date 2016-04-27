/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Erik
 */
@Entity
@Table(name = "trades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trades.findAll", query = "SELECT t FROM Trades t"),
    @NamedQuery(name = "Trades.findById", query = "SELECT t FROM Trades t WHERE t.id = :id"),
    @NamedQuery(name = "Trades.findByOfferTimestamp", query = "SELECT t FROM Trades t WHERE t.offerTimestamp = :offerTimestamp"),
    @NamedQuery(name = "Trades.findByApproved", query = "SELECT t FROM Trades t WHERE t.approved = :approved"),
    @NamedQuery(name = "Trades.findTradesByUserId", query = "SELECT t FROM Trades t WHERE t.creatorId.id = :creatorId OR t.recieverId.id = :recieverId"),
    @NamedQuery(name = "Trades.findByCompleted", query = "SELECT t FROM Trades t WHERE t.completed = :completed")})
public class Trades implements Serializable {

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

    public Trades() {
    }

    public Trades(Integer id) {
        this.id = id;
    }

    public Trades(Integer id, Date offerTimestamp, boolean approved, boolean completed) {
        this.id = id;
        this.offerTimestamp = offerTimestamp;
        this.approved = approved;
        this.completed = completed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Date getOfferTimestamp() {
        return offerTimestamp;
    }

    public void setOfferTimestamp(Date offerTimestamp) {
        long time = offerTimestamp.getTime();
        this.offerTimestamp = new Date(1000 * (time/ 1000));
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }

    public boolean getCompleted() {
        return completed;
    }

    public void setCompleted(boolean completed) {
        this.completed = completed;
    }

    @XmlTransient
    public Collection<Tradecomments> getTradecommentsCollection() {
        return tradecommentsCollection;
    }

    public void setTradecommentsCollection(Collection<Tradecomments> tradecommentsCollection) {
        this.tradecommentsCollection = tradecommentsCollection;
    }

    @XmlTransient
    public Collection<Trades> getTradesCollection() {
        return tradesCollection;
    }

    public void setTradesCollection(Collection<Trades> tradesCollection) {
        this.tradesCollection = tradesCollection;
    }

    public Trades getParentOfferId() {
        return parentOfferId;
    }

    public void setParentOfferId(Trades parentOfferId) {
        this.parentOfferId = parentOfferId;
    }
    
    public Users getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Users creatorId) {
        this.creatorId = creatorId;
    }
    
    public Users getRecieverId() {
        return recieverId;
    }

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
    
}
