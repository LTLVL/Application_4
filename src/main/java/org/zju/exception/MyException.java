package org.zju.exception;

/**
 * 自定义异常
 * @author Tao
 * @date 2023/11/29
 */
public class MyException extends Exception{
    public MyException(String msg) {
        System.out.println(msg);
    }
}
