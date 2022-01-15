package com.gerimedica.csvfileprocessor.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

@Data
@Table(name = "csv_file_table")
@Entity
@AllArgsConstructor
@NoArgsConstructor
public class CsvFileEntity implements Serializable {

    //source,"codeListCode","code","displayValue","longDescription","fromDate","toDate","sortingPriority"
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String source;

    private String codeListCode;

    @Column(unique = true)
    private String code;

    private String displayValue;

    private String longDescription;

    private LocalDate fromDate;

    private LocalDate toDate;

    private String sortingPriority;

}
