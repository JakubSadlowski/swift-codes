package org.js.swiftcodes.service.reader;

import lombok.extern.apachecommons.CommonsLog;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.js.swiftcodes.service.exceptions.GeneralException;
import org.js.swiftcodes.service.model.BankData;
import org.js.swiftcodes.service.model.SwiftCode;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@CommonsLog
public class SwiftCodesFileReader {
    private static final String HEADQUARTER_SUFFIX = "XXX";

    public static SwiftCodesFileReader newInstance() {
        return new SwiftCodesFileReader();
    }

    public Map<SwiftCode, BankData> readSwiftCodesFile(String fileName) {
        Map<SwiftCode, BankData> bankDataMap = getSwiftCodeBankDataWithNotAssignedHeadquarters(fileName);
        detectHeadquarters(bankDataMap);
        assignBranchesToHeadquarters(bankDataMap);

        return bankDataMap;
    }

    private Map<SwiftCode, BankData> getSwiftCodeBankDataWithNotAssignedHeadquarters(String fileName) {
        Map<SwiftCode, BankData> bankDataMap = new HashMap<>();

        Map<HeaderColumnName, Integer> headers = null;

        List<List<String>> rows = readExcelContentIntoList(fileName);
        for (List<String> row : rows) {
            if (headers == null) {
                headers = readHeader(row);
            } else {
                BankData bankData = readData(row, headers);
                bankDataMap.put(bankData.getSwiftCode(), bankData);
            }
        }
        return bankDataMap;
    }

    private static void detectHeadquarters(Map<SwiftCode, BankData> bankDataMap) {
        for (BankData data : bankDataMap.values()) {
            String code = data.getSwiftCode()
                .code();

            if (code.toUpperCase()
                .endsWith(HEADQUARTER_SUFFIX)) {
                data.setHeadquarter(true);
            }
        }
    }

    private static void assignBranchesToHeadquarters(Map<SwiftCode, BankData> bankDataMap) {
        Map<String, BankData> headquartersMap = getHeadquartersMap(bankDataMap);

        for (BankData data : bankDataMap.values()) {
            if (!data.isHeadquarter()) {
                String code = data.getSwiftCode()
                    .code();
                String hqCode = getHqCode(code);
                BankData hq = headquartersMap.get(hqCode);

                if (hq != null) {
                    hq.addRelatedBank(data);
                }
            }
        }
    }

    private static String getHqCode(String code) {
        return code.substring(0, code.length() - HEADQUARTER_SUFFIX.length());
    }

    private static Map<String, BankData> getHeadquartersMap(Map<SwiftCode, BankData> bankDataMap) {
        Map<String, BankData> headquartersMap = new HashMap<>();
        for (BankData bankData : bankDataMap.values()) {
            if (bankData.isHeadquarter()) {
                String code = bankData.getSwiftCode()
                    .code();
                String hqCode = getHqCode(code);
                headquartersMap.put(hqCode, bankData);
            }
        }
        return headquartersMap;
    }

    protected List<List<String>> readExcelContentIntoList(String filePath) {
        return ExcelToListReader.readExcelContentIntoList(filePath);
    }

    private static BankData readData(List<String> row, Map<HeaderColumnName, Integer> headers) {
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

    private static String getStringCellValue(List<String> row, Map<HeaderColumnName, Integer> headers, HeaderColumnName columnName) {
        return row.get(headers.get(columnName))
            .trim();
    }

    private static String getStringCellValueUpperCase(List<String> row, Map<HeaderColumnName, Integer> headers, HeaderColumnName columnName) {
        return getStringCellValue(row, headers, columnName).toUpperCase();
    }

    private static Map<HeaderColumnName, Integer> readHeader(List<String> row) {
        Map<HeaderColumnName, Integer> headers = new EnumMap<>(HeaderColumnName.class);

        for (int columnIndex = 0; columnIndex < row.size(); columnIndex++) {
            String header = row.get(columnIndex);
            if (columnIndex >= HeaderColumnName.values().length && header.isBlank()) {
                continue;
            }
            headers.put(HeaderColumnName.getByHeader(header), columnIndex);
        }

        return headers;
    }

    private static class ExcelToListReader {
        public static List<List<String>> readExcelContentIntoList(String filePath) {
            List<List<String>> rows = new ArrayList<>();
            Sheet sheet = getExcelSheet(filePath);
            for (Row row : sheet) {
                List<String> columns = new ArrayList<>();
                for (Cell cell : row) {
                    columns.add(cell.getStringCellValue());
                }
                rows.add(columns);
            }
            return rows;
        }

        private static Sheet getExcelSheet(String filePath) {
            try (FileInputStream file = new FileInputStream(filePath)) {
                try (Workbook workbook = new XSSFWorkbook(file)) {
                    return workbook.getSheetAt(0);
                }
            } catch (IOException | GeneralException e) {
                log.error(String.format("Failed to read file %s", filePath), e);
                throw new GeneralException(String.format("Failed to read file %s", filePath), e);
            }
        }
    }
}
