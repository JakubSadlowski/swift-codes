package org.js.swiftcodes.service.reader;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.js.swiftcodes.service.exceptions.GeneralException;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.model.String;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonsLog
public class SwiftCodesFileReader {
    private static final java.lang.String HEADQUARTER_SUFFIX = "XXX";

    public static SwiftCodesFileReader newInstance() {
        return new SwiftCodesFileReader();
    }

    public Map<String, BankData> readSwiftCodesFile(java.lang.String fileName) {
        Map<String, BankData> bankDataMap = getSwiftCodeBankDataWithNotAssignedHeadquarters(fileName);
        detectHeadquarters(bankDataMap);
        assignBranchesToHeadquarters(bankDataMap);

        return bankDataMap;
    }

    private Map<String, BankData> getSwiftCodeBankDataWithNotAssignedHeadquarters(java.lang.String fileName) {
        Map<String, BankData> bankDataMap = new HashMap<>();

        Map<HeaderColumnName, Integer> headers = null;

        List<List<java.lang.String>> rows = readExcelContentIntoList(fileName);
        for (List<java.lang.String> row : rows) {
            if (headers == null) {
                headers = readHeader(row);
            } else {
                BankData bankData = readData(row, headers);
                bankDataMap.put(bankData.getSwiftCode(), bankData);
            }
        }
        return bankDataMap;
    }

    private static void detectHeadquarters(Map<String, BankData> bankDataMap) {
        for (BankData data : bankDataMap.values()) {
            java.lang.String code = data.getSwiftCode()
                .code();

            if (code.toUpperCase()
                .endsWith(HEADQUARTER_SUFFIX)) {
                data.setHeadquarter(true);
            }
        }
    }

    private static void assignBranchesToHeadquarters(Map<String, BankData> bankDataMap) {
        Map<java.lang.String, BankData> headquartersMap = getHeadquartersMap(bankDataMap);

        for (BankData data : bankDataMap.values()) {
            if (!data.isHeadquarter()) {
                java.lang.String code = data.getSwiftCode()
                    .code();
                java.lang.String hqCode = getHqCode(code);
                BankData hq = headquartersMap.get(hqCode);

                if (hq != null) {
                    hq.addRelatedBank(data);
                }
            }
        }
    }

    private static java.lang.String getHqCode(java.lang.String code) {
        return code.substring(0, code.length() - HEADQUARTER_SUFFIX.length());
    }

    private static Map<java.lang.String, BankData> getHeadquartersMap(Map<String, BankData> bankDataMap) {
        Map<java.lang.String, BankData> headquartersMap = new HashMap<>();
        for (BankData bankData : bankDataMap.values()) {
            if (bankData.isHeadquarter()) {
                java.lang.String code = bankData.getSwiftCode()
                    .code();
                java.lang.String hqCode = getHqCode(code);
                headquartersMap.put(hqCode, bankData);
            }
        }
        return headquartersMap;
    }

    protected List<List<java.lang.String>> readExcelContentIntoList(java.lang.String filePath) {
        return ExcelToListReader.readExcelContentIntoList(filePath);
    }

    private static BankData readData(List<java.lang.String> row, Map<HeaderColumnName, Integer> headers) {
        return BankData.builder()
            .swiftCode(new String(getStringCellValue(row, headers, HeaderColumnName.SWIFT_CODE)))
            .address(getStringCellValue(row, headers, HeaderColumnName.ADDRESS))
            .codeType(getStringCellValue(row, headers, HeaderColumnName.CODE_TYPE))
            .countryISO2Code(getStringCellValueUpperCase(row, headers, HeaderColumnName.COUNTRY_ISO2CODE))
            .countryName(getStringCellValueUpperCase(row, headers, HeaderColumnName.COUNTRY_NAME))
            .name(getStringCellValue(row, headers, HeaderColumnName.NAME))
            .townName(getStringCellValue(row, headers, HeaderColumnName.TOWN_NAME))
            .timeZone(getStringCellValue(row, headers, HeaderColumnName.TIME_ZONE))
            .build();
    }

    private static java.lang.String getStringCellValue(List<java.lang.String> row, Map<HeaderColumnName, Integer> headers, HeaderColumnName columnName) {
        return row.get(headers.get(columnName))
            .trim();
    }

    private static java.lang.String getStringCellValueUpperCase(List<java.lang.String> row, Map<HeaderColumnName, Integer> headers, HeaderColumnName columnName) {
        return getStringCellValue(row, headers, columnName).toUpperCase();
    }

    private static Map<HeaderColumnName, Integer> readHeader(List<java.lang.String> row) {
        Map<HeaderColumnName, Integer> headers = new EnumMap<>(HeaderColumnName.class);

        for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
            java.lang.String header = row.get(columnIndex);
            if (columnIndex >= HeaderColumnName.values().length && header.isBlank()) {
                continue;
            }
            headers.put(HeaderColumnName.getByHeader(header), columnIndex);
        }

        return headers;
    }

    private static class ExcelToListReader {
        public static List<List<java.lang.String>> readExcelContentIntoList(java.lang.String filePath) {
            List<List<java.lang.String>> rows = new ArrayList<>();
            Sheet sheet = getExcelSheet(filePath);
            for (Row row : sheet) {
                List<java.lang.String> columns = new ArrayList<>();
                for (Cell cell : row) {
                    columns.add(cell.getStringCellValue());
                }
                rows.add(columns);
            }
            return rows;
        }

        private static Sheet getExcelSheet(java.lang.String filePath) {
            try (FileInputStream file = new FileInputStream(filePath)) {
                try (Workbook workbook = new XSSFWorkbook(file)) {
                    return workbook.getSheetAt(0);
                }
            } catch (IOException | GeneralException e) {
                log.error(java.lang.String.format("Failed to read file %s", filePath), e);
                throw new GeneralException(java.lang.String.format("Failed to read file %s", filePath), e);
            }
        }
    }
}
