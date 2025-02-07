package org.js.swiftcodes.service;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.api.model.BranchResponse;
import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToBankData;
import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToBranchResponse;
import static org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper.mapToHeadquarterResponse;

@Service
public class SingleSwiftCodeGetService {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public SingleSwiftCodeGetService(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public BankData getSwiftCode(String swiftCode) {
        BankDataEntity foundBank = findBankBySwiftCode(swiftCode);

        if (foundBank.isHeadquarter()) {
            List<BankDataEntity> branches = getBranches(foundBank);
            return mapToBankData(foundBank, branches);
        } else {
            return mapToBankData(foundBank);
        }
    }

    private List<BankDataEntity> getBranches(BankDataEntity foundBank) {
        List<BankDataEntity> branches = bankDataMapper.selectAllBranches(foundBank.getId());
        return branches != null ? branches : Collections.emptyList();
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
}
