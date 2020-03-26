package rnplayground;

import org.neuroph.core.NeuralNetwork;

public class AndMaisComplicadoDoMundo {
  private static final String RN_FILE = "AND.nnet";
  private static final double THRESHOLD = 0.79;
  private static NeuralNetwork AND_NN = null;

  private static int and(int a, int b) {
    NeuralNetwork nn = getAndNeuralNetwork();
    nn.setInput(a, b);
    nn.calculate();
    return nn.getOutput()[0] > THRESHOLD ? 1 : 0;
  }

  private static NeuralNetwork getAndNeuralNetwork() {
    if (AND_NN == null) {
      AND_NN = NeuralNetwork.load(AndMaisComplicadoDoMundo.class.getResourceAsStream(RN_FILE));
    }
    return AND_NN;
  }

  public static void main(String[] args) throws Exception {
    System.out.println("0 0 " + and(0, 0));
    System.out.println("0 1 " + and(0, 1));
    System.out.println("1 0 " + and(1, 0));
    System.out.println("1 1 " + and(1, 1));
  }
}
