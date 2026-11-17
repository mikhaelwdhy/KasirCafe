package controller;

import model.Menu;
import model.MenuMakanan;
import model.MenuMinuman;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class MenuController {

    public static Menu buatMenu(int id, String kode, String nama, String kategori,
                                double harga, int stok, String keterangan) {
        if ("Makanan".equalsIgnoreCase(kategori)) {
            return new MenuMakanan(id, kode, nama, harga, stok, keterangan);
        }
        return new MenuMinuman(id, kode, nama, harga, stok, keterangan);
    }

    private Menu mapRow(ResultSet rs) throws SQLException {
        return buatMenu(rs.getInt("id"), rs.getString("kode_menu"), rs.getString("nama_menu"),
                rs.getString("kategori"), rs.getDouble("harga"), rs.getInt("stok"),
                rs.getString("keterangan"));
    }

    public List<Menu> getAll() throws SQLException {
        return cari("", "Semua");
    }

    public List<Menu> cari(String keyword, String kategori) throws SQLException {
        boolean filterKategori = kategori != null && !kategori.equals("Semua");
        StringBuilder sql = new StringBuilder(
                "SELECT * FROM menu WHERE (nama_menu LIKE ? OR kode_menu LIKE ?)");
        if (filterKategori) {
            sql.append(" AND kategori = ?");
        }
        sql.append(" ORDER BY kategori, nama_menu");

        List<Menu> hasil = new ArrayList<>();
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql.toString())) {
            String like = "%" + (keyword == null ? "" : keyword) + "%";
            ps.setString(1, like);
            ps.setString(2, like);
            if (filterKategori) {
                ps.setString(3, kategori);
            }
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    hasil.add(mapRow(rs));
                }
            }
        }
        return hasil;
    }

    public void tambah(Menu m) throws SQLException {
        String sql = "INSERT INTO menu (kode_menu, nama_menu, kategori, harga, stok, keterangan) VALUES (?,?,?,?,?,?)";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getKodeMenu());
            ps.setString(2, m.getNamaMenu());
            ps.setString(3, m.getKategori());
            ps.setDouble(4, m.getHarga());
            ps.setInt(5, m.getStok());
            ps.setString(6, m.getKeterangan());
            ps.executeUpdate();
        }
    }

    public void ubah(Menu m) throws SQLException {
        String sql = "UPDATE menu SET kode_menu=?, nama_menu=?, kategori=?, harga=?, stok=?, keterangan=? WHERE id=?";
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement(sql)) {
            ps.setString(1, m.getKodeMenu());
            ps.setString(2, m.getNamaMenu());
            ps.setString(3, m.getKategori());
            ps.setDouble(4, m.getHarga());
            ps.setInt(5, m.getStok());
            ps.setString(6, m.getKeterangan());
            ps.setInt(7, m.getId());
            ps.executeUpdate();
        }
    }

    public void hapus(int id) throws SQLException {
        try (Connection con = DatabaseConnection.getConnection();
             PreparedStatement ps = con.prepareStatement("DELETE FROM menu WHERE id = ?")) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }
}