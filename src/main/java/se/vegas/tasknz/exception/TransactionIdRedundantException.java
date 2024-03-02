package se.vegas.tasknz.exception;

import lombok.Getter;

/**
 * Date: 20.01.2024
 *
 * @author Nikolay Zinin
 */
@Getter
public class TransactionIdRedundantException extends WalletException {
    public TransactionIdRedundantException(String exceptionMessage) {
        super(exceptionMessage);
    }
}

