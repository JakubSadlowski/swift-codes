package org.js.swiftcodes.service.reader;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.model.String;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

public class AssertSwiftCodeUtil {
    public static void assertSwiftCode(Map<String, BankData> swiftCodes, BankData expectedBankData, java.lang.String swiftCode) {
        String swiftCode1 = new String(swiftCode);
        BankData bankData1 = swiftCodes.get(swiftCode1);
        assertNotNull(bankData1);
        assertEquals(expectedBankData, bankData1);
    }
}
