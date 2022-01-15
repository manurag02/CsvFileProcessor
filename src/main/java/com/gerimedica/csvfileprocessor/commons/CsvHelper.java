package com.gerimedica.csvfileprocessor.commons;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.gerimedica.csvfileprocessor.model.CsvFileEntity;
import lombok.experimental.UtilityClass;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVPrinter;
import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.QuoteMode;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@UtilityClass
public class CsvHelper {
    public static String TYPE = "text/csv";
    static String[] csvFileHeaders = { "source","codeListCode","code","displayValue","longDescription","fromDate","toDate","sortingPriority" };


    public static List<CsvFileEntity> csvFileToCsvFileEntity(InputStream is) {
        try (BufferedReader fileReader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
             CSVParser csvParser = new CSVParser(fileReader,
                     CSVFormat.DEFAULT.withFirstRecordAsHeader().withIgnoreHeaderCase().withTrim());) {

            List<CsvFileEntity> csvFileEntityList = new ArrayList<>();

            Iterable<CSVRecord> csvRecords = csvParser.getRecords();

            for (CSVRecord csvRecord : csvRecords) {
                CsvFileEntity csvFileEntity = new CsvFileEntity(
                        Long.getLong("1l"),
                        csvRecord.get("source"),
                        csvRecord.get("codeListCode"),
                        csvRecord.get("code"),
                        csvRecord.get("displayValue"),
                        csvRecord.get("longDescription"),
                        csvRecord.get("fromDate"),
                        csvRecord.get("toDate"),
                        csvRecord.get("sortingPriority")


                );

                csvFileEntityList.add(csvFileEntity);
            }

            return csvFileEntityList;
        } catch (IOException e) {
            throw new RuntimeException("fail to parse CSV file: " + e.getMessage());
        }
    }

    public static ByteArrayInputStream csvFileEntityToCsvFile(List<CsvFileEntity> csvFileEntityList) {
        final CSVFormat format = CSVFormat.DEFAULT.withQuoteMode(QuoteMode.MINIMAL);

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             CSVPrinter csvPrinter = new CSVPrinter(new PrintWriter(out), format)) {
            for (CsvFileEntity csvFileEntity : csvFileEntityList) {
                List<String> data = Arrays.asList(
                        String.valueOf(csvFileEntity.getId()),
                        csvFileEntity.getCode(),
                        csvFileEntity.getSource(),
                        csvFileEntity.getCodeListCode(),
                        csvFileEntity.getDisplayValue(),
                        csvFileEntity.getLongDescription(),
                        String.valueOf(csvFileEntity.getFromDate()),
                        String.valueOf(csvFileEntity.getToDate()),
                        csvFileEntity.getSortingPriority()

                );

                csvPrinter.printRecord(data);
            }

            csvPrinter.flush();
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("Importing data to CSV file failed : " + e.getMessage());
        }
    }

    public static boolean checkCsvFormat(MultipartFile file) {
        if (TYPE.equals(file.getContentType())
                || file.getContentType().equals("application/vnd.ms-excel")) {
            return true;
        }

        return false;
    }
}
