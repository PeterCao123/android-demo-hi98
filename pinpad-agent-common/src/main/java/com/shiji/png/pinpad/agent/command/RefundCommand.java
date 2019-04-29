package com.shiji.png.pinpad.agent.command;

import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.util.AmtUtils;
import com.shiji.png.pinpad.agent.crypto.Key;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/11/26 10:16
 */
public class RefundCommand extends AbstractCommand {
    public RefundCommand(Key key) {
        super(key);
    }

    @Override
    TxRequest buildRequest(TransactionDto dto) {
        return TxRequest.builder()
                .merRef(dto.getMerRef())
                .txnAmt(dto.getTxnAmtAsDouble())
                .orgRefNo(dto.getOrgRefNo())
                .orgTraceNo(dto.getOrgTraceNo())
                .orgAcqTerId(dto.getOrgAcqTerId())
                .orgTxnDatetime(dto.getOrgTxnDatetime())
                .build();
    }

    @Override
    Observable<TxResponse> callService(TransactionDto dto) {
        return ServiceManager.services(getServiceTypeByIssuerCode(dto.getIssuerCode()))
                .flatMap(serviceInfo -> {
                    PaymentService service = ServiceManager.create(serviceInfo.getName());
                    return service.refund(buildRequest(dto));
                });
    }

    @Override
    void fillApprovedDto(TransactionDto dto, TxResponse response) {
        dto.setTxnAmt(AmtUtils.toString(response.getTxnAmt()));
        dto.setTipAmt(AmtUtils.toString(response.getTipAmt()));
        //dto.setPan(encodeText(response.getPan()));
        dto.setExpDate(encodeText(response.getExpDate()));
        dto.setMaskedPan(getMaskedPan(response.getMaskedPan()));
        dto.setCardholderName(response.getCardHolderName());
        dto.setCardType(response.getCardType());
        dto.setEntryMode(response.getEntryMode().getSpaValue());
        dto.setAuthCode(response.getAuthCode());
        dto.setIssuerCode(response.getIssuerCode().getCode());
        dto.setAcqTxnDatetime(response.getAcqTxnTimestamp());
        dto.setTxnCurrCode(response.getTxnCurrCode());
        dto.setTxnCurrText(response.getTxnCurrText());
        dto.setMerRef(response.getMerRef());
        dto.setCashierId(response.getCashierId());
        dto.setTraceNo(response.getTraceNo());
        dto.setRefNo(response.getRefNo());
        //dto.setBatchNo(response.getBatchNo());
        dto.setDccFlag("0");
        dto.setDccRate(response.getDccRate());
        dto.setMarkup(response.getDccMarkup());
        dto.setDccCurrText(response.getDccCurrText());
        dto.setDccAmt(response.getDccAmt());
    }
}
