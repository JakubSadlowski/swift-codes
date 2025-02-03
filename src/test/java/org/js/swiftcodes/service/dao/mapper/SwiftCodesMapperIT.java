package org.js.swiftcodes.service.dao.mapper;

import org.js.swiftcodes.config.DatabaseConfig;
import org.js.swiftcodes.service.dao.entity.SwiftCodesEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;

@SpringBootTest
@ContextConfiguration(classes = { DatabaseConfig.class })
    //@ActiveProfiles("test")
class SwiftCodesMapperIT {

    @Autowired
    private SwiftCodesMapper swiftCodesMapper;

    @Test
    void whenSelectAllSwiftCodes_thenReturnAll() {
        // when
        List<SwiftCodesEntity> swiftCodesEntities = swiftCodesMapper.fetchAllSwiftCodes();

        // then
        Assertions.assertNotNull(swiftCodesEntities);
        Assertions.assertEquals(1, swiftCodesEntities.size());
    }

}