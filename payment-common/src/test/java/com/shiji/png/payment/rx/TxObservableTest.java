package com.shiji.png.payment.rx;

import com.shiji.png.payment.message.TxResponse;

import org.junit.Test;

import io.reactivex.Emitter;

import static org.junit.Assert.*;

/**
 * @author bruce.wu
 * @since 2018/12/5 17:00
 */
public class TxObservableTest {

    @Test
    public void tx_complete() {
        final long id = 1234;
        TxObservable.create(new IdentifiedSource<Long, TxResponse>() {
            @Override
            public Long id() {
                return id;
            }

            @Override
            public void subscribe(Emitter<TxResponse> emitter) throws Exception {
                new Thread(() -> {
                    emitter.onNext(TxResponse.builder().build());
                    emitter.onComplete();
                }).start();
            }
        })
                .doOnSubscribe(d -> assertEquals(1, TxObservable.getRegistry().size()))
                .blockingSubscribe();
        assertEquals(0, TxObservable.getRegistry().size());
    }

    @Test
    public void tx_error() {
        final long id = 4321;
        TxObservable.create(new IdentifiedSource<Long, TxResponse>() {
            @Override
            public Long id() {
                return id;
            }

            @Override
            public void subscribe(Emitter<TxResponse> emitter) throws Exception {
                new Thread(() -> {
                    emitter.onError(new Exception("error"));
                }).start();
            }
        })
                .doOnSubscribe(d -> assertEquals(1, TxObservable.getRegistry().size()))
                .blockingSubscribe(new EmptyObserver<>());
        assertEquals(0, TxObservable.getRegistry().size());
    }

}