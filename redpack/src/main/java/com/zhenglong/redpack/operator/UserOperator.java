package com.zhenglong.redpack.operator;

import com.zhenglong.redpack.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/17
 */
@Mapper
public interface UserOperator {
    List<User> userList(@Param("start") int start, @Param("size") int size);

    int getTotalCount();

    List<User> userListFilter(@Param("start") int start, @Param("size") Integer size,
                        @Param("status") Integer status, @Param("subscribe") Integer subscribe,
                        @Param("sex") Integer sex,@Param("mallId") String mallId,@Param("nickName") String nickName);

    int getTotalCountFilter(@Param("status") Integer status, @Param("subscribe") Integer subscribe,
                      @Param("sex") Integer sex,@Param("mallId") String mallId,@Param("nickName") String nickName);

    List<User> userListNickName(String nickName);

    void updateMallId(@Param("userId") Long userId, @Param("mallUserId") String mallUserId,
                      @Param("statusCode") int statusCode);

    void updateMallStatus(@Param("statusCode") int statusCode,@Param("mallUserId") String mallUserId);

    void updateWxNumber(@Param("id") Integer id, @Param("wxNumber") String wxNumber);

    int countWithMallId(String mallUserId);

    List<User> userAll();

    void updateRemark(@Param("id") Integer id, @Param("remark") String remark);

    Long getWithMallId(String mallUserId);
}
