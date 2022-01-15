package com.gerimedica.csvfileprocessor.service;

import com.gerimedica.csvfileprocessor.api.CsvFileService;
import com.gerimedica.csvfileprocessor.commons.CsvHelper;
import com.gerimedica.csvfileprocessor.model.CsvFileEntity;
import com.gerimedica.csvfileprocessor.repository.CsvRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;


@Service
public class CsvFileServiceImpl implements CsvFileService {

    private CsvRepository csvRepository;

    @Autowired
    public CsvFileServiceImpl(CsvRepository csvRepository)
    {
        this.csvRepository = csvRepository;
    }

    @Override
    public void saveCsvFile(MultipartFile file) {
        try {
            List<CsvFileEntity> csvFileEntities = CsvHelper.csvFileToCsvFileEntity(file.getInputStream());
            csvRepository.saveAll(csvFileEntities);
        } catch (IOException e) {
            throw new RuntimeException("Failed to save csv data: " + e.getMessage());
        }
    }

    private InputStreamResource loadCsvFile(List<CsvFileEntity> csvFileEntityList) {
        InputStreamResource csvFile = new InputStreamResource(CsvHelper.csvFileEntityToCsvFile(csvFileEntityList));
        return csvFile;
    }

    @Override
    public InputStreamResource getAllRecords() {
        var csyFileEntityList = StreamSupport.stream(csvRepository.findAll().spliterator(),false).collect(Collectors.toList());
        return loadCsvFile(csyFileEntityList);
    }

    @Override
    public InputStreamResource getAllRecordsByCode(String code)
    {
        var csyFileEntityList = StreamSupport.stream(csvRepository.getAllRecordsByCode(code).spliterator(),false).collect(Collectors.toList());
        return loadCsvFile(csyFileEntityList);
    }

    @Override
    public String deleteAllData() {
         csvRepository.deleteAll();
        return "All records deleted";
    }

}
