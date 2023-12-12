package run;

import NN.NeuralNet;

public class AverageEpochs {
    public static void main(String[] args) {
        // Binary training set
        double[][] binaryInput = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
        double[] binaryOutput = new double[]{0.0, 1.0, 1.0, 0.0};
        // Bipolar training set
        double[][] bipolarInput = new double[][]{{-1.0, -1.0}, {-1.0, 1.0}, {1.0, -1.0}, {1.0, 1.0}};
        double[] bipolarOutput = new double[]{-1.0, 1.0, 1.0, -1.0};

        int sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet binary = new NeuralNet(2, new int[]{4, 1},
                    0.2, 0.0,
                    0.0, 1.0, 0.05);
            sum += binary.trainNN(binaryInput, binaryOutput);
        }
        System.out.println("Binary: " + (double)sum / 1000.0 + "epochs");

        sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet bipolar = new NeuralNet(2, new int[]{4, 1},
                    0.2, 0.0,
                    -1.0, 1.0, 0.05);
            sum += bipolar.trainNN(bipolarInput, bipolarOutput);
        }
        System.out.println("Bipolar: " + (double)sum / 1000.0 + "epochs");

        sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet binaryMomentum = new NeuralNet(2, new int[]{4, 1},
                    0.2, 0.9,
                    0.0, 1.0, 0.05);
            sum += binaryMomentum.trainNN(binaryInput, binaryOutput);
        }
        System.out.println("BinaryMomentum: " + (double)sum / 1000.0 + "epochs");

        sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet bipolarMomentum = new NeuralNet(2, new int[]{4, 1},
                    0.2, 0.9,
                    -1.0, 1.0, 0.05);
            sum += bipolarMomentum.trainNN(bipolarInput, bipolarOutput);
        }
        System.out.println("BipolarMomentum: " + (double)sum / 1000.0 + "epochs");
    }
}
