package com.gerimedica.csvfileprocessor.api;

import com.gerimedica.csvfileprocessor.model.CsvFileEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.util.List;

public interface CsvFileService {

    void saveCsvFile(MultipartFile file);

    InputStreamResource getAllRecords();

    InputStreamResource getAllRecordsByCode(String code);

    String deleteAllData();

}
