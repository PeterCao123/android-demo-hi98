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
 * @since 2018/11/20 14:42
 */
public class VoidAuthCommand extends AbstractCommand {
    public VoidAuthCommand(Key key) {
        super(key);
    }

    @Override
    TxRequest buildRequest(TransactionDto dto) {
        return TxRequest.builder()
                .txnAmt(dto.getTxnAmtAsDouble())
                .orgAuthCode(dto.getAuthCode())
                .orgTxnDatetime(dto.getOrgTxnDatetime())
                .orgTraceNo(dto.getOrgTraceNo())
                .build();
    }

    @Override
    Observable<TxResponse> callService(TransactionDto dto) {
        return ServiceManager.services(getServiceTypeByIssuerCode(dto.getIssuerCode()))
                .flatMap(serviceInfo -> {
                    PaymentService service = ServiceManager.create(serviceInfo.getName());
                    return service.voidAuth(buildRequest(dto));
                });
    }

    @Override
    void fillApprovedDto(TransactionDto dto, TxResponse response) {
        dto.setTotalAmt(AmtUtils.toString(response.getTotalAmt()));
        //dto.setPan(encodeText(response.getPan()));
        dto.setExpDate(encodeText(response.getExpDate()));
        dto.setMaskedPan(getMaskedPan(response.getMaskedPan()));
        dto.setCardholderName(response.getCardHolderName());
        dto.setCardType(response.getCardType());
        dto.setEntryMode(response.getEntryMode().getSpaValue());
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
