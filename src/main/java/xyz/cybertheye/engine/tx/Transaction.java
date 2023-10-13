package xyz.cybertheye.engine.tx;

/**
 * @description:
 */

public interface Transaction {
    void begin();
    void commit();
    void rollback();

    void setTxLevel(TxLevel txLevel);
}
