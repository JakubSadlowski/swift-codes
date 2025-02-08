package org.js.swiftcodes.config;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.dao.BanksDataStore;
import org.js.swiftcodes.service.reader.SwiftCodesFileReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Component
public class PopulateDatabaseOnStartup {

    public static final String SWIFT_CODES_XLSX = "src/main/resources/2025_swift_codes.xlsx";
    private final BanksDataStore banksDataStore;

    @Autowired
    public PopulateDatabaseOnStartup(BanksDataStore banksDataStore) {
        this.banksDataStore = banksDataStore;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onApplicationReady() {
        if (banksDataStore.isBanksDataEmpty()) {
            List<BankData> banksData = readBankDataFromFile();
            banksDataStore.insertList(banksData);
        }
    }

    private static List<BankData> readBankDataFromFile() {
        SwiftCodesFileReader fileReader = SwiftCodesFileReader.newInstance();
        Map<String, BankData> banksData = fileReader.readSwiftCodesFile(SWIFT_CODES_XLSX);
        return new ArrayList<>(banksData.values());
    }

}
