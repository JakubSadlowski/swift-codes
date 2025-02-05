package org.js.swiftcodes.api;

import org.js.swiftcodes.api.mappers.BankDataMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.SwiftCodesMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SwiftCodeController {
    private final SwiftCodesMapper swiftCodesMapper;

    @Autowired
    public SwiftCodeController(SwiftCodesMapper swiftCodesMapper,BankDataMapper bankDataMapper) {
        this.swiftCodesMapper = swiftCodesMapper;
    }

    @GetMapping("/v1/swift-codes/{swift-code}")
    public ResponseEntity<BankData> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        BankDataEntity foundSwiftCode = swiftCodesMapper.getSwiftCode(swiftCode);
        return ResponseEntity.ok(BankDataMapper.mapToBankData(foundSwiftCode));
    }
}
