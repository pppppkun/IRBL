package pgd.irbl.business.enums;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 16:21
 */
public enum QueryRecordState {

    preprocessing("preprocessing"),
    querying("querying"),
    fail("fail"),
    complete("complete");

    String value;
    QueryRecordState(String value){
        this.value = value;
    }
    public String getValue() {
        return value;
    }
}
