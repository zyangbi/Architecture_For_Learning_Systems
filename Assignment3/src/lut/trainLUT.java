package lut;

import NN.NeuralNet;
import utils.DatasetUtil;
import utils.FileUtil;

import java.io.File;

public class trainLUT {
    public static void main(String[] args) {
        try {
            String file = "out/production/Assignment3/robot/MicroBot.data/test.txt";
            double[][] dataset = FileUtil.readData(new File(file));
            double[][] input = DatasetUtil.getInput(dataset);
            double[] output = DatasetUtil.getOutput(dataset);
            double[] normalizedOutput = DatasetUtil.normalizeOutput(output);
            int numInputs = input[0].length;

            NeuralNet nn = new NeuralNet(
                    numInputs,
                    new int[]{20, 1},
                    0.2,
                    0.8,
                    -1.0,
                    1.0,
                    0.05);
            double epochs = nn.trainNN(input, normalizedOutput);
            System.out.println("Finish training in " + epochs + "epochs");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
