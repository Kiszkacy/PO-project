package evolution.util;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class CSVDumper {

    static public void save(String[][] data, String to, boolean append) throws IOException {
        File file = new File("data\\"+to+".csv");
        FileWriter fileWriter = new FileWriter(file, append);

        for(String[] d : data) {
            StringBuilder line = new StringBuilder();
            for (int i = 0; i < d.length; i++) {
                line.append(d[i]);
                if (i != d.length-1) line.append(";");
            }
            line.append("\n");
            fileWriter.write(line.toString());
        }
        fileWriter.close();
    }
}
