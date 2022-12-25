package top.expli.exceptions;

import java.time.Instant;

public class TokenExpired extends TokenAuthFailed {
    public TokenExpired(String msg) {
        super(msg);
    }
}
