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
    private JTextField nomeField, senhaField, filtroField;
    private JComboBox<String> perfilComboBox;
    private JButton btnAdicionar, btnExcluir, btnEditar, btnFiltrar, btnLimpar;
    private JTable tabelaUsuarios;
    private DefaultTableModel tableModel;
    private UsuarioService usuarioService;

    public AdminView() {
        setTitle("Administração de Usuários");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        usuarioService = new UsuarioService();

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        nomeField = new JTextField(20);
        nomeField.setPreferredSize(new Dimension(200, 40));
        senhaField = new JTextField(20);
        senhaField.setPreferredSize(new Dimension(200, 40));
        filtroField = new JTextField(20);
        filtroField.setPreferredSize(new Dimension(200, 40));


        perfilComboBox = new JComboBox<>(new String[]{"Colecionador", "Autor", "Administrador"});
        perfilComboBox.setPreferredSize(new Dimension(200, 30));

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Nome:"), constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 3;
        panel.add(nomeField, constraints);
        constraints.gridwidth = 1;

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Senha:"), constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 3;
        panel.add(senhaField, constraints);
        constraints.gridwidth = 1;

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Perfil:"), constraints);
        constraints.gridx = 1;
        constraints.gridwidth = 3;
        panel.add(perfilComboBox, constraints);
        constraints.gridwidth = 1;

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(btnAdicionar = new JButton("Adicionar"), constraints);
        constraints.gridx = 1;
        panel.add(btnExcluir = new JButton("Excluir"), constraints);
        constraints.gridx = 2;
        panel.add(btnEditar = new JButton("Editar"), constraints);

        constraints.gridy = 4;
        constraints.gridx = 0;
        constraints.gridwidth = 4;
        panel.add(filtroField, constraints);
        constraints.gridwidth = 1;
        constraints.gridy = 5;
        constraints.gridx = 1;
        panel.add(btnFiltrar = new JButton("Filtrar"), constraints);
        constraints.gridx = 2;
        panel.add(btnLimpar = new JButton("Limpar"), constraints);


        tableModel = new DefaultTableModel(new String[]{"ID", "Nome", "Senha", "Perfil"}, 0);
        tabelaUsuarios = new JTable(tableModel);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 4;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(new JScrollPane(tabelaUsuarios), constraints);

        refreshTable();

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

        btnLimpar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                LimparFiltros();
            }
        });

        add(panel);
        setVisible(true);
    }

    private void adicionarUsuario() {
        String nome = nomeField.getText();
        String senha = senhaField.getText();
        String perfil = (String) perfilComboBox.getSelectedItem();

        if (!nome.isEmpty() && !senha.isEmpty() && perfil != null) {
            Usuario novoUsuario = new Usuario(-1, nome, senha, perfil);
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
            String perfil = (String) perfilComboBox.getSelectedItem();

            if (!nome.isEmpty() && !senha.isEmpty() && perfil != null) {
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
        String filtro = filtroField.getText();
        List<Usuario> usuariosFiltrados = usuarioService.filtrarUsuarios(filtro);
        updateTable(usuariosFiltrados);
    }

    private void refreshTable() {
        List<Usuario> usuarios = usuarioService.getAllUsuarios();
        updateTable(usuarios);
    }

    private void updateTable(List<Usuario> usuarios) {
        tableModel.setRowCount(0);
        for (Usuario usuario : usuarios) {
            Object[] row = {usuario.getId(), usuario.getNome(), usuario.getSenha(), usuario.getPerfil()};
            tableModel.addRow(row);
        }
    }

    private void limparCampos() {
        nomeField.setText("");
        senhaField.setText("");
        perfilComboBox.setSelectedIndex(0);
        filtroField.setText("");
    }

    private void LimparFiltros() {
        filtroField.setText("");
        refreshTable();
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
