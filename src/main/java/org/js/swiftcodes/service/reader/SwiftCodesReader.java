package org.js.swiftcodes.service.reader;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.js.swiftcodes.service.exceptions.GeneralException;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@CommonsLog
public class SwiftCodesReader {
    private SwiftCodesReader() {
    }

    public static void readSwiftCodesFile(String fileName) {
        try (FileInputStream file = new FileInputStream(new File(fileName))) {
            try (Workbook workbook = new XSSFWorkbook(file)) {
                Sheet sheet = workbook.getSheetAt(0);

                for (Row row : sheet) {
                    for (Cell cell : row) {
                        switch (cell.getCellType()) {
                            case STRING -> System.out.print(cell.getStringCellValue() + "\t");
                            case NUMERIC -> System.out.print(cell.getNumericCellValue() + "\t");
                            default -> System.out.print("?");
                        }
                    }
                    System.out.println();
                }
            }

        } catch (IOException e) {
            log.error(String.format("Failed to read file %s", fileName), e);
            throw new GeneralException(String.format("Failed to read file %s", fileName), e);
        }

    }
}
