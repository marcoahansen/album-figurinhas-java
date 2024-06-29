package main.services;

import main.entities.Usuario;
import main.repositories.UsuarioRepository;

import java.util.List;

public class UsuarioService {
    private UsuarioRepository usuarioRepository;

    public UsuarioService() {
        this.usuarioRepository = new UsuarioRepository();
    }

    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.getAllUsuarios();
    }

    public void addUsuario(Usuario usuario) {
        usuarioRepository.addUsuario(usuario);
    }

    public void deleteUsuario(Usuario usuario) {
        usuarioRepository.deleteUsuario(usuario);
    }

    public void updateUsuario(Usuario usuario) {
        usuarioRepository.updateUsuario(usuario);
    }

    public List<Usuario> filtrarUsuarios(String nome) {
        return usuarioRepository.filtrarUsuarios(nome);
    }

    public Usuario getUsuarioById(int id) {
        return usuarioRepository.getUsuarioById(id);
    }

    public Usuario getUsuarioByNome(String nome) {
        return usuarioRepository.getUsuarioByNome(nome);
    }

}
