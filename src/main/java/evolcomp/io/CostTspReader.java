package evolcomp.io;

import evolcomp.tsp.Point;
import evolcomp.tsp.TSPInstance;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public final class CostTspReader {
    public static TSPInstance read(URL url) throws IOException, URISyntaxException {
        Path path = Path.of(url.toURI());

        String fileName = path.getFileName().toString();
        if (!fileName.endsWith(".csv")) {
            throw new RuntimeException("Tried to read \"" + fileName + "\", which is not a .csv file!");
        }

        String tspName = fileName.substring(0, fileName.length() - 4);

        try (BufferedReader reader = Files.newBufferedReader(path)) {
            try (CSVParser csvParser = new CSVParser(reader, CSVFormat.DEFAULT.builder()
                    .setDelimiter(";")
                    .build())) {

                List<Point> points = csvParser.stream()
                    .map(record -> {
                        int x = Integer.parseInt(record.get(0));
                        int y = Integer.parseInt(record.get(1));
                        int cost = Integer.parseInt(record.get(2));
                        int rowNum = (int) record.getRecordNumber();
                        return new Point(rowNum, x, y, cost);
                    })
                    .toList();

                return new TSPInstance(points, tspName);
            }
        }
    }
}
