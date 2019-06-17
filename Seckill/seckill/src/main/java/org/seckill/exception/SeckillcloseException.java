package org.seckill.exception;

/**
 * 秒杀关闭异常
 */
public class SeckillcloseException extends SeckillException {
    public SeckillcloseException(String message) {
        super(message);
    }

    public SeckillcloseException(String message, Throwable cause) {
        super(message, cause);
    }
}
