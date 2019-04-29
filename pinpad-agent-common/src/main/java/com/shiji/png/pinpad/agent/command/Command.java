package com.shiji.png.pinpad.agent.command;

import com.shiji.png.pinpad.agent.model.TransactionDto;

/**
 * @author bruce.wu
 * @since 2018/10/22
 */
public interface Command {

    TransactionDto execute(TransactionDto dto);

}
