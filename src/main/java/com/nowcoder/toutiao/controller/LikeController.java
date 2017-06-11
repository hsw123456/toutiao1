package com.nowcoder.toutiao.controller;

import com.nowcoder.toutiao.async.EventModel;
import com.nowcoder.toutiao.async.EventProducer;
import com.nowcoder.toutiao.async.EventType;
import com.nowcoder.toutiao.model.EntityType;
import com.nowcoder.toutiao.model.HostHolder;
import com.nowcoder.toutiao.model.News;
import com.nowcoder.toutiao.service.LikeService;
import com.nowcoder.toutiao.service.NewsService;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * Created by hsw on 2017/6/9.
 */
@Controller
public class LikeController {

    @Autowired
    LikeService likeService;

    @Autowired
    HostHolder hostHolder;
    @Autowired
    NewsService newsService;

    @Autowired
    EventProducer producer;

    @RequestMapping(path = {"/like"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String like(@RequestParam("newsId") int newsId) {
        long likeCount = 0;
        try {
            int userId = hostHolder.getUser().getId();
            News news = newsService.getById(newsId);
            likeCount = likeService.like(userId, EntityType.ENTITY_NEWS, newsId);
            //更新某个news 的喜欢数量
            newsService.updateLikeCount(newsId, (int) likeCount);

            producer.fireEvent(new EventModel(EventType.LIKE)
            .setActorId(hostHolder.getUser().getId())
            .setEntityId(newsId).setEntityType(EntityType.ENTITY_NEWS)
            .setEntityOwner(news.getUserId()));

        } catch (Exception e) {
            e.printStackTrace();
        }

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(path = {"/dislike"}, method = {RequestMethod.POST, RequestMethod.GET})
    @ResponseBody
    public String disLike(@RequestParam("newsId") int newsId) {
       int userId = hostHolder.getUser().getId();
        long likeCount = likeService.disLike(userId, EntityType.ENTITY_NEWS, newsId);
        newsService.updateLikeCount(newsId, (int) likeCount);

        return ToutiaoUtil.getJSONString(0, String.valueOf(likeCount));
    }
}
