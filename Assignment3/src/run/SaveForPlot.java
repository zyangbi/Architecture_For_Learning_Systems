package run;

import NN.NeuralNet;

public class SaveForPlot {
    public static void main(String[] args) {
        // Binary training set
        double[][] binaryInput = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
        double[] binaryOutput = new double[]{0.0, 1.0, 1.0, 0.0};
        // Bipolar training set
        double[][] bipolarInput = new double[][]{{-1.0, -1.0}, {-1.0, 1.0}, {1.0, -1.0}, {1.0, 1.0}};
        double[] bipolarOutput = new double[]{-1.0, 1.0, 1.0, -1.0};

        NeuralNet binary = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.0,
                0.0, 1.0, 0.05);
        binary.trainNN(binaryInput, binaryOutput);
        binary.saveErrorList("./plot/binary.csv");

        NeuralNet bipolar = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.0,
                -1.0, 1.0,  0.05);
        bipolar.trainNN(bipolarInput, bipolarOutput);
        bipolar.saveErrorList("./plot/bipolar.csv");

        NeuralNet binaryMomentum = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.9,
                0.0, 1.0, 0.05);
        binaryMomentum.trainNN(binaryInput, binaryOutput);
        binaryMomentum.saveErrorList("./plot/binaryMomentum.csv");

        NeuralNet bipolarMomentum = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.9,
                -1.0, 1.0, 0.05);
        bipolarMomentum.trainNN(bipolarInput, bipolarOutput);
        bipolarMomentum.saveErrorList("./plot/bipolarMomentum.csv");
    }

}
