package com.gerimedica.csvfileprocessor.controller;

import com.gerimedica.csvfileprocessor.api.CsvFileService;
import com.gerimedica.csvfileprocessor.commons.CsvHelper;
import com.gerimedica.csvfileprocessor.dto.ResponseMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/v1/")
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
                        .path("/api/v1/records/")
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


    @GetMapping(value = "/records" , produces="application/vnd.ms-excel")
    public ResponseEntity<Object> getAllRecords() {
        String filename = "allData.xlsx";
        byte[] bytes = csvFileServiceImpl.getAllRecords();
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.ms-excel;");
        headers.setContentDispositionFormData(filename, filename);
        return new ResponseEntity<Object>(bytes, headers, HttpStatus.OK);
    }


    @GetMapping(value = "/records/{code}/code" , produces="application/vnd.ms-excel")
    public ResponseEntity<Object> getAllRecordsByCode(@PathVariable String code) {
        String filename = "allDataByCode.xlsx";
        byte[] bytes = csvFileServiceImpl.getAllRecordsByCode(code);
        HttpHeaders headers = new HttpHeaders();
        headers.set("Content-Type", "application/vnd.ms-excel;");
        headers.setContentDispositionFormData(filename, filename);
        return new ResponseEntity<Object>(bytes, headers, HttpStatus.OK);
    }


    @DeleteMapping("/records")
    public ResponseEntity<String> deleteAllData()
    {
        return new ResponseEntity<>(csvFileServiceImpl.deleteAllData(),HttpStatus.OK);
    }

}
