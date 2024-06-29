package main.views;

import main.entities.Usuario;
import main.services.UsuarioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class AdminView extends JFrame {
    private JTextField nomeField, senhaField, perfilField;
    private JButton btnAdicionar, btnExcluir, btnEditar, btnFiltrar;
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;
    private UsuarioService usuarioService;

    public AdminView() {
        setTitle("Administração de Usuários");
        setSize(600, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usuarioService = new UsuarioService();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);

        nomeField = new JTextField(20);
        senhaField = new JTextField(20);
        perfilField = new JTextField(20);

        Dimension textFieldSize = new Dimension(200, 30);
        nomeField.setPreferredSize(textFieldSize);
        nomeField.setMinimumSize(textFieldSize);
        senhaField.setPreferredSize(textFieldSize);
        senhaField.setMinimumSize(textFieldSize);
        perfilField.setPreferredSize(textFieldSize);
        perfilField.setMinimumSize(textFieldSize);

        btnAdicionar = new JButton("Adicionar");
        btnExcluir = new JButton("Excluir");
        btnEditar = new JButton("Editar");
        btnFiltrar = new JButton("Filtrar");

        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Senha", "Perfil"}, 0);
        tabelaUsuarios = new JTable(tableModel);

        refreshTable();

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Nome:"), constraints);
        constraints.gridx = 1;
        panel.add(nomeField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Senha:"), constraints);
        constraints.gridx = 1;
        panel.add(senhaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Perfil:"), constraints);
        constraints.gridx = 1;
        panel.add(perfilField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(btnAdicionar, constraints);
        constraints.gridx = 1;
        panel.add(btnExcluir, constraints);
        constraints.gridx = 2;
        panel.add(btnEditar, constraints);
        constraints.gridx = 3;
        panel.add(btnFiltrar, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 4;
        panel.add(new JScrollPane(tabelaUsuarios), constraints);

        // Configuração de listeners para botões
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                adicionarUsuario();
            }
        });

        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                excluirUsuario();
            }
        });

        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editarUsuario();
            }
        });

        btnFiltrar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                filtrarUsuarios();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void adicionarUsuario() {
        String nome = nomeField.getText();
        String senha = senhaField.getText();
        String perfil = perfilField.getText();

        if (!nome.isEmpty() && !senha.isEmpty() && !perfil.isEmpty()) {
            Usuario novoUsuario = new Usuario(-1, nome, senha, perfil); // -1 ou qualquer valor, o ID será gerado pelo banco
            usuarioService.addUsuario(novoUsuario);
            refreshTable();
            limparCampos();
        } else {
            JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
        }
    }

    private void excluirUsuario() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row != -1) {
            int id = (int) tabelaUsuarios.getValueAt(row, 0);
            Usuario usuario = usuarioService.getUsuarioById(id);
            usuarioService.deleteUsuario(usuario);
            refreshTable();
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para excluir!");
        }
    }

    private void editarUsuario() {
        int row = tabelaUsuarios.getSelectedRow();
        if (row != -1) {
            int id = (int) tabelaUsuarios.getValueAt(row, 0);
            String nome = nomeField.getText();
            String senha = senhaField.getText();
            String perfil = perfilField.getText();

            if (!nome.isEmpty() && !senha.isEmpty() && !perfil.isEmpty()) {
                Usuario usuario = usuarioService.getUsuarioById(id);
                usuario.setNome(nome);
                usuario.setSenha(senha);
                usuario.setPerfil(perfil);
                usuarioService.updateUsuario(usuario);
                refreshTable();
                limparCampos();
            } else {
                JOptionPane.showMessageDialog(this, "Preencha todos os campos!");
            }
        } else {
            JOptionPane.showMessageDialog(this, "Selecione um usuário para editar!");
        }
    }

    private void filtrarUsuarios() {
        String nome = nomeField.getText();
        List<Usuario> usuariosFiltrados = usuarioService.filtrarUsuarios(nome);
        updateTable(usuariosFiltrados);
    }

    private void refreshTable() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        updateTable(usuarios);
    }

    private void updateTable(List<Usuario> usuarios) {
        tableModel.setRowCount(0); // Limpa a tabela
        for (Usuario usuario : usuarios) {
            Object[] row = {usuario.getId(), usuario.getNome(), usuario.getSenha(), usuario.getPerfil()};
            tableModel.addRow(row);
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        senhaField.setText("");
        perfilField.setText("");
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new AdminView();
            }
        });
    }
}
