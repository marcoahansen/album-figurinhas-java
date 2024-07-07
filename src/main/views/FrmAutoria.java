package main.views;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import main.services.FigurinhaService;
import main.entities.Figurinha;

public class FrmAutoria extends JFrame {
    private JTable tabelaFigurinhas;
    private JButton btnAdicionar, btnEditar, btnExcluir;
    private int albumId = 1; // Exemplo de ID do álbum
    private FigurinhaService figurinhaService;
    private DefaultTableModel tableModel;

    public FrmAutoria() {
        this.figurinhaService = new FigurinhaService();

        setTitle("Gerenciamento do Álbum");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        // Dados do álbum
        JLabel nomeAlbumLabel = new JLabel("Nome do Álbum: ");
        JLabel paginasLabel = new JLabel("Páginas: 12");
        JLabel capaLabel = new JLabel("Capa do Álbum: capa.png");

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(nomeAlbumLabel, constraints);

        constraints.gridx = 1;
        panel.add(new JLabel("Álbum Exemplo"), constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(paginasLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(capaLabel, constraints);

        // Tabela de figurinhas
        String[] colunas = {"ID", "Nome", "Página"};
        Object[][] dados = {}; // Inicialmente vazio, será populado pelo banco de dados
        tableModel = new DefaultTableModel(dados, colunas);
        tabelaFigurinhas = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(tabelaFigurinhas);

        constraints.gridx = 0;
        constraints.gridy = 3;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.BOTH;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        panel.add(scrollPane, constraints);

        // Botões
        btnAdicionar = new JButton("Adicionar");
        btnAdicionar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmFigurinha(albumId, FrmAutoria.this); // Passa a referência de FrmAutoria
            }
        });

        btnEditar = new JButton("Editar");
        btnEditar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelaFigurinhas.getSelectedRow();
                if (selectedRow != -1) {
                    int figurinhaId = (int) tabelaFigurinhas.getValueAt(selectedRow, 0);
                    Figurinha figurinha = figurinhaService.getFigurinhaById(figurinhaId);
                    new FrmFigurinha(albumId, figurinha, FrmAutoria.this); // Passa a referência de FrmAutoria
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma figurinha para editar.");
                }
            }
        });

        btnExcluir = new JButton("Excluir");
        btnExcluir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = tabelaFigurinhas.getSelectedRow();
                if (selectedRow != -1) {
                    int figurinhaId = (int) tabelaFigurinhas.getValueAt(selectedRow, 0);
                    figurinhaService.deleteFigurinha(figurinhaId);
                    // Atualizar a tabela de figurinhas
                    refreshTable();
                } else {
                    JOptionPane.showMessageDialog(null, "Selecione uma figurinha para excluir.");
                }
            }
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdicionar);
        buttonPanel.add(btnEditar);
        buttonPanel.add(btnExcluir);

        constraints.gridx = 0;
        constraints.gridy = 4;
        constraints.gridwidth = 2;
        constraints.fill = GridBagConstraints.NONE;
        constraints.weightx = 0;
        constraints.weighty = 0;
        panel.add(buttonPanel, constraints);

        add(panel);
        setVisible(true);

        carregarFigurinhas();
    }

    private void carregarFigurinhas() {
        List<Figurinha> figurinhas = figurinhaService.getAllFigurinhas();
        atualizarModeloTabela(figurinhas);
    }

    private void atualizarModeloTabela(List<Figurinha> figurinhas) {
        // Limpa os dados atuais da tabela
        tableModel.setRowCount(0);

        // Adiciona os novos dados
        for (Figurinha figurinha : figurinhas) {
            Object[] rowData = {figurinha.getId(), figurinha.getNome(), figurinha.getPagina()};
            tableModel.addRow(rowData);
        }
    }

    public void refreshTable() {
        List<Figurinha> figurinhas = figurinhaService.getAllFigurinhas();
        atualizarModeloTabela(figurinhas);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmAutoria();
            }
        });
    }
}
