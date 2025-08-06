package venom.greatrule.service;

import org.springframework.web.multipart.MultipartFile;
import venom.greatrule.entity.Rule;
import venom.greatrule.model.req.RuleDTO;

import java.util.List;

public interface RuleService {

    String importFile(MultipartFile file);

    String importUrl(String url, String app);

    String getRule(String appName);

    void addRule(RuleDTO ruleDTO);

    List<Rule> getRuleByAppName(String appName);

    void deleteRuleById(Long ruleId);

    List<Rule> getRuleByContent(String content);
}
