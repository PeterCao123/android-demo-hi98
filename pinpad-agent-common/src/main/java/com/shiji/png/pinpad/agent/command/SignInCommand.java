package com.shiji.png.pinpad.agent.command;

import com.shiji.png.payment.PaymentService;
import com.shiji.png.payment.ServiceManager;
import com.shiji.png.payment.annotation.ServiceType;
import com.shiji.png.payment.message.TxResponse;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

/**
 * @author bruce.wu
 * @since 2018/11/13 11:00
 */
public class SignInCommand extends AbstractCommand {

    @Override
    Observable<TxResponse> callService(TransactionDto dto) {
        return ServiceManager.services(ServiceType.BANK_CARD)
                .observeOn(Schedulers.io())
                .flatMap(serviceInfo -> {
                    PaymentService service = ServiceManager.create(serviceInfo.getName());
                    return service.signIn(buildRequest(dto));
                });
    }
}
