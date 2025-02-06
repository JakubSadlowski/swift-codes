package org.js.swiftcodes.api;

import org.js.swiftcodes.api.mappers.BankDataEntitiesMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwiftCodeController {
    private final BankDataMapper swiftCodesMapper;

    @Autowired
    public SwiftCodeController(BankDataMapper bankDataMapper) {
        this.swiftCodesMapper = bankDataMapper;
    }

    @GetMapping("/v1/swift-codes/{swift-code}")
    public ResponseEntity<BankData> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        BankDataEntity foundSwiftCode = swiftCodesMapper.selectOne(swiftCode);
        return ResponseEntity.ok(BankDataEntitiesMapper.mapToBankData(foundSwiftCode));
    }
}
