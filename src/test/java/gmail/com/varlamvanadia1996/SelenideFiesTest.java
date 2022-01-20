package gmail.com.varlamvanadia1996;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.Test;


import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFiesTest {

    private ClassLoader cl = SelenideFiesTest.class.getClassLoader();

    @Test
    void zipTest() throws Exception {
        ZipFile zp = new ZipFile(new File(cl.getResource("test.zip").toURI()));

        //файл pdf
        ZipEntry pdfFile = zp.getEntry("junit-user-guide-5.8.2.pdf");
        try (InputStream pdfStream = zp.getInputStream(pdfFile)) {
            PDF parsedPDF = new PDF(pdfStream);
            assertThat(parsedPDF.creator).contains("Asciidoctor PDF 1.5.3, based on Prawn 2.2.2");
        }
        // файл xlsx
        ZipEntry xlsxFile = zp.getEntry("test.xlsx");
        try (InputStream xlsxStream = zp.getInputStream(xlsxFile)) {
            XLS parsedXLS = new XLS(xlsxStream);
            assertThat(parsedXLS.excel.getSheetAt(0).getRow(1).getCell(1).getStringCellValue().contains("Варламова"));
        }

        // файл csv
        ZipEntry csvFile = zp.getEntry("test.csv");
        try (InputStream csvStream = zp.getInputStream(csvFile)) {
            CSVReader parsedCSV = new CSVReader(new InputStreamReader(csvStream));
            List<String[]> list = parsedCSV.readAll();
            assertThat(list).hasSize(3).contains(
                    new String[]{"Name", "LastName", "Pet"},
                    new String[]{"Nadezhda", "Varlamova", "Markiz"},
                    new String[]{"Elena", "Protcenko", "Musya"}
            );
        }

    }
}
