package com.hj.bobo.common;

/**
 * 封装一个工具类,用于处理在线程中获取当前用户id
 */
public class BaseContext {
    private static ThreadLocal<Long> threadLocal = new ThreadLocal<>();

    public static void setCurrentId(Long id){
        threadLocal.set(id);
    }
     public static Long getCurrentId(){
        return threadLocal.get();

     }


}
