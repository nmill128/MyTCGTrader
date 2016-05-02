/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import java.util.Date;
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
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Generated entity class for TradeComments
 * @author Erik
 */
@Entity
@Table(name = "TradeComments")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Tradecomments.findAll", query = "SELECT t FROM Tradecomments t"),
    @NamedQuery(name = "Tradecomments.findById", query = "SELECT t FROM Tradecomments t WHERE t.id = :id"),
    @NamedQuery(name = "Tradecomments.findByString", query = "SELECT t FROM Tradecomments t WHERE t.string = :string"),
    @NamedQuery(name = "Tradecomments.findCommentsByTradeId", query = "SELECT t FROM Tradecomments t WHERE t.tradeID.id = :tradeID"),
    @NamedQuery(name = "Tradecomments.findByCreateDate", query = "SELECT t FROM Tradecomments t WHERE t.createDate = :createDate")})
public class Tradecomments implements Serializable {
    //private variables and their db counter parts
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "string")
    private String string;
    @Basic(optional = false)
    @NotNull
    @Column(name = "create_date")
    @Temporal(TemporalType.DATE)
    private Date createDate;
    @JoinColumn(name = "tradeID", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Trades tradeID;
    @JoinColumn(name = "creator_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Users creatorId;

    /**
     * Constructor
     */
    public Tradecomments() {
    }

    /**
     * Constructs with id
     * @param id
     */
    public Tradecomments(Integer id) {
        this.id = id;
    }

    /**
     * constructors with params
     * @param id
     * @param string
     * @param createDate
     */
    public Tradecomments(Integer id, String string, Date createDate) {
        this.id = id;
        this.string = string;
        this.createDate = createDate;
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
     * gets creator id 
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
     * gets string (message)
     * @return
     */
    public String getString() {
        return string;
    }

    /**
     * sets string
     * @param string
     */
    public void setString(String string) {
        this.string = string;
    }

    /**
     * gets create date
     * @return
     */
    public Date getCreateDate() {
        return createDate;
    }

    /**
     * sets create date
     * @param createDate
     */
    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
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
        if (!(object instanceof Tradecomments)) {
            return false;
        }
        Tradecomments other = (Tradecomments) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.Tradecomments[ id=" + id + " ]";
    }

}
