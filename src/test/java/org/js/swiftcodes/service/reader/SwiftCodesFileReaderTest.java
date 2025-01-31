package org.js.swiftcodes.service.reader;

import org.js.swiftcodes.service.model.BankData;
import org.js.swiftcodes.service.model.SwiftCode;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.js.swiftcodes.service.TestBankData.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class SwiftCodesFileReaderTest {

    @Test
    void readSwiftCodesFile_Data_Read_Correctly() {
        // When
        Map<SwiftCode, BankData> swiftCodes = SwiftCodesFileReader.readSwiftCodesFile("src/main/resources/Test_swift_codes.xlsx");

        // Then
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA1, "BCHICLRMXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA2, "BCHICLRMEXP");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA3, "AIPOPLP1XXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA4, "BIGBPLPWXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA5, "BIGBPLPWCUS");
    }

    private static void assertSwiftCode(Map<SwiftCode, BankData> swiftCodes, BankData expectedBankData,  String swiftCode) {
        SwiftCode swiftCode1 = new SwiftCode(swiftCode);
        BankData bankData1 = swiftCodes.get(swiftCode1);
        assertNotNull(bankData1);
        assertEquals(expectedBankData, bankData1);
    }
}