package org.js.swiftcodes.service.reader;

import org.js.swiftcodes.api.model.BankData;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Map;

import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA1;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA2;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA3;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA4;
import static org.js.swiftcodes.service.TestBankData.EXPECTED_BANK_DATA5;
import static org.js.swiftcodes.service.reader.AssertSwiftCodeUtil.assertSwiftCode;
import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class SwiftCodesFileReaderTest {
    @InjectMocks
    @Spy
    private SwiftCodesFileReader reader;

    private static final String DUMMY_EXCEL_FILE = "dummy.xlsx";

    @Test
    void readSwiftCodesFile_Data_Read_Correctly() {
        // Given
        doReturn(List.of(List.of("COUNTRY ISO2 CODE", "SWIFT CODE", "CODE TYPE", "NAME", "ADDRESS", "TOWN NAME", "COUNTRY NAME", "TIME ZONE"),
            List.of("CL", "BCHICLRMXXX", "BIC11", "BANCO DE CHILE", "", "SANTIAGO", "CHILE", "Pacific/Easter"),
            List.of("CL", "BCHICLRMEXP", "BIC11", "BANCO DE CHILE", "", "SANTIAGO", "CHILE", "Pacific/Easter"),
            List.of("PL", "AIPOPLP1XXX", "BIC11", "SANTANDER CONSUMER BANK SPOLKA AKCYJNA", "STRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611", "WROCLAW", "POLAND", "Europe/Warsaw"),
            List.of("PL", "BIGBPLPWXXX", "BIC11", "BANK MILLENNIUM S.A.", "HARMONY CENTER UL. STANISLAWA ZARYNA 2A WARSZAWA, MAZOWIECKIE, 02-593", "WARSZAWA", "POLAND", "Europe/Warsaw"),
            List.of("PL", "BIGBPLPWCUS", "BIC11", "BANK MILLENNIUM S.A.", "HARMONY CENTER UL. STANISLAWA ZARYNA 2A WARSZAWA, MAZOWIECKIE, 02-593", "WARSZAWA", "POLAND", "Europe/Warsaw"))).when(reader)
            .readExcelContentIntoList(DUMMY_EXCEL_FILE);

        // When
        Map<String, BankData> swiftCodes = reader.readSwiftCodesFile(DUMMY_EXCEL_FILE);

        // Then
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA1, "BCHICLRMXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA2, "BCHICLRMEXP");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA3, "AIPOPLP1XXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA4, "BIGBPLPWXXX");
        assertSwiftCode(swiftCodes, EXPECTED_BANK_DATA5, "BIGBPLPWCUS");
    }

}