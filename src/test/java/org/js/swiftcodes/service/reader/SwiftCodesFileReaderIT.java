package org.js.swiftcodes.service.reader;

import org.js.swiftcodes.service.exceptions.GeneralException;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.model.String;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA1;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA2;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA3;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA4;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA5;
import static org.js.swiftcodes.service.reader.AssertSwiftCodeUtil.assertSwiftCode;

class SwiftCodesFileReaderIT {

    @Test
    void readSwiftCodesFile_Data_Read_Correctly() {
        // When
        Map<String, BankData> swiftCodes = SwiftCodesFileReader.newInstance()
            .readSwiftCodesFile("src/test/resources/test_swift_codes.xlsx");

        // Then
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA1, "BCHICLRMXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA2, "BCHICLRMEXP");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA3, "AIPOPLP1XXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA4, "BIGBPLPWXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA5, "BIGBPLPWCUS");
    }

    @Test
    void givenNonExistingFile_whenRead_ThenExceptionIsThrown() {
        // Given
        SwiftCodesFileReader fileReader = SwiftCodesFileReader.newInstance();

        // When Then
        Assertions.assertThrows(GeneralException.class, () -> {
            fileReader.readSwiftCodesFile("non existing file");
        });
    }

    @Test
    void givenFileWithUnexpectedHeader_whenRead_ThenExceptionIsThrown() {
        // Given
        SwiftCodesFileReader fileReader = SwiftCodesFileReader.newInstance();

        // When Then
        Assertions.assertThrows(GeneralException.class, () -> {
            fileReader.readSwiftCodesFile("src/test/resources/test_swift_codes_wrong_headers.xlsx");
        });
    }

}