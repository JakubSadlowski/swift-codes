package org.js.swiftcodes.api;

import lombok.extern.apachecommons.CommonsLog;
import org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper;
import org.js.swiftcodes.api.model.BranchResponse;
import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToBranchResponse;
import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToHeadquarterResponse;

@RestController
@RequestMapping("/v1/")
@CommonsLog
public class SwiftCodeController {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public SwiftCodeController(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    @GetMapping("swift-codes/{swift-code}")
    public ResponseEntity<?> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        if (swiftCode.length() != 8)
            throw new BadRequestException(String.format("Parameter swiftCode %s is invalid. SWIFT Code can't be empty and must have 8 characters.", swiftCode));

        BankDataEntity foundBank = bankDataMapper.selectOne(swiftCode);

        if (swiftCode.toUpperCase()
            .endsWith("XXX")) {
            List<BranchResponse> branchResponses = getBranchResponses(foundBank.getId());
            return ResponseEntity.ok(mapToHeadquarterResponse(foundBank, branchResponses));
        }

        return ResponseEntity.ok(mapToBranchResponse(foundBank));
    }

    @GetMapping("swift-codes/country/{countryISO2Code}")
    public ResponseEntity<List<BranchResponse>> getAllSwiftCodesForSpecificCountry(@PathVariable("countryISO2Code") String countryISO2Code) {
        return null;
    }

    private List<BranchResponse> getBranchResponses(Integer headquarterId) {
        return bankDataMapper.selectAllBranches(headquarterId)
            .stream()
            .map(BankDataAndResponsesMapper::mapToBranchResponse)
            .collect(Collectors.toList());
    }
}
