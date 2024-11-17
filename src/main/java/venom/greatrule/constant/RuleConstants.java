package venom.greatrule.constant;

import org.apache.commons.lang3.StringUtils;
import venom.greatrule.enums.RuleLevelEnum;

import java.util.Set;

public final class RuleConstants {

    public static final String DOMAIN = "DOMAIN";

    public static final String DOMAIN_SUFFIX = "DOMAIN-SUFFIX";

    public static final String IP_CIDR = "IP-CIDR";

    public static final String IP_CIDR6 = "IP-CIDR6";

    public static final String GEOIP = "GEOIP";

    public static final String IP_ASN = "IP-ASN";

    public static final String SRC_PORT = "SRC-PORT";

    public static final String DEST_PORT = "DEST-PORT";

    public static final String PROTOCOL = "PROTOCOL";

    public static final String DOMAIN_KEYWORD ="DOMAIN-KEYWORD";

    public static final String USER_AGENT ="USER-AGENT";

    public static final String URL_REGEX ="URL-REGEX";

    public static final Set<String> ALL_RULE_SET = Set.of(DOMAIN, DOMAIN_SUFFIX, IP_CIDR, IP_CIDR6, GEOIP, IP_ASN, SRC_PORT, DEST_PORT, PROTOCOL, DOMAIN_KEYWORD, USER_AGENT, URL_REGEX);

    public static Integer transRuleLevel(String rule) {
        if (StringUtils.isNotBlank(rule) && !rule.startsWith("#")) {
            String[] split = rule.split(",");
            if (split.length > 1) {
                return RuleLevelEnum.getSort(split[0]);
            }
        }
        return null;
    }

    private RuleConstants() {}
}
