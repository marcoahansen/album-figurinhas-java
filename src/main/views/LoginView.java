package main.views;

import main.services.UsuarioService;
import main.entities.Usuario;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class LoginView extends JFrame {
    private JTextField txtNome;
    private JPasswordField txtSenha;
    private JButton btnLogin;
    private UsuarioService usuarioService;

    public LoginView() {
        usuarioService = new UsuarioService();

        setTitle("Login");
        setSize(300, 200);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(null);

        JLabel lblNome = new JLabel("Nome:");
        lblNome.setBounds(30, 30, 80, 25);
        add(lblNome);

        txtNome = new JTextField();
        txtNome.setBounds(100, 30, 150, 25);
        add(txtNome);

        JLabel lblSenha = new JLabel("Senha:");
        lblSenha.setBounds(30, 70, 80, 25);
        add(lblSenha);

        txtSenha = new JPasswordField();
        txtSenha.setBounds(100, 70, 150, 25);
        add(txtSenha);

        btnLogin = new JButton("Login");
        btnLogin.setBounds(100, 110, 150, 25);
        add(btnLogin);

        btnLogin.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nome = txtNome.getText();
                String senha = new String(txtSenha.getPassword());
                autenticarUsuario(nome, senha);
            }
        });
    }

    private void autenticarUsuario(String nome, String senha) {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();

        for (Usuario usuario : usuarios) {
            if (usuario.getNome().equals(nome) && usuario.getSenha().equals(senha)) {
                if (usuario.getPerfil().equals("Administrador")) {
                    JOptionPane.showMessageDialog(this, "Bem-vindo, Administrador!");
                    new AdminView();
                    this.dispose();
                } else if (usuario.getPerfil().equals("Autor")) {
                    JOptionPane.showMessageDialog(this, "Bem-vindo, Autor!");
                    new FrmAutoria();
                    this.dispose();
                } else if (usuario.getPerfil().equals("Colecionador")) {
                    JOptionPane.showMessageDialog(this, "Bem-vindo, Colecionador!");
                }
                return;
            }
        }

        JOptionPane.showMessageDialog(this, "Nome ou senha incorretos", "Erro de autenticação", JOptionPane.ERROR_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new LoginView().setVisible(true);
        });
    }
}
