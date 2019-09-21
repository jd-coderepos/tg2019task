package utils;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author jld
 */
public class IO {

    public static List<String[]> readCSV(String fileStr, char sep, int skiplines) throws IOException {
        // Create an object of file reader
        // class with CSV file as a parameter.
        FileReader filereader = new FileReader(fileStr);

        CSVParser parser = new CSVParserBuilder().withSeparator(sep).build();

        // create csvReader object and skip first Line
        CSVReader tsvReader = new CSVReaderBuilder(filereader)
                .withSkipLines(skiplines)
                .withCSVParser(parser)
                .build();
        List<String[]> allData = tsvReader.readAll();

        return allData;
    }

    public static String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }

}
