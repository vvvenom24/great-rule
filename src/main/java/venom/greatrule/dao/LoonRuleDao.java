package venom.greatrule.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import venom.greatrule.entity.LoonRule;
import venom.greatrule.mapper.LoonRuleMapper;

import java.util.HashSet;
import java.util.Set;

@Repository
@AllArgsConstructor
public class LoonRuleDao {

    private final LoonRuleMapper loonRuleMapper;

    public void batchInsertUpdateRules(String appName, Set<LoonRule> rules) {
        Set<LoonRule> existLoonRuleSet = new HashSet<>(loonRuleMapper.selectByAppName(appName));
        // 去重
        if (!existLoonRuleSet.isEmpty()) {
            rules.removeIf(existLoonRuleSet::contains);
        }
        if (!CollectionUtils.isEmpty(rules)) {
            loonRuleMapper.batchInsert(rules);
        }
    }

    public void addSingleRule(String appName, String rule, Integer level) {
        if (loonRuleMapper.selectByAppNameAndRule(appName, rule) == null) {
            loonRuleMapper.insertSelective(new LoonRule(appName, rule, level));
        }
    }
}
