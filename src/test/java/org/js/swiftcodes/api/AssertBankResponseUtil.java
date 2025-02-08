package org.js.swiftcodes.api;

import org.js.swiftcodes.api.model.BankData;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

public class AssertBankResponseUtil {
    public static void assertBranchResponse(BankData expectedBranch, ResponseEntity<BankData> response) {
        BankData branchResponse = response.getBody();
        Assertions.assertNotNull(branchResponse);
        Assertions.assertEquals(expectedBranch.getSwiftCode(), branchResponse.getSwiftCode());
        Assertions.assertEquals(expectedBranch.getAddress(), branchResponse.getAddress());
        Assertions.assertEquals(expectedBranch.getCountryName(), branchResponse.getCountryName());
        Assertions.assertEquals(expectedBranch.getBankName(), branchResponse.getBankName());
        Assertions.assertEquals(expectedBranch.getCountryISO2(), branchResponse.getCountryISO2());
        Assertions.assertFalse(branchResponse.isHeadquarter());
    }

    public static void assertHeadquarterResponse(BankData expectedHeadquarter, BankData headquarterResponse) {
        Assertions.assertNotNull(headquarterResponse);
        Assertions.assertEquals(expectedHeadquarter.getSwiftCode(), headquarterResponse.getSwiftCode());
        Assertions.assertEquals(expectedHeadquarter.getAddress(), headquarterResponse.getAddress());
        Assertions.assertEquals(expectedHeadquarter.getCountryName(), headquarterResponse.getCountryName());
        Assertions.assertEquals(expectedHeadquarter.getBankName(), headquarterResponse.getBankName());
        Assertions.assertEquals(expectedHeadquarter.getCountryISO2(), headquarterResponse.getCountryISO2());
        Assertions.assertTrue(headquarterResponse.isHeadquarter());
        Assertions.assertEquals(1,
            headquarterResponse.getBranches()
                .size());
    }
}
