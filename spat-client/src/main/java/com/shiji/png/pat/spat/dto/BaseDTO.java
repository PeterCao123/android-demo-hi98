package com.shiji.png.pat.spat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
@Getter
@ToString
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class BaseDTO {

    private String respCode;

    private String respText;

    public boolean approved() {
        return RespCode.Approve.equals(respCode);
    }

    public String getErrorMessage() {
        return respCode + " - " + respText;
    }

}
