package model;

public class MenuMinuman extends Menu {

    public MenuMinuman(int id, String kodeMenu, String namaMenu, double harga, int stok, String keterangan) {
        super(id, kodeMenu, namaMenu, "Minuman", harga, stok, keterangan);
    }

    @Override
    public String getInfoPenyajian() {
        return "Barista Bar: Segar diracik (" + (getKeterangan() == null ? "-" : getKeterangan()) + ")";
    }
}