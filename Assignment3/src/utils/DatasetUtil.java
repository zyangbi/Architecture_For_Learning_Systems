package utils;

import java.util.Arrays;
import java.util.DoubleSummaryStatistics;

public class DatasetUtil {
    public static double[][] getInput(double[][] data) {
        double[][] input = new double[data.length][data[0].length - 1]; // elements other than last one are input

        for (int i = 0; i < data.length; ++i) {
            System.arraycopy(data[i], 0, input[i], 0, data[i].length - 1);
        }

        return input;
    }

    public static double[] getOutput(double[][] data) {
        double[] output = new double[data.length];

        for (int i = 0; i < data.length; ++i) {
            output[i] = data[i][data[i].length - 1];
        }

        return output;
    }

    public static double[] normalizeOutput(double[] array) {
        double max = Arrays.stream(array).summaryStatistics().getMax();
        double min = Arrays.stream(array).summaryStatistics().getMin();
        return Arrays.stream(array).map(val -> (val - min) / (max - min)).toArray();
    }
}
