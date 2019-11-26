package com.zhenglong.redpack.operator;

import com.zhenglong.redpack.entity.MallUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * @author Lion
 * @描述
 * @date 2019/5/11
 */
@Mapper
public interface MallUserOperator {
    int add(MallUser mallUser);

    int delete(Long id);

    int update(MallUser mallUser);

    MallUser get(Long id);

    List<MallUser> getList(@Param("condition") MallUser condition, @Param("start") int start, @Param("size") int size);

    List<MallUser> getAll(@Param("start") int start, @Param("size") int size,
                          @Param("mallId") String mallId, @Param("nickName") String nickName);

    int getTotalCountWithCondition(MallUser condition);

    int getTotalCount(@Param("mallId") String mallId, @Param("nickName") String nickName);

    List<MallUser> getListWithStatus(@Param("statusArray") Integer[] statusArray,
                                     @Param("start") int start, @Param("size") int size,
                                     @Param("mallId") String mallId, @Param("nickName") String nickName);

    int getTotalCountWithStatus(@Param("statusArray") Integer[] statusArray,
                                @Param("mallId") String mallId, @Param("nickName") String nickName);

    MallUser getWithMallId(String mallId);
}
