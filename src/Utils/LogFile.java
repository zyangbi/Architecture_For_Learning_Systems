package Utils;

import robocode.RobocodeFileOutputStream;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;

public class LogFile {
    private final PrintStream file;

    public LogFile(String fileName) throws Exception {
        file = new PrintStream(new RobocodeFileOutputStream(fileName), true);
    }

    public void print(String str) {
        file.print(str);
    }

    public void println(String str) {
        file.println(str);
    }

    public void close() {
        file.close();
    }
}

