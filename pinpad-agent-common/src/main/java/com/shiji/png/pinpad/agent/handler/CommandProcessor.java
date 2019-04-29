package com.shiji.png.pinpad.agent.handler;

import com.shiji.png.payment.model.RespCode;
import com.shiji.png.payment.model.RespText;
import com.shiji.png.pinpad.agent.command.CommandFactory;
import com.shiji.png.pinpad.agent.crypto.Key;
import com.shiji.png.pinpad.agent.model.TransactionDto;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author bruce.wu
 * @since 2019/1/2 9:17
 */
public class CommandProcessor {

    private static final Logger logger = LoggerFactory.getLogger("CommandProcessor");

    private final Key key;

    private final Preprocessor preprocessor;

    public CommandProcessor(Key key, Preprocessor preprocessor) {
        this.key = key;
        this.preprocessor = preprocessor;
    }

    public TransactionDto process(TransactionDto msg) {
        TransactionDto dto = msg;

        dto.setRespCode(RespCode.Approve);
        dto.setRespText(RespText.Approve);

        if (preprocessor != null) {
            preprocessor.process(dto);
        }

        try {
            dto = CommandFactory.create(msg.getTxnType(), key).execute(msg);
        } catch (Exception e) {
            logger.error("execute command failed", e);
            msg.setRespCode(RespCode.Approve);
            msg.setRespText("success");
        }

        return dto;
    }

}
