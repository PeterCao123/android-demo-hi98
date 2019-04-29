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
@ToString(callSuper = true)
@RequiredArgsConstructor(access = AccessLevel.PACKAGE)
public class DataDTO<T> extends BaseDTO {

    private T data;

}
