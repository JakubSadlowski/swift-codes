package org.js.swiftcodes.service.dao;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.config.DatabaseConfig;
import org.js.swiftcodes.service.TestBankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.ArrayList;
import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = { DatabaseConfig.class })
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
        Assertions.assertTrue(resultBankData1.isHeadquarter());
        Assertions.assertFalse(resultBankData2.isHeadquarter());
        Assertions.assertEquals(resultBankData2.getParentId(), resultBankData1.getId());
        BankDataEntity resultBankData3 = bankDataEntities.get(2);
        // TODO: test me further
    }

    @AfterEach
    void cleanupAfterTest() {
        for (BankData bankData : getBanks(false)) {
            bankDataMapper.deleteOne(bankData.getSwiftCode()
                .code());
        }
        for (BankData bankData : getBanks(true)) {
            bankDataMapper.deleteOne(bankData.getSwiftCode()
                .code());
        }
    }

    private List<BankData> getBanks(boolean isHeadquarter) {
        return inputBanksData.stream()
            .filter(b -> b.isHeadquarter() == isHeadquarter)
            .toList();
    }

}