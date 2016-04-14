/*
 * Created by Matt Morrison on 2016.04.13  * 
 * Copyright © 2016 Matt Morrison. All rights reserved. * 
 */
package com.mycompany.managers;

import com.mycompany.entitypackage.Cards;
import com.mycompany.sessionBeanPackage.CardsFacade;
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
    
    @EJB
    private CardsFacade cardsFacade;
    
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
    
    
    public void searchCardsByName() {
        System.out.println("Searching for cards");
        searchResults = cardsFacade.findByName(searchString);
    }
}
