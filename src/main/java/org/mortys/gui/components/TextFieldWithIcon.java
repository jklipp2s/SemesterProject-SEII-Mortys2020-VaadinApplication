package org.mortys.gui.components;

import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Label;
import com.vaadin.ui.TextField;

public class TextFieldWithIcon extends HorizontalLayout {
    TextField textField;
    Label textlabel;
    Label label;
    HorizontalLayout innerLayout;
    HorizontalLayout overLayout;

    public TextFieldWithIcon(TextField textField, String placeholder, Label label) {
        textField.setWidth("300px");
        this.setWidth("300px");
        this.textField = textField;
        this.SetUpComponent(placeholder, label);
    }


    public TextFieldWithIcon(String width,TextField textField, String placeholder, Label label) {
        if(!width.equals("0")) {
            this.setWidth(width);
            if(!width.contains("%")) textField.setWidth(width);
        }
        this.textField = textField;
        this.SetUpComponent(placeholder, label);
    }


    public void SetUpComponent(String placeholder, Label label) {
        textField.setStyleName("textfieldwidth");
        this.setStyleName("textfieldwidth");
        this.setStyleName("textfieldWithIcon");
        overLayout = new HorizontalLayout();
        innerLayout = new HorizontalLayout();
        innerLayout.setStyleName("innerLayout");
        this.label = label;
        this.label.setStyleName("textfieldicon");


        textlabel = new Label(placeholder);
        textlabel.setStyleName("textfieldplaceholder");

        innerLayout.addComponents(this.label, textlabel);
        overLayout.addComponents(this.textField, innerLayout);
        overLayout.setStyleName("overLayout");
        this.addComponents(overLayout);


        this.textField.addFocusListener(focusEvent -> {
            if(innerLayout.isAttached()) this.detachPlaceHolder();
        });

        this.textField.addBlurListener(blurEvent -> {
            if (this.textField.isEmpty()) this.attachPlaceHolder();
        });

        this.addLayoutClickListener(layoutClickEvent -> {
            this.textField.focus();
        });

    }

    private void detachPlaceHolder() {
        overLayout.removeComponent(innerLayout);
    }

    private void attachPlaceHolder() { overLayout.addComponent(innerLayout);    }

    public String getValue() {
        return this.textField.getValue();
    }




}
