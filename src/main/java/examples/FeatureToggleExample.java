package examples;

import org.mortys.gui.components.CheckBoxFeature;
import org.mortys.process.control.ToggleControl;

public class FeatureToggleExample {

    public static void main (String[] args) {

        //erzeuge new CheckBoxFeature
        CheckBoxFeature c = new CheckBoxFeature();

        //setze feature (darf nicht in DB vorhanden sein!) und Caption (optional) -> Voraussetzung für das hinzufuegen in der DB!
        c.setFeature("func-test-1");
        c.setCaption("TestCaption");

        //hinzufuegen in der DB
        ToggleControl.addToggle(c);

        //wechsel feature-status (manuell), wird aber auch via Click ausgeloest
        c.setValue((c.getValue() ? false : true));
    }
}
