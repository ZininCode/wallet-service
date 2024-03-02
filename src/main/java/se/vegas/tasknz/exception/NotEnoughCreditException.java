package se.vegas.tasknz.exception;

import lombok.Getter;

/**
 * Date: 20.01.2024
 *
 * @author Nikolay Zinin
 */
@Getter
public class NotEnoughCreditException extends WalletException {
    public NotEnoughCreditException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

