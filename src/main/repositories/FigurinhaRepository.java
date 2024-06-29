package main.repositories;

import main.entities.Figurinha;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FigurinhaRepository {
    public List<Figurinha> getAllFigurinhas() {
        String sql = "SELECT * FROM figurinhas";
        List<Figurinha> figurinhas = new ArrayList<>();

        try (Connection conn = BaseRepository.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Figurinha figurinha = new Figurinha(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("numero"),
                        rs.getString("descricao"),
                        rs.getInt("pagina"),
                        rs.getString("tag"),
                        rs.getBytes("foto")
                );
                figurinhas.add(figurinha);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return figurinhas;
    }

    public void addFigurinha(Figurinha figurinha) {
        String sql = "INSERT INTO figurinhas(nome, numero, descricao, pagina, tag, foto) VALUES(?, ?, ?, ?, ?, ?)";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, figurinha.getNome());
            pstmt.setInt(2, figurinha.getNumero());
            pstmt.setString(3, figurinha.getDescricao());
            pstmt.setInt(4, figurinha.getPagina());
            pstmt.setString(5, figurinha.getTag());
            pstmt.setBytes(6, figurinha.getFoto());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateFigurinha(Figurinha figurinha) {
        String sql = "UPDATE figurinhas SET nome = ?, numero = ?, descricao = ?, pagina = ?, tag = ?, foto = ? WHERE id = ?";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, figurinha.getNome());
            pstmt.setInt(2, figurinha.getNumero());
            pstmt.setString(3, figurinha.getDescricao());
            pstmt.setInt(4, figurinha.getPagina());
            pstmt.setString(5, figurinha.getTag());
            pstmt.setBytes(6, figurinha.getFoto());
            pstmt.setInt(7, figurinha.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteFigurinha(int id) {
        String sql = "DELETE FROM figurinhas WHERE id = ?";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Figurinha getFigurinhaById(int id) {
        String sql = "SELECT * FROM figurinhas WHERE id = ?";
        Figurinha figurinha = null;

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                figurinha = new Figurinha(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getInt("numero"),
                        rs.getString("descricao"),
                        rs.getInt("pagina"),
                        rs.getString("tag"),
                        rs.getBytes("foto")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return figurinha;
    }
}
