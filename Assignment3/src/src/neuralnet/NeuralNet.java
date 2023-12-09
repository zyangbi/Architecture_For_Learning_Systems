package src.neuralnet;

import nninterface.NeuralNetInterface;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class NeuralNet implements NeuralNetInterface {

    private final int numInputs;
    private final int numHidden;
    private final double learningRate;
    private final double momentumTerm;
    private final double a; // lower bound of sigmoid
    private final double b; // upper bound of sigmoid
    private final double errorThreshold; // stop when error is lower than threshold
    private final boolean isBinary; // 1 for binary dataset, 0 for bipolar dataset
    private double[][] w01; // weights from input to hidden layer
    private double[] w12; // weights from hidden to output layer
    private double[] y0; // inputs and bias
    private double[] y1; // outputs of hidden layer and bias
    private double y2; // outputs of output layer
    private double[] delta1; // error at output layer
    private double delta2; // errors at hidden layer
    private double[][] delta_w01; // change in weight w01
    private double[] delta_w12; // change in weight w12
    private double[][] input; // training input
    private double[] output; // training output
    private ArrayList<Double> errorList; // record errors


    public NeuralNet(int argNumInputs,
                     int argNumHidden,
                     double argLearningRate,
                     double argMomentumTerm,
                     double argA,
                     double argB,
                     boolean argIsBinary,
                     double argErrorThreshold) {
        this.numInputs = argNumInputs;
        this.numHidden = argNumHidden;
        this.learningRate = argLearningRate;
        this.momentumTerm = argMomentumTerm;
        this.a = argA;
        this.b = argB;
        this.isBinary = argIsBinary;
        this.errorThreshold = argErrorThreshold;

        this.w01 = new double[numHidden][numInputs + 1];
        this.w12 = new double[numHidden + 1];
        this.y0 = new double[numInputs + 1];
        this.y1 = new double[numHidden + 1];
        this.delta1 = new double[numHidden];
        this.delta_w01 = new double[numHidden][numInputs + 1];
        this.delta_w12 = new double[numHidden + 1];
        this.errorList = new ArrayList<Double>();
    }

    /**
     * Initialize weight value in the range -0.5 to 0.5
     */
    @Override
    public void initializeWeights() {
        for (int i = 0; i < numHidden; ++i) {
            for (int j = 0; j < numInputs + 1; ++j) {
                w01[i][j] = Math.random() - 0.5;
            }
        }
        for (int i = 0; i < numHidden + 1; ++i) {
            w12[i] = Math.random() - 0.5;
        }
    }

    /**
     * Initialize bias nodes to 1
     */
    private void initializeBias() {
        y0[numInputs] = 1;
        y1[numHidden] = 1;
    }

    /**
     * Initialize inputs and outputs of the training set
     */
    private void initializeInputOutput() {
        // Binary training set
        if (isBinary) {
            this.input = new double[][]{{0.0, 0.0}, {0.0, 1.0}, {1.0, 0.0}, {1.0, 1.0}};
            this.output = new double[]{0.0, 1.0, 1.0, 0.0};
        }
        // Bipolar training set
        else {
            this.input = new double[][]{{-1.0, -1.0}, {-1.0, 1.0}, {1.0, -1.0}, {1.0, 1.0}};
            this.output = new double[]{-1.0, 1.0, 1.0, -1.0};
        }
    }

    /**
     * Forward propagation
     * Calculate the output of each layer
     * y = sigmoid(sum(wi*xi))
     */
    private double forwardPropagation(double[] X, double Y) {
        for (int i = 0; i < numInputs; ++i) {
            y0[i] = X[i];
        }

        for (int i = 0; i < numHidden; ++i) {
            double sum = 0.0;
            for (int j = 0; j < numInputs + 1; ++j) {
                sum += w01[i][j] * y0[j];
            }
            y1[i] = customSigmoid(sum);
        }

        double sum = 0.0;
        for (int j = 0; j < numHidden + 1; ++j) {
            sum += w12[j] * y1[j];
        }
        y2 = customSigmoid(sum);

        return (Y - y2);
    }

    /**
     * Backward propagation
     * For output layer: delta2 = (y-a)(b-y)/(b-a) * (C-y)
     * For other layer: delta1 = (y-a)(b-y)/(b-a) * sum(delta_i * w_i)
     * Update weight: w(t+1) = w(t) + learningRate * delta * x_i + momentum * (w(t) - w(t-1))
     */
    private void backPropagation(double Y) {
        // output layer
        delta2 = (y2 - a) * (b - y2) * (Y - y2) / (b - a);
        for (int i = 0; i < numHidden + 1; ++i) {
            delta_w12[i] = learningRate * delta2 * y1[i] + momentumTerm * delta_w12[i];
            w12[i] += delta_w12[i];;
        }

        // hidden layer
        for (int i = 0; i < numHidden; ++i) {
            delta1[i] = (y1[i] - a) * (b - y1[i]) * (delta2 * w12[i]) / (b - a);
        }
        for (int i = 0; i < numHidden; ++i) {
            for(int j = 0; j < numInputs + 1; ++j) {
                delta_w01[i][j] = learningRate * delta1[i] * y0[j] + momentumTerm * delta_w01[i][j];
                w01[i][j] += delta_w01[i][j];
            }
        }
    }

    /**
     * One epoch of training
     */
    @Override
    public double train(double[] X, double argValue) {
        double error = forwardPropagation(X, argValue);
        backPropagation(argValue);
        return error;
    }

    /**
     * Train NN by epochs until error is lower than threshold
     * error = sum((Y - y)^2) / 2
     */
    public int trainNN() {
        initializeWeights();
        initializeBias();
        initializeInputOutput();

        errorList.clear();
        double error;
        int epoch = 0;
        do {
            error = 0.0;
            for(int i = 0; i < output.length; ++i) {
                error += Math.pow(train(input[i], output[i]), 2.0);
            }
            error /= 2.0;
            errorList.add(error);
//            if (epoch % 10 == 0) {
//                System.out.println("Epoch: " + epoch + ", Error: " + error + "\n");
//            }
            epoch++;
        } while (error > errorThreshold);
        return epoch;
    }

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
}




