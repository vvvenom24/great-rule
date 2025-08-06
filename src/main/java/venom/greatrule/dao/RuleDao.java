package venom.greatrule.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import venom.greatrule.entity.Rule;
import venom.greatrule.mapper.RuleMapper;

import java.util.HashSet;
import java.util.Set;

@Repository
@AllArgsConstructor
public class RuleDao {

    private final RuleMapper ruleMapper;

    public void batchInsertUpdateRules(String appName, Set<Rule> rules) {
        Set<Rule> existRuleSet = new HashSet<>(ruleMapper.selectByAppName(appName));
        // 去重
        if (!existRuleSet.isEmpty()) {
            rules.removeIf(existRuleSet::contains);
        }
        if (!CollectionUtils.isEmpty(rules)) {
            ruleMapper.batchInsert(rules);
        }
    }

    public void addSingleRule(String appName, String rule, Integer level) {
        if (ruleMapper.selectByAppNameAndRule(appName, rule) == null) {
            ruleMapper.insertSelective(new Rule(appName, rule, level));
        }
    }
}
