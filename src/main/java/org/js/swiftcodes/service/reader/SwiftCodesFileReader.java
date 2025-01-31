package org.js.swiftcodes.service.reader;

import lombok.Getter;
import lombok.extern.apachecommons.CommonsLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.js.swiftcodes.service.exceptions.GeneralException;
import org.js.swiftcodes.service.model.BankData;
import org.js.swiftcodes.service.model.SwiftCode;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@CommonsLog
public class SwiftCodesFileReader {
    private SwiftCodesFileReader() {
    }

    public static Map<SwiftCode, BankData> readSwiftCodesFile(String fileName) {
        Map<SwiftCode, BankData> bankDataMap = new HashMap<>();

        try (FileInputStream file = new FileInputStream(new File(fileName))) {
            try (Workbook workbook = new XSSFWorkbook(file)) {
                Sheet sheet = workbook.getSheetAt(0);

                boolean isHeader = false;
                Map<HeaderColumnName, Integer> headers = null;

                for (Row row : sheet) {
                    if (!isHeader) {
                        headers = readHeader(row);
                        isHeader = true;
                    } else {
                        BankData bankData = readData(row, headers);
                        bankDataMap.put(bankData.getSwiftCode(), bankData);
                    }
                }
            }

        } catch (IOException | GeneralException e) {
            log.error(String.format("Failed to read file %s", fileName), e);
            throw new GeneralException(String.format("Failed to read file %s", fileName), e);
        }

        return bankDataMap;
    }

    private static BankData readData(Row row, Map<HeaderColumnName, Integer> headers) {

        return BankData.builder()
            .swiftCode(new SwiftCode(getStringCellValue(row, headers, HeaderColumnName.SWIFT_CODE)))
            .address(getStringCellValue(row, headers, HeaderColumnName.ADDRESS))
            .codeType(getStringCellValue(row, headers, HeaderColumnName.CODE_TYPE))
            .countryISO2Code(getStringCellValueUpperCase(row, headers, HeaderColumnName.COUNTRY_ISO2CODE))
            .countryName(getStringCellValueUpperCase(row, headers, HeaderColumnName.COUNTRY_NAME))
            .name(getStringCellValue(row, headers, HeaderColumnName.NAME))
            .townName(getStringCellValue(row, headers, HeaderColumnName.TOWN_NAME))
            .timeZone(getStringCellValue(row, headers, HeaderColumnName.TIME_ZONE))
            .build();
    }

    private static String getStringCellValue(Row row, Map<HeaderColumnName, Integer> headers, HeaderColumnName columnName) {
        return row.getCell(headers.get(columnName))
            .getStringCellValue();
    }

    private static String getStringCellValueUpperCase(Row row, Map<HeaderColumnName, Integer> headers, HeaderColumnName columnName) {
        return getStringCellValue(row, headers, columnName).toUpperCase();
    }

    private static Map<HeaderColumnName, Integer> readHeader(Row row) {
        Map<HeaderColumnName, Integer> headers = new EnumMap<>(HeaderColumnName.class);

        for (Cell cell : row) {
            headers.put(HeaderColumnName.getByHeader(cell.getStringCellValue()), cell.getColumnIndex());
        }

        return headers;
    }

    @Getter
    private enum HeaderColumnName {
        COUNTRY_ISO2CODE("COUNTRY ISO2 CODE"), SWIFT_CODE("SWIFT CODE"), CODE_TYPE("CODE TYPE"), NAME("NAME"), ADDRESS("ADDRESS"), TOWN_NAME("TOWN NAME"), COUNTRY_NAME("COUNTRY NAME"), TIME_ZONE(
            "TIME ZONE");

        private final String header;
        private static final Map<String, HeaderColumnName> names = new HashMap<>();

        static {
            for (HeaderColumnName header : HeaderColumnName.values()) {
                names.put(header.header.toUpperCase(), header);
            }
        }

        public static HeaderColumnName getByHeader(String header) {
            HeaderColumnName readHeader = names.get(header.toUpperCase());
            if (readHeader == null) {
                throw new GeneralException(String.format("Unexpected header %s", header), null);
            }

            return readHeader;
        }

        HeaderColumnName(String header) {
            this.header = header;
        }

        @Override
        public String toString() {
            return "HeaderColumnName{" + "header='" + header + '\'' + '}';
        }
    }
}
