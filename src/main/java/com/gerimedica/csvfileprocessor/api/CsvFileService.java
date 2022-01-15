package com.gerimedica.csvfileprocessor.api;

import org.springframework.web.multipart.MultipartFile;

public interface CsvFileService {

    void saveCsvFile(MultipartFile file);

    byte[] getAllRecords();

    byte[] getAllRecordsByCode(String code);

    String deleteAllData();

}
