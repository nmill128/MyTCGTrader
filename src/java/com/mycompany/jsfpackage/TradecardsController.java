/*
 * Created by Erik Yeomans on 2016.05.02  * 
 * Copyright © 2016 Erik Yeomans. All rights reserved. * 
 */
package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.Tradecards;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.sessionBeanPackage.TradecardsFacade;

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

@Named("tradecardsController")
@SessionScoped
public class TradecardsController implements Serializable {

    private Tradecards current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.TradecardsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;

    /**
     * Empty constructor
     */
    public TradecardsController() {
    }

    /**
     * Get the selected tradecard objects
     * @return the tradecard tuples we have selected
     */
    public Tradecards getSelected() {
        if (current == null) {
            current = new Tradecards();
            selectedItemIndex = -1;
        }
        return current;
    }

    /**
     * Get the facade associated with the tradecards
     * @return 
     */
    private TradecardsFacade getFacade() {
        return ejbFacade;
    }

    /**
     * Get the appropriate pagination associated with the tradecards
     * @return is the helper for the pagination in this scenario
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
     * Prepare the list view of the tradecards
     * @return the xhtml file's name we're redirecting to
     */
    public String prepareList() {
        recreateModel();
        return "List";
    }

    /**
     * Prepare the view of one tradecard
     * @return the xhtml file's name we're redirecting to
     */
    public String prepareView() {
        current = (Tradecards) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    /**
     * Prepare the creat screen for the tradecard
     * @return the xhtml file's name we're redirecting to
     */
    public String prepareCreate() {
        current = new Tradecards();
        selectedItemIndex = -1;
        return "Create";
    }

    /**
     * Create a new tradecard and return either nothing or a successful xhtml file
     * @return whether its successful and redirects or is null
     */
    public String create() {
        try {
            getFacade().create(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradecardsCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Prepare the edit screen for a tradecard
     * @return the xhtml file's name
     */
    public String prepareEdit() {
        current = (Tradecards) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    /**
     * Update a tradecard object, the current one
     * @return the xhtml file's name if successful, otherwise null
     */
    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradecardsUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    /**
     * Destroy a tradecard, the current one
     * @return the xhtml file's name
     */
    public String destroy() {
        current = (Tradecards) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    /**
     * destroy a tradecard and go back to viewing it
     * @return the xhtml file's name either list if its gone or view to view another one
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
     * Actually destroy the trdecard from the DB
     */
    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("TradecardsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    /**
     * Update the current item in the DB
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
     * GEt the items in a page
     * @return get the bean for the page
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
     * REset the pagination values
     */
    private void recreatePagination() {
        pagination = null;
    }

    /**
     * GEt the next page
     * @return the next page in the list
     */
    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    /**
     * Get the previous page
     * @return the previous page in the pagination
     */
    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    /**
     * GEt all of the tradecards
     * @return all the tradecards in the DB
     */
    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    /**
     * Get the first tradecard
     * @return the very first tradecard in the DB
     */
    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    /**
     * Get the tradecard at an id
     * @param id is the id of the tradecard we want
     * @return the tradecard with id ID
     */
    public Tradecards getTradecards(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    /**
     * Convert between the controller and just a regular class so faces knows
     * what this class is
     */
    @FacesConverter(forClass = Tradecards.class)
    public static class TradecardsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            TradecardsController controller = (TradecardsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "tradecardsController");
            return controller.getTradecards(getKey(value));
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
            if (object instanceof Tradecards) {
                Tradecards o = (Tradecards) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Tradecards.class.getName());
            }
        }

    }

}
