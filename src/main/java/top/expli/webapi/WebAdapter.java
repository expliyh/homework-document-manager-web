package top.expli.webapi;

import org.jetbrains.annotations.NotNull;
import top.expli.ClientDocument;
import top.expli.ClientUser;
import top.expli.exceptions.KnifeException;
import top.expli.exceptions.ServerError;
import top.expli.webapi.Request.Operations;
import top.expli.webapi.Request.NameSpace;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

public class WebAdapter {
    public static SocketClient socketClient = null;

    public static ClientUser login(String userName, String password) throws IOException, KnifeException {
        Request request = new Request(NameSpace.SYSTEM, Operations.LOGIN);
        Map<String, String> detail = new HashMap<>();
        detail.put("userName", userName);
        detail.put("password", password);
        request.setDetail(detail);
        Response response = socketClient.sendMessage(request);
        return getClientUserFromResponse(response);
    }

    public static void deleteUser(String userName){

    }
    public static ClientDocument getDocumentInfo(String docName) throws IOException, KnifeException {
        Request request = new Request(NameSpace.DOCUMENTS, Operations.GET);
        Map<String, String> detail = new HashMap<>();
        detail.put("docName", docName);
        request.setDetail(detail);
        Response response = socketClient.sendMessage(request);
        return getClientDocFromResponse(response);
    }

    private static ClientDocument getClientDocFromResponse(Response response) throws KnifeException {
        if (response.getCode() == 200) {
            ClientDocument clientDocument = new ClientDocument(response.getDetail().get("docName"));
            clientDocument.setPermission(response.getDetail().get("permission"));
            clientDocument.setPermissionLevel(Integer.parseInt(response.getDetail().get("permissionLevel")));
            clientDocument.setDescription(response.getDetail().get("description"));
            return clientDocument;
        } else if (response.getException() == null) {
            throw new ServerError();
        } else {
            throw response.getException();
        }
    }

    @NotNull
    private static ClientUser getClientUserFromResponse(Response response) throws IOException, KnifeException {
        if (response.getCode() == 200) {
            ClientUser clientUser = new ClientUser(response.getDetail().get("userName"));
            clientUser.setPermission(response.getDetail().get("permission"));
            clientUser.setPermissionLevel(Integer.parseInt(response.getDetail().get("permissionLevel")));
            return clientUser;
        } else if (response.getException() == null) {
            throw new ServerError();
        } else {
            throw response.getException();
        }
    }

    public static ClientUser getUserInfo(String userName) throws IOException, KnifeException {
        Request request = new Request(NameSpace.USERS, Operations.GET);
        Map<String, String> detail = new HashMap<>();
        detail.put("userName", userName);
        request.setDetail(detail);
        Response response = socketClient.sendMessage(request);
        return getClientUserFromResponse(response);
    }

    public static void editUser(ClientUser newUser) throws IOException, KnifeException {
        Request request = new Request(NameSpace.USERS, Operations.EDIT);
        Map<String, String> detail = new HashMap<>();
        detail.put("userName", newUser.getUserName());
        detail.put("password", newUser.getPassword());
        detail.put("permissionLevel", String.valueOf(newUser.getPermissionLevel()));
        request.setDetail(detail);
        Response response = socketClient.sendMessage(request);
        if (response.getCode() == 200) {
            return;
        } else {
            if (response.getException() == null) {
                throw new ServerError();
            } else {
                throw response.getException();
            }
        }
    }

    public static Vector<Vector<String>> getUserList() throws IOException, KnifeException {
        Request request = new Request(Request.NameSpace.USERS, Request.Operations.LIST);
        Response response = socketClient.sendMessage(request);
        if (response.getCode() == 200) {
            return response.getListDetail();
        } else {
            if (response.getException() == null) {
                throw new ServerError();
            } else {
                throw response.getException();
            }
        }
    }

    public static Vector<Vector<String>> getDocumentList() throws IOException, KnifeException {
        Request request = new Request(NameSpace.DOCUMENTS, Operations.LIST);
        Response response = socketClient.sendMessage(request);
        if (response.getCode() == 200) {
            return response.getListDetail();
        } else {
            if (response.getException() == null) {
                throw new ServerError();
            } else {
                throw response.getException();
            }
        }
    }
}
