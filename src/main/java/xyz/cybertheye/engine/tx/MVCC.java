package xyz.cybertheye.engine.tx;

import java.util.*;

/**
 * @description:
 *
 * RC - create readview for every select
 *
 * RR - create readview just once at first select
 */
public class MVCC {
    /**
     * trxId -> readview
     */
    private Map<Integer,ReadView> readViewMap = new HashMap<>();


    public void putReadView(int trxId,ReadView readView){
        readViewMap.put(trxId,readView);
    }

    public ReadView getReadView(int trxId){
        return readViewMap.get(trxId);
    }

    public ReadView createReadView(int trxId,TransactionManager transactionManager){
        ReadViewBuilder readViewBuilder = new ReadViewBuilder();
        ReadView readView = null;

        synchronized (transactionManager) {
            readView = readViewBuilder
                    .setMLowLimitId(transactionManager.getMaxTrxId())
                    .setMUpLimitId(transactionManager.getMinTrxId())
                    .setCreateTrxId(trxId)
                    .setMIds(new HashSet<>(transactionManager.getActivatedTrxIds()))
                    .build();
        }

       return  readView;
    }
}
