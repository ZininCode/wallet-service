package se.vegas.tasknz.exception;

import lombok.Getter;
import lombok.Setter;

/**
 * Date: 20.01.2024
 *
 * @author Nikolay Zinin
 */
@Getter
@Setter
public class WalletNotFoundException extends WalletException{
    public WalletNotFoundException(String exceptionMessage) {
     super(exceptionMessage);
    }
}

