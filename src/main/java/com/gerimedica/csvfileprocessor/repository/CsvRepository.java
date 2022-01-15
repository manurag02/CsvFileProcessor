package com.gerimedica.csvfileprocessor.repository;

import com.gerimedica.csvfileprocessor.model.CsvFileEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CsvRepository extends CrudRepository<CsvFileEntity, Long> {

    @Query("SELECT record FROM CsvFileEntity record where record.code = ?1 ")
     List<CsvFileEntity> getAllRecordsByCode(String code);

}
