package rnplayground;

import org.neuroph.core.NeuralNetwork;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class ClassificaFormasGeometricas extends JFrame {
  class DesenhaFormaCanvas extends Canvas implements MouseListener, MouseMotionListener {
    private java.util.List<Point> pontos = new ArrayList<>();
    private boolean isPressed = false;
    private BufferedImage imagem;

    public DesenhaFormaCanvas() {
      super();
      this.addMouseListener(this);
      this.addMouseMotionListener(this);
    }

    @Override
    public void paint(Graphics g) {
      Graphics2D g2 = (Graphics2D)g;

      if (imagem == null) {
        imagem = new BufferedImage(100, 100, BufferedImage.TYPE_INT_RGB);
        Graphics2D big = (Graphics2D)imagem.getGraphics();
        big.setColor(Color.white);
        big.fillRect(0, 0, 100, 100);
      }

      g2.setColor(Color.white);
      g2.fillRect(0, 0, getBounds().width, getBounds().height);
      g2.drawImage(imagem, (getBounds().width - 100) / 2, (getBounds().height - 100) / 2, this);
      g2.setColor(Color.black);
      g2.drawRect((getBounds().width - 100) / 2, (getBounds().height - 100) / 2, 100, 100);
    }

    private void desenha() {
      int offX = (getBounds().width - 100) / 2;
      int offY = (getBounds().height - 100) / 2;

      Graphics2D g = (Graphics2D)imagem.getGraphics();
      g.setColor(Color.black);
      Stroke stroke = new BasicStroke(4.0f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_MITER);
      g.setStroke(stroke);

      for (int i = 1; i < pontos.size(); i++) {
        g.drawLine(pontos.get(i - 1).x - offX, pontos.get(i - 1).y - offY, pontos.get(i).x - offX, pontos.get(i).y - offY);
      }
      repaint();
    }

    @Override
    public void mouseClicked(MouseEvent mouseEvent) {
      // faz nada
    }

    @Override
    public void mousePressed(MouseEvent mouseEvent) {
      isPressed = true;
      pontos.clear();
      pontos.add(mouseEvent.getPoint());
    }

    @Override
    public void mouseReleased(MouseEvent mouseEvent) {
      isPressed = false;
      pontos.add(mouseEvent.getPoint());
      desenha();
    }

    @Override
    public void mouseEntered(MouseEvent mouseEvent) {
      // faz nada
    }

    @Override
    public void mouseExited(MouseEvent mouseEvent) {
      // faz nada
    }

    @Override
    public void mouseDragged(MouseEvent mouseEvent) {
      pontos.add(mouseEvent.getPoint());
      desenha();
    }

    @Override
    public void mouseMoved(MouseEvent mouseEvent) {
      // faz nada
    }

    @Override
    public Dimension getPreferredSize() {
      return new Dimension(100, 100);
    }

    public void limpar() {
      Graphics2D big = (Graphics2D)imagem.getGraphics();
      big.setColor(Color.white);
      big.fillRect(0, 0, 100, 100);
      repaint();
    }

    public BufferedImage getImagem() {
      return imagem;
    }
  }

  private DesenhaFormaCanvas desenha;
  private NeuralNetwork classificaNn;

  public ClassificaFormasGeometricas() {
    super();
    this.setTitle("Classificador");
    setupLayout();
    this.pack();
    this.setLocationRelativeTo(null);
    this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    classificaNn = NeuralNetwork.load(ClassificaFormasGeometricas.class.getResourceAsStream("FormasGeometricas.nnet"));
  }

  private void setupLayout() {
    desenha = new DesenhaFormaCanvas();

    JPanel painel = new JPanel(new BorderLayout());
    painel.add(BorderLayout.CENTER, desenha);

    JPanel botoesPanel = new JPanel(new FlowLayout());

    JButton classificar = new JButton("Classificar");
    JButton limpar = new JButton("Limpar");

    botoesPanel.add(classificar);
    botoesPanel.add(limpar);

    limpar.addActionListener(evento -> {
      desenha.limpar();
    });

    classificar.addActionListener(evento -> {
      try {
        classifica();
      } catch (Exception e) {
        e.printStackTrace();
      }
    });;

    painel.add(BorderLayout.SOUTH, botoesPanel);

    this.setContentPane(painel);
  }

  private void classifica() throws Exception {
    int[][] bitmap = GeraArquivoFormasGeometricas.compacta(desenha.getImagem());
    double[] input = new double[100];

    int k = 0;
    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        input[k++] = bitmap[i][j];
      }
    }

    classificaNn.setInput(input);
    classificaNn.calculate();

    double[] out = classificaNn.getOutput();

    if (out[0] >= out[1] && out[0] >= out[2]) { // quadrado
      JOptionPane.showMessageDialog(this, "É um QUADRADO", "Classificação", JOptionPane.QUESTION_MESSAGE);
    } else if (out[1] >= out[0] && out[1] >= out[2]) { // triângulo
      JOptionPane.showMessageDialog(this, "É um TRIÂNGULO", "Classificação",JOptionPane.WARNING_MESSAGE);
    } else { // círculo
      JOptionPane.showMessageDialog(this, "É um CÍRCULO", "Classificação", JOptionPane.INFORMATION_MESSAGE);
    }
  }

  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      ClassificaFormasGeometricas cfg = new ClassificaFormasGeometricas();
      cfg.setVisible(true);
    });
  }
}
