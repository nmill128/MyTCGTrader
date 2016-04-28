package com.mycompany.jsfpackage;

import com.mycompany.entitypackage.Cards;
import com.mycompany.entitypackage.CardPhotos;
import com.mycompany.entitypackage.Users;
import com.mycompany.jsfpackage.util.JsfUtil;
import com.mycompany.jsfpackage.util.PaginationHelper;
import com.mycompany.managers.Constants;
import com.mycompany.managers.FileManager;
import com.mycompany.sessionBeanPackage.CardsFacade;
import com.mycompany.sessionBeanPackage.CardPhotosFacade;
import com.mycompany.sessionBeanPackage.UsersFacade;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import java.io.Serializable;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.imageio.ImageIO;
import org.imgscalr.Scalr;
import org.primefaces.model.UploadedFile;

@Named("cardsController")
@SessionScoped
public class CardsController implements Serializable {

    private Cards current;
    private DataModel items = null;
    @EJB
    private com.mycompany.sessionBeanPackage.CardsFacade ejbFacade;
    private PaginationHelper pagination;
    private int selectedItemIndex;
    private UploadedFile file;
    private String fileName;
    private String message = "";

    /**
     * The instance variable 'userFacade' is annotated with the @EJB annotation.
     * This means that the GlassFish application server, at runtime, will inject
     * in this instance variable a reference to the @Stateless session bean
     * UserFacade.
     */
    @EJB
    private CardsFacade cardsFacade;

    /**
     * The instance variable 'photoFacade' is annotated with the @EJB
     * annotation. This means that the GlassFish application server, at runtime,
     * will inject in this instance variable a reference to the @Stateless
     * session bean PhotoFacade.
     */
    @EJB
    private CardPhotosFacade cardPhotosFacade;

    @EJB
    private UsersFacade userFacade;

