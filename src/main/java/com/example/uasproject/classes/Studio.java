package com.example.uasproject.classes;

public class Studio {
    private final int id_studio, kapasitas;
    private final String nama_studio, no_kursi;

    public int getId_studio() {
        return id_studio;
    }

    public int getKapasitas() {
        return kapasitas;
    }

    public String getNama_studio() {
        return nama_studio;
    }

    public String getNo_kursi() {
        return no_kursi;
    }

    public Studio(int idStudio, String namaStudio, int kapasitas, String noKursi) {
        id_studio = idStudio;
        nama_studio = namaStudio;
        this.kapasitas = kapasitas;
        no_kursi = noKursi;
    }
}
