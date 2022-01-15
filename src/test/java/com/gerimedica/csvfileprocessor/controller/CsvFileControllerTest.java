package com.gerimedica.csvfileprocessor.controller;

import com.gerimedica.csvfileprocessor.api.CsvFileService;
import com.gerimedica.csvfileprocessor.commons.CsvHelper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
public class CsvFileControllerTest {

    @Mock
    private CsvFileService csvFileServiceImpl;

    private MockMvc mockMvc;

    private CsvFileController csvFileController;

    @BeforeEach
    void setUpCsvFileControllerTest()
    {
        this.csvFileController = new CsvFileController(csvFileServiceImpl);
        this.mockMvc = MockMvcBuilders.standaloneSetup(csvFileController).build();
    }

    @Test
    void shouldReturn400BadRequest_whenUploadWrongFile() throws Exception {
        MockMultipartFile csvFile = new MockMultipartFile("test.csv", "", "application/json", "{\"key1\", \"value1\"}".getBytes());
        Mockito.doNothing().when(csvFileServiceImpl).saveCsvFile(csvFile);
    try (MockedStatic<CsvHelper> utilities = Mockito.mockStatic(CsvHelper.class)) {
      utilities.when(() -> CsvHelper.checkCsvFormat(csvFile)).thenReturn(true);

      mockMvc
          .perform(
              MockMvcRequestBuilders.multipart("/api/v1/records")
                  .file("file", csvFile.getBytes())
                  .contentType("application/json")
                  .characterEncoding("UTF-8"))
          .andExpect(status().is4xxClientError());
        }

    }


}
