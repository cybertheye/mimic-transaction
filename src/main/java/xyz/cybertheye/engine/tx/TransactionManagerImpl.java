package xyz.cybertheye.engine.tx;

import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

/**
 * @description: 事务管理器的角色
 */
public class TransactionManagerImpl implements TransactionManager{
    //////自增事务ID
    private volatile static int maxTrxId = 1;

    private Object lock = new Object();
    /////活跃事务组
    private Set<Integer> activatedTrxIds = new HashSet<>();


    // 事务的隔离级别
    private static final TxLevel DEFAULT_TX_LEVEL = TxLevel.RR;
    private TxLevel txLevel = DEFAULT_TX_LEVEL;
    @Override
    public void setTxLevel(TxLevel txLevel) {
        this.txLevel = txLevel;
    }

    @Override
    public TxLevel getTxLevel() {
        return txLevel;
    }
    //////////////

    @Override
    public int getNextTrxId() {
        synchronized (this) {
            synchronized (lock) {
                return maxTrxId++;
            }
        }
    }

    @Override
    public int getMaxTrxId() {
        synchronized (this){
            synchronized (lock){
                return maxTrxId;
            }
        }
    }

    @Override
    public Set<Integer> getActivatedTrxIds() {
        synchronized (this){
            synchronized (activatedTrxIds){
                return activatedTrxIds;
            }
        }
    }

    @Override
    public void registerTrx(int trxId) {
        synchronized (this){
            synchronized (activatedTrxIds){
                activatedTrxIds.add(trxId);
            }
        }
    }

    @Override
    public void unRegisterTrx(int trxId) {
        synchronized (this){
            synchronized (activatedTrxIds){
                activatedTrxIds.remove(trxId);
            }
        }
    }

    @Override
    public int getMinTrxId() {
        synchronized (this){
            synchronized (activatedTrxIds){
                return activatedTrxIds.stream().min(new Comparator<Integer>() {
                    @Override
                    public int compare(Integer o1, Integer o2) {
                        return o1-o2;
                    }
                }).get();
            }
        }
    }
}
