package venom.greatrule.dao;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Repository;
import venom.greatrule.entity.RuleSubscribe;
import venom.greatrule.mapper.RuleSubscribeMapper;

import java.util.List;

@Repository
@AllArgsConstructor
public class RuleSubscribeDao {

    private final RuleSubscribeMapper ruleSubscribeMapper;

    public void addSubscribe(String appName, String url) {
        List<RuleSubscribe> subscribes = ruleSubscribeMapper.selectByAppName(appName);
        subscribes.forEach(subscribe -> {
            if (subscribe.getUrl().equals(url)) {
                return;
            }
            RuleSubscribe newSubscribe = new RuleSubscribe();
            newSubscribe.setAppName(appName);
            newSubscribe.setUrl(url);
            ruleSubscribeMapper.insertSelective(newSubscribe);
        });
    }
}
