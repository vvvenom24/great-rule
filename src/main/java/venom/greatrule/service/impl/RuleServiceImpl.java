package venom.greatrule.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import venom.greatrule.constant.RuleConstants;
import venom.greatrule.dao.RuleDao;
import venom.greatrule.entity.Rule;
import venom.greatrule.mapper.RuleMapper;
import venom.greatrule.model.req.RuleDTO;
import venom.greatrule.service.RuleService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class RuleServiceImpl implements RuleService {

    private final RuleDao ruleDao;

    private final RuleMapper ruleMapper;

    private final RestTemplate restTemplate;

    @Override
    public void addRule(RuleDTO ruleDTO) {
        String rule = ruleDTO.getRule();
        Integer level = RuleConstants.transRuleLevel(rule);
        if (Objects.nonNull(level)) {
            ruleDao.addSingleRule(ruleDTO.getAppName(), rule, level);
        }
    }

    @Override
    public String importFile(MultipartFile file) {
        String appName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        StringBuilder errorRules = new StringBuilder();
        Set<Rule> rules = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String val = line.trim();
                Integer level = RuleConstants.transRuleLevel(val);
                if (level != null) {
                    rules.add(new Rule(appName, val, level));
                } else {
                    errorRules.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            log.error("读取导入的loon配置文件-{} 异常：", appName, e);
        }
        if (!CollectionUtils.isEmpty(rules)) {
            ruleDao.batchInsertUpdateRules(appName, rules);
        }
        return errorRules.toString();
    }

    @Override
    public String importUrl(String url, String appName) {
        StringBuilder errorRules = new StringBuilder();
        String forObject = restTemplate.getForObject(url, String.class);
        if (StringUtils.isBlank(forObject)) {
            return errorRules.append("blank rules").toString();
        }
        Set<Rule> rules = new HashSet<>();
        for (String line : forObject.split("\n")) {
            line = line.trim();
            Integer level = RuleConstants.transRuleLevel(line);
            if (level != null) {
                rules.add(new Rule(appName, line, level));
            } else {
                errorRules.append(line).append("\n");
            }
        }
        if (!CollectionUtils.isEmpty(rules)) {
            ruleDao.batchInsertUpdateRules(appName, rules);
        }
        return errorRules.toString();
    }

    @Override
    public String getRule(String appName) {
        List<Rule> rules = ruleMapper.selectByAppName(appName);
        if (CollectionUtils.isEmpty(rules)) {
            return StringUtils.EMPTY;
        }
        StringBuilder ruleStr = new StringBuilder();
        rules.sort(Rule::compareTo);
        for (Rule rule : rules) {
            ruleStr.append(rule.getRule()).append("\n");
        }
        return ruleStr.toString();
    }

    @Override
    public List<Rule> getRuleByAppName(String appName) {
        return ruleMapper.selectRuleLikeAppName(appName);
    }

    @Override
    public void deleteRuleById(Long ruleId) {
        ruleMapper.deleteByPrimaryKey(ruleId);
    }

    @Override
    public List<Rule> getRuleByContent(String content) {
        return ruleMapper.selectRuleContent(content);
    }
}
