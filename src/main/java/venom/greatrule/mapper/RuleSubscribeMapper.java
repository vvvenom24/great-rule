package venom.greatrule.mapper;

import org.apache.ibatis.annotations.Param;
import venom.greatrule.entity.RuleSubscribe;

import java.util.List;

public interface RuleSubscribeMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(RuleSubscribe record);

    int insertSelective(RuleSubscribe record);

    RuleSubscribe selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(RuleSubscribe record);

    int updateByPrimaryKey(RuleSubscribe record);

    List<RuleSubscribe> selectList();

    List<RuleSubscribe> selectByAppName(@Param("appName") String appName);
}