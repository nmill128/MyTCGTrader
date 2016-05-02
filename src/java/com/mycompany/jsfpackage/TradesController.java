/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.Trades;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.TradesFacade;

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
@Named("tradesController")
@SessionScoped
public class TradesController implements Serializable {

    private Trades current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.TradesFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /*
    * Empty constructor
    */
    public TradesController() {
    }
    
/**
 * GEt all the selected trades
 * @return the selected trades
 */
    public Trades getSelected() {
        if (current == null) {
            current = new Trades();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * GEt the associated facade for trades from the DB
     * @return the TradesFacade
     */
    private TradesFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Create and get a pagination helper for trades
     * @return 
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
     * Create a dataview model for the list to present
     * @return the xhtml file's name
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Create a view for one trade and view it
     * @return the view for the trade the xhtml file's name
     */
    public String prepareView() {
        current = (Trades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Get ready to create and select a new trade
     * @return the xhtml file's name
     */
    public String prepareCreate() {
        current = new Trades();
        selectedItemIndex = -1;
        return "Create";
    }

    /**
     * Create a new trade and go to the page if successful
     * @return the xhtml file's name if it works, null if not
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradesCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepare to edit the selected item
     * @return the xhtml file's name
     */
    public String prepareEdit() {
        current = (Trades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * Actually update the tarde object in the database
     * @return the xhtml file's name if it works, null if not
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradesUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Destroy the trade in the database
     * @return the xhtml file's name
     */
    public String destroy() {
        current = (Trades) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    /**
     * Destroy the tarde in the database but then view the next one if another
     * is selected
     * @return the xhtml file's name
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
     * Actually destroy something in the database and see the message
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradesDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * Edit the trade in the database and get the message out
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
     * Get the model being used for the trade
     * @return the bean for trades
     */
    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    /**
     * Drop the current model to be remade
     */
    private void recreateModel() {
        items = null;
    }

    /**
     * Drop the current pagination helper for a new one
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * Get the next page from the helper
     * @return the xhtml file's name
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previous page from the pagination helper
     * @return 
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
    * Get every trade in the database
    * @return an array of all the trades in the database
    */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }
    /**
     * Get the first trade int he db
     * @return an array of up to one trade
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Get the trades with a specific ID
     * @param id the id we're looking for
     * @return the trade with that id if there is one
     */
    public Trades getTrades(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * Faces needs to convert this into a controller for the trades class so 
     * this method has to be here so that it knows what class to control
     */
    @FacesConverter(forClass = Trades.class)
    public static class TradesControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TradesController controller = (TradesController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tradesController");
            return controller.getTrades(getKey(value));
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
            if (object instanceof Trades) {
                Trades o = (Trades) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Trades.class.getName());
            }
        }

    }

}
