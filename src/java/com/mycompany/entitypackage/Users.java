/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.entitypackage;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 * generated entity class for USer
 * @author Erik
 */
@Entity
@Table(name = "Users")
@XmlRootElement
@NamedQueries({ //queries
    @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u"),
    @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id"),
    @NamedQuery(name = "Users.findByUsername", query = "SELECT u FROM Users u WHERE u.username = :username"),
    @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password"),
    @NamedQuery(name = "Users.findByFirstName", query = "SELECT u FROM Users u WHERE u.firstName = :firstName"),
    @NamedQuery(name = "Users.findByMiddleName", query = "SELECT u FROM Users u WHERE u.middleName = :middleName"),
    @NamedQuery(name = "Users.findByLastName", query = "SELECT u FROM Users u WHERE u.lastName = :lastName"),
    @NamedQuery(name = "Users.findByAddress1", query = "SELECT u FROM Users u WHERE u.address1 = :address1"),
    @NamedQuery(name = "Users.findByAddress2", query = "SELECT u FROM Users u WHERE u.address2 = :address2"),
    @NamedQuery(name = "Users.findByCity", query = "SELECT u FROM Users u WHERE u.city = :city"),
    @NamedQuery(name = "Users.findByUsState", query = "SELECT u FROM Users u WHERE u.usState = :usState"),
    @NamedQuery(name = "Users.findByZipcode", query = "SELECT u FROM Users u WHERE u.zipcode = :zipcode"),
    @NamedQuery(name = "Users.findBySecurityQuestion", query = "SELECT u FROM Users u WHERE u.securityQuestion = :securityQuestion"),
    @NamedQuery(name = "Users.findBySecurityAnswer", query = "SELECT u FROM Users u WHERE u.securityAnswer = :securityAnswer"),
    @NamedQuery(name = "Users.findByGoodTrades", query = "SELECT u FROM Users u WHERE u.goodTrades = :goodTrades"),
    @NamedQuery(name = "Users.findByBadTrades", query = "SELECT u FROM Users u WHERE u.badTrades = :badTrades"),
    @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")})
public class Users implements Serializable {
    //private variables with db counterparts
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "username")
    private String username;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 256)
    @Column(name = "password")
    private String password;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "first_name")
    private String firstName;
    @Size(max = 32)
    @Column(name = "middle_name")
    private String middleName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "last_name")
    private String lastName;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "address1")
    private String address1;
    @Size(max = 255)
    @Column(name = "address2")
    private String address2;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "city")
    private String city;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 32)
    @Column(name = "usState")
    private String usState;
    @Basic(optional = false)
    @NotNull
    @Column(name = "zipcode")
    private int zipcode;
    @Basic(optional = false)
    @NotNull
    @Column(name = "security_question")
    private int securityQuestion;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "security_answer")
    private String securityAnswer;
    @Basic(optional = false)
    @NotNull
    @Column(name = "good_trades")
    private int goodTrades;
    @Basic(optional = false)
    @NotNull
    @Column(name = "bad_trades")
    private int badTrades;
    // @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?", message="Invalid email")//if the field contains email address consider using this annotation to enforce field validation
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 64)
    @Column(name = "email")
    private String email;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "userId")
    private Collection<Wants> wantsCollection;

    /**
     * Constructor 
     */
    public Users() {
    }

    /**
     * Constructs with id
     * @param id
     */
    public Users(Integer id) {
        this.id = id;
    }

    /**
     * constructs with params
     * @param id
     * @param username
     * @param password
     * @param firstName
     * @param lastName
     * @param address1
     * @param city
     * @param usState
     * @param zipcode
     * @param securityQuestion
     * @param securityAnswer
     * @param goodTrades
     * @param badTrades
     * @param email
     */
    public Users(Integer id, String username, String password, String firstName, String lastName, String address1, String city, String usState, int zipcode, int securityQuestion, String securityAnswer, int goodTrades, int badTrades, String email) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address1 = address1;
        this.city = city;
        this.usState = usState;
        this.zipcode = zipcode;
        this.securityQuestion = securityQuestion;
        this.securityAnswer = securityAnswer;
        this.goodTrades = goodTrades;
        this.badTrades = badTrades;
        this.email = email;
    }

    /**
     * get id
     * @return
     */
    public Integer getId() {
        return id;
    }

    /**
     * set id 
     * @param id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * get username
     * @return
     */
    public String getUsername() {
        return username;
    }

    /**
     * set username
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }

    /**
     * get password
     * @return
     */
    public String getPassword() {
        return password;
    }

    /**
     * set password
     * @param password
     */
    public void setPassword(String password) {
        this.password = password;
    }

    /**
     * get first name
     * @return
     */
    public String getFirstName() {
        return firstName;
    }

    /**
     * set first name 
     * @param firstName
     */
    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    /**
     * get middle name
     * @return
     */
    public String getMiddleName() {
        return middleName;
    }

    /**
     * set middle name 
     * @param middleName
     */
    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    /** 
     * get last name
     * @return
     */
    public String getLastName() {
        return lastName;
    }

    /**
     * set last name
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * gets address1
     * @return
     */
    public String getAddress1() {
        return address1;
    }

    /**
     * sets address1
     * @param address1
     */
    public void setAddress1(String address1) {
        this.address1 = address1;
    }

    /**
     * gets address2 
     * @return
     */
    public String getAddress2() {
        return address2;
    }

    /**
     * sets address2
     * @param address2
     */
    public void setAddress2(String address2) {
        this.address2 = address2;
    }

    /**
     * gets cidy
     * @return
     */
    public String getCity() {
        return city;
    }

    /**
     * sets city
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * gets us state
     * @return
     */
    public String getUsState() {
        return usState;
    }

    /**
     * sets us state
     * @param usState
     */
    public void setUsState(String usState) {
        this.usState = usState;
    }

    /**
     * gets zipcode
     * @return
     */
    public int getZipcode() {
        return zipcode;
    }

    /**
     * sets zipcode
     * @param zipcode
     */
    public void setZipcode(int zipcode) {
        this.zipcode = zipcode;
    }

    /**
     * gets security Question
     * @return
     */
    public int getSecurityQuestion() {
        return securityQuestion;
    }

    /**
     * sets security q
     * @param securityQuestion
     */
    public void setSecurityQuestion(int securityQuestion) {
        this.securityQuestion = securityQuestion;
    }

    /**
     * get security a
     * @return
     */
    public String getSecurityAnswer() {
        return securityAnswer;
    }

    /**
     * sets security a
     * @param securityAnswer
     */
    public void setSecurityAnswer(String securityAnswer) {
        this.securityAnswer = securityAnswer;
    }

    /**
     * get Good trades
     * @return
     */
    public int getGoodTrades() {
        return goodTrades;
    }

    /**
     * set good trades 
     * @param goodTrades
     */
    public void setGoodTrades(int goodTrades) {
        this.goodTrades = goodTrades;
    }

    /**
     * get bad trades
     * @return
     */
    public int getBadTrades() {
        return badTrades;
    }

    /**
     * set bad trades
     * @param badTrades
     */
    public void setBadTrades(int badTrades) {
        this.badTrades = badTrades;
    }

    /**
     * gets email
     * @return
     */
    public String getEmail() {
        return email;
    }

    /**
     * sets email
     * @param email
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /**
     * gets wants
     * @return
     */
    @XmlTransient
    public Collection<Wants> getWantsCollection() {
        return wantsCollection;
    }

    /**
     * sets wants
     * @param wantsCollection
     */
    public void setWantsCollection(Collection<Wants> wantsCollection) {
        this.wantsCollection = wantsCollection;
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
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.mycompany.entitypackage.Users[ id=" + id + " ]";
    }
    
}
