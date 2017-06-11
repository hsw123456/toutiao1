package com.nowcoder.toutiao.async;

import java.util.List;

/**
 * Created by hsw on 2017/6/11.
 */
public interface EventHandler {
    void doHandler(EventModel model);
    List<EventType> getSupportEventTypes();
}
