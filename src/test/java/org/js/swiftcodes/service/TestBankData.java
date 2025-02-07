package org.js.swiftcodes.service;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;

public class TestBankData {
    private TestBankData() {
    }

    public static final BankData EXPECTED_BANK_DATA1 = BankData.builder()
        .swiftCode("BCHICLRMXXX")
        .codeType("BIC11")
        .name("BANCO DE CHILE")
        .address("")
        .countryISO2Code("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .isHeadquarter(true)
        .build();

    public static final BankData EXPECTED_BANK_DATA2 = BankData.builder()
        .swiftCode("BCHICLRMEXP")
        .codeType("BIC11")
        .name("BANCO DE CHILE")
        .address("")
        .countryISO2Code("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .build();

    public static final BankData EXPECTED_BANK_DATA3 = BankData.builder()
        .swiftCode("AIPOPLP1XXX")
        .codeType("BIC11")
        .name("SANTANDER CONSUMER BANK SPOLKA AKCYJNA")
        .countryISO2Code("PL")
        .address("STRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611")
        .countryName("POLAND")
        .townName("WROCLAW")
        .timeZone("Europe/Warsaw")
        .isHeadquarter(true)
        .build();

    public static final BankData EXPECTED_BANK_DATA4 = BankData.builder()
        .swiftCode("BIGBPLPWXXX")
        .codeType("BIC11")
        .name("BANK MILLENNIUM S.A.")
        .countryISO2Code("PL")
        .address("HARMONY CENTER UL. STANISLAWA ZARYNA 2A WARSZAWA, MAZOWIECKIE, 02-593")
        .countryName("POLAND")
        .townName("WARSZAWA")
        .timeZone("Europe/Warsaw")
        .isHeadquarter(true)
        .build();

    public static final BankData EXPECTED_BANK_DATA5 = BankData.builder()
        .swiftCode("BIGBPLPWCUS")
        .codeType("BIC11")
        .name("BANK MILLENNIUM S.A.")
        .countryISO2Code("PL")
        .address("HARMONY CENTER UL. STANISLAWA ZARYNA 2A WARSZAWA, MAZOWIECKIE, 02-593")
        .countryName("POLAND")
        .townName("WARSZAWA")
        .timeZone("Europe/Warsaw")
        .build();

    public static final BankDataEntity EXPECTED_BANK_DATA_ENTITY1 = BankDataEntity.builder()
        .swiftCode("BCHICLRMXXX")
        .codeType("BIC11")
        .name("BANCO DE CHILE")
        .address("")
        .countryIso2Code("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .isHeadquarter(true)
        .build();

    public static final BankDataEntity EXPECTED_BANK_DATA_ENTITY2 = BankDataEntity.builder()
        .swiftCode("BCHICLRMEXP")
        .codeType("BIC11")
        .name("BANCO DE CHILE")
        .address("")
        .countryIso2Code("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .parentId(EXPECTED_BANK_DATA_ENTITY1.getId())
        .build();

    static {
        EXPECTED_BANK_DATA1.addRelatedBank(EXPECTED_BANK_DATA2);
        EXPECTED_BANK_DATA4.addRelatedBank(EXPECTED_BANK_DATA5);
    }
}
