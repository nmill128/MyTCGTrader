/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
 *
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

    public Tradecomments() {
    }

    public Tradecomments(Integer id) {
        this.id = id;
    }

    public Tradecomments(Integer id, String string, Date createDate) {
        this.id = id;
        this.string = string;
        this.createDate = createDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Users getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(Users creatorId) {
        this.creatorId = creatorId;
    }

    public Trades getTradeID() {
        return tradeID;
    }

    public void setTradeID(Trades tradeID) {
        this.tradeID = tradeID;
    }

    public String getString() {
        return string;
    }

    public void setString(String string) {
        this.string = string;
    }

    public Date getCreateDate() {
        return createDate;
    }

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
