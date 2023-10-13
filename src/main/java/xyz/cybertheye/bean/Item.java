package xyz.cybertheye.bean;

/**
 * @description: 模拟表字段的公共/隐藏字段
 */
public abstract class Item implements Cloneable{
    protected final int id = getCount() ;

    protected abstract int getCount();

    protected int trxId;


    //clone
    @Override
    public Object clone() throws CloneNotSupportedException {
        return super.clone();
    }


    ////getter ,setter


    public int getId() {
        return id;
    }

    public int getTrxId() {
        return trxId;
    }

    public void setTrxId(int trxId) {
        this.trxId = trxId;
    }
}
