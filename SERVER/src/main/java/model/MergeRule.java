package model;

/**
 * Created by infinitu on 14. 12. 18..
 */
public class MergeRule {
    public String hash;
    public int oid;
    public int idx;

    @Override
    public String toString() {
        return String.format("hash : %s, oid : %d, idx : %d",hash,oid,idx);
    }
}
