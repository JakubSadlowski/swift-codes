package org.js.swiftcodes.service;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

import static org.js.swiftcodes.api.mappers.BankDataAndEntityMapper.mapToBankData;

@Service
public class SingleSwiftCodeGetService {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public SingleSwiftCodeGetService(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public BankData getSwiftCode(String swiftCode) {
        BankDataEntity fetchedBankData = findBankBySwiftCode(swiftCode);

        if (fetchedBankData.isHeadquarter()) {
            List<BankDataEntity> branches = getBranches(fetchedBankData);
            return mapToBankData(fetchedBankData, branches);
        } else {
            return mapToBankData(fetchedBankData);
        }
    }

    private BankDataEntity findBankBySwiftCode(String swiftCode) {
        BankDataEntity fetchedBankData = bankDataMapper.selectOne(swiftCode);

        if (fetchedBankData == null) {
            throw new SwiftCodeNotFoundException(String.format("Swift code %s not found.", swiftCode));
        }

        return fetchedBankData;
    }

    private List<BankDataEntity> getBranches(BankDataEntity foundBank) {
        List<BankDataEntity> branches = bankDataMapper.selectAllBranches(foundBank.getId());
        return branches != null ? branches : Collections.emptyList();
    }
}
