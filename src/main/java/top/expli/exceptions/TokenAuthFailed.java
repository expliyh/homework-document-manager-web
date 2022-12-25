package top.expli.exceptions;

public class TokenAuthFailed extends KnifeException{
    public TokenAuthFailed(String msg) {
        super(msg);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
