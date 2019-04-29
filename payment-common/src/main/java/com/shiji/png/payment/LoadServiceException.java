package com.shiji.png.payment;

/**
 * indicate find channel failed
 *
 * @author bruce.wu
 * @date 2018/8/8
 */
public class LoadServiceException extends RuntimeException {

    public LoadServiceException(String message) {
        super(message);
    }

    LoadServiceException(String message, Throwable cause) {
        super(message, cause);
    }

}
