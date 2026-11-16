package model;

public abstract class Menu {
    private int id;
    private String kodeMenu;
    private String namaMenu;
    protected String kategori;
    private double harga;
    private int stok;
    private String keterangan;

    public Menu() {
    }

    public Menu(int id, String kodeMenu, String namaMenu, String kategori, double harga, int stok, String keterangan) {
        this.id = id;
        this.kodeMenu = kodeMenu;
        this.namaMenu = namaMenu;
        this.kategori = kategori;
        this.harga = harga;
        this.stok = stok;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getKodeMenu() {
        return kodeMenu;
    }

    public void setKodeMenu(String kodeMenu) {
        this.kodeMenu = kodeMenu;
    }

    public String getNamaMenu() {
        return namaMenu;
    }

    public void setNamaMenu(String namaMenu) {
        this.namaMenu = namaMenu;
    }

    public String getKategori() {
        return kategori;
    }

    public double getHarga() {
        return harga;
    }

    public void setHarga(double harga) {
        this.harga = harga;
    }

    public int getStok() {
        return stok;
    }

    public void setStok(int stok) {
        this.stok = stok;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

    public abstract String getInfoPenyajian();

    public String getDeskripsiSingkat() {
        return kodeMenu + " - " + namaMenu + " (" + kategori + ")";
    }
}