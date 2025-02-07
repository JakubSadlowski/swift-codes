package org.js.swiftcodes.api.model;

import lombok.Builder;
import lombok.Data;
import org.js.swiftcodes.service.exceptions.GeneralException;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BankData {
    private final String swiftCode;
    private final String codeType;
    private final String countryISO2Code;
    private final String name;
    private final String address;
    private final String townName;
    private final String countryName;
    private final String timeZone;
    private boolean isHeadquarter;
    private final List<BankData> relatedBanks = new ArrayList<>();

    public void addRelatedBank(BankData relatedBank) {
        if (!isHeadquarter) {
            throw new GeneralException("Adding branch is only allowed for headquarter banks.");
        } else if (!relatedBank.getSwiftCode()
            .startsWith(getSwiftCodeWithoutXXX())) {
            throw new GeneralException(String.format("Branch SWIFT code %s doesn't match the given headquarter bank SWIFT code %s.", relatedBank.getSwiftCode(), this.getSwiftCode()));
        }

        relatedBanks.add(relatedBank);
    }

    private String getSwiftCodeWithoutXXX() {
        return swiftCode.substring(0, 7);
    }
}