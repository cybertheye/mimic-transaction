package xyz.cybertheye.engine;

import xyz.cybertheye.bean.Item;
import xyz.cybertheye.engine.log.Undolog;
import xyz.cybertheye.engine.store.StorageManager;
import xyz.cybertheye.engine.store.StorageManagerImpl;
import xyz.cybertheye.engine.tx.*;
import xyz.cybertheye.utils.BeanUtils;

import java.util.*;
import java.util.concurrent.locks.Lock;

/**
 * @description:
 */
public class Engine {
//    private Map<Class<?>,Undolog> undologMap;
    private Undolog undolog;
    private StorageManager storageManager;
    private TransactionManager transactionManager;

    private MVCC mvcc;
    public Engine() {
//        this.undologMap = new HashMap<>();
        this.undolog = new Undolog();
        this.storageManager = new StorageManagerImpl();
        this.transactionManager = new TransactionManagerImpl();
        this.mvcc = new MVCC();
    }

    private static int TX_TURN_ON = 1;
    private static int TX_NOT_TURN_ON = 0;

    //    private int trxState = TX_NOT_TURN_ON;
    private ThreadLocal<Integer> trxState = ThreadLocal.withInitial(()-> TX_NOT_TURN_ON);
    private ThreadLocal<Integer> trxId = new ThreadLocal<>();
    private ThreadLocal<List<Lock>> locks = ThreadLocal.withInitial(()->new ArrayList<>());

    private ThreadLocal<Set<Integer>> updateIds = ThreadLocal.withInitial(()->new HashSet<>());


    public void doUpdate(int id, String fieldName, Object fieldValue){
        Integer state = trxState.get();
        Integer trxId;
        if(state == TX_NOT_TURN_ON){
            trxId = this.getMaxTrxId();
            Lock lock = this.getStorageManager().getRowLock(id);
            try{
                lock.lock();
                this.getStorageManager().modify(trxId,id,fieldName,fieldValue);
            }finally {
                lock.unlock();
            }
        }else{ //在一个显示事务里面
            trxId = this.trxId.get();
            // 如果是第一次修改，
            // 把当前 "buffer pool" 放到 undo 版本链里面
            if(!this.ifMatchTrxId(trxId,id)) {
                this.pushVersionToUndolog(id);
            }
            // 如果不是，修改buffer pool里面的数据就行了，

            Lock lock = this.getStorageManager().getRowLock(id);
            lock.lock();
            locks.get().add(lock);
            this.getStorageManager().modify(trxId,id,fieldName,fieldValue);
            updateIds.get().add(id);
        }
    }

    ////// MVCC
    public Item doSelect(int id){
        Integer state = trxState.get();
        if(state == TX_NOT_TURN_ON){
            return this.getStorageManager().get(id);
        }
        ReadView readView = null;
        Integer trxId = this.trxId.get();
        if(this.getTransactionManager().getTxLevel() == TxLevel.RC){
            readView = this.mvcc.createReadView(trxId,this.transactionManager);
        }else{
            readView = this.mvcc.getReadView(trxId);
            if(readView == null){
                readView = this.mvcc.createReadView(trxId,this.transactionManager);
                this.mvcc.putReadView(trxId,readView);
            }
        }

        return getItemByReadView(id,readView);

    }

    private Item getItemByReadView(int id,ReadView readView) {
        //要去storage里面看一下最新的数据是不是属于这个事务的
        Item item = this.storageManager.get(id);
        int trxId = item.getTrxId();
        if(readView.getVisibility(trxId)){
            return item;
        }
        //// 要去版本链里面找到最近的一个可见的版本
        return this.getUndolog().getItemByReadView(id,readView);
    }


    public int getMaxTrxId(){
        return this.transactionManager.getNextTrxId();
    }

    public boolean ifMatchTrxId(int trxId,int id){
        return this.getStorageManager().get(id).getTrxId() == trxId;
    }

    public void pushVersionToUndolog(int id) {
        Item item = this.storageManager.get(id);
        Item copyItem = null;
        try {
            copyItem = (Item) item.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }

        this.undolog.addFirst(copyItem.getTrxId(), copyItem);
    }



    public void setTxLevel(TxLevel txLevel){
        this.getTransactionManager().setTxLevel(txLevel);
    }

    ///getter ,setter


//    public Map<Class<?>, Undolog> getUndologMap() {
//        return undologMap;
//    }


    public Undolog getUndolog() {
        return undolog;
    }

    public StorageManager getStorageManager() {
        return storageManager;
    }

    public TransactionManager getTransactionManager() {
        return transactionManager;
    }


    public void startTx() {
        trxState.set(TX_TURN_ON);
        trxId.set(this.getMaxTrxId());
        Integer tx= trxId.get();
        this.getTransactionManager().registerTrx(tx);
    }

    public void finishTx() {
        trxState.set(TX_NOT_TURN_ON);
        removeSelfTx();
        releaseLock();
    }

    private void removeSelfTx() {
        Integer tx = trxId.get();

        this.getTransactionManager().unRegisterTrx(tx);
    }

    private void releaseLock() {
        for (Lock lock : locks.get()) {
            lock.unlock();
        }
    }

    public void recover() {
        Set<Integer> itemIds = updateIds.get();

        for (Integer itemId : itemIds) {
            Item lastVersion = undolog.pollFirst(itemId);
            Item curData= this.storageManager.get(itemId);

            try {
                BeanUtils.copyProperties(lastVersion,curData);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
