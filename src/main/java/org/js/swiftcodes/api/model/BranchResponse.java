package org.js.swiftcodes.api.model;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class BranchResponse {
    private String address;
    private String bankName;
    private String countryISO2;
    private String countryName;
    private boolean isHeadquarter;
    private String swiftCode;
}
