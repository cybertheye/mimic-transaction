package xyz.cybertheye.engine.tx;

import java.util.Set;

/**
 * @description:
 */

public interface TransactionManager {
    void setTxLevel(TxLevel txLevel);
    TxLevel getTxLevel();
    int getNextTrxId();

    int getMaxTrxId();

    Set<Integer> getActivatedTrxIds();

    void registerTrx(int trxId);

    void unRegisterTrx(int trxId);

    int getMinTrxId();
}
