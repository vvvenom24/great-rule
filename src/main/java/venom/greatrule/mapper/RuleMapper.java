package venom.greatrule.mapper;

import org.apache.ibatis.annotations.Param;
import venom.greatrule.entity.Rule;

import java.util.Collection;
import java.util.List;

public interface RuleMapper {
    int deleteByPrimaryKey(Long ruleId);

    int deleteByPrimaryKeyIn(List<Long> list);

    int insert(Rule record);

    int insertSelective(Rule record);

    Rule selectByPrimaryKey(Long ruleId);

    int updateByPrimaryKeySelective(Rule record);

    int updateByPrimaryKey(Rule record);

    int batchInsert(@Param("list") Collection<Rule> list);

    List<Rule> selectByAppName(@Param("appName") String appName);

    Rule selectByAppNameAndRule(@Param("appName") String appName, @Param("rule") String rule);

    List<Rule> selectRuleLikeAppName(@Param("appName") String appName);

    List<Rule> selectRuleContent(@Param("content") String content);
}