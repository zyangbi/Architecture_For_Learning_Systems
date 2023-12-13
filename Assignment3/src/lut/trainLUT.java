package lut;

import utils.DatasetUtil;
import utils.FileUtil;
import java.io.File;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class trainLUT {
    public static void main(String[] args) {
        try {
            String file = "out/production/Assignment3/robot/MicroBot.data/" +
                    "lut_13608";
            double[][] dataset = FileUtil.readData(new File(file));
            double[][] input = DatasetUtil.getInput(dataset);
            double[] output = DatasetUtil.getOutput(dataset);
            double[] normalizedOutput = DatasetUtil.normalizeOutput(output);

            int[][] hiddenLayerSizesOptions = {{101, 200, 5}};
            double[] learningRates = {0.1, 0.2, 0.3};
            double[] momentums = {0.3, 0.5, 0.7};

            double errorThreshold = 0.05;
            int epochThreshold = 100;

            ExecutorService executor = Executors.newFixedThreadPool(12);

            // Vary hyper-parameters
            for (int[] hiddenLayerSizes : hiddenLayerSizesOptions) {
                for (double learningRate : learningRates) {
                    for (double momentum : momentums) {
                        String resultFile = "result/" +
                                Arrays.toString(hiddenLayerSizes) + "_" +
                                learningRate + "_" +
                                momentum;
                        // Run multi-threaded training tasks
                        trainLUTTask task = new trainLUTTask(
                                input, normalizedOutput, hiddenLayerSizes,
                                learningRate, momentum,
                                errorThreshold, epochThreshold,
                                resultFile);
                        executor.execute(task);
                    }
                }
            }

            executor.shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
