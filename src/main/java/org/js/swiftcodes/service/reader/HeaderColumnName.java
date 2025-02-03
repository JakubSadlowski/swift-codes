package org.js.swiftcodes.service.reader;

import lombok.Getter;
import org.js.swiftcodes.service.exceptions.GeneralException;

import java.util.HashMap;
import java.util.Map;

@Getter
public enum HeaderColumnName {
    COUNTRY_ISO2CODE("COUNTRY ISO2 CODE"), SWIFT_CODE("SWIFT CODE"), CODE_TYPE("CODE TYPE"), NAME("NAME"), ADDRESS("ADDRESS"), TOWN_NAME("TOWN NAME"), COUNTRY_NAME("COUNTRY NAME"), TIME_ZONE(
        "TIME ZONE");

    private final String header;
    private static final Map<String, HeaderColumnName> names = new HashMap<>();

    static {
        for (HeaderColumnName header : HeaderColumnName.values()) {
            names.put(header.header.toUpperCase(), header);
        }
    }

    public static HeaderColumnName getByHeader(String header) {
        HeaderColumnName readHeader = names.get(header.toUpperCase());
        if (readHeader == null) {
            throw new GeneralException(String.format("Unexpected header %s", header), null);
        }

        return readHeader;
    }

    HeaderColumnName(String header) {
        this.header = header;
    }

    @Override
    public String toString() {
        return "HeaderColumnName{" + "header='" + header + '\'' + '}';
    }
}