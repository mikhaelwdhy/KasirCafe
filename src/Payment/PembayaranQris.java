package payment;

public class PembayaranQris extends Pembayaran {
    private final String referensi;
    private boolean terkonfirmasi = false;

    public PembayaranQris(double total) {
        super(total);
        this.referensi = "QRIS" + System.currentTimeMillis();
    }

    public String getReferensi() {
        return referensi;
    }

    public String getPayload() {
        return "QRIS|" + referensi + "|" + (long) getTotal();
    }

    public void konfirmasi() {
        this.terkonfirmasi = true;
    }

    @Override
    public String getMetode() {
        return "QRIS";
    }

    @Override
    public double getUangBayar() {
        return terkonfirmasi ? getTotal() : 0;
    }

    @Override
    public boolean isValid() {
        return terkonfirmasi;
    }

    @Override
    public String getInfoStruk() {
        return "QRIS Ref: " + referensi;
    }
}