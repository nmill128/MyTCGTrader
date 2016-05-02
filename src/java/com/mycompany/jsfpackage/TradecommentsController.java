/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.Tradecomments;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.TradecommentsFacade;

import java.io.Serializable;
import java.util.ResourceBundle;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;

/**
 *
 * @author Erik
 * 
 * CardPhotos generated controller class with additional methods to help with
 * viewing and creating from the mybinder page
 */
@Named("tradecommentsController")
@SessionScoped
public class TradecommentsController implements Serializable {

    private Tradecomments current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.TradecommentsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /**
     * EMpty Constructor
     */
    public TradecommentsController() {
    }

    /**
     * Get the selected tradecomment
     * @return the tradecomment last accessed
     */
    public Tradecomments getSelected() {
        if (current == null) {
            current = new Tradecomments();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * Get the facade associated with the trade comment
     * @return 
     */
    private TradecommentsFacade getFacade() {
        return ejbFacade;
    }

    /**
     * GEt the pagination helper to appropriately paginate the tradecomments
     * @return the pagination helper for this table
     */
    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    /**
     * Prepare for the list 
     * @return the xhtml file's name
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Get the current tradecomment ready to view
     * @return the xhtml file's name
     */
    public String prepareView() {
        current = (Tradecomments) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Make a new tradecomment to be created
     * @return the xhtml file's name
     */
    public String prepareCreate() {
        current = new Tradecomments();
        selectedItemIndex = -1;
        return "Create";
    }

    /**
     * Actually make a new tradecomment in the DB and return it
     * @return the xhtml file's name
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradecommentsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepare the current tradecomment to be edited
     * @return the xhtml file's name
     */
    public String prepareEdit() {
        current = (Tradecomments) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * Update the current tradecomment in the database
     * @return the xhtml file's name if its successful, null if not
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradecommentsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Destroy the current tradecomment in the database
     * @return the xhtml file's name if successful, null if not
     */
    public String destroy() {
        current = (Tradecomments) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    /**
     * Destroy the current tradecomment but then view the next one
     * @return the xhtml file's name view if there are mroe, list if not
     */
    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    /**
     * Actually destroy something in the database
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradecommentsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * Actually update a tradecomment in the database
     */
    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    /**
     * Get the datamodel for the tradecomments in this table
     * @return a datamodel for tradecomments
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }
    

    /**
     * Reset the datamodel
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Reset the paginationhelper
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * GEt the next page in the pagination
     * @return the xhtml file's name of the list we're viewing
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previous page in the pagination
     * @return the xhtml file's name of the list we're viewing
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
     * GEt every tradecomment
     * @return an array of every tradecomment
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    /**
     * Get the first tradecomment
     * @return the very first trade comment
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Find the tradecomment with the associated ID
     * @param id is the id we're looking for
     * @return the tradecomment with that ID
     */
    public Tradecomments getTradecomments(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * make this into an actual controller class for jsf so that it
     * can be used effectively
     */
    @FacesConverter(forClass = Tradecomments.class)
    public static class TradecommentsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TradecommentsController controller = (TradecommentsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tradecommentsController");
            return controller.getTradecomments(getKey(value));
        }

        java.lang.Integer getKey(String value) {
            java.lang.Integer key;
            key = Integer.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Integer value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof Tradecomments) {
                Tradecomments o = (Tradecomments) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tradecomments.class.getName());
            }
        }

    }

}
