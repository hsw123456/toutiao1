package com.nowcoder.toutiao.service;

import com.nowcoder.toutiao.dao.MessageDAO;
import com.nowcoder.toutiao.model.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by hsw on 2017/6/8.
 */
@Service
public class MessageService {
    @Autowired
    private MessageDAO messageDAO;

    public int addMessage(Message message){
       return  messageDAO.addMessage(message);
    }

    public List<Message> getConversationDetail(String conversationId,int offset,int limit){
        return messageDAO.getConversationDetail(conversationId,offset,limit);
    }
    public List<Message> getConversationList(int userId,int offset,int limit){
        return messageDAO.getConversationList(userId,offset,limit);
    }

    public int getUnreadCount(int userId ,String conversationId){
        return messageDAO.getUnreadCount(userId,conversationId);
    }
}
