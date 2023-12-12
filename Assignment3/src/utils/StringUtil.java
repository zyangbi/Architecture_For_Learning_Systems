package utils;

import java.util.stream.Collectors;
import java.util.Arrays;

public class StringUtil {
    public static String arrayToStr(double[] array) {
        return Arrays.stream(array)
                .mapToObj(Double::toString)
                .collect(Collectors.joining(" "));
    }

    public static double[] StrToArray(String str) {
        if (str == null || str.isEmpty()) {
            return new double[0];
        }

        String[] parts = str.trim().split(" ");
        double[] array = new double[parts.length];

        for (int i = 0; i < parts.length; ++i) {
            array[i] = Double.parseDouble(parts[i].trim());
        }

        return array;
    }
}
