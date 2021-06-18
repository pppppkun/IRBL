package pgd.irbl.business.constant;

/**
 * @Author: pkun
 * @CreateTime: 2021-04-18 12:56
 */
public class RecordConstant {
    public static final int SUCCESS_CHANGE_STATE = 0;
    public static final int RECORD_NON_EXISTS = -1;
    public static final int NOT_MEET_EXPECTATIONS = 1;
    private RecordConstant() {
        throw new IllegalStateException("Utility class");
    }
}
