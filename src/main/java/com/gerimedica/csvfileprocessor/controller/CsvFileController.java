package com.gerimedica.csvfileprocessor.controller;

import com.gerimedica.csvfileprocessor.api.CsvFileService;
import com.gerimedica.csvfileprocessor.commons.CsvHelper;
import com.gerimedica.csvfileprocessor.dto.ResponseMessage;
import com.gerimedica.csvfileprocessor.model.CsvFileEntity;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/api/v1/csvFileProcessor")
public class CsvFileController {

    private CsvFileService csvFileServiceImpl;

    public CsvFileController(CsvFileService csvFileServiceImpl)
    {
        this.csvFileServiceImpl = csvFileServiceImpl;
    }

    @PostMapping("/records")
    public ResponseEntity<ResponseMessage> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";

    if (CsvHelper.checkCsvFormat(file)) {
            try {
                csvFileServiceImpl.saveCsvFile(file);

                message = "Uploaded the file successfully: " + file.getOriginalFilename();

                String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                        .path("/api/csvFileProcessor/download/")
                        .path(file.getOriginalFilename())
                        .toUriString();

                return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message,fileDownloadUri));
            } catch (Exception e) {
                message = "Could not upload the file: " + file.getOriginalFilename() + "!";
                return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message,""));
            }
        }

        message = "Please upload a csv file!";
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseMessage(message,""));
    }

    @GetMapping("/records")
    public ResponseEntity<Resource> getAllRecords() {
        try {
            var csvRecordFile = csvFileServiceImpl.getAllRecords();

      if (csvRecordFile.contentLength() <= 0) {
                return new ResponseEntity<>(HttpStatus.NO_CONTENT);
            }

            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "GeriMedica")
                    .contentType(MediaType.parseMediaType("application/csv"))
                    .body(csvRecordFile);
        } catch (Exception e) {
            return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/records/{code}/code")
    public ResponseEntity<Resource> getAllRecordsByCode(@PathVariable String code) {
        try {
        var csvRecordFile = csvFileServiceImpl.getAllRecordsByCode(code);

        if (csvRecordFile.contentLength() <= 0) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + code)
                .contentType(MediaType.parseMediaType("application/csv"))
                .body(csvRecordFile);
    } catch (Exception e) {
        return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
    }
    }

    @DeleteMapping("/records")
    public ResponseEntity<String> deleteAllData()
    {
        return new ResponseEntity<>(csvFileServiceImpl.deleteAllData(),HttpStatus.OK);
    }

}
