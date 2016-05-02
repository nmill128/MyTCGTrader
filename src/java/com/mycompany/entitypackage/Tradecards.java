/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Erik
 */
@Entity
@Table(name = "TradeCards")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tradecards.findAll", query = "SELECT t FROM Tradecards t"),
    @NamedQuery(name = "Tradecards.findByTradeId", query = "SELECT t FROM Tradecards t WHERE t.tradeID.id = :tradeId"),
    @NamedQuery(name = "Tradecards.findById", query = "SELECT t FROM Tradecards t WHERE t.id = :id")})
public class Tradecards implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "tradeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Trades tradeID;
    @JoinColumn(name = "cardID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Cards cardID;

    /**
     * Constructor 
     */
    public Tradecards() {
    }

    /**
     * constructs with id 
     * @param id
     */
    public Tradecards(Integer id) {
        this.id = id;
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
     * gets trade id 
     * @return
     */
    public Trades getTradeID() {
        return tradeID;
    }

    /**
     * sets trade id 
     * @param tradeID
     */
    public void setTradeID(Trades tradeID) {
        this.tradeID = tradeID;
    }
    
    /**
     * gets card id
     * @return
     */
    public Cards getCardID() {
        return cardID;
    }

    /**
     * sets card id
     * @param cardID
     */
    public void setCardID(Cards cardID) {
        this.cardID = cardID;
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
        if (!(object instanceof Tradecards)) {
            return false;
        }
        Tradecards other = (Tradecards) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.Tradecards[ id=" + id + " ]";
    }
    
}
