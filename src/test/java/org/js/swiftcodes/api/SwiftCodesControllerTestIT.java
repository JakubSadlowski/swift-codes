package org.js.swiftcodes.api;

import org.js.swiftcodes.api.model.BranchResponse;
import org.js.swiftcodes.api.model.HeadquarterResponse;
import org.js.swiftcodes.service.TestBankData;
import org.js.swiftcodes.service.dao.entity.BankDataEntity;
import org.js.swiftcodes.service.dao.mapper.BankDataMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.js.swiftcodes.api.AssertBankResponseUtil.assertBranchResponse;
import static org.js.swiftcodes.api.AssertBankResponseUtil.assertHeadquarterResponse;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ExtendWith(MockitoExtension.class)
class SwiftCodesControllerTestIT {
    @MockBean
    private BankDataMapper bankDataMapper;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    void shouldReturnBranch_whenCorrectBranchSwiftCodeIsProvided() {
        // given
        String branchSwiftCode = "BCHICLRMEXP";
        Mockito.when(bankDataMapper.selectOne(branchSwiftCode))
            .thenReturn(TestBankData.EXPECTED_BANK_DATA_ENTITY2);

        BankDataEntity expectedBranch = TestBankData.EXPECTED_BANK_DATA_ENTITY2;

        // when
        ResponseEntity<BranchResponse> response = restTemplate.getForEntity("/v1/swift-codes/BCHICLRMEXP", BranchResponse.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        assertBranchResponse(expectedBranch, response);

        // Verify the mapper was called
        verify(bankDataMapper, times(1)).selectOne(branchSwiftCode);
    }

    @Test
    void shouldReturnHeadquarterWithBranches_whenCorrectHeadquarterSwiftCodeIsProvided() {
        // given
        String branchSwiftCode = "BCHICLRMXXX";
        Mockito.when(bankDataMapper.selectOne(branchSwiftCode))
            .thenReturn(TestBankData.EXPECTED_BANK_DATA_ENTITY1);

        Mockito.when(bankDataMapper.selectAllBranches(TestBankData.EXPECTED_BANK_DATA_ENTITY1.getId()))
            .thenReturn(List.of(TestBankData.EXPECTED_BANK_DATA_ENTITY2));

        BankDataEntity expectedHeadquarter = TestBankData.EXPECTED_BANK_DATA_ENTITY1;
        BankDataEntity expectedBranch = TestBankData.EXPECTED_BANK_DATA_ENTITY2;

        // when
        ResponseEntity<HeadquarterResponse> response = restTemplate.getForEntity("/v1/swift-codes/BCHICLRMXXX", HeadquarterResponse.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        HeadquarterResponse headquarterResponse = response.getBody();
        assertHeadquarterResponse(expectedHeadquarter, headquarterResponse);

        List<BranchResponse> branchResponses = headquarterResponse.getBranches();
        assertBranchResponsesList(expectedBranch, branchResponses);

        // Verify the mapper was called
        verify(bankDataMapper, times(1)).selectOne(branchSwiftCode);
    }

    private static void assertBranchResponsesList(BankDataEntity expectedBranch, List<BranchResponse> branchResponses) {
        Assertions.assertNotNull(branchResponses);
        Assertions.assertEquals(expectedBranch.getSwiftCode(),
            branchResponses.get(0)
                .getSwiftCode());
        Assertions.assertEquals(expectedBranch.getAddress(),
            branchResponses.get(0)
                .getAddress());
        Assertions.assertEquals(expectedBranch.getCountryName(),
            branchResponses.get(0)
                .getCountryName());
        Assertions.assertEquals(expectedBranch.getName(),
            branchResponses.get(0)
                .getBankName());
        Assertions.assertEquals(expectedBranch.getCountryIso2Code(),
            branchResponses.get(0)
                .getCountryISO2());
        Assertions.assertFalse(branchResponses.get(0)
            .isHeadquarter());
    }

}