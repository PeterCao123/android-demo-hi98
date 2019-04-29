package com.shiji.png.pat.spat.dto;

import java.math.BigDecimal;
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
public class ChecksDTO extends DataDTO<ChecksDTO.Data> {

    @Getter
    @ToString
    @RequiredArgsConstructor(access = AccessLevel.PACKAGE)
    public static class Data {

        /**
         * PNG merchant id
         */
        private String merId;

        /**
         * PNG revenue center id
         */
        private String rvcId;

        /**
         * the count of checks
         */
        private int count;

        /**
         * the table no.
         */
        private String tableNo;

        private GetConfigurationDTO.Data configuration;

        private List<Check> checks;

        @Getter
        @ToString
        @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
        public static class Check {

            /**
             * PNG merchant id
             */
            private String merId;

            /**
             * PNG revenue center id
             */
            private String rvcId;

            /**
             * POS merchant id
             */
            private String posMerId;

            /**
             * POS revenue center id
             */
            private String posRvcId;

            /**
             * the table no.
             */
            private String tableNo;

            /**
             * the check no.
             */
            private String checkNo;

            /**
             * the seat no.
             */
            private String seatNo;

            /**
             * Check open employee Id
             */
            private String checkOpenEmpId;

            /**
             * Check open time
             */
            private String checkOpenTime;

            /**
             * Check close time
             */
            private String checkCloseTime;

            /**
             * Check status. Opened/Closed/Locked
             */
            private String checkStatus;

            /**
             * Check open workstation Id
             */
            private String checkOpenStationCode;

            /**
             * Number of guests of this check
             */
            private Integer guests;

            /**
             * The check total amount
             */
            private BigDecimal checkAmt;

            /**
             * the service charge total amount
             */
            private BigDecimal svcTotalAmt;

            /**
             * Surcharge amount
             */
            private BigDecimal surchargeAmt;

            /**
             * total amount to pay. totalAmt = checkAmt + svcTotalAmt + surchargeAmt
             */
            private BigDecimal totalAmt;

            /**
             * remaining amount to pay. amt = totalAmt - paidTotalAmt
             */
            private BigDecimal amt;

            /**
             * total paid amount
             */
            private BigDecimal paidTotalAmt;

            private List<Item> items;

            private List<Tax> taxItems;

            private List<Extension> extensions;

            private List<Payment> payments;

            @Getter
            @ToString
            @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Item {

                /**
                 * The item code
                 */
                private String code;

                /**
                 * The item name
                 */
                private String name;

                /**
                 * The item quantity
                 */
                private Double qty;

                /**
                 * The item unit price
                 */
                private BigDecimal unitPrice;

                /**
                 * item total amount
                 */
                private BigDecimal total;

            }

            @Getter
            @ToString
            @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Tax {

                /**
                 * Tax item name
                 */
                private String name;

                /**
                 * Formated tax amount
                 */
                private BigDecimal amount;

            }

            @Getter
            @ToString
            @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Extension {

                /**
                 * Extension item name
                 */
                private String name;

                /**
                 * Extension item value
                 */
                private String value;

            }

            @Getter
            @ToString
            @RequiredArgsConstructor(access = AccessLevel.PRIVATE)
            public static class Payment {

                /**
                 * The check base amount
                 */
                private BigDecimal txnAmt;

                /**
                 * The payment tip amount
                 */
                private BigDecimal tipAmt;

                /**
                 * The payment surcharge amount
                 */
                private BigDecimal surchargeAmt;

                /**
                 * payment total amount
                 */
                private BigDecimal totalAmt;

                /**
                 * the payment code
                 */
                private String code;

                /**
                 * Masked PAN
                 */
                private String maskedPan;

                /**
                 * payment description
                 */
                private String name;

                /**
                 * PNG payment txn no.
                 */
                private String txnNo;

                /**
                 * payment ref no, provided by bank/acquirer
                 */
                private String refNo;

            }

        }

    }

}
