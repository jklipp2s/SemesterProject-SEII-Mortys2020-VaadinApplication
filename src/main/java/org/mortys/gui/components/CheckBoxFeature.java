  package org.mortys.gui.components;

  import com.vaadin.ui.CheckBox;

  import javax.validation.constraints.NotNull;

  /**
   * Component zur Realisierung des Toggle-Patterns
   *
   * Bitte (vor allem bei setter-Methoden) stets die CheckBoxFeature Instanzmethoden verwenden!
   */
  public class CheckBoxFeature extends CheckBox {

      private String feature;

      public CheckBoxFeature() {
          super();
      }

      public String getFeature() {
          return feature;
      }

      public void setFeature(String feature) {
          if (!(feature == null || feature.equals("")) || feature.contains(" "))
              this.feature = feature;
          else
              throw new IllegalArgumentException("feature darf nicht null oder leer sein! ");
      }

      @NotNull
      public boolean equals(CheckBoxFeature other) {
          boolean captionEql = false;
          if (getCaption() == null || other.getCaption() == null) {
              if (getCaption() == null ^ other.getCaption() == null)
                  return false;
              captionEql = true;
          } else {
              captionEql = getCaption().equals(other.getCaption());
          }

          return captionEql
                  && getFeature().equals(other.getFeature())
                  && getValue() == other.getValue();
      }

      public String toString() {
          return "Feature: " + getFeature()
                  + ", Caption: " + getCaption()
                  + ", Status: " + getValue();
      }

  }
