package vitalii.qa.files;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import io.qameta.allure.Feature;
import io.qameta.allure.Owner;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static vitalii.qa.files.DataTest.*;

public class FilesTest extends BaseTest {

    @Test
    @Owner("velichkovv")
    @DisplayName("Upload to herokuapp.com")
    public void uploadFile() {
        open(UPLOAD_FILE_PAGE_1);

        $("#file-upload").uploadFromClasspath(FILE1);
        $("#file-submit").click();
        $("#uploaded-files").shouldHave(text(FILE1));
    }

    @Test
    @Owner("velichkovv")
    @DisplayName("Upload to demoqa.com")
    public void uploadFile2() {
        open(UPLOAD_FILE_PAGE_2);

        $("#uploadFile").uploadFromClasspath(FILE2);
        $("#downloadButton").click();
        $("#uploadedFilePath").shouldHave(text(FILE2));
    }

    @Test
    @Feature("Парсим txt файл и проверяем его содержимое")
    @DisplayName("Download txt file and check")
    public void downloadTXT_FileTest() throws IOException {
        open("https://github.com/junit-team/junit5/blob/main/README.md");
        File download = $("#raw-url").download();
        String fileContent = IOUtils.toString(new FileReader(download));
        assertTrue(fileContent.contains("Contributions to JUnit 5 are both welcomed and appreciated."));
    }

    @Test
    @Feature("Парсим pdf файл и проверяем его содержимое")
    @DisplayName("Download pdf file and check")
    public void downloadPDF_FileTest() throws IOException {
        open(DOWNLOAD_FILE_PDF_PAGE);

        File pdf = $(byText("Download sample pdf file")).download();
        PDF parsePdf = new PDF(pdf);
        Assertions.assertEquals(4, parsePdf.numberOfPages);
        //Assertions.assertEquals("Lorem ipsum", parsePdf.text); !!!не работает блин
        //Assertions.assertEquals("Lorem ipsum", parsePdf.title); !!!не работает блин
    }

    @Test
    @Feature("Парсим XLS файл и проверяем его содержимое")
    @DisplayName("Download XLS file and check")
    public void downloadXLS_FileTest() throws IOException {
        open(DOWNLOAD_FILE_XLS_PAGE);

        File xls = $$(".download-button").get(1).download();
        XLS parsedXls = new XLS(xls);
        boolean checkPassed = parsedXls.excel
                .getSheetAt(0)
                .getRow(50)
                .getCell(1)
                .getStringCellValue()
                .contains(XLS_TEXT);

        assertTrue(checkPassed);
    }
}
