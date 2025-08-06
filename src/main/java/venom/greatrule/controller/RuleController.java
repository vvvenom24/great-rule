package venom.greatrule.controller;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import venom.greatrule.enums.CommonResultEnum;
import venom.greatrule.model.req.RuleDTO;
import venom.greatrule.model.resp.BaseDataResp;
import venom.greatrule.service.RuleService;

/**
 * loon相关接口
 */
@RestController
@RequestMapping("/loon")
@AllArgsConstructor
public class RuleController {

    private final RuleService ruleService;

    @PostMapping("/rule/add")
    public BaseDataResp addRule(@RequestBody @Valid RuleDTO ruleDTO) {
        ruleService.addRule(ruleDTO);
        return BaseDataResp.ofResultEnum(CommonResultEnum.SUCCESS);
    }

    /**
     * 从文件导入规则
     */
    @PostMapping("/rule/import/file")
    public BaseDataResp importFile(@RequestParam("rules") MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename) || !filename.contains("txt")) {
            return BaseDataResp.ofResultEnum(CommonResultEnum.IMPORT_FILE_SCHEMA_ERROR);
        }
        String errorRule = ruleService.importFile(file);
        return new BaseDataResp(errorRule);
    }

    /**
     * 从url导入规则
     */
    @PostMapping("/rule/import/url")
    public BaseDataResp importUrl(@RequestParam("url") String url, @RequestParam("app") String app) {
        if (StringUtils.isBlank(url)) {
            return BaseDataResp.ofResultEnum(CommonResultEnum.IMPORT_URL_ERROR);
        }
        String errorRule = ruleService.importUrl(url, app);
        return new BaseDataResp(errorRule);
    }

    /**
     * 删除规则
     */
    @DeleteMapping("/rule/delete/{ruleId}")
    public BaseDataResp delete(@PathVariable("ruleId") Long ruleId) {
        ruleService.deleteRuleById(ruleId);
        return new BaseDataResp(CommonResultEnum.SUCCESS);
    }
}
