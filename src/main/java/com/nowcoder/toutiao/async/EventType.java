package com.nowcoder.toutiao.async;

/**
 * Created by hsw on 2017/6/11.
 * 发生的事件抽象出来的类
 */
public enum EventType {
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;
     EventType(int value){
        this.value = value;
    }

    public int getValue(){
         return value;
    }
}
