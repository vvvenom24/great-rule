package venom.greatrule.model.req;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class LoonRuleDTO {

    @NotNull(message = "param1 cannot be null")
    private String appName;

    @NotNull(message = "param2 cannot be null")
    private String rule;
}
