package venom.greatrule.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;
import venom.greatrule.constant.RuleConstants;
import venom.greatrule.dao.LoonRuleDao;
import venom.greatrule.entity.LoonRule;
import venom.greatrule.mapper.LoonRuleMapper;
import venom.greatrule.model.req.LoonRuleDTO;
import venom.greatrule.service.LoonService;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

@Slf4j
@Service
@AllArgsConstructor
public class LoonServiceImpl implements LoonService {

    private final LoonRuleDao loonRuleDao;

    private final LoonRuleMapper loonRuleMapper;

    private final RestTemplate restTemplate;

    @Override
    public void addRule(LoonRuleDTO loonRuleDTO) {
        String rule = loonRuleDTO.getRule();
        Integer level = RuleConstants.transRuleLevel(rule);
        if (Objects.nonNull(level)) {
            loonRuleDao.addSingleRule(loonRuleDTO.getAppName(), rule, level);
        }
    }

    @Override
    public String importFile(MultipartFile file) {
        String appName = file.getOriginalFilename().substring(0, file.getOriginalFilename().lastIndexOf("."));
        StringBuilder errorRules = new StringBuilder();
        Set<LoonRule> rules = new HashSet<>();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(file.getInputStream()))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String val = line.trim();
                Integer level = RuleConstants.transRuleLevel(val);
                if (level != null) {
                    rules.add(new LoonRule(appName, val, level));
                } else {
                    errorRules.append(line).append("\n");
                }
            }
        } catch (Exception e) {
            log.error("读取导入的loon配置文件-{} 异常：", appName, e);
        }
        if (!CollectionUtils.isEmpty(rules)) {
            loonRuleDao.batchInsertUpdateRules(appName, rules);
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
        Set<LoonRule> rules = new HashSet<>();
        for (String line : forObject.split("\n")) {
            line = line.trim();
            Integer level = RuleConstants.transRuleLevel(line);
            if (level != null) {
                rules.add(new LoonRule(appName, line, level));
            } else {
                errorRules.append(line).append("\n");
            }
        }
        if (!CollectionUtils.isEmpty(rules)) {
            loonRuleDao.batchInsertUpdateRules(appName, rules);
        }
        return errorRules.toString();
    }

    @Override
    public String getRule(String appName) {
        List<LoonRule> loonRules = loonRuleMapper.selectByAppName(appName);
        if (CollectionUtils.isEmpty(loonRules)) {
            return StringUtils.EMPTY;
        }
        StringBuilder ruleStr = new StringBuilder();
        loonRules.sort(LoonRule::compareTo);
        for (LoonRule rule : loonRules) {
            ruleStr.append(rule.getRule()).append("\n");
        }
        return ruleStr.toString();
    }

    @Override
    public List<LoonRule> getRuleByAppName(String appName) {
        return loonRuleMapper.selectRuleLikeAppName(appName);
    }

    @Override
    public void deleteRuleById(Long ruleId) {
        loonRuleMapper.deleteByPrimaryKey(ruleId);
    }

    @Override
    public List<LoonRule> getRuleByContent(String content) {
        return loonRuleMapper.selectRuleContent(content);
    }
}
