package top.expli;

public class ClientDocument {
    private String docName;
    private String owner;
    private String permission;
    private int permissionLevel;
    private String description;

    public ClientDocument() {
    }

    public ClientDocument(String docName) {
        this.docName = docName;
    }

    public ClientDocument(String docName, String owner, String permission, String description) {
        this.docName = docName;
        this.owner = owner;
        this.permission = permission;
        this.description = description;
    }

    public String getDocName() {
        return docName;
    }

    public void setDocName(String docName) {
        this.docName = docName;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public int getPermissionLevel() {
        return permissionLevel;
    }

    public void setPermissionLevel(int permissionLevel) {
        this.permissionLevel = permissionLevel;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
