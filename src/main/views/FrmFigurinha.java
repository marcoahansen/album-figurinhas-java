package main.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import javax.imageio.ImageIO;
import main.services.FigurinhaService;
import main.entities.Figurinha;
import java.awt.image.BufferedImage;

public class FrmFigurinha extends JFrame {
    private JTextField nomeField, numeroField, paginaField, descricaoField;
    private JLabel tagLabel, imagemLabel;
    private JButton btnSalvar, btnSelecionarImagem;
    private byte[] imagemBytes;  // Armazena a imagem como byte array
    private ImageIcon imagemFigurinha;  // Armazena a imagem como ImageIcon para exibição
    private int albumId;
    private FigurinhaService figurinhaService;
    private Figurinha figurinhaParaEditar;
    private FrmAutoria frmAutoria; // Referência ao FrmAutoria

    public FrmFigurinha(int albumId, FrmAutoria frmAutoria) {
        this(albumId);
        this.frmAutoria = frmAutoria;
    }

    public FrmFigurinha(int albumId) {
        this.albumId = albumId;
        this.figurinhaService = new FigurinhaService();

        setTitle("Gerenciamento de Figurinhas");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.insets = new Insets(10, 10, 10, 10);
        constraints.fill = GridBagConstraints.HORIZONTAL;

        nomeField = new JTextField(20);
        numeroField = new JTextField(10);
        paginaField = new JTextField(10);
        descricaoField = new JTextField(20);
        tagLabel = new JLabel("Tag: ");
        imagemLabel = new JLabel("Imagem não selecionada");

        btnSelecionarImagem = new JButton("Selecionar Imagem");
        btnSelecionarImagem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                selecionarImagem();
            }
        });

        btnSalvar = new JButton("Salvar");
        btnSalvar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                salvarOuAtualizarFigurinha();
            }
        });

        constraints.gridx = 0;
        constraints.gridy = 0;
        panel.add(new JLabel("Nome:"), constraints);
        constraints.gridx = 1;
        panel.add(nomeField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 1;
        panel.add(new JLabel("Número:"), constraints);
        constraints.gridx = 1;
        panel.add(numeroField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 2;
        panel.add(new JLabel("Página:"), constraints);
        constraints.gridx = 1;
        panel.add(paginaField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 3;
        panel.add(new JLabel("Descrição:"), constraints);
        constraints.gridx = 1;
        panel.add(descricaoField, constraints);

        constraints.gridx = 0;
        constraints.gridy = 4;
        panel.add(tagLabel, constraints);

        constraints.gridx = 0;
        constraints.gridy = 5;
        panel.add(imagemLabel, constraints);

        constraints.gridx = 1;
        panel.add(btnSelecionarImagem, constraints);

        constraints.gridx = 0;
        constraints.gridy = 6;
        constraints.gridwidth = 2;
        panel.add(btnSalvar, constraints);

        add(panel);
        setVisible(true);
    }

    public FrmFigurinha(int albumId, Figurinha figurinhaParaEditar, FrmAutoria frmAutoria) {
        this(albumId, frmAutoria);
        this.figurinhaParaEditar = figurinhaParaEditar;
        preencherCampos(figurinhaParaEditar);
    }

    private void preencherCampos(Figurinha figurinha) {
        nomeField.setText(figurinha.getNome());
        numeroField.setText(String.valueOf(figurinha.getNumero()));
        paginaField.setText(String.valueOf(figurinha.getPagina()));
        descricaoField.setText(figurinha.getDescricao());
        tagLabel.setText("Tag: " + figurinha.getTag());
        // Exibe a foto convertida byte array para ImageIcon
        if (figurinha.getFoto() != null) {
            exibirImagem(figurinha.getFoto());
        }
    }

    private void exibirImagem(byte[] fotoBytes) {
        if (fotoBytes != null) {
            BufferedImage bufferedImage;
            try {
                bufferedImage = ImageIO.read(new ByteArrayInputStream(fotoBytes));
                if (bufferedImage != null) {
                    imagemFigurinha = new ImageIcon(bufferedImage);
                    imagemLabel.setIcon(imagemFigurinha);
                } else {
                    System.out.println("Imagem selecionada é nula ou inválida.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);
        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                BufferedImage bufferedImage = ImageIO.read(file);
                if (bufferedImage != null) {
                    imagemFigurinha = new ImageIcon(bufferedImage);
                    imagemLabel.setIcon(imagemFigurinha);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    ImageIO.write(bufferedImage, "png", baos);
                    baos.flush();
                    imagemBytes = baos.toByteArray();
                    baos.close();
                } else {
                    System.out.println("Imagem selecionada é nula ou inválida.");
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    private void salvarOuAtualizarFigurinha() {
        String nome = nomeField.getText();
        int numero = Integer.parseInt(numeroField.getText());
        int pagina = Integer.parseInt(paginaField.getText());
        String descricao = descricaoField.getText();
        String tag = calcularMD5(imagemBytes);

        Figurinha figurinha = new Figurinha(-1, nome, numero, descricao, pagina, tag, imagemBytes, albumId);

        if (figurinhaParaEditar != null) {
            figurinha.setId(figurinhaParaEditar.getId());
            figurinhaService.updateFigurinha(figurinha);
            JOptionPane.showMessageDialog(this, "Figurinha atualizada com sucesso!");
            if (frmAutoria != null) {
                frmAutoria.refreshTable(); // Chama o método para atualizar a tabela de figurinhas no FrmAutoria
            }
        } else {
            figurinhaService.addFigurinha(figurinha);
            JOptionPane.showMessageDialog(this, "Figurinha salva com sucesso!");
            if (frmAutoria != null) {
                frmAutoria.refreshTable(); // Chama o método para atualizar a tabela de figurinhas no FrmAutoria
            }
        }

        dispose(); // Fechar a janela após salvar ou atualizar
    }

    private String calcularMD5(byte[] imagemBytes) {
        if (imagemBytes == null) {
            return null;
        }
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(imagemBytes);
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmFigurinha(1); // Passando um exemplo de albumId
            }
        });
    }
}
