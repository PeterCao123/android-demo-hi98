package com.shiji.png.pinpad.agent.command;

import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.payment.model.RespCode;
import com.shiji.png.payment.model.RespText;
import com.shiji.png.payment.util.AmtUtils;
import com.shiji.png.pinpad.agent.model.SettlementDto;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import java.util.ArrayList;
import java.util.List;

/**
 * @author bruce.wu
 * @since 2018/11/20 13:37
 */
public class SettleCommand implements Command {
    @Override
    public TransactionDto execute(TransactionDto dto) {
        TxRequest request = TxRequest.builder().build();

        dto.setRespCode(RespCode.Approve);
        dto.setRespText(RespText.Approve);
        dto.setAcqRespCode(RespCode.Approve);
        dto.setAcqRespText(RespCode.Approve);

        List<SettlementDto> settlements = new ArrayList<>();
        ServiceManager.services()
                .blockingForEach(serviceInfo -> {
                    PaymentService service = ServiceManager.create(serviceInfo.getName());
                    TxResponse response = service.settle(request)
                            .onErrorReturn(tr ->  TxResponse.builder()
                                    .respCode(RespCode.AppError)
                                    .respText(RespText.Approve)
                                    .build())
                            .blockingFirst();
                    SettlementDto.SettlementDtoBuilder builder = SettlementDto.builder();
                    builder.respCode(response.getRespCode())
                            .respText(response.getRespText());
                    List<TxResponse.Settlement> settles = response.getSettlements();
                    if (settles != null) {
                        for (TxResponse.Settlement settlement: settles) {
                            builder.channel(settlement.getChannel())
                                    .respCode(settlement.getRespCode())
                                    .respText(settlement.getRespText())
                                    .acqMerId(settlement.getAcqMerId())
                                    .acqTerId(settlement.getAcqTerId())
                                    .batchNo(settlement.getBatchNo())
                                    .txnNum(Integer.toString(settlement.getTxnNum()))
                                    .txnAmt(AmtUtils.toString(settlement.getTxnAmt()))
                                    .refundNum(Integer.toString(settlement.getRefundNum()))
                                    .refundAmt(AmtUtils.toString(settlement.getRefundAmt()))
                                    .acqTxnDatetime(settlement.getAcqTxnDatetime());
                            settlements.add(builder.build());
                        }
                    }
                });

        dto.setSettData(settlements);
        dto.setRefNo(Long.toString(System.currentTimeMillis()));

        return dto;
    }
}
