/*
 * Created by Matt Morrison on 2016.04.13  * 
 * Copyright © 2016 Matt Morrison. All rights reserved. * 
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

@Named("searchManager")
@SessionScoped
/**
 *
 * @author Matt
 */
public class SearchManager implements Serializable {
    private String searchString;
    private List<Cards> searchResults = null;
    private int searchType = 0;
    
    @EJB
    private CardsFacade cardsFacade;
    
    @EJB
    private UsersFacade usersFacade;
    
    public SearchManager(){
        
    }
    
    public String getSearchString() {
        return searchString;
    }
    
    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }
    
    public List<Cards> getSearchResults() {
        return searchResults;
    }
    
    public void setSearchResults(List<Cards> searchResults) {
        this.searchResults = searchResults;
    }
    
    public int getSearchType() {
        return searchType;
    }
    
    public void setSearchType(int searchType) {
        this.searchType = searchType;
    }
    
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
}
