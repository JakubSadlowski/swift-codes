package org.js.swiftcodes.service;

import org.js.swiftcodes.api.validation.BadRequestException;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeAlreadyExistException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        validateInput(address, bankName, countryISO2, countryName, swiftCode, isHeadquarter);
        validateIfHeadquarterExistForGivenBranch(swiftCode);

        BankDataEntity bankEntity = createBankEntity(address, bankName, countryISO2, countryName, isHeadquarter, swiftCode);

        int insertResult = bankDataMapper.insert(bankEntity);
        if (insertResult > 0) {
            updateParentId(bankEntity);
        }

        return insertResult;
    }

    private void validateIfHeadquarterExistForGivenBranch(String swiftCode) {
        if (!isHeadquarterSwiftCode(swiftCode) && isBlank(bankDataMapper.selectHeadquartersSwiftCode(swiftCode))) {
                throw new BadRequestException("Can't add branch with non-existent headquarter.");
            }
    }

    private BankDataEntity createBankEntity(String address, String bankName, String countryISO2, String countryName, boolean isHeadquarter, String swiftCode) {
        return BankDataEntity.builder()
            .address(address)
            .name(bankName)
            .isHeadquarter(isHeadquarter)
            .swiftCode(swiftCode)
            .countryIso2Code(countryISO2)
            .countryName(countryName)
            .build();
    }

    private void updateParentId(BankDataEntity bankEntity) {
        Integer headquarterId = bankDataMapper.selectHeadquartersId(bankEntity.getSwiftCode());
        if (headquarterId != null) {
            bankDataMapper.updateBranchParentId(bankEntity.getSwiftCode(), headquarterId);
        }
    }

    private void validateInput(String address, String bankName, String countryISO2, String countryName, String swiftCode, boolean isHeadquarter) {
        validateIfInputIsBlank(address, bankName, countryISO2, countryName, swiftCode);
        validateSwiftCode(swiftCode);
        validateIfSwiftCodeExist(swiftCode);
        validateHeadquarterConsistency(swiftCode, isHeadquarter);
    }

    private void validateHeadquarterConsistency(String swiftCode, boolean isHeadquarter) {
        boolean isHeadquarterCode = isHeadquarterSwiftCode(swiftCode);
        if (isHeadquarterCode != isHeadquarter) {
            throw new BadRequestException("Headquarter flag must match SWIFT code format (XXX ending indicates headquarter)");
        }
    }

    private void validateIfSwiftCodeExist(String swiftCode) {
        if (bankDataMapper.selectOne(swiftCode.toUpperCase()) != null) {
            throw new SwiftCodeAlreadyExistException(String.format("Swift code %s already exists", swiftCode));
        }
    }

    private static void validateIfInputIsBlank(String address, String bankName, String countryISO2, String countryName, String swiftCode) {
        Map<String, String> fieldsToValidate = Map.of("address", address, "bank name", bankName, "country ISO2", countryISO2, "country name", countryName, "SWIFT code", swiftCode);

        fieldsToValidate.forEach((fieldName, value) -> {
            if (isBlank(value)) {
                throw new BadRequestException(String.format("Provided %s mustn't be empty.", fieldName));
            }
        });
    }
}