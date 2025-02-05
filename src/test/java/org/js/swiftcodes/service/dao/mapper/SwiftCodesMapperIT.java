package org.js.swiftcodes.service.dao.mapper;

import org.js.swiftcodes.api.mappers.BankDataMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.config.DatabaseConfig;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.model.SwiftCode;
import org.js.swiftcodes.service.reader.SwiftCodesFileReader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Map;

@SpringBootTest
@ContextConfiguration(classes = { DatabaseConfig.class })
    //@ActiveProfiles("test")
class SwiftCodesMapperIT {

    @Autowired
    private SwiftCodesMapper swiftCodesMapper;

    @Test
    void whenSelectAllSwiftCodes_thenReturnAll() {
        // when
        List<BankDataEntity> swiftCodesEntities = swiftCodesMapper.fetchAllSwiftCodes();

        // then
        Assertions.assertNotNull(swiftCodesEntities);
        Assertions.assertEquals(1, swiftCodesEntities.size());
    }

    @Test
    void insertSwiftCodesListIntoDatabase() {
        // Given
        Map<SwiftCode, BankData> swiftCodes = SwiftCodesFileReader.newInstance()
            .readSwiftCodesFile("src/test/resources/test_swift_codes.xlsx");

        List<BankDataEntity> bankDataEntities = BankDataMapper.mapToBankDataEntitiesListWithIds(swiftCodes.values().stream().toList());

        // When
        swiftCodesMapper.insert(bankDataEntities);
        List<BankDataEntity> insertedSwiftCodesList = swiftCodesMapper.fetchAllSwiftCodes();

        // Then
        Assertions.assertNotNull(insertedSwiftCodesList);
    }

}