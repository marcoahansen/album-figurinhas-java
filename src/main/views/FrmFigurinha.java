package main.views;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import main.services.FigurinhaService;
import main.entities.Figurinha;
import java.awt.image.BufferedImage;

public class FrmFigurinha extends JFrame {
    private JTextField nomeField, numeroField, paginaField, descricaoField, tagField;
    private JLabel imagemLabel;
    private JButton btnSalvar, btnSelecionarImagem;
    private byte[] imagemBytes;
    private ImageIcon imagemFigurinha;
    private int albumId;
    private FigurinhaService figurinhaService;
    private Figurinha figurinhaParaEditar;
    private FrmAutoria frmAutoria;

    public FrmFigurinha(int albumId, FrmAutoria frmAutoria) {
        this.albumId = albumId;
        this.figurinhaService = new FigurinhaService();
        this.frmAutoria = frmAutoria;

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
        tagField = new JTextField(20);
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
        panel.add(new JLabel("Tag:"), constraints);
        constraints.gridx = 1;
        panel.add(tagField, constraints);

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
        tagField.setText(figurinha.getTag());
        if (figurinha.getFoto() != null) {
            imagemFigurinha = new ImageIcon(figurinha.getFoto());
            imagemLabel.setIcon(imagemFigurinha);
            imagemBytes = figurinha.getFoto();
        }
    }

    private void selecionarImagem() {
        JFileChooser fileChooser = new JFileChooser();
        int result = fileChooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            try {
                if (!file.isFile() || !isImageFile(file)) {
                    System.out.println("Selected file is not a valid image.");
                    return;
                }

                BufferedImage bufferedImage = ImageIO.read(file);
                if (bufferedImage == null) {
                    System.out.println("Error reading image file.");
                    return;
                }

                ImageIcon imagemFigurinha = new ImageIcon(bufferedImage);
                imagemLabel.setIcon(imagemFigurinha);

                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                ImageIO.write(bufferedImage, getFileExtension(file), stream); // Use appropriate extension
                stream.flush();
                byte[] imagemBytes = stream.toByteArray();
                stream.close();

                this.imagemBytes = imagemBytes;

            } catch (IOException ex) {
                ex.printStackTrace();
                System.out.println("Error processing image: " + ex.getMessage());
            }
        }
    }

    private boolean isImageFile(File file) {
        String extension = getFileExtension(file);
        return extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("png")
                || extension.equalsIgnoreCase("gif") || extension.equalsIgnoreCase("bmp"); // Add more extensions as needed
    }

    private String getFileExtension(File file) {
        String fileName = file.getName();
        int dotPos = fileName.lastIndexOf('.');
        if (dotPos > 0) {
            return fileName.substring(dotPos + 1).toLowerCase();
        }
        return "";
    }

    private void salvarOuAtualizarFigurinha() {
        String nome = nomeField.getText();
        int numero = Integer.parseInt(numeroField.getText());
        int pagina = Integer.parseInt(paginaField.getText());
        String descricao = descricaoField.getText();
        String tag = tagField.getText();

        Figurinha figurinha = new Figurinha(-1, nome, numero, descricao, pagina, tag, imagemBytes, albumId);

        if (figurinhaParaEditar != null) {
            figurinha.setId(figurinhaParaEditar.getId());
            figurinhaService.updateFigurinha(figurinha);
            JOptionPane.showMessageDialog(this, "Figurinha atualizada com sucesso!");
        } else {
            figurinhaService.addFigurinha(figurinha);
            JOptionPane.showMessageDialog(this, "Figurinha salva com sucesso!");
        }

        if (frmAutoria != null) {
            frmAutoria.refreshTable();
        }

        dispose();
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                new FrmFigurinha(1, null); // Passando um exemplo de albumId
            }
        });
    }
}
