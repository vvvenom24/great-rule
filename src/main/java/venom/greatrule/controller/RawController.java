package venom.greatrule.controller;

import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import venom.greatrule.service.LoonService;

@RestController
@AllArgsConstructor
@RequestMapping("/raw")
public class RawController {

    private final LoonService loonService;

    /**
     * 查询规则
     */
    @GetMapping(value = "/rule/{appName}", produces = "text/plain")
    public String getRule(@PathVariable("appName") String appName) {
        return loonService.getRule(appName);
    }
}
