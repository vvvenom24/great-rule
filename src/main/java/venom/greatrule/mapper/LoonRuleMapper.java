package venom.greatrule.mapper;

import org.apache.ibatis.annotations.Param;
import venom.greatrule.entity.LoonRule;

import java.util.Collection;
import java.util.List;

public interface LoonRuleMapper {
    int deleteByPrimaryKey(Long ruleId);

    int deleteByPrimaryKeyIn(List<Long> list);

    int insert(LoonRule record);

    int insertSelective(LoonRule record);

    LoonRule selectByPrimaryKey(Long ruleId);

    int updateByPrimaryKeySelective(LoonRule record);

    int updateByPrimaryKey(LoonRule record);

    int batchInsert(@Param("list") Collection<LoonRule> list);

    List<LoonRule> selectByAppName(@Param("appName") String appName);

    LoonRule selectByAppNameAndRule(@Param("appName") String appName, @Param("rule") String rule);

    List<LoonRule> selectRuleLikeAppName(@Param("appName") String appName);

    List<LoonRule> selectRuleContent(@Param("content") String content);
}