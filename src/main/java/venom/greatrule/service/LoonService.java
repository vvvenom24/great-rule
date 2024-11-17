package venom.greatrule.service;

import org.springframework.web.multipart.MultipartFile;
import venom.greatrule.entity.LoonRule;
import venom.greatrule.model.req.LoonRuleDTO;

import java.util.List;

public interface LoonService {

    String importFile(MultipartFile file);

    String importUrl(String url, String app);

    String getRule(String appName);

    void addRule(LoonRuleDTO loonRuleDTO);

    List<LoonRule> getRuleByAppName(String appName);

    void deleteRuleById(Long ruleId);

    List<LoonRule> getRuleByContent(String content);
}
