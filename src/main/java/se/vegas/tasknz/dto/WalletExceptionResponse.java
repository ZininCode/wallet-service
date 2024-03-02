package se.vegas.tasknz.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

/**
 * Date: 20.01.2024
 *
 * @author Nikolay Zinin
 */
@Getter
@Setter
@Builder
public class WalletExceptionResponse {
    private int statusCode;
    private Date timestamp;
    private String path;
    private String descriptionMessage;
}
