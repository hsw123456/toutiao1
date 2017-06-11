package com.nowcoder.toutiao.async;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by hsw on 2017/6/11.
 * 触发某个事件时的现场数据打包到该类
 */
public class EventModel {

    private EventType type;
    //事件触发者
    private int actorId;

    //触发对象
    private int EntityId;
    private int EntityType;

    //对象拥有者
    private int entityOwner;

    //触发现场（参数等）
    private Map<String, String> exts = new HashMap<>();

    public String getExt(String key) {
        return exts.get(key);
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventModel(EventType type) {
        this.type = type;
    }

    public EventModel() {
    }

    public EventType getType() {
        return type;
    }

    public EventModel setType(EventType type) {
        this.type = type;
        return this;
    }

    public int getActorId() {
        return actorId;
    }

    public EventModel setActorId(int actorId) {
        this.actorId = actorId;
        return this;

    }

    public int getEntityId() {
        return EntityId;
    }

    public EventModel setEntityId(int entityId) {
        EntityId = entityId;
        return this;

    }

    public int getEntityType() {
        return EntityType;
    }

    public EventModel setEntityType(int entityType) {
        EntityType = entityType;
        return this;

    }

    public int getEntityOwner() {
        return entityOwner;
    }

    public EventModel setEntityOwner(int entityOwner) {
        this.entityOwner = entityOwner;
        return this;

    }

    public Map<String, String> getExts() {
        return exts;
    }

    public void setExts(Map<String, String> exts) {
        this.exts = exts;

    }
}
