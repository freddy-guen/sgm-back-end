package fr.guen.dev.sgm.common.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@AllArgsConstructor
public class SgmException extends Exception {
    private final String message;
    private final HttpStatus httpStatus;
}
