package NN;

import interfaces.NeuralNetInterface;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NeuralNet implements NeuralNetInterface {

    private final int numInputs;
    private final int[] hiddenOutputLayerSizes;
    private final double learningRate;
    private final double momentumTerm;
    private final double a; // lower bound of sigmoid
    private final double b; // upper bound of sigmoid
    private ArrayList<double[][]> wList; // weights for each layer
    private double[] y0; // outputs for input layer
    private ArrayList<double[]> yList; // outputs for hidden and output layer
    private ArrayList<double[]> deltaList; // errors for each layer
    private ArrayList<double[][]> deltaWList; // change in weights for each layer
    private ArrayList<Double> errorList; // record errors


    public NeuralNet(int argNumInputs,
                     int[] argHiddenLayerSizes,
                     int argNumOutputs,
                     double argLearningRate,
                     double argMomentumTerm,
                     double argA,
                     double argB) {
        this.numInputs = argNumInputs;
        this.hiddenOutputLayerSizes = new int[argHiddenLayerSizes.length + 1];
        System.arraycopy(argHiddenLayerSizes, 0, this.hiddenOutputLayerSizes, 0, argHiddenLayerSizes.length);
        this.hiddenOutputLayerSizes[this.hiddenOutputLayerSizes.length - 1] = argNumOutputs;

        this.learningRate = argLearningRate;
        this.momentumTerm = argMomentumTerm;
        this.a = argA;
        this.b = argB;

        this.wList = new ArrayList<>();
        this.y0 = new double[numInputs + 1];
        this.yList = new ArrayList<>();
        this.deltaList = new ArrayList<>();
        this.deltaWList = new ArrayList<>();
        this.errorList = new ArrayList<>();

        initializeNetwork();
    }

    /**
     * Initialize weights, bias, deltas of neural network
     */
    private void initializeNetwork() {
        // Input layer
        y0[numInputs] = 1.0; // bias

        // Hidden and output layers
        int prevSize = numInputs;
        for (int size : hiddenOutputLayerSizes) {
            double[][] w = new double[size][prevSize + 1];
            double[] y = new double[size + 1];
            double[] delta = new double[size];
            double[][] delta_w = new double[size][prevSize + 1];

            for (int i = 0; i < size; ++i) {
                for (int j = 0; j < prevSize + 1; ++j) {
                    w[i][j] = Math.random() - 0.5; // weights
                }
                y[size] = 1.0; // bias
            }

            wList.add(w);
            yList.add(y);
            deltaList.add(delta);
            deltaWList.add(delta_w);
            prevSize = size;
        }
    }

    /**
     * Forward propagation
     * Calculate the output of each layer
     * y = sigmoid(sum(wi*xi))
     */
    private double forwardPropagation(double[] X, double Y) {
        // Input layer
        for (int i = 0; i < numInputs; ++i) {
            y0[i] = X[i];
        }

        // Hidden layers and output layer
        double[] x = y0; // x is y of prev layer
        for (int layer = 0; layer < hiddenOutputLayerSizes.length; ++layer) {
            double[][] w = wList.get(layer);
            double[] y = yList.get(layer);

            for (int i = 0; i < y.length - 1; ++i) {
                double sum = 0.0;
                for (int j = 0; j < x.length; ++j) {
                    sum += w[i][j] * x[j];
                }
                y[i] = customSigmoid(sum);
            }

            x = y;
        }

        // Return the error
        return (Y - x[0]);
    }

    /**
     * Backward propagation
     * For output layer: delta = (y-a)(b-y)/(b-a) * (C-y)
     * For hidden layer: delta = (y-a)(b-y)/(b-a) * sum(delta_i * w_i)
     * Update weight: w(t+1) = w(t) + learningRate * delta * x_i + momentum * (w(t) - w(t-1))
     */
    private void backPropagation(double Y) {
        for (int layer = hiddenOutputLayerSizes.length - 1; layer >= 0; --layer) {
            double[][] w = wList.get(layer);
            double[] y = yList.get(layer);
            double[] delta = deltaList.get(layer);
            double[][] delta_w = deltaWList.get(layer);
            double[] x = (layer == 0) ? y0 : yList.get(layer - 1);

            // Output layer delta
            if (layer == hiddenOutputLayerSizes.length - 1) {
                for (int j = 0; j < delta.length; ++j) {
                    delta[j] = (y[j] - a) * (b - y[j]) * (Y - y[j]) / (b - a);
                }
            }

            // Hidden layer delta
            else {
                for (int j = 0; j < delta.length; ++j) {
                    double[][] w_next = wList.get(layer + 1);
                    double[] delta_next = deltaList.get(layer + 1);

                    double sum = 0.0;
                    for (int h = 0; h < delta_next.length; ++h) {
                        sum += w_next[h][j] * delta_next[h];
                    }

                    delta[j] = (y[j] - a) * (b - y[j]) * sum / (b - a);
                }
            }

            // Update weights
            for (int j = 0; j < delta.length; ++j) {
                for (int i = 0; i < x.length; ++i) {
                    delta_w[j][i] = learningRate * delta[j] * x[i] + momentumTerm * delta_w[j][i];
                    w[j][i] += delta_w[j][i];
                }
            }
        }
    }

    /**
     * One step of training
     */
    public double trainOneStep(double[] X, double Y) {
        double error = forwardPropagation(X, Y);
        backPropagation(Y);
        return error;
    }

    /**
     * One epoch of training
     */
    public double trainOneEpoch(double[][] X, double[] Y) {
        double totalError = 0.0;
        int n = Y.length;

        for(int i = 0; i < n; ++i) {
            double error = trainOneStep(X[i], Y[i]);
            totalError += Math.pow(error, 2);
        }

        double RMSError = Math.sqrt(totalError / n);
        return RMSError;
    }
    
    /**
     * Train NN by epochs until error is lower than threshold
     */
    public double[] trainNN(double[][] X, double[] Y,
                            double errorThreshold, int epochThreshold) {
        errorList.clear();
        double RMSError = 0.0;
        int epoch = 0;

        do {
            RMSError = trainOneEpoch(X, Y);
            errorList.add(RMSError);
            if (epoch % 100 == 0) {
                System.out.println("Epoch: " + epoch + ", RMS Error: " + RMSError + "\n");
            }
            epoch++;
        } while (RMSError > errorThreshold && epoch < epochThreshold);

        return new double[] {epoch, RMSError};
    }

//    public int trainNN(double[][] X, double[] Y, double errorThreshold) {
//        errorList.clear();
//        double RMSError;
//        int epoch = 0;
//
//        do {
//            RMSError = trainOneEpoch(X, Y);
//            errorList.add(RMSError);
////            if (epoch % 10 == 0) {
////                System.out.println("Epoch: " + epoch + ", RMS Error: " + RMSError + "\n");
////            }
////            epoch++;
//        } while (RMSError > errorThreshold);
//
//        return epoch;
//    }
//
    public void saveErrorList(String filename) {
        try (PrintWriter writer = new PrintWriter(filename)) {
            for (Double error : errorList) {
                writer.println(error);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Return a binary sigmoid of the input X
     * @param x The input
     * @return f(x) = 1 / (1 + e(-x))
     */
    @Override
    public double sigmoid(double x) {
        return 1.0 / (1.0 + Math.exp(-x));
    }

    /**
     * This method implements a general sigmoid with asymptotes bounded by (a,b)
     * @param x The input
     * @return f(x) = (b - a) / (1 + e(-x)) + a
     */
    @Override
    public double customSigmoid(double x) {
        return ((b - a) / (1.0 + Math.exp(-x))) + a;
    }

    @Override
    public double train(double[] X, double argValue) {
        return 0;
    }

    @Override
    public double outputFor(double[] X) {
        return 0;
    }

    @Override
    public void save(File argFile) {

    }

    @Override
    public void load(String argFileName) throws IOException {

    }

    @Override
    public void zeroWeights() {

    }

    @Override
    public void initializeWeights() {

    }
}




