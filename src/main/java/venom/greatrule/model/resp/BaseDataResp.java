package venom.greatrule.model.resp;

import lombok.Data;
import lombok.NoArgsConstructor;
import venom.greatrule.enums.CommonResultEnum;

@Data
@NoArgsConstructor
public class BaseDataResp {

    private int code = 100000;

    private String msg = "操作成功";

    private Object data;

    public BaseDataResp(CommonResultEnum resultEnum) {
        this.code = resultEnum.getCode();
        this.msg = resultEnum.getMsg();
    }

    public static BaseDataResp ofResultEnum(CommonResultEnum resultEnum) {
        return new BaseDataResp(resultEnum);
    }

    public BaseDataResp(Object data) {
        this.data = data;
    }
}
