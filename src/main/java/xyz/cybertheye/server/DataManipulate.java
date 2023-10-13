package xyz.cybertheye.server;

import xyz.cybertheye.bean.Item;

/**
 * @description:
 */

public interface DataManipulate {
    Item select(int id);
    void insert(Item data);
    void update(int id,String fieldName, Object fieldValue);

}
