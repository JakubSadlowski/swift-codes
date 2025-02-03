package org.js.swiftcodes.service.model;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class BankData {
    private final SwiftCode swiftCode;
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
        relatedBanks.add(relatedBank);
    }
}