package org.js.swiftcodes.api.validation;

import org.js.swiftcodes.service.exceptions.GeneralException;

public class BadRequestException extends GeneralException {
    public BadRequestException(String message) {
        super(message);
    }
}
