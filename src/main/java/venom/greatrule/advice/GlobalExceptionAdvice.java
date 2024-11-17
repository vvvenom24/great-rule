package venom.greatrule.advice;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import venom.greatrule.enums.CommonResultEnum;
import venom.greatrule.model.resp.BaseDataResp;

@Slf4j
@RestControllerAdvice(annotations = RestController.class)
public class GlobalExceptionAdvice {

    @ExceptionHandler(value = Exception.class)
    public BaseDataResp handleException(final Exception e) {
        log.error("{}", e.getMessage(), e);
        return BaseDataResp.ofResultEnum(CommonResultEnum.UNKNOWN_ERROR);
    }
}
