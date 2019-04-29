package com.shiji.png.pat.spat.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
@Builder
@Getter
@ToString
public class UserValidationModel {

    private final String operator;

    private final String password;

}
