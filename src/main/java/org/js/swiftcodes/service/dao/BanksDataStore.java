package org.js.swiftcodes.service.dao;

import org.apache.ibatis.annotations.Param;
import org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class BanksDataStore {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public BanksDataStore(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public List<BankDataEntity> insertList(@Param("swiftCodes") List<BankData> bankDataList) {
        // note those entities do not have id and parentID assigned, this will be done after db insert of headquarters
        List<BankDataEntity> bankDataEntities = mapToBankDataEntities(bankDataList);

        Map<String, BankDataEntity> headquarters = firstInsertHeadquarters(bankDataEntities);
        List<BankDataEntity> branches = getBranches(bankDataEntities);

        for (BankDataEntity branch : branches) {
            String hqSwiftCode = branch.getHeadquarterSwiftCode();
            mapParentIdIfBranchMatchesWithHq(branch, headquarters, hqSwiftCode);
            bankDataMapper.insert(branch);
        }

        return bankDataEntities;
    }

    private List<BankDataEntity> mapToBankDataEntities(List<BankData> bankDataList) {
        List<BankDataEntity> resultList = new ArrayList<>(bankDataList.size());
        for (BankData bankData : bankDataList) {
            resultList.add(BankDataAndResponsesMapper.mapToBankDataEntity(bankData));
        }
        return resultList;
    }

    private static void mapParentIdIfBranchMatchesWithHq(BankDataEntity branch, Map<String, BankDataEntity> headquarters, String hqSwiftCode) {
        BankDataEntity headquarter = headquarters.get(hqSwiftCode);
        if (headquarter != null) {
            branch.setParentId(headquarter.getId());
        }
    }

    private static List<BankDataEntity> getBranches(List<BankDataEntity> swiftCodes) {
        return swiftCodes.stream()
            .filter(b -> !b.isHeadquarter())
            .toList();
    }

    private Map<String, BankDataEntity> firstInsertHeadquarters(List<BankDataEntity> swiftCodes) {
        Map<String, BankDataEntity> headquarters = swiftCodes.stream()
            .filter(BankDataEntity::isHeadquarter)
            .collect(Collectors.toMap(BankDataEntity::getHeadquarterSwiftCode, Function.identity(), (key1, key2) -> {
                throw new GeneralException(String.format("duplicate key value found %s", key1));
            }));
        for (BankDataEntity bankDataEntity : headquarters.values()) {
            bankDataMapper.insert(bankDataEntity);
        }

        return headquarters;
    }
}
