package run;

import NN.NeuralNet;

public class SaveForPlot {
    public static void main(String[] args) {
        NeuralNet binary = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.0,
                0.0, 1.0, true, 0.05);
        binary.trainNN();
        binary.saveErrorList("./plot/binary.csv");

        NeuralNet bipolar = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.0,
                -1.0, 1.0, false, 0.05);
        bipolar.trainNN();
        bipolar.saveErrorList("./plot/bipolar.csv");

        NeuralNet binaryMomentum = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.9,
                0.0, 1.0, true, 0.05);
        binaryMomentum.trainNN();
        binaryMomentum.saveErrorList("./plot/binaryMomentum.csv");

        NeuralNet bipolarMomentum = new NeuralNet(2, new int[]{4, 1},
                0.2, 0.9,
                -1.0, 1.0, false, 0.05);
        bipolarMomentum.trainNN();
        bipolarMomentum.saveErrorList("./plot/bipolarMomentum.csv");
    }

}
