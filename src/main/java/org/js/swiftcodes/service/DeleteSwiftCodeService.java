package org.js.swiftcodes.service;

import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.js.swiftcodes.service.util.SwiftCodeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class DeleteSwiftCodeService {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public DeleteSwiftCodeService(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public int deleteSwiftCode(String swiftCode, String bankName, String countryISO2) {
        validateInput(swiftCode, bankName, countryISO2);

        int deletedSwiftCode = bankDataMapper.deleteOne(swiftCode);
        if (deletedSwiftCode == 0) {
            throw new SwiftCodeNotFoundException("Failed delete. Probably bank data was modified in other session. Please repeat operation.");
        }

        return deletedSwiftCode;
    }

    private void validateInput(String swiftCode, String bankName, String countryISO2) {
        BankDataEntity fetchedBankData = bankDataMapper.selectOne(swiftCode);

        SwiftCodeUtil.validateSwiftCode(swiftCode);
        if (Objects.isNull(bankName)) {
            throw new BadRequestException("Provided bank name mustn't be empty.");
        } else if (Objects.isNull(countryISO2)) {
            throw new BadRequestException("Provided country ISO2 mustn't be empty.");
        } else if (fetchedBankData == null) {
            throw new SwiftCodeNotFoundException(String.format("Swift code %s not found in database.", swiftCode));
        } else if (!bankName.equals(fetchedBankData.getName()) || !countryISO2.equals(fetchedBankData.getCountryIso2Code())) {
            throw new BadRequestException(String.format("Parameter %s is not a valid bank name and parameter %s is not a valid country ISO2.",
                fetchedBankData.getName(),
                fetchedBankData.getCountryIso2Code()));
        }
    }
}
