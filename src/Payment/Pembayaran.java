package payment;

public abstract class Pembayaran {
    private final double total;

    protected Pembayaran(double total) {
        if (total <= 0) {
            throw new IllegalArgumentException("Total tagihan harus lebih dari 0");
        }
        this.total = total;
    }

    public double getTotal() {
        return total;
    }

    public abstract String getMetode();

    public abstract double getUangBayar();

    public abstract boolean isValid();

    public abstract String getInfoStruk();

    public double getKembalian() {
        return Math.max(0, getUangBayar() - total);
    }

    public double getKekurangan() {
        return Math.max(0, total - getUangBayar());
    }
}