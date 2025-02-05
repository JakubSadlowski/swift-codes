package org.js.swiftcodes.service.dao.mapper;

import org.js.swiftcodes.config.DatabaseConfig;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = { DatabaseConfig.class })
    //@ActiveProfiles("test")
class BankDataInsertUpdateDeleteIT {
    private static final String TEST_SWIFT_CODE1 = "TEST1XXX";
    private static final String TEST_SWIFT_CODE2 = "TEST2XXX";

    @Autowired
    private BankDataMapper bankDataMapper;

    @Test
    void initializeBeforeTest() {
        bankDataMapper.deleteOne(TEST_SWIFT_CODE1);
        bankDataMapper.deleteOne(TEST_SWIFT_CODE2);

        BankDataEntity bankData1 = BankDataEntity.builder()
            .swiftCode(TEST_SWIFT_CODE1)
            .codeType("BIC11")
            .name("TEST1")
            .address("")
            .countryIso2Code("CL")
            .countryName("CHILE")
            .townName("SANTIAGO")
            .timeZone("Pacific/Easter")
            .isHeadquarter(true)
            .build();
        bankDataMapper.insert(bankData1);

        BankDataEntity bankData2 = BankDataEntity.builder()
            .swiftCode(TEST_SWIFT_CODE2)
            .codeType("BIC11")
            .name("TEST2")
            .address("")
            .countryIso2Code("CL")
            .countryName("CHILE")
            .townName("SANTIAGO")
            .timeZone("Pacific/Easter")
            .isHeadquarter(true)
            .build();
        bankDataMapper.insert(bankData2);
    }

    @Test
    void testSelectSingleSwiftCode() {
        // when
        BankDataEntity fetchedBankData = bankDataMapper.selectOne(TEST_SWIFT_CODE1);

        // then
        // TODO: Add full check of all fields
        Assertions.assertNotNull(fetchedBankData);
        Assertions.assertEquals("BIC11", fetchedBankData.getCodeType());
    }

    @Test
    void testSelectAllInsertedSwiftCodes() {
        // when
        List<BankDataEntity> fetchedBankData = bankDataMapper.selectAll();

        // then
        Assertions.assertNotNull(fetchedBankData);
        Assertions.assertTrue(fetchedBankData.size() >= 2);
    }

    void cleanupAfterTest() {
        bankDataMapper.deleteOne(TEST_SWIFT_CODE1);
        bankDataMapper.deleteOne(TEST_SWIFT_CODE2);
    }

}
