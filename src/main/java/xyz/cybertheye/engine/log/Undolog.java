package xyz.cybertheye.engine.log;

import xyz.cybertheye.bean.Item;
import xyz.cybertheye.engine.tx.ReadView;

import java.util.HashMap;
import java.util.Map;

/**
 * @description:
 *
 * Person
 * Map
 *           head -> node1
 * id1 -> person : ver1 - ver2 - ver3
 * id2 -> person
 *
 */
public class Undolog<T extends Item> {

    /**
     *  id -> historyNode
     */
    private Map<Integer,HistoryNode> versionMap = new HashMap<>();


    public Item getItemByReadView(int id, ReadView readView){
        HistoryNode head = versionMap.get(id);
        HistoryNode travel = head.next;

        while(travel!=null){
            if(readView.getVisibility(travel.getTrxId())){
                return travel.getData();
            }
            travel = travel.next;
        }

        return null;
    }


    public void addFirst(int trxId,T data){
        int id = data.getId();
        HistoryNode head = versionMap.computeIfAbsent(id, x -> new HistoryNode());
        HistoryNode node = new HistoryNode(trxId, data);

        node.next = head.next;
        head.next = node;
    }

    public T pollFirst(int id){
        HistoryNode head = versionMap.get(id);
        HistoryNode lastVersion = head.next;
        head.next = lastVersion.next;
        return lastVersion.data;
    }



    public class HistoryNode{
        private int trxId;
        T data;
        HistoryNode next;

        public HistoryNode() {
        }

        public HistoryNode(int trxId, T data) {
            this.trxId = trxId;
            this.data = data;
        }

        public int getTrxId() {
            return trxId;
        }

        public T getData() {
            return data;
        }

        public HistoryNode getNext() {
            return next;
        }

        public void setNext(HistoryNode next) {
            this.next = next;
        }
    }



}
