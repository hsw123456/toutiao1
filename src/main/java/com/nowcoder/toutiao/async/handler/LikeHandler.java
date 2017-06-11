package com.nowcoder.toutiao.async.handler;

import com.nowcoder.toutiao.async.EventHandler;
import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.Message;
import com.nowcoder.toutiao.model.User;
import com.nowcoder.toutiao.service.MessageService;
import com.nowcoder.toutiao.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * Created by hsw on 2017/6/11.
 */
@Component
public class LikeHandler implements EventHandler{
    @Autowired
    UserService userService;
    @Autowired
    MessageService messageService;

    @Autowired
    HostHolder hostHolder;
    @Override
    public void doHandler(EventModel model) {


        //System.out.println("Liked");
        Message msg = new Message();
        msg.setFromId(3);
        msg.setToId(model.getActorId());
        User user = userService.getUser(model.getActorId());
        msg.setCreatedDate(new Date());
        msg.setContent("用户" + user.getName() +"赞了你的资讯，http://127.0.0.1:8080/news/" + model.getEntityId());
        messageService.addMessage(msg);

    }

    @Override
    public List<EventType> getSupportEventTypes() {
        return Arrays.asList(EventType.LIKE);
    }
}
