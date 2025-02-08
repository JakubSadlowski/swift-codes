package org.js.swiftcodes.service;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static org.mockito.Mockito.doReturn;

@ExtendWith(MockitoExtension.class)
class GetBanksDataByCountryCodeServiceTest {
    @Mock
    private BankDataMapper bankDataMapper;

    @InjectMocks
    @Spy
    private GetBanksDataByCountryCodeService service;

    @Test
    void testFetchBanksDataByCountryCode() {
        // Given
        String countryCode = "PL";
        doReturn(List.of(TestBankData.EXPECTED_BANK_DATA_ENTITY3,
            TestBankData.EXPECTED_BANK_DATA_ENTITY1,
            TestBankData.EXPECTED_BANK_DATA_ENTITY2)).when(bankDataMapper)
            .selectBankDataByCountryISO2Code(countryCode);

        // When
        List<BankData> banksData = service.getBanksData(countryCode);

        // Then
        Assertions.assertNotNull(banksData);
        Assertions.assertEquals(2, banksData.size());
        BankData firstHq = banksData.get(0);
        Assertions.assertEquals(TestBankData.EXPECTED_BANK_DATA_ENTITY3.getSwiftCode(), firstHq.getSwiftCode());
        Assertions.assertEquals(0,
            firstHq.getBranches()
                .size());
        BankData secondHq = banksData.get(1);
        Assertions.assertEquals(TestBankData.EXPECTED_BANK_DATA_ENTITY1.getSwiftCode(), secondHq.getSwiftCode());
        Assertions.assertEquals(1,
            secondHq.getBranches()
                .size());
    }

}