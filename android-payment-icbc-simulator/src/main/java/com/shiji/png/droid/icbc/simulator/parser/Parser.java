package com.shiji.png.droid.icbc.simulator.parser;

import com.shiji.png.droid.icbc.simulator.model.TransResponseData;
import com.shiji.png.payment.message.TxRequest;
import com.shiji.png.payment.message.TxResponse;

/**
 * @author bruce.wu
 * @since 2018/11/22 15:32
 */
public interface Parser {

    TxResponse parse(TxRequest request, TransResponseData data);

}
