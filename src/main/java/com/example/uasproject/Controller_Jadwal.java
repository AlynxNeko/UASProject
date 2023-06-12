package com.example.uasproject;

import com.example.uasproject.classes.Jadwal;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.*;
import java.util.Date;
import java.util.Objects;

// ALTER TABLE tablename AUTO_INCREMENT = 1

public class Controller_Jadwal {
    @FXML
    private TableView<Jadwal> jadwalView;
    @FXML
    private TableColumn<Jadwal, Integer> id_jadwal;
    @FXML
    private TableColumn<Jadwal, Date> tanggal;
    @FXML
    private TableColumn<Jadwal, Time> jam_mulai;
    @FXML
    private TableColumn<Jadwal, Time> jam_selesai;
    @FXML
    private DatePicker tanggalField;
    @FXML
    private TextField jamMulaiField;
    @FXML
    private TextField jamSelesaiField;
    @FXML
    private Label errorMessage;
    private final ObservableList<Jadwal> listJadwal = FXCollections.observableArrayList();
    private final Connection db = DatabaseConnector.getConnection();
    private int index = -1;
    @FXML
    void initialize() throws SQLException {
        errorMessage.setStyle("-fx-color: #0000ff; ");
        tanggalField.getEditor().setDisable(true);
        tanggalField.getEditor().setOpacity(1);
        id_jadwal.setCellValueFactory(new PropertyValueFactory<>("id_jadwal"));
        tanggal.setCellValueFactory(new PropertyValueFactory<>("tanggal"));
        jam_mulai.setCellValueFactory(new PropertyValueFactory<>("jam_mulai"));
        jam_selesai.setCellValueFactory(new PropertyValueFactory<>("jam_selesai"));
        updateTable();
    }
    private boolean checkIsi(){
        return Objects.equals(tanggalField.getEditor().getText(), "")
                || Objects.equals(jamMulaiField.getText(), "")
                || Objects.equals(jamSelesaiField.getText(), "");
    }
    @FXML
    void insert() {
        if (checkIsi()){
            errorMessage.setText("Please fill all the data!");
        } else {
            try {
                java.sql.Date date = java.sql.Date.valueOf(tanggalField.getValue());
                Time timeStart = textField_to_time(jamMulaiField);
                Time timeEnd = textField_to_time(jamSelesaiField);

                //insert to db
                String connectQuery = "INSERT INTO `jadwal` (`id_jadwal`, `tanggal`, `jam_mulai`, `jam_selesai`) VALUES (NULL, '" + date + "', '" + timeStart + "', '" + timeEnd + "');";
                Statement statement = db.createStatement();
                statement.executeUpdate(connectQuery);

                updateTable();
                reset();
            } catch (SQLException e) {
                errorMessage.setText("Something wrong with the table");
            } catch (RuntimeException e){
                errorMessage.setText("Please insert the correct time format");
            }
        }
    }
    private void reset(){
        tanggalField.setValue(null);
        jamMulaiField.setText("");
        jamSelesaiField.setText("");
        errorMessage.setText("");
    }
    private void updateTable() throws SQLException {
        listJadwal.clear();
        String connectQuery = "SELECT * FROM `jadwal`";
        Statement statement = db.createStatement();
        ResultSet queryOutput = statement.executeQuery(connectQuery);
        while (queryOutput.next()){
            listJadwal.add(new Jadwal(
                    queryOutput.getInt("id_jadwal"),
                    queryOutput.getDate("tanggal"),
                    queryOutput.getTime("jam_mulai"),
                    queryOutput.getTime("jam_selesai")));
        }
        jadwalView.setItems(listJadwal);
    }

    private Time textField_to_time(TextField jam) {
        return Time.valueOf(jam.getText());
    }

    @FXML
    void update() throws SQLException {
        if (index == -1)
            return;
        if (checkIsi())
            errorMessage.setText("Please fill all the data!");
        else {
            int id = listJadwal.get(index).getId_jadwal();
            java.sql.Date date = java.sql.Date.valueOf(tanggalField.getValue());
            Time timeStart = textField_to_time(jamMulaiField);
            Time timeEnd = textField_to_time(jamSelesaiField);
            listJadwal.set(index, new Jadwal(
                    id,
                    date,
                    timeStart,
                    timeEnd
            ));
            jadwalView.setItems(listJadwal);
            String connectQuery = "UPDATE jadwal SET tanggal = '"+date+"', jam_mulai = '"+timeStart+".000000', jam_selesai = '"+timeEnd+".000000' WHERE id_jadwal = " + id;
            Statement statement = db.createStatement();
            statement.executeUpdate(connectQuery);
        }
        updateTable();
    }

    @FXML
    void getRowIndex() {
        index = jadwalView.getSelectionModel().getSelectedIndex();
        if (index != -1) {
            Jadwal jadwalTemp = listJadwal.get(index);
            tanggalField.setValue(jadwalTemp.getTanggal().toLocalDate());
            jamMulaiField.setText(String.valueOf(jadwalTemp.getJam_mulai()));
            jamSelesaiField.setText(String.valueOf(jadwalTemp.getJam_selesai()));
        } else reset();
    }

    @FXML
    void delete() throws SQLException {
        if (index == -1)
            return;

        int idJadwal = listJadwal.get(index).getId_jadwal();
        listJadwal.remove(index);

        String connectQuery = "DELETE FROM jadwal WHERE id_jadwal=" + idJadwal;
        Statement statement = db.createStatement();
        statement.executeUpdate(connectQuery);

        updateTable();
    }
}

