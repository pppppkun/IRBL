package pgd.irbl.business.enums;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-27 10:00
 */
public enum RepoState {

    Dev("Dev"),
    Abandon("Abandon");
    private String value;
    RepoState(String s){
        value=s;
    }
}
