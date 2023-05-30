package fr.guen.dev.sgm.common.exceptions;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.lang.Nullable;

/**
 * Exception API.
 */
@Getter
@Setter
public class ApiError extends Exception {

    /**
     * Statut de l'erreur.
     */
    private HttpStatus status;


    /**
     * Description de l'erreur.
     */
    private String errordescription;

    public ApiError(HttpStatus status,  String errordescription) {
        this(status, errordescription, null);
    }

    /**
     * Constructeur.
     *
     * @param status le status de l'erreur
     * @param errordescription la description de l'erreur
     * @param cause la cause de l'erreur
     */
    public ApiError(HttpStatus status, String errordescription, @Nullable Throwable cause) {
        super(errordescription, cause, false, false);
        this.status = status;
        this.errordescription = errordescription;
    }


}
