package sample.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import sample.models.*;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable
{
    public TableView mainTable;

    GadgetModel gadgetModel = new GadgetModel();

    @Override
    public  void initialize (URL location, ResourceBundle resources)
    {
        gadgetModel.addDataChangedListener(gadget ->
        {
            mainTable.setItems(FXCollections.observableArrayList(gadget));
        });

        gadgetModel.load(); // метож загрузки данных

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

        // контроллер
        GadgetFormController controller = loader.getController();

        // передача модели
        controller.gadgetModel = gadgetModel;

        // открываем окно и ждем пока его закроют
        stage.showAndWait();
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
        controller.gadgetModel = gadgetModel;

        stage.showAndWait();
    }

    public void onDeleteClick(ActionEvent actionEvent)
    {
        Gadget gadget = (Gadget) this.mainTable.getSelectionModel().getSelectedItem();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Подьверждение");
        alert.setHeaderText(String.format("Точно удалить %s?" , gadget.getTitle()));

        Optional<ButtonType> option = alert.showAndWait();
        if (option.get() == ButtonType.OK)
        {
            gadgetModel.delete((gadget.id));
        }
    }

    public void onSaveToFileClick(ActionEvent actionEvent)
    {
        gadgetModel.saveToFile("data.json");
    }

    public void onLoadToFileClick(ActionEvent actionEvent)
    {
    }
}
