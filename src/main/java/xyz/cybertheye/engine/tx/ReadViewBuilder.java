package xyz.cybertheye.engine.tx;

import java.util.Set;

/**
 * @description:
 */
public class ReadViewBuilder {
    /**
     * 高水位
     */
    private Integer mLowLimitId;

    /**
     * 低水位
     */
    private Integer mUpLimitId;

    /*
     * 创建当前视图的 事务id
     */
    private Integer mCreateTrxId;

    /**
     * 在创建视图时候，所有的活跃的事务Id
     */
    private Set<Integer> mIds;

    /**
     *
     */
    private Integer mLowLimitNo;

    public ReadViewBuilder() {
    }

    public ReadViewBuilder setMLowLimitId(int mLowLimitId){
        this.mLowLimitId = mLowLimitId;
        return this;
    }


    public ReadViewBuilder setMUpLimitId(int mUpLimitId){
        this.mUpLimitId = mUpLimitId;
        return this;
    }

    public ReadViewBuilder setCreateTrxId(int trxId){
        this.mCreateTrxId = trxId;
        return this;
    }

    public ReadViewBuilder setMIds(Set<Integer> mIds){
        this.mIds = mIds;
        return this;
    }

    public ReadViewBuilder setMLowLimitNo(int trxNo){
        this.mLowLimitNo = trxNo;
        return this;
    }

    public ReadView build(){
        if(this.mLowLimitNo == null){
            return new ReadView(mLowLimitId,mUpLimitId,mCreateTrxId,mIds);
        }
        return new ReadView(mLowLimitId,mLowLimitId,mCreateTrxId,mIds,mLowLimitNo);
    }
}
