package xyz.cybertheye.engine.tx;

import java.util.Set;

/**
 * @description:
 */
public class ReadView {

    /**
     * 高水位
     */
    private int mLowLimitId;

    /**
     * 低水位
     */
    private int mUpLimitId;

    /*
     * 创建当前视图的 事务id
     */
    private int mCreateTrxId;

    /**
     * 在创建视图时候，所有的活跃的事务Id
     */
    private Set<Integer> mIds;

    /**
     *
     */
    private int mLowLimitNo;


    public ReadView(int mLowLimitId, int mUpLimitId, int mCreateTrxId, Set<Integer> mIds) {
        this.mLowLimitId = mLowLimitId;
        this.mUpLimitId = mUpLimitId;
        this.mCreateTrxId = mCreateTrxId;
        this.mIds = mIds;
        this.mLowLimitNo = -1;
    }


    public ReadView(int mLowLimitId, int mUpLimitId, int mCreateTrxId, Set<Integer> mIds, int mLowLimitNo) {
        this(mLowLimitId,mUpLimitId,mCreateTrxId,mIds);
        this.mLowLimitNo = mLowLimitNo;
    }

    ////// visibility

    /**
     * 看一下这个版本是不是对这个事务可见
     * @param trxId undolog每一个版本中记录的trxId
     * @return
     */
    public boolean getVisibility(int trxId){
        if(trxId < mUpLimitId || trxId == mCreateTrxId){
            return true;
        }

        if(trxId >= mLowLimitId){
            return false;
        }

        return !mIds.contains(trxId);
    }

}
