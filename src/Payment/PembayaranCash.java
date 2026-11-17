package payment;

public class PembayaranCash extends Pembayaran {
    private final double uangBayar;

    public PembayaranCash(double total, double uangBayar) {
        super(total);
        this.uangBayar = uangBayar;
    }

    @Override
    public String getMetode() {
        return "CASH";
    }

    @Override
    public double getUangBayar() {
        return uangBayar;
    }

    @Override
    public boolean isValid() {
        return uangBayar >= getTotal();
    }

    @Override
    public String getInfoStruk() {
        return "Pembayaran Tunai";
    }
}