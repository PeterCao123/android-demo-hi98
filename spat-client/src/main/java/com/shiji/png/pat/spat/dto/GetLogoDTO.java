package com.shiji.png.pat.spat.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

/**
 * @author bruce.wu
 * @date 2018/9/3
 */
@ToString(callSuper = true)
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public class GetLogoDTO extends DataDTO<GetLogoDTO.Data> {

    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Data {

        /**
         * Content of logo, base64 encoded
         */
        private String data;

        /**
         * Logo file type. (JPG/JPEG/PNG)
         */
        private String fileType;

        /**
         * Logo md5 value
         */
        private String md5;

    }

}
