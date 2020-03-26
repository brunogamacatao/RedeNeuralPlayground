package rnplayground;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class GeraArquivoFormasGeometricas {
  public static int[][] compacta(BufferedImage bi) throws Exception {
    int[][] bitmap = new int[10][10];

    for (int x = 0; x < bi.getWidth(); x += 10) {
      for (int y = 0; y < bi.getHeight(); y += 10) {
        boolean um = false;
        for (int i = 0; i < 10 ; i++) {
          for (int j = 0; j < 10; j++) {
            um = um || bi.getRGB(x + i, y + j) != -1;
          }
        }
        bitmap[x/10][y/10] = um ? 1 : 0;
      }
    }

    return bitmap;
  }

  private static String geraLinha(String arquivo, boolean quadrado, boolean triangulo, boolean circulo) throws Exception {
    BufferedImage bi = ImageIO.read(GeraArquivoFormasGeometricas.class.getResourceAsStream(arquivo));
    int[][] bitmap = compacta(bi);
    List<String> pixels = new ArrayList<>();

    for (int i = 0; i < 10; i++) {
      for (int j = 0; j < 10; j++) {
        pixels.add(bitmap[i][j] + "");
      }
    }

    pixels.add(quadrado ? "1" : "0");
    pixels.add(triangulo ? "1" : "0");
    pixels.add(circulo ? "1" : "0");

    return pixels.stream().collect(Collectors.joining(","));
  }

  public static void main(String[] args) throws Exception {
    PrintWriter out = new PrintWriter("formas.csv");

    List<String> cabecalho = new ArrayList<>();
    for (int i = 0; i < 100; i++) {
      cabecalho.add("in" + i);
    }

    cabecalho.add("quadrado");
    cabecalho.add("triangulo");
    cabecalho.add("circulo");

    out.println(cabecalho.stream().collect(Collectors.joining(",")));

    out.println(geraLinha("imagens/quad01.png", true, false, false));
    out.println(geraLinha("imagens/quad02.png", true, false, false));
    out.println(geraLinha("imagens/tri01.png", false, true, false));
    out.println(geraLinha("imagens/tri02.png", false, true, false));
    out.println(geraLinha("imagens/cir01.png", false, false, true));
    out.println(geraLinha("imagens/cir02.png", false, false, true));
    out.close();
  }
}
