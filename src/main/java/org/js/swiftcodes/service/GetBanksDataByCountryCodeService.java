package org.js.swiftcodes.service;

import org.js.swiftcodes.api.mappers.BankDataAndEntityMapper;
import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.CountryISO2CodeNotFoundException;
import org.js.swiftcodes.service.util.SwiftCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class GetBanksDataByCountryCodeService {
    private static final int COUNTRY_CODE_LENGTH = 2;
    private final BankDataMapper bankDataMapper;

    @Autowired
    public GetBanksDataByCountryCodeService(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public List<BankData> getBanksData(String countryCodeISO2) {
        if (!isValidCountryCode(countryCodeISO2)) {
            throw new BadRequestException(String.format("Provided country code %s is not valid", countryCodeISO2));
        }

        List<BankDataEntity> banksDataEntities = bankDataMapper.selectBankDataByCountryISO2Code(countryCodeISO2.toUpperCase());

        if (banksDataEntities == null || banksDataEntities.isEmpty()) {
            throw new CountryISO2CodeNotFoundException(String.format("Provided country code %s not found in database.", countryCodeISO2));
        }
        return convertToBanksData(banksDataEntities);
    }

    private static List<BankData> convertToBanksData(List<BankDataEntity> bankDataEntities) {
        List<BankData> banksResult = new ArrayList<>(bankDataEntities.size());
        BankData currentHeadquarter = null;
        for (BankDataEntity bankDataEntity : bankDataEntities) {
            BankData bankData = BankDataAndEntityMapper.mapToBankData(bankDataEntity);
            if (bankDataEntity.isHeadquarter()) {
                banksResult.add(bankData);
                currentHeadquarter = bankData;
            } else if (currentHeadquarter != null) {
                String headquarterBIC = SwiftCodeUtil.getHeadquarterBIC(currentHeadquarter.getSwiftCode());
                String branchHeadquarterBIC = SwiftCodeUtil.getHeadquarterBIC(bankData.getSwiftCode());
                if (headquarterBIC.equals(branchHeadquarterBIC)) {
                    currentHeadquarter.addBranch(bankData);
                } else {
                    banksResult.add(bankData);
                }
            } else {
                banksResult.add(bankData);
            }
        }
        return banksResult;
    }

    private boolean isValidCountryCode(String countryCodeISO2) {
        return countryCodeISO2 != null && countryCodeISO2.trim()
            .length() == COUNTRY_CODE_LENGTH;
    }
}
