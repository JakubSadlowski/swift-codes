package org.js.swiftcodes.service.dao;

import org.apache.ibatis.annotations.Param;
import org.js.swiftcodes.api.mappers.BankDataAndResponsesMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.GeneralException;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
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

    public boolean isBanksDataEmpty() {
        return this.bankDataMapper.selectAll()
            .isEmpty();
    }

    public List<BankDataEntity> insertList(@Param("swiftCodes") List<BankData> bankDataList) {
        // note those entities do not have id and parentID assigned, this will be done after db insert of headquarters
        List<BankDataEntity> bankDataEntities = mapToBankDataEntities(bankDataList);

        // read data
        Map<String, BankDataEntity> headquarters = getHeadquartersGroupedByBaseHqSwiftCode(bankDataEntities);
        List<BankDataEntity> branches = getBranches(bankDataEntities);

        validateDataBeforeStore(bankDataList, headquarters.values(), branches);

        // store data
        insertHeadquarters(headquarters.values());
        insertBranches(branches, headquarters);

        return bankDataEntities;
    }

    private List<BankDataEntity> mapToBankDataEntities(List<BankData> bankDataList) {
        List<BankDataEntity> resultList = new ArrayList<>(bankDataList.size());
        for (BankData bankData : bankDataList) {
            resultList.add(BankDataAndResponsesMapper.mapToBankDataEntity(bankData));
        }
        return resultList;
    }

    private static Map<String, BankDataEntity> getHeadquartersGroupedByBaseHqSwiftCode(List<BankDataEntity> bankDataList) {
        return bankDataList.stream()
            .filter(BankDataEntity::isHeadquarter)
            .collect(Collectors.toMap(BankDataEntity::getHeadquarterBaseSwiftCode, Function.identity(), ((existing, replacement) -> replacement)));
    }

    private static List<BankDataEntity> getBranches(List<BankDataEntity> swiftCodes) {
        return swiftCodes.stream()
            .filter(b -> !b.isHeadquarter())
            .toList();
    }

    private static void validateDataBeforeStore(Collection<BankData> bankDataList,
        Collection<BankDataEntity> headquarters,
        Collection<BankDataEntity> branches) {
        if (headquarters.size() + branches.size() != bankDataList.size()) {
            throw new GeneralException("Number of headquarters and branches does not match with the input.");
        }
    }

    private void insertHeadquarters(Collection<BankDataEntity> headquarters) {
        for (BankDataEntity bankDataEntity : headquarters) {
            bankDataMapper.insert(bankDataEntity);
        }
    }

    private void insertBranches(List<BankDataEntity> branches, Map<String, BankDataEntity> headquarters) {
        for (BankDataEntity branch : branches) {
            String hqSwiftCode = branch.getHeadquarterBaseSwiftCode();
            mapParentIdIfBranchMatchesWithHq(branch, headquarters, hqSwiftCode);
            bankDataMapper.insert(branch);
        }
    }

    private static void mapParentIdIfBranchMatchesWithHq(BankDataEntity branch, Map<String, BankDataEntity> headquarters, String hqSwiftCode) {
        BankDataEntity headquarter = headquarters.get(hqSwiftCode);
        if (headquarter != null) {
            branch.setParentId(headquarter.getId());
        }
    }

}
