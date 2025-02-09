package org.js.swiftcodes.api;

import org.js.swiftcodes.api.model.BankData;
import org.js.swiftcodes.service.TestBankData;
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
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
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
    void shouldReturnBadRequest_whenIncorrectSwiftCodeIsProvided() {
        // given
        String swiftCode = "wrong";
        Mockito.when(bankDataMapper.selectOne(swiftCode))
            .thenReturn(null);

        // when
        ResponseEntity<String> response = restTemplate.exchange("/v1/swift-codes/wrong", HttpMethod.GET, null, String.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBadRequest_whenEmptySwiftCodeIsProvided() {
        // given
        String swiftCode = "";
        Mockito.when(bankDataMapper.selectOne(swiftCode))
            .thenReturn(null);

        // when
        ResponseEntity<String> response = restTemplate.exchange("/v1/swift-codes/''", HttpMethod.GET, null, String.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnBranch_whenCorrectBranchSwiftCodeIsProvided() {
        // given
        String branchSwiftCode = "BCHICLRMEXP";
        Mockito.when(bankDataMapper.selectOne(branchSwiftCode))
            .thenReturn(TestBankData.EXPECTED_BANK_DATA_ENTITY2);

        BankData expectedBranch = TestBankData.EXPECTED_BANK_DATA2;

        // when
        ResponseEntity<BankData> response = restTemplate.getForEntity("/v1/swift-codes/BCHICLRMEXP", BankData.class);

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

        BankData expectedHeadquarter = TestBankData.EXPECTED_BANK_DATA1;
        BankData expectedBranch = TestBankData.EXPECTED_BANK_DATA2;

        // when
        ResponseEntity<BankData> response = restTemplate.getForEntity("/v1/swift-codes/BCHICLRMXXX", BankData.class);

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        BankData headquarterResponse = response.getBody();
        assertHeadquarterResponse(expectedHeadquarter, headquarterResponse);

        List<BankData> branchResponses = headquarterResponse.getBranches();
        assertBranchResponsesList(expectedBranch, branchResponses);

        // Verify the mapper was called
        verify(bankDataMapper, times(1)).selectOne(branchSwiftCode);
    }

    @Test
    void shouldReturnListOfBanksWithGivenCountryISO2Code_whenCorrectCountryISO2CodeIsProvided() {
        // given
        String countryISO2 = "CL";

        Mockito.when(bankDataMapper.selectBankDataByCountryISO2Code(countryISO2))
            .thenReturn(List.of(TestBankData.EXPECTED_BANK_DATA_ENTITY1, TestBankData.EXPECTED_BANK_DATA_ENTITY2, TestBankData.EXPECTED_BANK_DATA_ENTITY3));

        // when
        ResponseEntity<List<BankData>> response = restTemplate.exchange("/v1/swift-codes/country/CL", HttpMethod.GET, null, new ParameterizedTypeReference<List<BankData>>() {
        });

        // then
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        List<BankData> listOfHeadquarters = response.getBody();
        Assertions.assertNotNull(listOfHeadquarters);
        Assertions.assertEquals(2, listOfHeadquarters.size());
        List<BankData> listOfBranches = listOfHeadquarters.get(0)
            .getBranches();
        Assertions.assertNotNull(listOfBranches);
        Assertions.assertEquals(1, listOfBranches.size());

        // Verify the mapper was called
        verify(bankDataMapper, times(1)).selectBankDataByCountryISO2Code(countryISO2);
    }

    @Test
    void shouldReturnBadRequest_whenIncorrectCountryISO2CodeIsProvided() {
        // given
        String countryISO2 = "HJK";

        Mockito.when(bankDataMapper.selectBankDataByCountryISO2Code(countryISO2))
            .thenReturn(List.of(TestBankData.EXPECTED_BANK_DATA_ENTITY1));

        // when
        ResponseEntity<String> response = restTemplate.exchange("/v1/swift-codes/country/HJK", HttpMethod.GET, null, String.class);

        // then
        Assertions.assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void shouldReturnNotFound_whenNonExistingInDatabaseCountryISO2CodeIsProvided() {
        // given
        String countryISO2 = "HJ";

        Mockito.when(bankDataMapper.selectBankDataByCountryISO2Code(countryISO2))
            .thenReturn(List.of());

        // when
        ResponseEntity<String> response = restTemplate.exchange("/v1/swift-codes/country/HJ", HttpMethod.GET, null, String.class);

        // then
        Assertions.assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }



    private static void assertBranchResponsesList(BankData expectedBranch, List<BankData> branchResponses) {
        Assertions.assertNotNull(branchResponses);
        BankData branch = branchResponses.get(0);
        Assertions.assertEquals(expectedBranch.getSwiftCode(), branch.getSwiftCode());
        Assertions.assertEquals(expectedBranch.getAddress(), branch.getAddress());
        Assertions.assertEquals(expectedBranch.getCountryName(), branch.getCountryName());
        Assertions.assertEquals(expectedBranch.getBankName(), branch.getBankName());
        Assertions.assertEquals(expectedBranch.getCountryISO2(), branch.getCountryISO2());
        Assertions.assertFalse(branch.isHeadquarter());
    }

}