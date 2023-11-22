package run;

import neuralnet.NeuralNet;

public class AverageEpochs {
    public static void main(String[] args) {
        int sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet binary = new NeuralNet(2, 4, 0.2, 0.0, 0.0, 1.0, true, 0.05);
            sum += binary.trainNN();
        }
        System.out.println("Binary: " + (double)sum / 1000.0 + "epochs");

        sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet bipolar = new NeuralNet(2, 4, 0.2, 0.0, -1.0, 1.0, false, 0.05);
            sum += bipolar.trainNN();
        }
        System.out.println("Bipolar: " + (double)sum / 1000.0 + "epochs");

        sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet binaryMomentum = new NeuralNet(2, 4, 0.2, 0.9, 0.0, 1.0, true, 0.05);
            sum += binaryMomentum.trainNN();
        }
        System.out.println("BinaryMomentum: " + (double)sum / 1000.0 + "epochs");

        sum = 0;
        for (int i = 0; i < 1000; ++i) {
            NeuralNet bipolarMomentum = new NeuralNet(2, 4, 0.2, 0.9, -1.0, 1.0, false, 0.05);
            sum += bipolarMomentum.trainNN();
        }
        System.out.println("BipolarMomentum: " + (double)sum / 1000.0 + "epochs");
    }
}
