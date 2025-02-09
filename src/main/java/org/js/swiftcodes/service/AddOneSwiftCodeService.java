package org.js.swiftcodes.service;

import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import static org.js.swiftcodes.service.util.SwiftCodeUtil.isBlank;
import static org.js.swiftcodes.service.util.SwiftCodeUtil.isHeadquarterSwiftCode;
import static org.js.swiftcodes.service.util.SwiftCodeUtil.validateSwiftCode;

@Service
public class AddOneSwiftCodeService {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public AddOneSwiftCodeService(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public int addSwiftCode(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        validateIfInputIsBlank(address, bankName, countryISO2, countryName, swiftCode);
        validateSwiftCode(swiftCode);
        validateIfSwiftCodeExist(swiftCode);
        insertIntoDatabase(address, bankName, countryISO2, countryName, isHeadquarter, swiftCode);


        return 0;
    }

    private void insertIntoDatabase(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        if (isHeadquarterSwiftCode(swiftCode) && isHeadquarter) {
            BankDataEntity headquarter = BankDataEntity.builder()
                .address(address)
                .name(bankName)
                .isHeadquarter(true)
                .swiftCode(swiftCode)
                .countryIso2Code(countryISO2)
                .countryName(countryName)
                .build();

            bankDataMapper.insert(headquarter);
        } else if (!isHeadquarterSwiftCode(swiftCode) && !isHeadquarter) {
            BankDataEntity branch = BankDataEntity.builder()
                .address(address)
                .name(bankName)
                .isHeadquarter(false)
                .swiftCode(swiftCode)
                .countryIso2Code(countryISO2)
                .countryName(countryName)
                .build();

            bankDataMapper.insert(branch);
        }
    }

    private void validateIfSwiftCodeExist(String swiftCode) {
        if (bankDataMapper.selectOne(swiftCode) != null) {
            throw new SwiftCodeAlreadyExistException(String.format("Swift code %s already exists", swiftCode));
        }
    }

    private static void validateIfInputIsBlank(String address, String bankName, String countryISO2, String countryName, String swiftCode) {
        if (isBlank(address)) {
            throw new BadRequestException("Provided address mustn't be empty.");
        } else if (isBlank(bankName)) {
            throw new BadRequestException("Provided bank name mustn't be empty.");
        } else if (isBlank(countryISO2)) {
            throw new BadRequestException("Provided country iso2 mustn't be empty.");
        } else if (isBlank(countryName)) {
            throw new BadRequestException("Provided countryName mustn't be empty.");
        } else if (isBlank(swiftCode)) {
            throw new BadRequestException("Provided swiftCode mustn't be empty.");
        }
    }
}
