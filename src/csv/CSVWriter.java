package csv;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public class CSVWriter {
    private File file;
    private BufferedWriter writer;

    public CSVWriter(File file) {
        this.file = file;
        if (!file.getName().matches(".*\\.[Cc][Ss][Vv]$")) {
            this.file = new File(file.getPath().replaceAll("^(.*)\\..{3,4}$", "$1") + ".csv");
        }
        try {
            writer = new BufferedWriter(new FileWriter(this.file));
        } catch (IOException e) {
            System.err.println("I/O exception when opening " + this.file.getAbsolutePath() + " for writing");
        }
    }

    public void writeLists(List<List<String>> data) {
        int columnsFinished = 0;
        try {
            for (int row = 0; columnsFinished < data.size(); row++) {
                for (int col = 0; col < data.size(); col++) {
                    if (row < data.get(col).size()) {
                        writer.write(data.get(col).get(row));
                        if (row == data.get(col).size() - 1) {
                            columnsFinished++;
                        }
                    }
                    if (col < data.size() - 1) {
                        writer.write(",");
                    }
                }
                writer.write("\n");
            }
        } catch (IOException e) {
            System.err.println("I/O exception trying to writeLists() to " + file.getAbsolutePath());
        }
    }

    public void close() {
        try {
            writer.close();
        } catch (IOException e) {
            System.err.println("I/O error trying to flush and close " + file.getAbsolutePath());
        }
    }
}
