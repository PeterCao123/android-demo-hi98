package com.shiji.png.droid.icbc.simulator.model;

import android.os.Bundle;

import lombok.Getter;
import lombok.Setter;

/**
 * @author bruce.wu
 * @since 2018/11/22 15:12
 */
@Getter
@Setter
public class TransResponse {

    private long sn;

    private Bundle baseResult;

    private Bundle transResult;

    private Bundle extraInfo;

}
