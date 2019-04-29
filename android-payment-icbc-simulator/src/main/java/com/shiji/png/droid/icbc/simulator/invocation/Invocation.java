package com.shiji.png.droid.icbc.simulator.invocation;

import android.os.RemoteException;

/**
 * @author bruce.wu
 * @since 2018/11/22 14:18
 */
public interface Invocation {

    long invoke() throws RemoteException;

}
