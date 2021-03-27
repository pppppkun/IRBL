package pgd.irbl.business.enums;

/**
 * @Author: pkun
 * @CreateTime: 2021-03-27 09:56
 */
public enum UserRole {
    Admin("Admin"),
    Developer("Developer");

    private String value;
    UserRole(String s){
        value=s;
    }
}
