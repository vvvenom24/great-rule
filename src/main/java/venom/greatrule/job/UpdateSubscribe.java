package venom.greatrule.job;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import venom.greatrule.constant.RuleConstants;
import venom.greatrule.dao.RuleDao;
import venom.greatrule.entity.Rule;
import venom.greatrule.entity.RuleSubscribe;
import venom.greatrule.mapper.RuleSubscribeMapper;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class UpdateSubscribe {

    private final RuleSubscribeMapper ruleSubscribeMapper;

    private final RestTemplate restTemplate;

    private final RuleDao ruleDao;

    @Scheduled(cron = "0 0 12 * * ?")
    public void updateRule() {
        log.info("开始更新订阅规则");
        List<RuleSubscribe> subscribes = ruleSubscribeMapper.selectList();
        if (subscribes.isEmpty()) {
            log.info("没有可更新的订阅");
            return;
        }
        log.info("共找到 {} 个订阅地址", subscribes.size());
        subscribes.parallelStream().forEach(this::pullRules);
    }

    private void pullRules(RuleSubscribe subscribe) {
        String appName = subscribe.getAppName();
        log.info("[{}]开始拉取订阅规则: {}", appName, subscribe.getUrl());
        String forObject = restTemplate.getForObject(subscribe.getUrl(), String.class);
        if (StringUtils.isBlank(forObject)) {
            return;
        }
        Set<Rule> rules = new HashSet<>();
        for (String line : forObject.split("\n")) {
            line = line.trim();
            Integer level = RuleConstants.transRuleLevel(line);
            if (level != null) {
                rules.add(new Rule(appName, line, level));
            } else {
                log.warn("[{}]规则 {} 无效，无法转换为合法规则", appName, line);
            }
        }
        int newRulesCount = 0;
        if (!CollectionUtils.isEmpty(rules)) {
            newRulesCount = ruleDao.batchInsertUpdateRules(appName, rules);
        }
        log.info("[{}]拉取订阅规则完成: {}, 新增 {} 条规则", appName,
                subscribe.getUrl(), newRulesCount);
    }
}
