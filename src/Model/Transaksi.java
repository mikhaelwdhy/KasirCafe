package model;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class Transaksi {
    private int idTransaksi;
    private String noFaktur;
    private Timestamp tanggal;
    private User kasir;
    private double totalHarga;
    private String metodePembayaran;
    private double uangBayar;
    private double kembalian;
    private List<DetailTransaksi> items = new ArrayList<>();

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public String getNoFaktur() {
        return noFaktur;
    }

    public void setNoFaktur(String noFaktur) {
        this.noFaktur = noFaktur;
    }

    public Timestamp getTanggal() {
        return tanggal;
    }

    public void setTanggal(Timestamp tanggal) {
        this.tanggal = tanggal;
    }

    public User getKasir() {
        return kasir;
    }

    public void setKasir(User kasir) {
        this.kasir = kasir;
    }

    public double getTotalHarga() {
        return totalHarga;
    }

    public void setTotalHarga(double totalHarga) {
        this.totalHarga = totalHarga;
    }

    public String getMetodePembayaran() {
        return metodePembayaran;
    }

    public void setMetodePembayaran(String metodePembayaran) {
        this.metodePembayaran = metodePembayaran;
    }

    public double getUangBayar() {
        return uangBayar;
    }

    public void setUangBayar(double uangBayar) {
        this.uangBayar = uangBayar;
    }

    public double getKembalian() {
        return kembalian;
    }

    public void setKembalian(double kembalian) {
        this.kembalian = kembalian;
    }

    public List<DetailTransaksi> getItems() {
        return items;
    }

    public void setItems(List<DetailTransaksi> items) {
        this.items = items;
    }
}