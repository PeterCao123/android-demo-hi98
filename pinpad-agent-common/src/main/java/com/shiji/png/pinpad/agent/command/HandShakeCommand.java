package com.shiji.png.pinpad.agent.command;

import com.shiji.png.payment.util.StringUtils;
import com.shiji.png.pinpad.agent.crypto.Key;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bruce.wu
 * @since 2018/11/13 14:06
 */
public class HandShakeCommand implements Command {

    private static final Logger logger = LoggerFactory.getLogger("HandShakeCommand");

    private final Key key;

    public HandShakeCommand(Key key) {
        this.key = key;
    }

    @Override
    public TransactionDto execute(TransactionDto dto) {
        if (!StringUtils.isEmpty(dto.getEncryptedDek())
                && !StringUtils.isEmpty(dto.getCheckValue())
                && key != null) {
            logger.debug("updating DEK...");
            key.setDTK(dto.getEncryptedDek(), dto.getCheckValue());
            logger.debug("DEK updated");
        }
        return dto;
    }

}
