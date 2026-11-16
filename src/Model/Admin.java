package model;

public class Admin extends User {

    public Admin(int id, String username, String password, String namaLengkap) {
        super(id, username, password, namaLengkap, "ADMIN");
    }

    @Override
    public String getHakAkses() {
        return "Full Access: CRUD Menu, Stok & Laporan Penjualan";
    }
}