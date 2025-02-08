package org.js.swiftcodes.service;

import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.js.swiftcodes.service.exceptions.SwiftCodeNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DeleteSwiftCodeService {
    private final BankDataMapper bankDataMapper;

    @Autowired
    public DeleteSwiftCodeService(BankDataMapper bankDataMapper) {
        this.bankDataMapper = bankDataMapper;
    }

    public int deleteSwiftCode(String swiftCode) {
        int deletedSwiftCode = bankDataMapper.deleteOne(swiftCode);
        if (deletedSwiftCode == 0) {
            throw new SwiftCodeNotFoundException(String.format("Swift code %s not found.", swiftCode));
        }

        return deletedSwiftCode;
    }
}