    public CardsController() {
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Cards getSelected() {
        if (current == null) {
            current = new Cards();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CardsFacade getFacade() {
        return ejbFacade;
    }

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

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Cards) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "View";
    }

    public String binderView(Integer id) {
        current = (Cards) getFacade().find(id);
        List<CardPhotos> photos = cardPhotosFacade.findPhotosByCardID(current.getId());
        if (!photos.isEmpty()) {
            CardPhotos photo = photos.get(0);
            setFileName(photo.getThumbnailName());
        }
        return "ViewCard";
    }

    public String prepareCreate() {
        current = new Cards();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            if (file.getSize() != 0) {
                current.setUserId(userFacade.find(FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("user_id")));
                current.setDateAdded(new java.sql.Date(new Date().getTime()).toString());
                getFacade().create(current);
                copyFile(file);
                //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CardsCreated"));
                message = "";
                return "MyBinder";
            } else {
                message = "You need to upload a file first!";
                return "MyBinder";
            }
        } catch (Exception e) {
            e.printStackTrace();
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public FacesMessage copyFile(UploadedFile file) {
        try {
            deletePhoto();

            InputStream in = file.getInputstream();

            File tempFile = inputStreamToFile(in, Constants.TEMP_FILE);
            in.close();

            FacesMessage resultMsg;

            Cards card = cardsFacade.findById(current.getId());

            // Insert photo record into database
            String extension = file.getContentType();
            extension = extension.startsWith("image/") ? extension.subSequence(6, extension.length()).toString() : "png";
            List<CardPhotos> photoList = cardPhotosFacade.findPhotosByCardID(card.getId());
            if (!photoList.isEmpty()) {
                cardPhotosFacade.remove(photoList.get(0));
            }

            cardPhotosFacade.create(new CardPhotos(extension, card));
            CardPhotos photo = cardPhotosFacade.findPhotosByCardID(card.getId()).get(0);
            in = file.getInputstream();
            File uploadedFile = inputStreamToFile(in, photo.getFilename());
            saveThumbnail(uploadedFile, photo);
            resultMsg = new FacesMessage("Success!", "File Successfully Uploaded!");
            return resultMsg;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new FacesMessage("Upload failure!",
                "There was a problem reading the image file. Please try again with a new photo file.");
    }

    private File inputStreamToFile(InputStream inputStream, String childName)
            throws IOException {
        // Read in the series of bytes from the input stream
        byte[] buffer = new byte[inputStream.available()];
        inputStream.read(buffer);

        // Write the series of bytes on file.
        File targetFile = new File(Constants.ROOT_DIRECTORY, childName);

        OutputStream outStream;
        outStream = new FileOutputStream(targetFile);
        outStream.write(buffer);
        outStream.close();

        // Save reference to the current image.
        return targetFile;
    }

    private void saveThumbnail(File inputFile, CardPhotos inputPhoto) {
        try {
            BufferedImage original = ImageIO.read(inputFile);
            BufferedImage thumbnail = Scalr.resize(original, Constants.THUMBNAIL_SZ);
            ImageIO.write(thumbnail, inputPhoto.getExtension(),
                    new File(Constants.ROOT_DIRECTORY, inputPhoto.getThumbnailName()));
        } catch (IOException ex) {
            Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void deletePhoto() {
        FacesMessage resultMsg;

        Cards card = cardsFacade.findById(current.getId());

        List<CardPhotos> photoList = cardPhotosFacade.findPhotosByCardID(card.getId());
        if (photoList.isEmpty()) {
            resultMsg = new FacesMessage("Error", "You do not have a photo to delete.");
        } else {
            CardPhotos photo = photoList.get(0);
            try {
                Files.deleteIfExists(Paths.get(photo.getFilePath()));
                Files.deleteIfExists(Paths.get(photo.getThumbnailFilePath()));

                Files.deleteIfExists(Paths.get(Constants.ROOT_DIRECTORY + "tmp_file"));

                cardPhotosFacade.remove(photo);
            } catch (IOException ex) {
                Logger.getLogger(FileManager.class.getName()).log(Level.SEVERE, null, ex);
            }

            resultMsg = new FacesMessage("Success", "Photo successfully deleted!");
        }
        FacesContext.getCurrentInstance().addMessage(null, resultMsg);
    }

    //Fileupload controlling
    // Returns the uploaded file
    public UploadedFile getFile() {
        return file;
    }

    // Obtains the uploaded file
    public void setFile(UploadedFile file) {
        this.file = file;
    }

    // Returns the message
    public String getMessage() {
        return message;
    }

    // Obtains the message
    public void setMessage(String message) {
        this.message = message;
    }

    public String prepareEdit() {
        current = (Cards) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String binderEdit(int id) {
        current = (Cards) cardsFacade.findById(id);
        List<CardPhotos> photos = cardPhotosFacade.findPhotosByCardID(current.getId());
        if (photos.size() > 0) {
            CardPhotos photo = photos.get(0);
            setFileName(photo.getThumbnailName());
        }
        return "EditCard";
    }

    public String update() {
        try {
            getFacade().edit(current);
            if(file.getSize() != 0){
                copyFile(file);
            }
            //JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CardsUpdated"));
            return "MyBinder";
        } catch (Exception e) {
            //JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Cards) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
        recreateModel();
        return "List";
    }

    public String binderDestroy(int id) {

        current = (Cards) cardsFacade.findById(id);
        performDestroy();
        return "MyBinder";
    }

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

    private void performDestroy() {
        try {
            getFacade().remove(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CardsDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

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

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public SelectItem[] getItemsAvailableSelectMany() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), false);
    }

    public SelectItem[] getItemsAvailableSelectOne() {
        return JsfUtil.getSelectItems(ejbFacade.findAll(), true);
    }

    public Cards getCards(java.lang.Integer id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = Cards.class)
    public static class CardsControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CardsController controller = (CardsController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "cardsController");
            return controller.getCards(getKey(value));
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
            if (object instanceof Cards) {
                Cards o = (Cards) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + Cards.class.getName());
            }
        }

    }

    //Outside link generators
    public String createTCGPLink(String cardName) {
        String[] tokens = cardName.split(" ");
        String result = "";
        for (int i = 0; i < tokens.length; i++) {
            if (result.equals("")) {
                result += tokens[i];
            } else {
                result += "%20" + tokens[i];
            }
        }
        return "http://shop.tcgplayer.com/productcatalog/product/show?newSearch=false&ProductType=All&IsProductNameExact=false&ProductName=" + result;
    }

    public String createSCLink(String cardName) {
        String[] tokens = cardName.split(" ");
        String result = "";
        for (int i = 0; i < tokens.length; i++) {
            if (result.equals("")) {
                result += tokens[i];
            } else {
                result += "+" + tokens[i];
            }
        }
        return "http://sales.starcitygames.com/search.php?substring=" + result + "&go.x=25&go.y=5&go=GO&t_all=All&start_date=2010-01-29&end_date=2012-04-22&order_1=finish&limit=25&action=Show%2BDecks&card_qty%5B1%5D=1";
    }

    public String createCFLink(String cardName) {
        String[] tokens = cardName.split(" ");
        String result = "";
        for (int i = 0; i < tokens.length; i++) {
            if (result.equals("")) {
                result += tokens[i];
            } else {
                result += "+" + tokens[i];
            }
        }
        return "http://store.channelfireball.com/products/search?query=" + result;
    }

}
