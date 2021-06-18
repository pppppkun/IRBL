package pgd.irbl.business.constant;


/**
 * @Author: pkun
 * @CreateTime: 2021-04-01 19:25
 */
public class ManageConstant {
    public static final String REPO_EXISTS = "仓库已经注册";
    public static final String REPO_NO_EXISTS = "仓库未注册";
    public static final String REGISTER_SUCCESS = "注册成功";
    public static final String REGISTER_FAIL = "注册失败";
    public static final String MODIFY_SUCCESS = "更新成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String QUERY_FAIL = "查询失败";
    public static final String QUERY_NULL_FAIL = "查询失败";
    public static final String PREPROCESS_NULL_FAIL = "预处理失败";
    private ManageConstant() {
        throw new IllegalStateException("Utility class");
    }
}
