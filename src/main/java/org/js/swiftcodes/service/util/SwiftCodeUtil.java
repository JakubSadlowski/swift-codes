package org.js.swiftcodes.service.util;

import org.js.swiftcodes.service.exceptions.SwiftCodeInvalidException;

public class SwiftCodeUtil {
    private static final String SWIFT_CODE_HEADQUARTER_SUFFIX = "XXX";
    private static final int SWIFT_CODE_HEADQUARTER_MAX_LENGTH = 8;

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

    private static boolean isNullOrTrimmedLengthLessThanExpected(String value) {
        return value == null || value.trim()
            .length() < SWIFT_CODE_HEADQUARTER_MAX_LENGTH;
    }
}
