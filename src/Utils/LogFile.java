package Utils;

import robocode.RobocodeFileOutputStream;

import java.io.File;
import java.io.PrintStream;

public class LogFile {
    private PrintStream file;

    public LogFile(File directory, String fileName) {
        try {
            file = new PrintStream(new RobocodeFileOutputStream(new File(directory, fileName)), true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void println(String str) {
        file.println(str);
    }

    public void close() {
        file.close();
    }
}

