package org.js.swiftcodes.service.model;

import lombok.Data;

@Data
public class BankData {
    private int id;
    private String name;
    private CountryISO2Code countryISO2Code;
    private String address;
    private String townName;
    private String countryName;
    private String swiftCode;
    private String codeType;
    private String timeZone;
}
