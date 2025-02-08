package org.js.swiftcodes.service;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;

public class TestBankData {
    private TestBankData() {
    }

    public static final BankData EXPECTED_BANK_DATA1 = BankData.builder()
        .swiftCode("BCHICLRMXXX")
        .codeType("BIC11")
        .bankName("BANCO DE CHILE")
        .address("")
        .countryISO2("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .isHeadquarter(true)
        .build();

    public static final BankData EXPECTED_BANK_DATA2 = BankData.builder()
        .swiftCode("BCHICLRMEXP")
        .codeType("BIC11")
        .bankName("BANCO DE CHILE")
        .address("")
        .countryISO2("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .build();

    public static final BankData EXPECTED_BANK_DATA3 = BankData.builder()
        .swiftCode("AIPOPLP1XXX")
        .codeType("BIC11")
        .bankName("SANTANDER CONSUMER BANK SPOLKA AKCYJNA")
        .countryISO2("PL")
        .address("STRZEGOMSKA 42C  WROCLAW, DOLNOSLASKIE, 53-611")
        .countryName("POLAND")
        .townName("WROCLAW")
        .timeZone("Europe/Warsaw")
        .isHeadquarter(true)
        .build();

    public static final BankData EXPECTED_BANK_DATA4 = BankData.builder()
        .swiftCode("BIGBPLPWXXX")
        .codeType("BIC11")
        .bankName("BANK MILLENNIUM S.A.")
        .countryISO2("PL")
        .address("HARMONY CENTER UL. STANISLAWA ZARYNA 2A WARSZAWA, MAZOWIECKIE, 02-593")
        .countryName("POLAND")
        .townName("WARSZAWA")
        .timeZone("Europe/Warsaw")
        .isHeadquarter(true)
        .build();

    public static final BankData EXPECTED_BANK_DATA5 = BankData.builder()
        .swiftCode("BIGBPLPWCUS")
        .codeType("BIC11")
        .bankName("BANK MILLENNIUM S.A.")
        .countryISO2("PL")
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

    public static final BankDataEntity EXPECTED_BANK_DATA_ENTITY3 = BankDataEntity.builder()
        .swiftCode("BCECCLRRXXX")
        .codeType("BIC11")
        .name("BANCO CENTRAL DE CHILE")
        .address("")
        .countryIso2Code("CL")
        .countryName("CHILE")
        .townName("SANTIAGO")
        .timeZone("Pacific/Easter")
        .parentId(null)
        .isHeadquarter(true)
        .build();

    static {
        EXPECTED_BANK_DATA1.addBranch(EXPECTED_BANK_DATA2);
        EXPECTED_BANK_DATA4.addBranch(EXPECTED_BANK_DATA5);
    }
}
