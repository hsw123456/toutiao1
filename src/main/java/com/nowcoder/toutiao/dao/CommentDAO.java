package com.nowcoder.toutiao.dao;

import com.nowcoder.toutiao.model.Comment;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * Created by hsw on 2017/6/8.
 */
@Mapper
public interface CommentDAO {

    String TABLE_NAME = " comment ";
    String INSERT_FIELDS = "  user_id, content, entity_id, entity_type, status, created_date  ";
    String SELECT_FIELDS = "id, " + INSERT_FIELDS;

    @Insert({"insert into",TABLE_NAME,"(",INSERT_FIELDS,") values(#{userId},#{content},#{entityId},#{entityType}, " +
            "#{status} ,#{createdDate})"})
    int addComment(Comment comment);

    /**
     * 改变status 的值用于删除评论
     * @param entityId
     * @param entityType
     * @param status
     */
    @Update({"update" , TABLE_NAME," set status = #{status} where entity_id=#{entityId} AND entity_type=#{entityType} "})
    void updateStatus(@Param("entityId") int entityId,@Param("entityType") int entityType,@Param("status") int status);

    @Select({"select", SELECT_FIELDS ,"from" ,TABLE_NAME,"where entity_id=#{entityId} AND entity_type=#{entityType} order by id desc"})
    List<Comment> selectByEntity(@Param("entityId") int entityId,@Param("entityType") int entityType);

    @Select({"select count(id) from ",TABLE_NAME, "where entity_id=#{entityId} AND entity_type=#{entityType}" })
    int getCommentCount(@Param("entityId") int entityId,@Param("entityType") int entityType);
}
