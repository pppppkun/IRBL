package pgd.irbl.business.enums;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-05 16:21
 */
public enum QueryRecordState {

    create("initializing"),
    querying("querying"),
    complete("complete");

    String value;
    QueryRecordState(String value){
        this.value = value;
    }
}
