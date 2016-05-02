/*
 * Created by Matt Morrison on 2016.04.13  * 
 * Copyright © 2016 Matt Morrison. All rights reserved. * 

Manager class to control the search functionality.  Recieves form data and 
requests from the search page and performs the basic and advanced seaerches.
 */
package com.mycompany.managers;

import com.mycompany.entitypackage.Cards;
import com.mycompany.sessionBeanPackage.CardsFacade;
import com.mycompany.sessionBeanPackage.UsersFacade;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

/**
 *
 * @author Erik
 */
@Named("searchManager")
@SessionScoped
/**
 * 
 * Manager class to control the search functionality.  Recieves form data and 
 * requests from the search page and performs the basic and advanced seaerches.
 *
 * @author Matt
 */
public class SearchManager implements Serializable {
    
    //private variables
    private String searchString;
    private List<Cards> searchResults = null;
    private int searchType = 0;
    //variables being populated from the page
    private String cardName;
    private String username;
    private String value;
    private String edition;
    private int valueType;
    private int cardCondition;
    
    @EJB
    private CardsFacade cardsFacade;
    
    //constructor

    /**
     *
     */
    public SearchManager(){
        
    }
    
////Getter and setters section
    //G+S for search string

    /**
     *
     * @return
     */
    public String getSearchString() {
        return searchString;
    }
    
    /**
     *
     * @param searchString
     */
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
 
   //G+S for searchResults

    /**
     *
     * @return
     */
    public List<Cards> getSearchResults() {
        return searchResults;
    }
    
    /**
     *
     * @param searchResults
     */
    public void setSearchResults(List<Cards> searchResults) {
        this.searchResults = searchResults;
    }
    
    //G+S for searchType

    /**
     *
     * @return
     */
    public int getSearchType() {
        return searchType;
    }
    
    /**
     *
     * @param searchType
     */
    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
    
    //G+S for cardName

    /**
     *
     * @return
     */
    public String getCardName() {
        return cardName;
    }
    
    /**
     *
     * @param cardName
     */
    public void setCardName(String cardName) {
        this.cardName = cardName;
    }
    
    //G+S for username

    /**
     *
     * @return
     */
    public String getUsername() {
        return username;
    }
    
    /**
     *
     * @param username
     */
    public void setUsername(String username) {
        this.username = username;
    }
    
    //G+S for value

    /**
     *
     * @return
     */
    public String getValue() {
        return value;
    }
    
    /**
     *
     * @param value
     */
    public void setValue(String value) {
        this.value = value;
    }
    
    //G+S for edition

    /**
     *
     * @return
     */
    public String getEdition() {
        return edition;
    }
    
    /**
     *
     * @param edition
     */
    public void setEdition(String edition) {
        this.edition = edition;
    }
    
    //G+S for cardCondition

    /**
     *
     * @return
     */
    public int getCardCondition() {
        return cardCondition;
    }
    
    /**
     *
     * @param cardCondition
     */
    public void setCardCondition(int cardCondition) {
        this.cardCondition = cardCondition;
    }
    
    //G+S for valueType

    /**
     *
     * @return
     */
    public int getValueType() {
        return valueType;
    }
    
    /**
     *
     * @param valueType
     */
    public void setValueType(int valueType) {
        this.valueType = valueType;
    }
    
    //redirect for advancedsearch

    /**
     *
     * @return
     */
    public String toAdvancedSearch() {
        return "AdvancedSearch";
    }
    
    //redirect for basic search

    /**
     *
     * @return
     */
    public String toBasicSearch() {
        return "search";
    }
 
////Search Functions
    
    //Search cards by name

    /**
     *
     */
    public void searchCardsByName() {
        switch (searchType) {
            case 0:
                searchResults = cardsFacade.findByName(searchString);
                List<Cards> userCards = cardsFacade.findByOwnerName(searchString);
                searchResults.addAll(userCards);
                break;
            case 1:
                searchResults = cardsFacade.findByName(searchString);
                break;
            case 2:
                searchResults = cardsFacade.findByOwnerName(searchString);
                break;
            default:
                break;
        }
    }
    
    //perform an advanced search with many critaria 

    /**
     *
     */
    public void advancedSearch() {
        searchResults = cardsFacade.findByAdvancedCriteria(cardName, username, cardCondition, value, valueType);
    }
}
