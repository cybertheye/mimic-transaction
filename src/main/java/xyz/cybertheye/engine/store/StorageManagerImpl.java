package xyz.cybertheye.engine.store;

import xyz.cybertheye.bean.Item;

import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @description: 这里的Storage --》 buffer pool
 */
public class StorageManagerImpl implements StorageManager{
    /**
     * id -> data
     */
    private Map<Integer,Item> dataMap = new HashMap<>();
    /**
     * 行锁
     */
    private Map<Integer, Lock> rowLock = new HashMap<>();




    @Override
    public void save(Item data) {
        int id = data.getId();
        dataMap.put(id,data);
    }

    @Override
    public  Item get(int id) {
        return dataMap.get(id);
    }

    @Override
    public void modify(int id, String fieldName, Object fieldValue) {
        Item item = dataMap.get(id);
        try {
            Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(item,fieldValue);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void modify(int trxId, int id, String fieldName, Object fieldValue) {
        Item item = dataMap.get(id);
        item.setTrxId(trxId);
        try {
            Field field = item.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            field.set(item,fieldValue);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void batchModify(int id, Map<String, Object> params) {
        params.forEach((key,value)->{
            modify(id,key,value);
        });
    }

    @Override
    public void batchModify(int trxId, int id, Map<String, Object> params) {
        params.forEach((key,value)->{
            modify(trxId,id,key,value);
        });
    }

    @Override
    public Lock getRowLock(int id) {

        return rowLock.computeIfAbsent(id,x->new ReentrantLock());
    }
}
