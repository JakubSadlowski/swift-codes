package org.js.swiftcodes.api;

import org.js.swiftcodes.api.model.BranchResponse;
import org.js.swiftcodes.api.model.HeadquarterResponse;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.junit.jupiter.api.Assertions;
import org.springframework.http.ResponseEntity;

public class AssertBankResponseUtil {
    public static void assertBranchResponse(BankDataEntity expectedBranch, ResponseEntity<BranchResponse> response) {
        BranchResponse branchResponse = response.getBody();
        Assertions.assertNotNull(branchResponse);
        Assertions.assertEquals(expectedBranch.getSwiftCode(), branchResponse.getSwiftCode());
        Assertions.assertEquals(expectedBranch.getAddress(), branchResponse.getAddress());
        Assertions.assertEquals(expectedBranch.getCountryName(), branchResponse.getCountryName());
        Assertions.assertEquals(expectedBranch.getName(), branchResponse.getBankName());
        Assertions.assertEquals(expectedBranch.getCountryIso2Code(), branchResponse.getCountryISO2());
        Assertions.assertFalse(branchResponse.isHeadquarter());
    }

    public static void assertHeadquarterResponse(BankDataEntity expectedHeadquarter, HeadquarterResponse headquarterResponse) {
        Assertions.assertNotNull(headquarterResponse);
        Assertions.assertEquals(expectedHeadquarter.getSwiftCode(), headquarterResponse.getSwiftCode());
        Assertions.assertEquals(expectedHeadquarter.getAddress(), headquarterResponse.getAddress());
        Assertions.assertEquals(expectedHeadquarter.getCountryName(), headquarterResponse.getCountryName());
        Assertions.assertEquals(expectedHeadquarter.getName(), headquarterResponse.getBankName());
        Assertions.assertEquals(expectedHeadquarter.getCountryIso2Code(), headquarterResponse.getCountryISO2());
        Assertions.assertTrue(headquarterResponse.isHeadquarter());
        Assertions.assertEquals(1,
            headquarterResponse.getBranches()
                .size());
    }
}
