package org.js.swiftcodes.api.mappers;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class BankDataMapper {
    private BankDataMapper() {
    }

    public static BankDataEntity mapToBankDataEntity(BankData bankData, Integer id, Integer parentId) {
        return BankDataEntity.builder()
            .id(id)
            .address(bankData.getAddress())
            .name(bankData.getName())
            .codeType(bankData.getCodeType())
            .countryIso2Code(bankData.getCountryISO2Code())
            .countryName(bankData.getCountryName())
            .swiftCode(bankData.getSwiftCode()
                .code())
            .isHeadquarter(bankData.isHeadquarter())
            .timeZone(bankData.getTimeZone())
            .townName(bankData.getTownName())
            .parentId(parentId)
            .build();
    }

    public static List<BankDataEntity> mapToBankDataEntitiesListWithIds(List<BankData> bankDataList) {
        AtomicInteger id = new AtomicInteger(1);
        List<BankDataEntity> bankDataEntitiesWithIds = new ArrayList<>();

        bankDataList.stream()
            .filter(BankData::isHeadquarter)
            .forEach(headquarterBank -> {
                Integer headquarterEntityId = id.getAndIncrement();
                BankDataEntity headquarterEntity = BankDataMapper.mapToBankDataEntity(headquarterBank, headquarterEntityId, null);
                bankDataEntitiesWithIds.add(headquarterEntity);

                headquarterBank.getRelatedBanks()
                    .forEach(relatedBank -> {
                        Integer relatedBankEntityId = id.getAndIncrement();
                        BankDataEntity relatedEntity = BankDataMapper.mapToBankDataEntity(relatedBank, relatedBankEntityId, headquarterEntityId);
                        bankDataEntitiesWithIds.add(relatedEntity);
                    });
            });

        return bankDataEntitiesWithIds;
    }
}
