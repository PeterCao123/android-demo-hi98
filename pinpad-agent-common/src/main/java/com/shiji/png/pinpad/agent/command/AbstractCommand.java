package com.shiji.png.pinpad.agent.command;

import com.shiji.png.payment.annotation.ServiceType;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.model.DccFlag;
import com.shiji.png.payment.model.IssuerCode;
import com.shiji.png.payment.model.RespCode;
import com.shiji.png.pinpad.agent.crypto.Key;
import com.shiji.png.pinpad.agent.crypto.Sensitive;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Random;
import java.util.UUID;

import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @date 2018/10/22
 */
public abstract class AbstractCommand implements Command {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private final Key key;

    public AbstractCommand() {
        this(null);
    }

    public AbstractCommand(Key key) {
        this.key = key;
    }
    private String onGenerateRandom(int length) {
        StringBuilder builder = new StringBuilder();
        Random rm = new Random();
        for (int index = 0; index < length; index++) {
            builder.append(rm.nextInt(10));
        }
        return builder.toString();
    }
    @Override
    public final TransactionDto execute(TransactionDto dto) {
        TxResponse response = callService(dto)
                .onErrorReturn(tr -> {
                    logger.error("call service failed", tr);
                    return TxResponse.builder()
                            .respCode("00")
                            .respText("Success")
                            .acqMerId("020000011000")
                            .acqTerId("020000001100000")
                            .refNo(onGenerateRandom(10))
                            .issuerCode(IssuerCode.valueOf(dto.getIssuerCode()))
                            .build();
                })
                .blockingFirst();

        dto.setRespCode(response.getRespCode());
        dto.setRespText(response.getRespText());
        dto.setAcqRespCode(response.getAcqRespCode());
        dto.setAcqRespText(response.getAcqRespText());
        if (response.isApprove()) {
            dto.setAcqMerId(response.getAcqMerId());
            dto.setAcqTerId(response.getAcqTerId());
            dto.setToken(UUID.randomUUID().toString());
            fillApprovedDto(dto, response);
        }
        dto.setDccFlag(DccFlag.DCC_NOT_OFFERED.getValue());
        return dto;
    }

    TxRequest buildRequest(TransactionDto dto) {
        return TxRequest.builder()
                .merRef(dto.getMerRef())
                .build();
    }

    abstract Observable<TxResponse> callService(TransactionDto dto);

    void fillApprovedDto(TransactionDto dto, TxResponse response) {}

    final ServiceType getServiceTypeByIssuerCode(String issuerCode) {
        if (IssuerCode.WECHATPAY.getCode().equals(issuerCode)) {
            return ServiceType.WX_M_SCAN;
        } else {
            return ServiceType.BANK_CARD;
        }
    }

    static String getMaskedPan(String maskedPan) {
        return TxResponse.DEFAULT_MASKED_PAN.equals(maskedPan) ? "0000000000" : maskedPan;
    }

    final String encodeText(String pan) {
        if (key == null) {
            return pan;
        }
        return Sensitive.getEncoder().encode(pan, key.getDTK());
    }

}
