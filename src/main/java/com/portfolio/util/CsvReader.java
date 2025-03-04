// CsvReader.java
package com.portfolio.util;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.HashMap;
import java.util.Map;

public class CsvReader {

    public static Map<String, Integer> readPositionsFromCsv(String filePath) throws IOException {
        Map<String, Integer> positions = new HashMap<>();

        try (Reader reader = new FileReader(filePath);
             CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT
                     .withFirstRecordAsHeader()
                     .withIgnoreHeaderCase()
                     .withTrim())) {

            for (CSVRecord csvRecord : csvParser) {
                String symbol = csvRecord.get("symbol");
                int positionSize = Integer.parseInt(csvRecord.get("positionSize"));
                positions.put(symbol, positionSize);
            }
        }

        return positions;
    }
}