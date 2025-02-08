package org.js.swiftcodes.api.mappers;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;

import java.util.Collections;
import java.util.List;

public class BankDataAndEntityMapper {
    private BankDataAndEntityMapper() {

    }

    public static BankDataEntity mapToBankDataEntity(BankData bankData) {
        return mapToBankDataEntity(bankData, null, null);
    }

    public static BankDataEntity mapToBankDataEntity(BankData bankData, Integer id, Integer parentId) {
        return BankDataEntity.builder()
            .id(id)
            .address(bankData.getAddress())
            .name(bankData.getName())
            .codeType(bankData.getCodeType())
            .countryIso2Code(bankData.getCountryISO2Code())
            .countryName(bankData.getCountryName())
            .swiftCode(bankData.getSwiftCode())
            .isHeadquarter(bankData.isHeadquarter())
            .timeZone(bankData.getTimeZone())
            .townName(bankData.getTownName())
            .parentId(parentId)
            .build();
    }

    public static BankData mapToBankData(BankDataEntity bankDataEntity) {
        List<BankDataEntity> branches = Collections.emptyList();
        return mapToBankData(bankDataEntity, branches);
    }

    public static BankData mapToBankData(BankDataEntity bankDataEntity, List<BankDataEntity> branches) {

        BankData bankData = BankData.builder()
            .name(bankDataEntity.getName())
            .countryISO2Code(bankDataEntity.getCountryIso2Code())
            .swiftCode(bankDataEntity.getSwiftCode())
            .isHeadquarter(bankDataEntity.isHeadquarter())
            .townName(bankDataEntity.getTownName())
            .timeZone(bankDataEntity.getTimeZone())
            .address(bankDataEntity.getAddress())
            .countryName(bankDataEntity.getCountryName())
            .codeType(bankDataEntity.getCodeType())
            .build();

        for (BankDataEntity b : branches) {
            BankData branchData = mapToBankData(b);
            bankData.addRelatedBank(branchData);
        }

        return bankData;
    }
}
