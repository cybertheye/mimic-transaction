package xyz.cybertheye.server;

import xyz.cybertheye.bean.Item;
import xyz.cybertheye.engine.Engine;
import xyz.cybertheye.engine.store.StorageManager;
import xyz.cybertheye.engine.tx.Transaction;
import xyz.cybertheye.engine.tx.TxLevel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @description:
 */
public class Executor implements DataManipulate, Transaction {

    private Engine engine;

    public Executor(Engine engine) {
        this.engine = engine;
    }




    @Override
    public Item select(int id) {
        Item item = this.engine.doSelect(id);
        System.out.println(item);
        return item;
    }

    @Override
    public void insert(Item data) {
        this.engine.getStorageManager().save(data);
    }

    @Override
    public void update(int id, String fieldName, Object fieldValue) {
        this.engine.doUpdate(id,fieldName,fieldValue);
    }

    @Override
    public void begin() {
        this.engine.startTx();
    }

    @Override
    public void commit() {
        // 设置trx_no，
        this.engine.finishTx();
    }

    @Override
    public void rollback() {
        this.engine.recover();
        this.engine.finishTx();
    }

    @Override
    public void setTxLevel(TxLevel txLevel) {
        this.engine.setTxLevel(txLevel);
    }
}
