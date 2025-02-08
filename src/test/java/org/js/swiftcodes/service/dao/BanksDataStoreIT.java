package org.js.swiftcodes.service.dao;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.config.BuiltinDbConfig;
import org.js.swiftcodes.service.TestBankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = { BuiltinDbConfig.class })
@ActiveProfiles("test")
class BanksDataStoreIT {

    @Autowired
    private BanksDataStore banksDataStore;
    @Autowired
    private BankDataMapper bankDataMapper;

    List<BankData> inputBanksData = new ArrayList<>();

    @BeforeEach
    void prepareInputData() {
        inputBanksData.addAll(List.of(TestBankData.EXPECTED_BANK_DATA1, TestBankData.EXPECTED_BANK_DATA2, TestBankData.EXPECTED_BANK_DATA3));
    }

    @Test
    void testStoreBankDataIntoDatabase() {
        // when
        List<BankDataEntity> bankDataEntities = banksDataStore.insertList(inputBanksData);

        // then
        Assertions.assertEquals(3, bankDataEntities.size());
        BankDataEntity resultBankData1 = bankDataEntities.get(0);
        BankDataEntity resultBankData2 = bankDataEntities.get(1);
        BankDataEntity resultBankData3 = bankDataEntities.get(2);
        Assertions.assertTrue(resultBankData3.isHeadquarter());
        Assertions.assertTrue(resultBankData1.isHeadquarter());
        Assertions.assertFalse(resultBankData2.isHeadquarter());
        Assertions.assertEquals(resultBankData2.getParentId(), resultBankData1.getId());

        assertEqualsBankData(TestBankData.EXPECTED_BANK_DATA1, resultBankData1);
        assertEqualsBankData(TestBankData.EXPECTED_BANK_DATA2, resultBankData2);
        assertEqualsBankData(TestBankData.EXPECTED_BANK_DATA3, resultBankData3);
    }

    private static void assertEqualsBankData(BankData expectedBankData, BankDataEntity resultBankData) {
        Assertions.assertEquals(expectedBankData.getSwiftCode(), resultBankData.getSwiftCode());
        Assertions.assertEquals(expectedBankData.getName(), resultBankData.getName());
        Assertions.assertEquals(expectedBankData.getAddress(), resultBankData.getAddress());
        Assertions.assertEquals(expectedBankData.getCodeType(), resultBankData.getCodeType());
        Assertions.assertEquals(expectedBankData.getCountryName(), resultBankData.getCountryName());
        Assertions.assertEquals(expectedBankData.getTownName(), resultBankData.getTownName());
        Assertions.assertEquals(expectedBankData.getTimeZone(), resultBankData.getTimeZone());
        Assertions.assertEquals(expectedBankData.getCountryISO2Code(), resultBankData.getCountryIso2Code());
    }

    @AfterEach
    void cleanupAfterTest() {
        for (BankData bankData : getBanks(false)) {
            bankDataMapper.deleteOne(bankData.getSwiftCode());
        }
        for (BankData bankData : getBanks(true)) {
            bankDataMapper.deleteOne(bankData.getSwiftCode());
        }
    }

    private List<BankData> getBanks(boolean isHeadquarter) {
        return inputBanksData.stream()
            .filter(b -> b.isHeadquarter() == isHeadquarter)
            .toList();
    }

}