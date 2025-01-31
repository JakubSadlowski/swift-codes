package org.js.swiftcodes.service.model;

import lombok.Data;

import java.util.List;

@Data
public class BankData {
    private String swiftCode;
    private String codeType;
    private String countryISO2Code;
    private String name;
    private String address;
    private String townName;
    private String countryName;
    private String timeZone;
    private boolean isHeadquarter;
    private List<BankData> relatedBanks;
}
