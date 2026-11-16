package model;

public class DetailTransaksi {
    private int idDetail;
    private int idTransaksi;
    private Menu menu;
    private int jumlah;
    private double hargaSatuan;
    private double subtotal;

    public DetailTransaksi(Menu menu, int jumlah) {
        this.menu = menu;
        this.jumlah = jumlah;
        this.hargaSatuan = menu.getHarga();
        this.subtotal = this.hargaSatuan * jumlah;
    }

    public int getIdDetail() {
        return idDetail;
    }

    public void setIdDetail(int idDetail) {
        this.idDetail = idDetail;
    }

    public int getIdTransaksi() {
        return idTransaksi;
    }

    public void setIdTransaksi(int idTransaksi) {
        this.idTransaksi = idTransaksi;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
        this.subtotal = this.hargaSatuan * jumlah;
    }

    public double getHargaSatuan() {
        return hargaSatuan;
    }

    public double getSubtotal() {
        return subtotal;
    }
}