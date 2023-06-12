package com.example.uasproject.classes;

import java.sql.Date;
import java.sql.Time;

public class Jadwal {
    private final int id_jadwal;
    private final Date tanggal;
    private final Time jam_mulai;
    private final Time jam_selesai;
    public int getId_jadwal() {
        return id_jadwal;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public Time getJam_mulai() {
        return jam_mulai;
    }

    public Time getJam_selesai() {
        return jam_selesai;
    }

    public Jadwal(int idJadwal, Date tanggal, Time jamMulai, Time jamSelesai) {
        id_jadwal = idJadwal;
        this.tanggal = tanggal;
        jam_mulai = jamMulai;
        jam_selesai = jamSelesai;
    }
}
