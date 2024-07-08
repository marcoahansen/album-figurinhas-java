package main.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.List;
import main.services.FigurinhaService;
import main.entities.Figurinha;

public class FrmAlbum extends JFrame {
    private JPanel panelAlbum;
    private int albumId = 1; // Exemplo de ID do álbum
    private FigurinhaService figurinhaService;

    public FrmAlbum() {
        this.figurinhaService = new FigurinhaService();

        setTitle("Álbum");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        panelAlbum = new JPanel(new GridLayout(0, 3, 10, 10)); // Layout de grid com 3 colunas
        carregarAlbum();

        JScrollPane scrollPane = new JScrollPane(panelAlbum);
        add(scrollPane, BorderLayout.CENTER);

        JButton btnAdicionarFigurinha = new JButton("Adicionar Figurinha");
        btnAdicionarFigurinha.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new FrmNovaFigurinha(albumId);
            }
        });

        JPanel panelBotoes = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panelBotoes.add(btnAdicionarFigurinha);

        add(panelBotoes, BorderLayout.SOUTH);

        setVisible(true);
    }

    private void carregarAlbum() {
        List<Figurinha> figurinhas = figurinhaService.getAllFigurinhas();

        for (Figurinha figurinha : figurinhas) {
            JPanel panelFigurinha = new JPanel(new BorderLayout());
            panelFigurinha.setBorder(BorderFactory.createLineBorder(Color.BLACK)); // Borda para melhor visualização

            if (figurinha.getFoto() != null) {
                ImageIcon imagem = new ImageIcon(figurinha.getFoto());
                JLabel labelFoto = new JLabel(imagem);
                panelFigurinha.add(labelFoto, BorderLayout.CENTER);
            } else {
                JPanel panelSemImagem = new JPanel();
                panelSemImagem.setBackground(Color.LIGHT_GRAY);
                JLabel labelNome = new JLabel(figurinha.getNome(), SwingConstants.CENTER);
                panelSemImagem.add(labelNome);
                panelFigurinha.add(panelSemImagem, BorderLayout.CENTER);
            }

            JLabel labelNomeFigurinha = new JLabel(figurinha.getNome(), SwingConstants.CENTER);
            panelFigurinha.add(labelNomeFigurinha, BorderLayout.SOUTH);

            panelAlbum.add(panelFigurinha);

            panelFigurinha.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if (e.getClickCount() == 2) { // Verificar se foi um duplo clique
                        mostrarDetalhesFigurinha(figurinha);
                    }
                }
            });
        }
    }

    private void mostrarDetalhesFigurinha(Figurinha figurinha) {
        JOptionPane.showMessageDialog(this,
                "Detalhes da Figurinha:\n" +
                        "Nome: " + figurinha.getNome() + "\n" +
                        "Número: " + figurinha.getNumero() + "\n" +
                        "Página: " + figurinha.getPagina() + "\n" +
                        "Descrição: " + figurinha.getDescricao(),
                "Detalhes da Figurinha",
                JOptionPane.INFORMATION_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new FrmAlbum();
        });
    }
}
