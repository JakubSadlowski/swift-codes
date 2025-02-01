package org.js.swiftcodes.service.reader;

import org.js.swiftcodes.service.model.BankData;
import org.js.swiftcodes.service.model.SwiftCode;
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
        Map<SwiftCode, BankData> swiftCodes = SwiftCodesFileReader.newInstance()
            .readSwiftCodesFile("src/test/resources/test_swift_codes.xlsx");

        // Then
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA1, "BCHICLRMXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA2, "BCHICLRMEXP");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA3, "AIPOPLP1XXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA4, "BIGBPLPWXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA5, "BIGBPLPWCUS");
    }

}