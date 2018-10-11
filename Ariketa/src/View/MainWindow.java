/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package View;

import Controller.Kontroladorea;
import Model.Ibilgailuak;
import java.io.File;
import java.io.IOException;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.converter.IntegerStringConverter;
import javax.xml.parsers.ParserConfigurationException;
import org.xml.sax.SAXException;

/**
 *
 * @author DM3-2-11
 */
public class MainWindow extends Application {

    private final TableView<Ibilgailuak> table = new TableView<>();
    final HBox hb = new HBox();

    @Override
    public void start(Stage stage) throws IOException, ParserConfigurationException, SAXException {
        Scene scene = new Scene(new Group());

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Aukeratu fitxategia");
        FileChooser.ExtensionFilter extFilter = new FileChooser.ExtensionFilter("XML files (*.xml)", "*.xml");
        fileChooser.getExtensionFilters().add(extFilter);
        File aukeratutakoa = fileChooser.showOpenDialog(stage);

        ObservableList<Ibilgailuak> data = Kontroladorea.datuakKargatuxml(aukeratutakoa);

        stage.setTitle("Kontzesionarioko Datuen Taula");
        stage.setWidth(550);
        stage.setHeight(550);
        final Label label = new Label("Ibilgailuak");
        label.setFont(new Font("Arial", 20));

        table.setEditable(true);

        TableColumn<Ibilgailuak, Integer> IdZut = new TableColumn<>("Id");
        IdZut.setMinWidth(100);
        IdZut.setCellValueFactory(new PropertyValueFactory<Ibilgailuak, Integer>("id"));
        IdZut.setCellFactory(TextFieldTableCell.<Ibilgailuak, Integer>forTableColumn(new IntegerStringConverter()));
        IdZut.setOnEditCommit((TableColumn.CellEditEvent<Ibilgailuak, Integer> t) -> {
            ((Ibilgailuak) t.getTableView().getItems().get(t.getTablePosition().getRow())).setId(t.getNewValue());
        });

        TableColumn<Ibilgailuak, String> ModeloZut = new TableColumn<>("Izena");
        ModeloZut.setMinWidth(100);
        ModeloZut.setCellValueFactory(new PropertyValueFactory<>("modeloa"));
        ModeloZut.setCellFactory(TextFieldTableCell.<Ibilgailuak>forTableColumn());
        ModeloZut.setOnEditCommit((TableColumn.CellEditEvent<Ibilgailuak, String> t) -> {
            ((Ibilgailuak) t.getTableView().getItems().get(t.getTablePosition().getRow())).setModeloa(t.getNewValue());
        });

        TableColumn<Ibilgailuak, String> MarkaZut = new TableColumn<>("Marka");
        MarkaZut.setMinWidth(100);
        MarkaZut.setCellValueFactory(new PropertyValueFactory<>("marka"));
        MarkaZut.setCellFactory(TextFieldTableCell.<Ibilgailuak>forTableColumn());
        MarkaZut.setOnEditCommit((TableColumn.CellEditEvent<Ibilgailuak, String> t) -> {
            ((Ibilgailuak) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMarka(t.getNewValue());
        });

        TableColumn<Ibilgailuak, String> MatrikulaZut = new TableColumn<>("Matrikula");
        MatrikulaZut.setMinWidth(200);
        MatrikulaZut.setCellValueFactory(
                new PropertyValueFactory<>("matrikula"));
        MatrikulaZut.setCellFactory(TextFieldTableCell.<Ibilgailuak>forTableColumn());
        MatrikulaZut.setOnEditCommit((TableColumn.CellEditEvent<Ibilgailuak, String> t) -> {
            ((Ibilgailuak) t.getTableView().getItems().get(t.getTablePosition().getRow())).setMatrikula(t.getNewValue());
        });

        table.setItems(data);
        table.getColumns().addAll(IdZut, ModeloZut, MarkaZut, MatrikulaZut);
        final TextField addId = new TextField();
        addId.setPromptText("Id");
        addId.setMaxWidth(ModeloZut.getPrefWidth());
        
        final TextField addModeloa = new TextField();
        addModeloa.setPromptText("Modeloa");
        addModeloa.setMaxWidth(ModeloZut.getPrefWidth());
        
        final TextField addMarka = new TextField();
        addMarka.setMaxWidth(MarkaZut.getPrefWidth());
        addMarka.setPromptText("Marka");
        
        final TextField addMatrikula = new TextField();
        addMatrikula.setMaxWidth(MatrikulaZut.getPrefWidth());
        addMatrikula.setPromptText("Matrikula");

        final Button addButton = new Button("Gehitu");
        addButton.setOnAction((ActionEvent e) -> {
            Ibilgailuak i = new Ibilgailuak(Integer.parseInt(addId.getText()), addModeloa.getText(), addMarka.getText(), addMatrikula.getText());
            data.add(i);

            addId.clear();
            addModeloa.clear();
            addMarka.clear();
            addMatrikula.clear();
            Kontroladorea.lista_gordexml(data, aukeratutakoa);
            System.out.println("Sartuta");
        });

        final Button removeButton = new Button("Ezabatu");
        removeButton.setOnAction((ActionEvent e) -> {
            Ibilgailuak ibilgailu = table.getSelectionModel().getSelectedItem();
            data.remove(ibilgailu);
            Kontroladorea.lista_gordexml(data, aukeratutakoa);
            System.out.println("Ezabatuta");
        });

        hb.getChildren().addAll(addId, addModeloa, addMarka, addMatrikula, addButton, removeButton);
        hb.setSpacing(3);
        final VBox vbox = new VBox();
        vbox.setSpacing(5);
        vbox.setPadding(new Insets(10, 0, 0, 10));
        vbox.getChildren().addAll(label, table, hb);
        ((Group) scene.getRoot()).getChildren().addAll(vbox);
        stage.setOnCloseRequest((WindowEvent event)-> {
            Kontroladorea.lista_gordexml(data, aukeratutakoa);
        });
        stage.setScene(scene);
        stage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }
}
