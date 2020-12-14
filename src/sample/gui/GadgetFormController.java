package sample.gui;

import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.models.Gadget;
import sample.models.Notebook;
import sample.models.Smartphone;
import sample.models.Tablet;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class GadgetFormController implements Initializable
{

    // общее
    public ChoiceBox cmdGadgetType;
    public TextField txtTitle;
    public TextField txtDisplaySize;

    // смартфон
    public VBox smartphonePane;
    public CheckBox chkSim;
    public TextField txtBattery;
    public ChoiceBox cmbSmartphoneType;

    // ноут
    public VBox notebookPane;
    public CheckBox chkKeyBacklight;
    public TextField txtCore;
    public TextField txtHardDisk;

    // планшет
    public VBox tabletPane;
    public CheckBox chkWithCamera;
    public TextField txtDPI;

    final String GADGET_SMARTPHONE = "Смартфон";
    final String GADGET_NOTEBOOK = "Ноутбук";
    final String GADGET_TABLET = "Планшет";

    private Boolean modalResult = false;
    private Node mainTable;


    @Override
    public void initialize(URL location, ResourceBundle resources)
    {
        cmdGadgetType.setItems(FXCollections.observableArrayList(
                GADGET_SMARTPHONE,
                GADGET_NOTEBOOK,
                GADGET_TABLET
        ));

        cmdGadgetType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            updatePanes((String) newValue);

        });

        cmbSmartphoneType.getItems().setAll(
                Smartphone.Type.android,
                Smartphone.Type.ios,
                Smartphone.Type.windows
        );

        cmbSmartphoneType.setConverter(new StringConverter<Smartphone.Type>()
        {
            @Override
            public String toString(Smartphone.Type object)
            {
                switch (object)
                {
                    case windows:
                        return "windows";
                    case ios:
                        return "ios";
                    case android:
                        return "android";
                }
                return null;
            }

            @Override
            public Smartphone.Type fromString(String string)
            {
                return null;
            }
        });


        updatePanes("");
    }

    public void updatePanes(String value)
    {
        this.notebookPane.setVisible(value.equals(GADGET_NOTEBOOK));
        this.notebookPane.setManaged(value.equals(GADGET_NOTEBOOK));
        this.smartphonePane.setVisible(value.equals(GADGET_SMARTPHONE));
        this.smartphonePane.setManaged(value.equals(GADGET_SMARTPHONE));
        this.tabletPane.setVisible(value.equals(GADGET_TABLET));
        this.tabletPane.setManaged(value.equals(GADGET_TABLET));
    }

    public void onSaveClick(ActionEvent actionEvent)
    {
        this.modalResult = true;
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }

    public void onCancelClick(ActionEvent actionEvent)
    {
        this.modalResult = false;
        ((Stage) ((Node) actionEvent.getSource()).getScene().getWindow()).close();
    }


    public Boolean getModalResult()
    {
        return modalResult;
    }

    public void setGadget(Gadget gadget) {
        // делаем так что если объект редактируется, то нельзя переключать тип
        this.cmdGadgetType.setDisable(gadget != null);
        if (gadget != null) {

            this.txtTitle.setText(String.valueOf(gadget.getTitle()));
            this.txtDisplaySize.setText(String.valueOf(gadget.getDisplaySize()));

            if (gadget instanceof Smartphone)
            { // если смартфон
                this.cmdGadgetType.setValue(GADGET_SMARTPHONE); // вызываю тип
                this.cmbSmartphoneType.setValue(((Smartphone)gadget).type); // тип смарт
                this.chkSim.setSelected(((Smartphone)gadget).withSimSlot); // симка
                this.txtBattery.setText(String.valueOf(((Smartphone)gadget).getBatteryPower())); // батарея
                Integer.parseInt(txtBattery.getText()); //парс

            }
            else if (gadget instanceof Notebook)
            { // если ноут
                this.cmdGadgetType.setValue(GADGET_NOTEBOOK);
                this.chkKeyBacklight.setSelected(((Notebook)gadget).withKeyBacklight);
                this.txtCore.setText(String.valueOf(((Notebook)gadget).getNumberOfCores()));
                Integer.parseInt(txtCore.getText());
                this.txtHardDisk.setText(String.valueOf(((Notebook)gadget).getHardDiskSpace()));
                Integer.parseInt(txtHardDisk.getText());
            }
            else if (gadget instanceof Tablet)
            { // если планшет
                this.cmdGadgetType.setValue(GADGET_TABLET);
                this.chkWithCamera.setSelected(((Tablet)gadget).camera);
                this.txtDPI.setText(String.valueOf(((Tablet)gadget).getDPI()));
                Integer.parseInt(txtDPI.getText());
            }
        }
    }

    public Gadget getGadget()
    {
        Gadget result = null;
        int displaySize = Integer.parseInt(this.txtDisplaySize.getText());
        String title = this.txtTitle.getText();

        switch ((String) this.cmdGadgetType.getValue())
        {
            case GADGET_SMARTPHONE:
                result = new Smartphone(
                        displaySize,
                        title,
                        (Smartphone.Type) this.cmbSmartphoneType.getValue(),
                        Integer.parseInt(txtBattery.getText()),
                        this.chkSim.isSelected());

            case GADGET_NOTEBOOK:
                result = new Notebook(
                        displaySize,
                        title,
                        this.chkKeyBacklight.isSelected(),
                        Integer.parseInt(txtCore.getText()),
                        Integer.parseInt(txtHardDisk.getText()));

            case GADGET_TABLET:
                result = new Tablet(
                        displaySize,
                        title,
                        this.chkWithCamera.isSelected(),
                        Integer.parseInt(txtDPI.getText()));
        }
        return result;
    }



}
