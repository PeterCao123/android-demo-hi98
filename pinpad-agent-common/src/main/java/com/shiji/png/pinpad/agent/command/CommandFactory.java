package com.shiji.png.pinpad.agent.command;

import com.shiji.png.common.enums.TransactionType;
import com.shiji.png.pinpad.agent.crypto.Key;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author bruce.wu
 * @since 2018/12/6 9:45
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class CommandFactory {

    public static Command create(String txnType, Key key) {
        TransactionType type = TransactionType.valueOfEnum(txnType);
        switch (type) {
            case HANDSHAKE:
                return new HandShakeCommand(key);
            case SALE:
                return new SaleCommand(key);
            case TIP_ADJUSTMENT:
                return new AdjustTipsCommand();
            case SALE_ADJUSTMENT:
                return new AdjustSalesCommand();
            case VOID_SALE:
                return new VoidCommand(key);
            case AUTHORIZATION:
                return new AuthCommand(key);
            case TOP_UP_AUTHORIZATION:
                return new TopUpAuthCommand(key);
            case VOID_AUTHORIZATION:
                return new VoidAuthCommand(key);
            case REFUND:
                return new RefundCommand(key);
            case CAPTURE:
                return new AuthCompletionCommand(key);
            case VOID_CAPTURE:
                return new VoidAuthCompletionCommand(key);
            case EOD:
                return new SettleCommand();
            case LOGIN:
                return new SignInCommand();
            case PRINT_LAST_RECEIPT:
                return new PrintLastCommand();
            default:
                throw new RuntimeException("Unsupported txn type: " + txnType);
        }
    }

}
