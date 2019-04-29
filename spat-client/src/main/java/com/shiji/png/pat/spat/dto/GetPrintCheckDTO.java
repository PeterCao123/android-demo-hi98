package com.shiji.png.pat.spat.dto;

import java.util.List;

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
public class GetPrintCheckDTO extends DataDTO<GetPrintCheckDTO.Data> {

    @Getter
    @ToString(callSuper = true)
    @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
    public static class Data extends ChecksDTO.Data {

        private RvcInfo rvcInfo;

        private List<String> printLines;

        /**
         * ormated check receipt, base64 encoded
         */
        private String receiptContent;

        @Getter
        @ToString
        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class RvcInfo {

            /**
             * Merchant Id, assigned by POS
             */
            private String posMerId;

            /**
             * Merchant name, assigned by POS
             */
            private String posMerName;

            /**
             * RVC id, assigned by POS
             */
            private String posRvcId;

            /**
             * RVC name, assigned by POS
             */
            private String posRvcName;

            /**
             * Merchant Id, assigned by PNG
             */
            private String merId;

            /**
             * RVC id, assigned by PNG
             */
            private String rvcId;

            /**
             * RVC address1
             */
            private String address1;

            /**
             * RVC address2
             */
            private String address2;

            /**
             * RVC info
             */
            private String info;

            /**
             * RVC phone
             */
            private String phone;

            /**
             * RVC fax
             */
            private String fax;

            /**
             * RVC website
             */
            private String website;

        }

    }

}
