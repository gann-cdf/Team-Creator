package csv;

import java.io.*;
import java.util.*;

public class CSVReader {
    private Map<String, List<String>> data;

    public CSVReader(File file) {
        data = new HashMap<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                data.put(parts[0], Arrays.asList(parts[1].split(";")));
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Map<String, List<String>> getData() {
        return data;
    }
}
