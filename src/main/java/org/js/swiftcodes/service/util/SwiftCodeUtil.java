package org.js.swiftcodes.service.util;

import org.js.swiftcodes.service.exceptions.SwiftCodeInvalidException;

import java.util.Objects;

public class SwiftCodeUtil {
    private static final String SWIFT_CODE_HEADQUARTER_SUFFIX = "XXX";
    private static final int SWIFT_CODE_HEADQUARTER_MAX_LENGTH = 8;
    private static final int SWIFT_CODE_LENGTH = 11;

    private SwiftCodeUtil() {
    }

    public static boolean isHeadquarterSwiftCode(String value) {
        return value.endsWith(SWIFT_CODE_HEADQUARTER_SUFFIX);
    }

    public static String getHeadquarterBIC(String value) {
        if (isNullOrTrimmedLengthLessThanExpected(value)) {
            throw new SwiftCodeInvalidException(String.format("Not valid swift code %s as its length is less than %d",
                value,
                SWIFT_CODE_HEADQUARTER_MAX_LENGTH));
        }
        return value.substring(0, SWIFT_CODE_HEADQUARTER_MAX_LENGTH);
    }

    public static void validateSwiftCode(String swiftCode) {
        if (Objects.isNull(swiftCode) || swiftCode.length() != SWIFT_CODE_LENGTH) {
            throw new SwiftCodeInvalidException(String.format("Provided SWIFT code %s is not valid.", swiftCode));
        }
    }

    private static boolean isNullOrTrimmedLengthLessThanExpected(String value) {
        return value == null || value.trim()
            .length() < SWIFT_CODE_HEADQUARTER_MAX_LENGTH;
    }

    public static boolean isBlank(String str) {
        return str == null || str.trim().isEmpty();
    }
}
