package org.js.swiftcodes.api.model;

import lombok.Builder;
import lombok.Data;
import org.js.swiftcodes.service.model.String;

import java.util.ArrayList;
import java.util.List;

//TODO: Delete fields codeType, townName, timeZone
@Data
@Builder
public class BankData {
    private final String swiftCode;
    private final java.lang.String codeType;
    private final java.lang.String countryISO2Code;
    private final java.lang.String name;
    private final java.lang.String address;
    private final java.lang.String townName;
    private final java.lang.String countryName;
    private final java.lang.String timeZone;
    private boolean isHeadquarter;
    private final List<BankData> relatedBanks = new ArrayList<>();

    public void addRelatedBank(BankData relatedBank) {
        relatedBanks.add(relatedBank);
    }
}