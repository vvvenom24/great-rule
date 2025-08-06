package venom.greatrule;

import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import venom.greatrule.entity.Rule;
import venom.greatrule.mapper.RuleMapper;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@SpringBootTest
class GreatRuleApplicationTests {

    @Autowired
    RuleMapper ruleMapper;

    private final String rule_config_file_path = "/Users/venom/Projects/great-rule/SR." +
            LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS")) + ".conf";

    private final File file = new File(rule_config_file_path);

    private final List<String> proxyApps = List.of("google", "youtube", "im", "openai", "instagram", "twitter",
            "paypal", "spotify", "discord", "netflix", "disneyPlus", "abroad", "dev");

    private final List<String> directApps = List.of("appleService", "appleMusic", "appleTV", "microsoft",
            "icloud", "oneDrive", "bing", "cn");

    private final List<String> rejectApps = List.of("Advertising");

    private List<String> rules;

    private Map<String, List<String>> appMap;

    @BeforeEach
    public void beforeAll() {
        rules = new ArrayList<>();
        rules.add("# Shadowrocket: " + LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        rules.add("[General]");
        rules.add("# 默认关闭 ipv6 支持，如果需要请手动开启");
        rules.add("ipv6 = false");
        rules.add("bypass-system = true");
        rules.add("skip-proxy = 192.168.0.0/16, 10.0.0.0/8, 172.16.0.0/12, localhost, *.local, e.crashlytics.com, captive.apple.com");
        rules.add("bypass-tun = 10.0.0.0/8,100.64.0.0/10,127.0.0.0/8,169.254.0.0/16,172.16.0.0/12,192.0.0.0/24,192.0.2.0/24,192.88.99.0/24,192.168.0.0/16,198.18.0.0/15,198.51.100.0/24,203.0.113.0/24,224.0.0.0/4,255.255.255.255/32");
        rules.add("dns-server = https://1.12.12.12/dns-query, https://223.5.5.5/dns-query");
        rules.add("update-url = http://localhost:2332");
        rules.add("");
        rules.add("[Rule]");
        appendFile(file, rules);
        appMap = new HashMap<>();
        appMap.put("PROXY", proxyApps);
        appMap.put("DIRECT", directApps);
        appMap.put("REJECT", rejectApps);
    }

    @AfterEach
    public void afterAll() {
        rules.add("FINAL,DIRECT");
        rules.add("");
        rules.add("[URL Rewrite]");
        rules.add("^https?://(www.)?(g|google)\\.cn https://www.google.com 302");
        rules.add("");
        rules.add("[MITM]");
        rules.add("enable = false");
        rules.add("hostname = *.google.cn,*.googlevideo.com");
        appendFile(file, rules);
    }

    @Test
    void contextLoads() {
        for (Map.Entry<String, List<String>> entry : appMap.entrySet()) {
            String proxy = entry.getKey();
            List<String> apps = entry.getValue();
            for (String app : apps) {
                List<Rule> rules = ruleMapper.selectByAppName(app);
                if (rules.isEmpty()) System.err.println(app + " not found");
                this.rules = new ArrayList<>(rules.size() + 1);
                this.rules.add("# " + app);
                for (Rule rule : rules) {
                    this.rules.add(rule.getRule() + "," + proxy);
                }
                appendFile(file, this.rules);
            }
        }
    }

    private void appendFile(File file, List<String> text) {
        try {
            FileUtils.writeLines(file, "UTF-8", text, true);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
