package sample.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.models.Gadget;
import sample.models.Notebook;
import sample.models.Smartphone;
import sample.models.Tablet;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainFormController implements Initializable
{
    public TableView mainTable;
    // создали список
    ObservableList<Gadget> gadgetList = FXCollections.observableArrayList();

    @Override
    public  void initialize (URL location, ResourceBundle resources)
    {
        // заполнили список данными
        gadgetList.add(new Tablet(250,"Lenovo Tab4",true, 255));
        gadgetList.add(new Notebook(650, "Lenovo", false, 64,2000));
        gadgetList.add(new Smartphone(120, "Lenovo", Smartphone.Type.android, 200, false));

        mainTable.setItems(gadgetList);

        TableColumn<Gadget, String> titleColumn = new TableColumn<>("Название");
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));

        TableColumn<Gadget, String> display_sizeColumn = new TableColumn<>("Размер диспллея");
        display_sizeColumn.setCellValueFactory(new PropertyValueFactory<>("displaySize"));

        TableColumn<Gadget, String> descriptionColumn = new TableColumn<>("Описание");

        descriptionColumn.setCellValueFactory(cellData ->
        {
            return new SimpleStringProperty(cellData.getValue().getDescription());
        });

        mainTable.getColumns().addAll(titleColumn, display_sizeColumn, descriptionColumn);
    }

    public void onAddClick(ActionEvent actionEvent) throws IOException
    {
        // создание fxml файла
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GadgetForm.fxml"));
        Parent root = loader.load();

        // новое окно
        Stage stage = new Stage();
        stage.setScene(new Scene(root));

        // указатель на модальность
        stage.initModality(Modality.WINDOW_MODAL);

        // блокировка окна на которое мы нажали
        stage.initOwner(this.mainTable.getScene().getWindow());

        // открываем окно и ждем пока его закроют
        stage.showAndWait();

        GadgetFormController controller = loader.getController();
        if (controller.getModalResult())
        {
            Gadget newGadget = controller.getGadget();
            this.gadgetList.add(newGadget);
        }
    }

    public void onEditClick(ActionEvent actionEvent) throws IOException
    {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("GadgetForm.fxml"));
        Parent root = loader.load();

        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(this.mainTable.getScene().getWindow());

        GadgetFormController controller = loader.getController();
        controller.setGadget((Gadget)this.mainTable.getSelectionModel().getSelectedItem());

        stage.showAndWait();

        if (controller.getModalResult())
        {
            int index = this.mainTable.getSelectionModel().getSelectedIndex();
            this.mainTable.getItems().set(index, controller.getGadget());
        }
    }
}
