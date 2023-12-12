package utils;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileUtil {
    public static double[][] readData(File argFile) throws IOException {
        List<double[]> dataList = new ArrayList<>();

        BufferedReader br = new BufferedReader(new FileReader(argFile));
        String line;
        while ((line = br.readLine()) != null) {
            double[] data = StringUtil.StrToArray(line);
            dataList.add(data);
        }

        return dataList.toArray(new double[0][]);
    }
}
