/*
 * Created by Nicholas Miller on 2016.04.12  * 
 * Copyright © 2016 Nicholas Miller. All rights reserved. * 
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author nmiller
 */
@Entity
@Table(name = "Trades")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Trades.findAll", query = "SELECT t FROM Trades t"),
    @NamedQuery(name = "Trades.findById", query = "SELECT t FROM Trades t WHERE t.id = :id"),
    @NamedQuery(name = "Trades.findByCreatorId", query = "SELECT t FROM Trades t WHERE t.creatorId = :creatorId"),
    @NamedQuery(name = "Trades.findByRecieverId", query = "SELECT t FROM Trades t WHERE t.recieverId = :recieverId"),
    @NamedQuery(name = "Trades.findByCcardsId", query = "SELECT t FROM Trades t WHERE t.ccardsId = :ccardsId"),
    @NamedQuery(name = "Trades.findByRcardsId", query = "SELECT t FROM Trades t WHERE t.rcardsId = :rcardsId"),
    @NamedQuery(name = "Trades.findByOfferDate", query = "SELECT t FROM Trades t WHERE t.offerDate = :offerDate"),
    @NamedQuery(name = "Trades.findByParentOffer", query = "SELECT t FROM Trades t WHERE t.parentOffer = :parentOffer"),
    @NamedQuery(name = "Trades.findByApproved", query = "SELECT t FROM Trades t WHERE t.approved = :approved")})
public class Trades implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "creator_id")
    private int creatorId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "reciever_id")
    private int recieverId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "ccards_id")
    private String ccardsId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "rcards_id")
    private String rcardsId;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "offer_date")
    private String offerDate;
    @Size(max = 255)
    @Column(name = "parent_offer")
    private String parentOffer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "approved")
    private boolean approved;

    public Trades() {
    }

    public Trades(Integer id) {
        this.id = id;
    }

    public Trades(Integer id, int creatorId, int recieverId, String ccardsId, String rcardsId, String offerDate, boolean approved) {
        this.id = id;
        this.creatorId = creatorId;
        this.recieverId = recieverId;
        this.ccardsId = ccardsId;
        this.rcardsId = rcardsId;
        this.offerDate = offerDate;
        this.approved = approved;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public int getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(int recieverId) {
        this.recieverId = recieverId;
    }

    public String getCcardsId() {
        return ccardsId;
    }

    public void setCcardsId(String ccardsId) {
        this.ccardsId = ccardsId;
    }

    public String getRcardsId() {
        return rcardsId;
    }

    public void setRcardsId(String rcardsId) {
        this.rcardsId = rcardsId;
    }

    public String getOfferDate() {
        return offerDate;
    }

    public void setOfferDate(String offerDate) {
        this.offerDate = offerDate;
    }

    public String getParentOffer() {
        return parentOffer;
    }

    public void setParentOffer(String parentOffer) {
        this.parentOffer = parentOffer;
    }

    public boolean getApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
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
