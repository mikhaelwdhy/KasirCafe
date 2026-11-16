package model;

public class Kasir extends User {

    public Kasir(int id, String username, String password, String namaLengkap) {
        super(id, username, password, namaLengkap, "KASIR");
    }

    @Override
    public String getHakAkses() {
        return "POS Access: Transaksi Penjualan, CASH/QRIS & Cetak Struk";
    }
}