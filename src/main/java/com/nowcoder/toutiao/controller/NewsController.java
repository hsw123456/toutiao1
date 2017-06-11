package com.nowcoder.toutiao.controller;

import com.nowcoder.toutiao.model.*;
import com.nowcoder.toutiao.service.*;
import com.nowcoder.toutiao.util.ToutiaoUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.handler.UserRoleAuthorizationInterceptor;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by hsw on 2017/5/26.
 */
@Controller
public class NewsController {

    private static final Logger logger = LoggerFactory.getLogger(LoginController.class);
    @Autowired
    private NewsService newsService;

    @Autowired
    private QiNiuService qiNiuService;
    @Autowired
    private HostHolder hostHolder;
    @Autowired
    private UserService userService;
    @Autowired
    private CommentService commentService;
    @Autowired
    private LikeService likeService;


    /**
     * 资讯詳情页
     *
     * @param newsId
     * @param model
     * @return
     */
    @RequestMapping(path = {"/news/{newsId}"}, method = {RequestMethod.GET})
    public String newsDetail(@PathVariable("newsId") int newsId, Model model) {
        News news = newsService.getById(newsId);


        if (news != null) {
            List<Comment> comments = commentService.getCommentsByEntity(newsId, EntityType.ENTITY_NEWS);
            List<ViewObject> CommentVOs = new ArrayList<>();
            int localUserId = hostHolder.getUser() != null ? hostHolder.getUser().getId() : 0;
            if (localUserId != 0) {
                model.addAttribute("like", likeService.getLikeStauts(localUserId, EntityType.ENTITY_NEWS, newsId));
            } else {
                model.addAttribute("like", 0);
            }
            //评论
            for (Comment comment : comments) {
                ViewObject vo = new ViewObject();
                vo.set("comment", comment);
                vo.set("user", userService.getUser(comment.getUserId()));
                CommentVOs.add(vo);
            }
            model.addAttribute("comments", CommentVOs);

        }
        model.addAttribute("news", news);
        model.addAttribute("owner", userService.getUser(news.getUserId()));
        return "detail";
    }

    /**
     * 添加一条评论，考虑怎么做分页？？？
     *
     * @param newsId
     * @param content
     * @return
     */
    @RequestMapping(path = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam("newsId") int newsId,
                             @RequestParam("content") String content) {

        try {

            //需要做敏感词过滤，涉及状态机？？？
            Comment comment = new Comment();
            comment.setContent(content);
            comment.setStatus(0);
            comment.setCreatedDate(new Date());
            comment.setUserId(hostHolder.getUser().getId());
            comment.setEntityId(newsId);
            comment.setEntityType(EntityType.ENTITY_NEWS);
            commentService.addComment(comment);
            //跟新news评论数量
            int count = commentService.getCommentCount(newsId, EntityType.ENTITY_NEWS);
            newsService.updateCommentCount(newsId, count);
            //怎么异步化？？

        } catch (Exception e) {
            logger.error("添加评论失败！" + e.getMessage());
        }
        return "redirect:/news/" + String.valueOf(newsId);
    }

    /**
     * 添加一条资讯
     *
     * @return
     */
    @RequestMapping(path = {"/user/addNews/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addNews(@RequestParam("image") String image,
                          @RequestParam("title") String title,
                          @RequestParam("link") String link) {

        try {
            News news = new News();
            if (hostHolder.getUser() != null) {
                news.setUserId(hostHolder.getUser().getId());
            } else {
                //匿名Id
                news.setUserId(3);
            }
            news.setTitle(title);
            news.setImage(image);
            news.setLink(link);
            news.setCreatedDate(new Date());
            newsService.addNews(news);
            return ToutiaoUtil.getJSONString(0);

        } catch (Exception e) {
            logger.error("添加资讯错误" + e.getMessage());
            return ToutiaoUtil.getJSONString(1, "发布资讯失败");
        }
    }

    /**
     * 讀取图片
     *
     * @param imageName
     * @return
     */
    @RequestMapping(path = {"/image"}, method = {RequestMethod.GET})
    @ResponseBody
    public void getImage(@RequestParam("name") String imageName, HttpServletResponse response) {

        response.setContentType("image/jpeg");
        try {
            StreamUtils.copy(new FileInputStream(new File(ToutiaoUtil.IMAGE_DIR + imageName)),
                    response.getOutputStream());
        } catch (Exception e) {
            logger.error("读取图片错误！" + e.getMessage());
        }
    }

    /**
     * 上传图片的方法
     *
     * @param file
     * @return
     */
    @RequestMapping(path = {"/uploadImage/"}, method = {RequestMethod.POST})
    @ResponseBody
    public String uploadImage(@RequestParam("file") MultipartFile file) {

        try {
            //String fileUrl = newsService.saveImage(file);
            String fileUrl = qiNiuService.saveImage(file);
            if (fileUrl == null) {
                return ToutiaoUtil.getJSONString(1, "上传失败");
            }
            return ToutiaoUtil.getJSONString(0, fileUrl);

        } catch (Exception e) {
            logger.error("上传图片失败");
            return ToutiaoUtil.getJSONString(1, "上传失败");
        }

    }
}
