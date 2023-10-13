package xyz.cybertheye.engine.store;

import xyz.cybertheye.bean.Item;

import java.util.Map;
import java.util.concurrent.locks.Lock;

/**
 * @description:
 */

public interface StorageManager {
    /**
     * 保存数据
     * @param data
     */
    void save(Item data);

    /**
     * 获取注解值为id的数据
     * @param id
     * @return
     */
     Item get(int id);

     @Deprecated
     void modify(int id, String fieldName, Object fieldValue);
    void modify(int trxId,int id, String fieldName, Object fieldValue);

    @Deprecated
     void batchModify(int id, Map<String,Object> params);
    void batchModify(int trxId,int id, Map<String,Object> params);



     Lock getRowLock(int id);
}
