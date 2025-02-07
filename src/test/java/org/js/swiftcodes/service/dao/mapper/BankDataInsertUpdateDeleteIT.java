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
    private static final String TEST_SWIFT_CODE_HEADQUARTER1 = "TEST1DDDXXX";
    private static final String TEST_SWIFT_CODE_HEADQUARTER2 = "TEST2DDDXXX";
    private static final String TEST_SWIFT_CODE_BRANCH_CODE = "TEST1DDDB1C";
    private static final String TEST_SWIFT_CODE_INVALID = "K";

    @Autowired
    private BankDataMapper bankDataMapper;

    @Test
    void initializeBeforeTest() {
        bankDataMapper.deleteOne(TEST_SWIFT_CODE_BRANCH_CODE);
        bankDataMapper.deleteOne(TEST_SWIFT_CODE_HEADQUARTER1);
        bankDataMapper.deleteOne(TEST_SWIFT_CODE_HEADQUARTER2);

        BankDataEntity bankData1 = BankDataEntity.builder()
            .swiftCode(TEST_SWIFT_CODE_HEADQUARTER1)
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
            .swiftCode(TEST_SWIFT_CODE_HEADQUARTER2)
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

        BankDataEntity bankData3 = BankDataEntity.builder()
            .swiftCode(TEST_SWIFT_CODE_BRANCH_CODE)
            .codeType("BIC11")
            .name("TEST3")
            .address("")
            .countryIso2Code("CL")
            .countryName("CHILE")
            .townName("SANTIAGO")
            .timeZone("Pacific/Easter")
            .isHeadquarter(false)
            .parentId(bankData1.getId())
            .build();
        bankDataMapper.insert(bankData3);
    }

    @Test
    void testSelectSingleSwiftCode() {
        // when
        BankDataEntity fetchedBankData = bankDataMapper.selectOne(TEST_SWIFT_CODE_HEADQUARTER1);

        // then
        Assertions.assertNotNull(fetchedBankData);
        Assertions.assertEquals("BIC11", fetchedBankData.getCodeType());
        Assertions.assertEquals("TEST1", fetchedBankData.getName());
        Assertions.assertEquals("", fetchedBankData.getAddress());
        Assertions.assertEquals("CL", fetchedBankData.getCountryIso2Code());
        Assertions.assertEquals("CHILE", fetchedBankData.getCountryName());
        Assertions.assertEquals("SANTIAGO", fetchedBankData.getTownName());
        Assertions.assertEquals("Pacific/Easter", fetchedBankData.getTimeZone());
        Assertions.assertEquals(TEST_SWIFT_CODE_HEADQUARTER1, fetchedBankData.getSwiftCode());
        Assertions.assertTrue(fetchedBankData.isHeadquarter());
    }

    @Test
    void shouldReturnNull_whenSelectSingleSwiftCode() {
        // when
        BankDataEntity fetchedBankData = bankDataMapper.selectOne(TEST_SWIFT_CODE_INVALID);

        // then
        Assertions.assertNull(fetchedBankData);
    }

    @Test
    void testSelectAllInsertedSwiftCodes() {
        // when
        List<BankDataEntity> fetchedBankData = bankDataMapper.selectAll();

        // then
        Assertions.assertNotNull(fetchedBankData);
        Assertions.assertTrue(fetchedBankData.size() >= 2);
    }

    @Test
    void testSelectAllInsertedBranchesSwiftCodes() {
        // when
        List<BankDataEntity> fetchedBankData = bankDataMapper.selectAllBranches(bankDataMapper.selectOne(TEST_SWIFT_CODE_HEADQUARTER1).getId());

        // then
        Assertions.assertNotNull(fetchedBankData);
        Assertions.assertFalse(fetchedBankData.isEmpty());
    }

    void cleanupAfterTest() {
        bankDataMapper.deleteOne(TEST_SWIFT_CODE_HEADQUARTER1);
        bankDataMapper.deleteOne(TEST_SWIFT_CODE_HEADQUARTER2);
    }

}
