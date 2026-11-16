package model;

public class MenuMakanan extends Menu {

    public MenuMakanan(int id, String kodeMenu, String namaMenu, double harga, int stok, String keterangan) {
        super(id, kodeMenu, namaMenu, "Makanan", harga, stok, keterangan);
    }

    @Override
    public String getInfoPenyajian() {
        return "Kitchen: Disajikan hangat (" + (getKeterangan() == null ? "-" : getKeterangan()) + ")";
    }
}