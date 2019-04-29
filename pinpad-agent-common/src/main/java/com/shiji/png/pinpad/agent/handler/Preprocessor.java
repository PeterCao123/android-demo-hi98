package com.shiji.png.pinpad.agent.handler;

import com.shiji.png.pinpad.agent.model.TransactionDto;

/**
 * @author bruce.wu
 * @since 2018/12/6 13:26
 */
public interface Preprocessor {

    /**
     * extend an provide fields: deviceBrand, deviceModel
     * for android example:
     *   dto.setDeviceBrand(Build.MANUFACTURER);
     *   dto.setDeviceModel(Build.PRODUCT);
     */
    void process(TransactionDto dto);

}
