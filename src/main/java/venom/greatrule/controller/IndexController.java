package venom.greatrule.controller;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import venom.greatrule.entity.LoonRule;
import venom.greatrule.service.LoonService;

import java.util.List;

@Controller
@AllArgsConstructor
public class IndexController {

    private final LoonService loonService;
    private static final int PAGE_SIZE = 10;

    @GetMapping("/")
    public String login(@RequestParam(required = false) String appName,
                       @RequestParam(defaultValue = "appName") String searchType,
                       @RequestParam(defaultValue = "1") int page,
                       Model model) {
        if (appName != null && !appName.trim().isEmpty()) {
            PageHelper.startPage(page, PAGE_SIZE, "rule_id");
            List<LoonRule> rules;
            if ("rule".equals(searchType)) {
                rules = loonService.getRuleByContent(appName);
            } else {
                rules = loonService.getRuleByAppName(appName);
            }
            PageInfo<LoonRule> pageInfo = new PageInfo<>(rules);
            
            model.addAttribute("appName", appName);
            model.addAttribute("searchType", searchType);
            model.addAttribute("rules", rules);
            model.addAttribute("currentPage", page);
            model.addAttribute("totalPages", pageInfo.getPages());
        }
        return "index";
    }
}
