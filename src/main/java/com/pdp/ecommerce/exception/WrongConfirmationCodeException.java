package com.pdp.ecommerce.exception;

import org.apache.coyote.BadRequestException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;


@ResponseStatus(HttpStatus.BAD_REQUEST)
public class WrongConfirmationCodeException extends BadRequestException {
    public WrongConfirmationCodeException(String message) {
        super(message);
    }

}
