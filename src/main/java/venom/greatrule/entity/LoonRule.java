package venom.greatrule.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * loon规则
 */
@Data
@NoArgsConstructor
public class LoonRule implements Comparable<LoonRule> {
    /**
     * 主键
     */
    private Long ruleId;

    /**
     * 应用名称
     */
    private String appName;

    /**
     * 规则
     */
    private String rule;

    /**
     * 排序
     */
    private Integer sort;

    public LoonRule(String appName, String rule, Integer sort) {
        this.appName = appName;
        this.rule = rule;
        this.sort = sort;
    }

    @Override
    public int compareTo(LoonRule o) {
        return sort - o.sort;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof LoonRule loonRule)) return false;
        return StringUtils.equalsIgnoreCase(appName, loonRule.appName) &&
                StringUtils.equalsIgnoreCase(rule, loonRule.rule);
    }

    @Override
    public int hashCode() {
        return Objects.hash(appName, rule);
    }
}