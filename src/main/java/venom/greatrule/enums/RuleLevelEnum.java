package venom.greatrule.enums;

import lombok.Getter;
import venom.greatrule.constant.RuleConstants;

@Getter
public enum RuleLevelEnum {

    DOMAIN(RuleConstants.DOMAIN, 1),

    DOMAIN_SUFFIX(RuleConstants.DOMAIN_SUFFIX, 2),

    IP_CIDR(RuleConstants.IP_CIDR, 3),

    IP_CIDR6(RuleConstants.IP_CIDR6, 4),

    GEOIP(RuleConstants.GEOIP, 5),

    IP_ASN(RuleConstants.IP_ASN, 6),

    SRC_PORT(RuleConstants.SRC_PORT, 7),

    DEST_PORT(RuleConstants.DEST_PORT, 8),

    PROTOCOL(RuleConstants.PROTOCOL, 9),

    DOMAIN_KEYWORD(RuleConstants.DOMAIN_KEYWORD, 10),

    USER_AGENT(RuleConstants.USER_AGENT, 11),

    URL_REGEX(RuleConstants.URL_REGEX, 12),

    ;

    private final String rule;

    private final int level;

    public static int getSort(String rule) {
        for (RuleLevelEnum anEnum : values()) {
            if (rule.equals(anEnum.getRule())) {
                return anEnum.getLevel();
            }
        }
        throw new RuntimeException("Invalid rule level: " + rule);
    }

    RuleLevelEnum(String rule, int level) {
        this.rule = rule;
        this.level = level;
    }

}
