package lut;

import NN.NeuralNet;

public class trainLUTTask implements Runnable {
    private double[][] input;
    private double[] output;
    private int numInputs;
    private int[] hiddenLayerSizes;
    private double learningRate;
    private double momentum;
    private double errorThreshold;
    private int epochThreshold;
    private String file;

    public trainLUTTask(double[][] input, double[] output, int[] hiddenLayerSizes,
                        double learningRate, double momentum,
                        double errorThreshold, int epochThreshold,
                        String argFile) {
        this.input = input;
        this.output = output;
        this.numInputs = input[0].length;
        this.hiddenLayerSizes = hiddenLayerSizes;
        this.learningRate = learningRate;
        this.momentum = momentum;
        this.errorThreshold = errorThreshold;
        this.epochThreshold = epochThreshold;
        this.file = argFile;
    }

    @Override
    public void run() {
        NeuralNet nn = new NeuralNet(numInputs, hiddenLayerSizes, 1,
                learningRate, momentum, -1.0, 1.0);
        nn.trainNN(input, output, errorThreshold, epochThreshold);
        nn.saveErrorList(file);
    }
}
