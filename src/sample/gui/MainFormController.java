package sample.gui;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import sample.models.*;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

public class MainFormController implements Initializable
{
    public TableView mainTable;
    public ComboBox cmdGadgetType;


    ObservableList<Class<? extends Gadget>> gadgetTypes = FXCollections.observableArrayList(
            Gadget.class,
            Smartphone.class,
            Notebook.class,
            Tablet.class
    );
    GadgetModel gadgetModel = new GadgetModel();

    @Override
    public  void initialize (URL location, ResourceBundle resources)
    {
        gadgetModel.addDataChangedListener(gadget ->
        {
            mainTable.setItems(FXCollections.observableArrayList(gadget));
        });

        cmdGadgetType.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) ->
        {
            this.gadgetModel.setGadgetFilter((Class<? extends Gadget>) newValue);
        });


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

        cmdGadgetType.setItems(gadgetTypes);
        cmdGadgetType.getSelectionModel().select(0);

        cmdGadgetType.setConverter(new StringConverter<Class>()
        {
            @Override
            public String toString(Class object)
            {
                if (Gadget.class.equals(object))
                {
                    return "Все";
                }
                else if (Smartphone.class.equals(object))
                {
                    return "Смартфон";
                }
                else if (Notebook.class.equals(object))
                {
                    return "Ноутбук";
                }
                else if (Tablet.class.equals(object))
                {
                    return "Планшет";
                }
                return null;
            }

            @Override
            public Class fromString(String string)
            {
                return null;
            }
        });
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
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Сохранить данные");
        fileChooser.setInitialDirectory(new File("."));

        // диалог для сохранения файла
        File file = fileChooser.showSaveDialog(mainTable.getScene().getWindow());

        if (file != null)
        {
            gadgetModel.saveToFile(file.getPath());
        }
    }

    public void onLoadToFileClick(ActionEvent actionEvent)
    {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Загрузить данные");
        fileChooser.setInitialDirectory(new File("."));

        // диалог для открытия файла
        File file = fileChooser.showOpenDialog(mainTable.getScene().getWindow());

        if (file != null)
        {
            gadgetModel.loadFromFile(file.getPath());
        }
    }
}
