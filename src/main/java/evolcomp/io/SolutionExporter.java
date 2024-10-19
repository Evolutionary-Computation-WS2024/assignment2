package evolcomp.io;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;

public final class SolutionExporter {
    // Format (CSV): instance; method_name; nodes
    public static void export(List<SolutionRow> records, String path) throws IOException {
        try (FileWriter out = new FileWriter(path)) {
            CSVFormat csvFormat = CSVFormat.DEFAULT.builder()
                    .setDelimiter(';')
                    .build();

            CSVPrinter printer = csvFormat.print(out);
            records.forEach(record -> {
                try {
                    printer.printRecord(record.instance(), record.methodName(), record.path());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            printer.flush();
        }
    }
}
