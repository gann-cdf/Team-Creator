package csv;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CSVReader {
    private Map<String, List<String>> data;

    private CSVReader() {
        data = new HashMap<>();
    }

    public static CSVReader parseOffice365Responses(File file) {
        CSVReader csv = new CSVReader();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                csv.data.put(parts[0], Arrays.asList(parts[1].split(";")));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csv;
    }

    public static CSVReader parseSurveyMonkeyResponses(File file, int nameIndex, int prefStartIndex, int prefEndIndex) {
        CSVReader csv = new CSVReader();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(file));
            reader.readLine(); // lose the questions row
            String[] columns = reader.readLine().split(",");
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                String[] preferences = new String[prefEndIndex - prefStartIndex + 1];
                for (int i = prefStartIndex; i <= prefEndIndex && i < parts.length; i++) {
                    if (!columns[i].equals(parts[nameIndex]) && parts[i].length() > 0) {
                        preferences[new Integer(parts[i]) - 1] = columns[i];
                    }
                }
                csv.data.put(parts[nameIndex], Arrays.asList(preferences));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return csv;
    }

    public Map<String, List<String>> getData() {
        return data;
    }
}
