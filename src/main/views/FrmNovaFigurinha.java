package main.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import main.services.FigurinhaService;
import main.entities.Figurinha;

public class FrmNovaFigurinha extends JFrame {
    private JTextField tagField;
    private JLabel nomeLabel, numeroLabel, paginaLabel, descricaoLabel, imagemLabel;
    private JButton btnBuscar, btnInserir;
    private FigurinhaService figurinhaService;
    private Figurinha figurinhaEncontrada;

    public FrmNovaFigurinha(int albumId) {
        this.figurinhaService = new FigurinhaService();

        setTitle("Nova Figurinha");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        tagField = new JTextField(20);
        nomeLabel = new JLabel("Nome: ");
        numeroLabel = new JLabel("Número: ");
        paginaLabel = new JLabel("Página: ");
        descricaoLabel = new JLabel("Descrição: ");
        imagemLabel = new JLabel("Imagem não selecionada");

        btnBuscar = new JButton("Buscar");
        btnBuscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarFigurinha();
            }
        });

        btnInserir = new JButton("Inserir");
        btnInserir.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                inserirFigurinha(albumId);
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Tag:"), constraints);
        constraints.gridx = 1;
        panel.add(tagField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(btnBuscar, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(nomeLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(numeroLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(paginaLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(descricaoLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        panel.add(imagemLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 7;
        panel.add(btnInserir, constraints);

        add(panel);
        setVisible(true);
    }

    private void buscarFigurinha() {
        String tag = tagField.getText();
        figurinhaEncontrada = figurinhaService.getFigurinhaPorTag(tag);
        if (figurinhaEncontrada != null) {
            nomeLabel.setText("Nome: " + figurinhaEncontrada.getNome());
            numeroLabel.setText("Número: " + figurinhaEncontrada.getNumero());
            paginaLabel.setText("Página: " + figurinhaEncontrada.getPagina());
            descricaoLabel.setText("Descrição: " + figurinhaEncontrada.getDescricao());
            imagemLabel.setIcon(new ImageIcon(figurinhaEncontrada.getFoto()));
        } else {
            JOptionPane.showMessageDialog(this, "Figurinha não encontrada.");
        }
    }

    private void inserirFigurinha(int albumId) {
        if (figurinhaEncontrada != null) {
            figurinhaEncontrada.setAlbumId(albumId);
            figurinhaService.addFigurinha(figurinhaEncontrada);
            JOptionPane.showMessageDialog(this, "Figurinha inserida com sucesso!");
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Nenhuma figurinha encontrada para inserir.");
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new FrmNovaFigurinha(1)); // Exemplo de albumId
    }
}
