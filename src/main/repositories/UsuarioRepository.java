package main.repositories;

import main.entities.Usuario;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {
    public List<Usuario> getAllUsuarios() {
        String sql = "SELECT * FROM usuarios";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = BaseRepository.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuarios;
    }

    public void addUsuario(Usuario usuario) {
        String sql = "INSERT INTO usuarios(nome, senha, perfil) VALUES(?, ?, ?)";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getSenha());
            pstmt.setString(3, usuario.getPerfil());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void updateUsuario(Usuario usuario) {
        String sql = "UPDATE usuarios SET nome = ?, senha = ?, perfil = ? WHERE id = ?";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, usuario.getNome());
            pstmt.setString(2, usuario.getSenha());
            pstmt.setString(3, usuario.getPerfil());
            pstmt.setInt(4, usuario.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void deleteUsuario(Usuario usuario) {
        String sql = "DELETE FROM usuarios WHERE id = ?";

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, usuario.getId());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Usuario getUsuarioById(int id) {
        String sql = "SELECT * FROM usuarios WHERE id = ?";
        Usuario usuario = null;

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuario;
    }

    public Usuario getUsuarioByNome(String nome) {
        String sql = "SELECT * FROM usuarios WHERE nome = ?";
        Usuario usuario = null;

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, nome);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuario;
    }

    public List<Usuario> filtrarUsuarios(String nome) {
        String sql = "SELECT * FROM usuarios WHERE nome LIKE ?";
        List<Usuario> usuarios = new ArrayList<>();

        try (Connection conn = BaseRepository.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, "%" + nome + "%");
            ResultSet rs = pstmt.executeQuery();
            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("nome"),
                        rs.getString("senha"),
                        rs.getString("perfil")
                );
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }

        return usuarios;
    }

}
