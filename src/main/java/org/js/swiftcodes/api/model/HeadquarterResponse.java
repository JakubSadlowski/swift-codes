package org.js.swiftcodes.api.model;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class HeadquarterResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private String swiftCode;
    private List<BranchResponse> branches;
}
