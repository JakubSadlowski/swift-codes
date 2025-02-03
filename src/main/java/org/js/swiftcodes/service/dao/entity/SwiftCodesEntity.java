package org.js.swiftcodes.service.dao.entity;

import lombok.Data;

@Data
public class SwiftCodesEntity {
    private final int id;
    private final String swiftCode;
    private final String countryIso2Code;
    private final boolean isHeadquarter;
    private final String name;
    private final String codeType;
    private final String address;
    private final String townName;
    private final String countryName;
    private final String timeZone;
    private final Integer parentId;
}
