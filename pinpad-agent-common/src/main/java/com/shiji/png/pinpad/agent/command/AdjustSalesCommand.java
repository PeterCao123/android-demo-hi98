package com.shiji.png.pinpad.agent.command;

import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.util.AmtUtils;
import com.shiji.png.pinpad.agent.model.TransactionDto;
import io.reactivex.Observable;

/**
 * @author bruce.wu
 * @since 2018/11/20 11:52
 */
public class AdjustSalesCommand extends AbstractCommand {
    @Override
    TxRequest buildRequest(TransactionDto dto) {
        return TxRequest.builder()
                .merRef(dto.getMerRef())
                .orgTraceNo(dto.getOrgTraceNo())
                .txnAmt(dto.getTxnAmtAsDouble())
                .tipAmt(dto.getTipAmtAsDouble())
                .orgRefNo(dto.getOrgRefNo())
                .build();
    }

    @Override
    Observable<TxResponse> callService(TransactionDto dto) {
        return ServiceManager.services(getServiceTypeByIssuerCode(dto.getIssuerCode()))
                .flatMap(serviceInfo -> {
                    PaymentService service = ServiceManager.create(serviceInfo.getName());
                    return service.adjustSales(buildRequest(dto));
                });
    }

    @Override
    void fillApprovedDto(TransactionDto dto, TxResponse response) {
        dto.setTipAmt(AmtUtils.toString(response.getTipAmt()));
        dto.setTxnAmt(null);
        dto.setTotalAmt(null);
        dto.setAcqTxnDatetime(response.getAcqTxnTimestamp());
        dto.setEntryMode(response.getEntryMode().getSpaValue());
        dto.setIssuerCode(response.getIssuerCode().getCode());
        dto.setRefNo(response.getRefNo());
        //dto.setBatchNo(response.getBatchNo());
        dto.setAuthCode(response.getAuthCode());
        dto.setTraceNo(dto.getOrgTraceNo());
    }
}
