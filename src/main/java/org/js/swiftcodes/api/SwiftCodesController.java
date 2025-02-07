package org.js.swiftcodes.api;

import lombok.extern.apachecommons.CommonsLog;
import org.js.swiftcodes.api.model.BranchResponse;
import org.js.swiftcodes.api.model.Error;
import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;

import java.util.ArrayList;
import java.util.List;

import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToBranchResponse;
import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToHeadquarterResponse;

@RestController
@RequestMapping("/v1/")
@CommonsLog
public class SwiftCodesController {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public SwiftCodesController(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    @GetMapping("swift-codes/{swift-code}")
    public ResponseEntity<?> getSwiftCode(@PathVariable("swift-code") String swiftCode) {
        validateSwiftCode(swiftCode);

        BankDataEntity foundBank = findBankBySwiftCode(swiftCode);

        return isHeadquarterSwiftCode(swiftCode) ? createHeadquarterResponse(foundBank) : createBranchResponse(foundBank);
    }

    @GetMapping("swift-codes/country/{countryISO2Code}")
    public ResponseEntity<List<BranchResponse>> getAllSwiftCodesForSpecificCountry(@PathVariable("countryISO2Code") String countryISO2Code) {
        return null;
    }

    private void validateSwiftCode(String swiftCode) {
        if (swiftCode.length() != 11) {
            throw new BadRequestException(String.format("Parameter swiftCode %s is invalid. SWIFT Code can't be empty and must have 8 characters.", swiftCode));
        }
    }

    private BankDataEntity findBankBySwiftCode(String swiftCode) {
        BankDataEntity foundBank = bankDataMapper.selectOne(swiftCode);

        if (foundBank == null) {
            throw new SwiftCodeNotFoundException(String.format("Swift code %s not found.", swiftCode));
        }

        return foundBank;
    }

    private boolean isHeadquarterSwiftCode(String swiftCode) {
        return swiftCode.toUpperCase()
            .endsWith("XXX");
    }

    private ResponseEntity<?> createHeadquarterResponse(BankDataEntity foundBank) {
        List<BranchResponse> branchResponses = getBranchResponses(foundBank.getId());
        return ResponseEntity.ok(mapToHeadquarterResponse(foundBank, branchResponses));
    }

    private ResponseEntity<?> createBranchResponse(BankDataEntity foundBank) {
        return ResponseEntity.ok(mapToBranchResponse(foundBank));
    }

    private List<BranchResponse> getBranchResponses(Integer headquarterId) {
        List<BankDataEntity> branchesFromDatabase = bankDataMapper.selectAllBranches(headquarterId);
        List<BranchResponse> branchResponses = new ArrayList<>();
        for (BankDataEntity bankDataEntity : branchesFromDatabase) {
            branchResponses.add(mapToBranchResponse(bankDataEntity));
        }
        return branchResponses;
    }

    @ExceptionHandler(SwiftCodeNotFoundException.class)
    public ResponseEntity<Error> handleAnimalNotFoundException(SwiftCodeNotFoundException ex, WebRequest request) {
        Error response = Error.of("NOT_FOUND", ex.getMessage());
        log.warn("Handled AnimalNotFoundException: " + ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(response);
    }
}
