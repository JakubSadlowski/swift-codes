package org.js.swiftcodes.api;

import org.js.swiftcodes.api.mappers.BankDataEntitiesMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/")
public class SwiftCodeController {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public SwiftCodeController(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    @GetMapping("swift-codes/{swift-code}")
    public ResponseEntity<BankData> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        BankDataEntity foundSwiftCode = bankDataMapper.selectOne(swiftCode);
        return ResponseEntity.ok(BankDataEntitiesMapper.mapToBankData(foundSwiftCode));
    }

    @GetMapping("swift-codes/country/{countryISO2Code}")
    public ResponseEntity<List<BankData>> getAllSwiftCodesForSpecificCountry(@PathVariable("countryISO2Code") String countryISO2Code) {
        return null;
    }
}
