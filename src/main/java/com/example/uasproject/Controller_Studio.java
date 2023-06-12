package com.example.uasproject;

import com.example.uasproject.classes.Studio;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.Objects;

public class Controller_Studio {

    @FXML
    private TableView<Studio> studioView;
    @FXML
    private TableColumn<Studio, Integer> id_studio;
    @FXML
    private TableColumn<Studio, String> nama_studio;
    @FXML
    private TableColumn<Studio, Integer> kapasitas;
    @FXML
    private TableColumn<Studio, String> no_kursi;
    @FXML
    private TextField nama_studioField;
    @FXML
    private Spinner<Integer> kapasitasField;
    @FXML
    private TextField no_kursiField;
    @FXML
    private Label errorMessage;
    private final ObservableList<Studio> listStudio = FXCollections.observableArrayList();
    private final Connection db = DatabaseConnector.getConnection();
    private int index = -1;
    private final SpinnerValueFactory<Integer> valueFactory =
            new SpinnerValueFactory.IntegerSpinnerValueFactory(1, Integer.MAX_VALUE);
    @FXML
    void initialize() throws SQLException {
        valueFactory.setValue(1);
        kapasitasField.setValueFactory(valueFactory);

        errorMessage.setStyle("-fx-color: #0000ff; ");
        id_studio.setCellValueFactory(new PropertyValueFactory<>("id_studio"));
        nama_studio.setCellValueFactory(new PropertyValueFactory<>("nama_studio"));
        kapasitas.setCellValueFactory(new PropertyValueFactory<>("kapasitas"));
        no_kursi.setCellValueFactory(new PropertyValueFactory<>("no_kursi"));
        updateTable();
    }
    private boolean checkIsi(){
        return Objects.equals(nama_studioField.getText(), "")
                || Objects.equals(no_kursiField.getText(), "");
        //kapasitas gaperlu dicek soalnya udah pasti ada isinya
    }
    @FXML
    void insert() {
        if (checkIsi()){
            errorMessage.setText("Please fill all the data!");
        } else {
            try {
                String name = nama_studioField.getText();
                int cap = kapasitasField.getValue();
                String numK = no_kursiField.getText();

                //insert to db
                String connectQuery = String.format("INSERT INTO `studio` (`id_studio`, `nama_studio`, `kapasitas`, `no_kursi`) " +
                                "VALUES (NULL, '%s', '%d', '%s');",name, cap, numK);
                Statement statement = db.createStatement();
                statement.executeUpdate(connectQuery);

                updateTable();
                reset();
            } catch (SQLException e) {
                errorMessage.setText("Something wrong with the table");
            } catch (RuntimeException e){
                errorMessage.setText("Please insert the correct data");
            }
        }
    }
    private void reset(){
        nama_studioField.setText("");
        no_kursiField.setText("");
        errorMessage.setText("");
        valueFactory.setValue(1);
        kapasitasField.setValueFactory(valueFactory);
    }
    private void updateTable() throws SQLException {
        listStudio.clear();
        String connectQuery = "SELECT * FROM `studio`";
        Statement statement = db.createStatement();
        ResultSet queryOutput = statement.executeQuery(connectQuery);
        while (queryOutput.next()){
            listStudio.add(new Studio(
                    queryOutput.getInt("id_studio"),
                    queryOutput.getString("nama_studio"),
                    queryOutput.getInt("kapasitas"),
                    queryOutput.getString("no_kursi")));
        }
        studioView.setItems(listStudio);
        valueFactory.setValue(1);
        kapasitasField.setValueFactory(valueFactory);
        reset();
    }

    @FXML
    void update() throws SQLException {
        if (index == -1)
            return;
        if (checkIsi())
            errorMessage.setText("Please fill all the data!");
        else {
            int id = listStudio.get(index).getId_studio();
            String name = nama_studioField.getText();
            int cap = kapasitasField.getValue();
            String numK = no_kursiField.getText();
            listStudio.set(index, new Studio(
                    id,
                    name,
                    cap,
                    numK
            ));
            studioView.setItems(listStudio);
            String connectQuery = String.format("UPDATE studio SET nama_studio = '%s', kapasitas = '%d', no_kursi = '%s' WHERE id_studio = %d"
            ,name, cap, numK, id);
            Statement statement = db.createStatement();
            statement.executeUpdate(connectQuery);
        }
        updateTable();
    }

    @FXML
    void getRowIndex() {
        index = studioView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Studio studioTemp = listStudio.get(index);
            nama_studioField.setText(studioTemp.getNama_studio());
            valueFactory.setValue(studioTemp.getKapasitas());
            kapasitasField.setValueFactory(valueFactory);
            no_kursiField.setText(studioTemp.getNo_kursi());
        } else reset();
    }
    @FXML
    private void min100(){
        plusmin(-100);
    }
    @FXML
    private void min10(){
        plusmin(-10);
    }
    @FXML
    private void plus10(){
        plusmin(10);
    }
    @FXML
    private void plus100(){
        plusmin(100);
    }
    private void plusmin(int x){
        valueFactory.setValue(valueFactory.getValue() + x);
    }

    @FXML
    void delete() throws SQLException {
        if (index == -1)
            return;

        int idStudio = listStudio.get(index).getId_studio();
        listStudio.remove(index);

        String connectQuery = "DELETE FROM studio WHERE id_studio=" + idStudio;
        Statement statement = db.createStatement();
        statement.executeUpdate(connectQuery);

        updateTable();
    }
}
