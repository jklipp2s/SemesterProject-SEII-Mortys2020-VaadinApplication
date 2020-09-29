package org.mortys.process.control;

import com.vaadin.data.HasValue;
import org.mortys.gui.components.CheckBoxFeature;
import org.mortys.model.dao.FeatureDAO;
import org.mortys.process.control.exception.DatabaseException;
import org.mortys.services.db.JDBCConnection;

import java.util.logging.Level;
import java.util.logging.Logger;

public class ToggleControl {

    public static boolean featureIsEnabled(String toggle) {
        boolean status = false;
        try {
            status = FeatureDAO.getInstance().fetchFeatureToggle(toggle).getValue();
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }

        return status;
    }

    public static CheckBoxFeature fetchToggle(String toggle) {
        CheckBoxFeature res = null;
        try {
            res = FeatureDAO.getInstance().fetchFeatureToggle(toggle);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
        return res;
    }

    public static void changeToggleStatus(String toggle, boolean value) {
        FeatureDAO featureDAO = FeatureDAO.getInstance();
        try {
            featureDAO.changeToggleStatus(toggle, value);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);

        }
    }

    public static void setValue(CheckBoxFeature checkBoxFeature) {
        boolean res = fetchToggle(checkBoxFeature.getFeature()).getValue();
        checkBoxFeature.setValue(res);
    }


    /*
    public static void changeToggleFeature(String toggle, String changeTo) {
        try {
            FeatureDAO.getInstance().changeToggleFeature(toggle, changeTo);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

     */

    public static void changeToggleCaption(String toggle, String changeTo) {
        try {
            FeatureDAO.getInstance().changeToggleCaption(toggle, changeTo);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void addToggle(CheckBoxFeature checkBoxFeature) {
        String feature = checkBoxFeature.getFeature();
        if (feature == null || feature.equals("") || feature.contains(" "))
            throw new IllegalArgumentException("feature darf nicht null oder leer sein! ");
        try {
            addToggleValueChangeListener(checkBoxFeature);
            if (fetchToggle(checkBoxFeature.getFeature()) == null) {
                FeatureDAO.getInstance().insertFeatureToggle(checkBoxFeature);
            }
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    public static void deleteToggle(CheckBoxFeature checkBoxFeature) {
        try {
            FeatureDAO.getInstance().deleteToggle(checkBoxFeature);
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }



    public static void deleteAllToggle() {
        try {
            FeatureDAO.getInstance().deleteAllToggle();
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }
    }

    /*
    public static boolean isRegistered(String toggle) {
        boolean res = false;

        try {
            res = FeatureDAO.getInstance().fetchFeatureToggle(toggle) != null;
        } catch (DatabaseException e) {
            Logger.getLogger(JDBCConnection.class.getName()).log(Level.SEVERE, null, e);
        }

        return res;
    }

     */

    public static void addToggleValueChangeListener(CheckBoxFeature checkBoxFeature) {
        checkBoxFeature.addValueChangeListener(new HasValue.ValueChangeListener() {
            @Override
            public void valueChange(HasValue.ValueChangeEvent valueChangeEvent) {
                ToggleControl.changeToggleStatus(checkBoxFeature.getFeature(),checkBoxFeature.getValue());
                System.out.println("Feature \"" + checkBoxFeature.getFeature() + "\" " + (featureIsEnabled(checkBoxFeature.getFeature()) ? "enabled" : "disabled"));
            }
        });
    }
}
