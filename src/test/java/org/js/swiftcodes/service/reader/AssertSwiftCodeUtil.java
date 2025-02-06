package org.js.swiftcodes.service.reader;

import org.js.swiftcodes.api.model.BankData;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AssertSwiftCodeUtil {
    public static void assertSwiftCode(Map<String, BankData> swiftCodes, BankData expectedBankData, String swiftCode) {
        BankData bankData1 = swiftCodes.get(swiftCode);
        assertNotNull(bankData1);
        assertEquals(expectedBankData, bankData1);
    }
}
