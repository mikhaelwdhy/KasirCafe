package controller;

import model.DetailTransaksi;
import model.Kasir;
import model.Menu;
import model.Transaksi;
import model.User;
import payment.Pembayaran;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class TransaksiController {
    public static final String NAMA_TOKO = "CAFE POS";

    public String generateNoFaktur() {
        String ts = new SimpleDateFormat("yyyyMMddHHmmss").format(new java.util.Date());
        return "TRX-" + ts + "-" + (100 + new Random().nextInt(900));
    }

    public Transaksi simpan(User kasir, List<DetailTransaksi> items, Pembayaran pembayaran) throws SQLException {
        if (items == null || items.isEmpty()) {
            throw new IllegalArgumentException("Keranjang kosong");
        }
        if (!pembayaran.isValid()) {
            throw new IllegalStateException("Pembayaran belum valid");
        }
        double total = 0;
        for (DetailTransaksi d : items) {
            total += d.getSubtotal();
        }
        if (Math.abs(total - pembayaran.getTotal()) > 0.01) {
            throw new IllegalStateException("Total pembayaran tidak sesuai dengan isi keranjang");
        }

        Transaksi t = new Transaksi();
        t.setNoFaktur(generateNoFaktur());
        t.setTanggal(new Timestamp(System.currentTimeMillis()));
        t.setKasir(kasir);
        t.setTotalHarga(total);
        t.setMetodePembayaran(pembayaran.getMetode());
        t.setUangBayar(pembayaran.getUangBayar());
        t.setKembalian(pembayaran.getKembalian());
        t.setItems(new ArrayList<>(items));

        String sqlTrx = "INSERT INTO transaksi (no_faktur, tanggal, id_kasir, total_harga, metode_pembayaran, uang_bayar, kembalian) VALUES (?,?,?,?,?,?,?)";
        String sqlStok = "UPDATE menu SET stok = stok - ? WHERE id = ? AND stok >= ?";
        String sqlDetail = "INSERT INTO detail_transaksi (id_transaksi, id_menu, jumlah, harga_satuan, subtotal) VALUES (?,?,?,?,?)";

        try (Connection con = DatabaseConnection.getConnection()) {
            con.setAutoCommit(false);
            try {
                try (PreparedStatement ps = con.prepareStatement(sqlTrx, Statement.RETURN_GENERATED_KEYS)) {
                    ps.setString(1, t.getNoFaktur());
                    ps.setTimestamp(2, t.getTanggal());
                    ps.setInt(3, kasir.getId());
                    ps.setDouble(4, t.getTotalHarga());
                    ps.setString(5, t.getMetodePembayaran());
                    ps.setDouble(6, t.getUangBayar());
                    ps.setDouble(7, t.getKembalian());
                    ps.executeUpdate();
                    try (ResultSet rs = ps.getGeneratedKeys()) {
                        if (rs.next()) {
                            t.setIdTransaksi(rs.getInt(1));
                        }
                    }
                }

                try (PreparedStatement psStok = con.prepareStatement(sqlStok);
                     PreparedStatement psDet = con.prepareStatement(sqlDetail, Statement.RETURN_GENERATED_KEYS)) {
                    for (DetailTransaksi d : items) {
                        psStok.setInt(1, d.getJumlah());
                        psStok.setInt(2, d.getMenu().getId());
                        psStok.setInt(3, d.getJumlah());
                        if (psStok.executeUpdate() == 0) {
                            throw new SQLException("Stok tidak cukup untuk menu: " + d.getMenu().getNamaMenu());
                        }

                        d.setIdTransaksi(t.getIdTransaksi());
                        psDet.setInt(1, t.getIdTransaksi());
                        psDet.setInt(2, d.getMenu().getId());
                        psDet.setInt(3, d.getJumlah());
                        psDet.setDouble(4, d.getHargaSatuan());
                        psDet.setDouble(5, d.getSubtotal());
                        psDet.executeUpdate();
                        try (ResultSet rs = psDet.getGeneratedKeys()) {
                            if (rs.next()) {
                                d.setIdDetail(rs.getInt(1));
                            }
                        }
                    }
                }
                con.commit();
            } catch (SQLException | RuntimeException e) {
                con.rollback();
                throw e;
            }
        }
        return t;
    }

    public List<Transaksi> getRiwayat(boolean hanyaHariIni) throws SQLException {
        String sql = "SELECT t.*, u.username, u.nama_lengkap FROM transaksi t "
                + "JOIN users u ON u.id = t.id_kasir"
                + (hanyaHariIni ? " WHERE DATE(t.tanggal) = CURDATE()" : "")
                + " ORDER BY t.tanggal DESC";
        List<Transaksi> hasil = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                Transaksi t = new Transaksi();
                t.setIdTransaksi(rs.getInt("id_transaksi"));
                t.setNoFaktur(rs.getString("no_faktur"));
                t.setTanggal(rs.getTimestamp("tanggal"));
                t.setKasir(new Kasir(rs.getInt("id_kasir"), rs.getString("username"), "", rs.getString("nama_lengkap")));
                t.setTotalHarga(rs.getDouble("total_harga"));
                t.setMetodePembayaran(rs.getString("metode_pembayaran"));
                t.setUangBayar(rs.getDouble("uang_bayar"));
                t.setKembalian(rs.getDouble("kembalian"));
                hasil.add(t);
            }
        }
        return hasil;
    }

    public List<DetailTransaksi> getDetail(int idTransaksi) throws SQLException {
        String sql = "SELECT d.id_detail, d.jumlah, d.harga_satuan, m.id AS id_menu, m.kode_menu, "
                + "m.nama_menu, m.kategori, m.stok, m.keterangan "
                + "FROM detail_transaksi d JOIN menu m ON m.id = d.id_menu WHERE d.id_transaksi = ?";
        List<DetailTransaksi> hasil = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setInt(1, idTransaksi);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    // Harga menu diisi dengan harga saat transaksi (harga_satuan), bukan harga menu sekarang
                    Menu m = MenuController.buatMenu(rs.getInt("id_menu"), rs.getString("kode_menu"),
                            rs.getString("nama_menu"), rs.getString("kategori"),
                            rs.getDouble("harga_satuan"), rs.getInt("stok"), rs.getString("keterangan"));
                    DetailTransaksi d = new DetailTransaksi(m, rs.getInt("jumlah"));
                    d.setIdDetail(rs.getInt("id_detail"));
                    d.setIdTransaksi(idTransaksi);
                    hasil.add(d);
                }
            }
        }
        return hasil;
    }

    // ---------- Struk ----------

    public String buatStruk(Transaksi t) {
        final int w = 34;
        String garis = "-".repeat(w);
        StringBuilder sb = new StringBuilder();
        sb.append(tengah(NAMA_TOKO, w)).append("\n");
        sb.append(garis).append("\n");
        sb.append("No    : ").append(t.getNoFaktur()).append("\n");
        sb.append("Tgl   : ").append(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(t.getTanggal())).append("\n");
        sb.append("Kasir : ").append(t.getKasir().getNamaLengkap()).append("\n");
        sb.append(garis).append("\n");
        for (DetailTransaksi d : t.getItems()) {
            sb.append(d.getMenu().getNamaMenu()).append("\n");
            sb.append(baris("  " + d.getJumlah() + " x " + angka(d.getHargaSatuan()), angka(d.getSubtotal()), w)).append("\n");
        }
        sb.append(garis).append("\n");
        sb.append(baris("TOTAL", "Rp " + angka(t.getTotalHarga()), w)).append("\n");
        sb.append(baris("Bayar (" + t.getMetodePembayaran() + ")", "Rp " + angka(t.getUangBayar()), w)).append("\n");
        sb.append(baris("Kembalian", "Rp " + angka(t.getKembalian()), w)).append("\n");
        sb.append(garis).append("\n");
        sb.append(tengah("Terima kasih!", w)).append("\n");
        return sb.toString();
    }

    private String angka(double v) {
        return String.format(Locale.GERMANY, "%,.0f", v);
    }

    private String baris(String kiri, String kanan, int w) {
        int spasi = Math.max(1, w - kiri.length() - kanan.length());
        return kiri + " ".repeat(spasi) + kanan;
    }

    private String tengah(String s, int w) {
        return " ".repeat(Math.max(0, (w - s.length()) / 2)) + s;
    }
}