package org.js.swiftcodes.service.dao.entity;

import lombok.Builder;
import lombok.Data;
import org.js.swiftcodes.service.util.SwiftCodeUtil;

@Data
@Builder
public class BankDataEntity {
    private final Integer id;
    private final String swiftCode;
    private final String countryIso2Code;
    private final boolean isHeadquarter;
    private final String name;
    private final String codeType;
    private final String address;
    private final String townName;
    private final String countryName;
    private final String timeZone;
    private Integer parentId;

    public String getHeadquarterBaseSwiftCode() {
        return SwiftCodeUtil.getHeadquarterBIC(swiftCode);
    }

}
