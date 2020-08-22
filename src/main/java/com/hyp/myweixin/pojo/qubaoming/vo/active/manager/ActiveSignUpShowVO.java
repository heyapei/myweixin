package com.hyp.myweixin.pojo.qubaoming.vo.active.manager;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 9:52
 * @Description: TODO
 */
@Data
public class ActiveSignUpShowVO {

    public static ActiveSignUpShowVO init() {
        ActiveSignUpShowVO activeSignUpShowVO = new ActiveSignUpShowVO();
        activeSignUpShowVO.setName(null);
        activeSignUpShowVO.setAge(null);
        activeSignUpShowVO.setPhone(null);
        activeSignUpShowVO.setGender(null);
        activeSignUpShowVO.setAvatar("");
        return activeSignUpShowVO;
    }

    @ApiModelProperty(value = "姓名", name = "name", required = true)
    private String name;
    @ApiModelProperty(value = "年龄", name = "age", required = true)
    private String age;
    @ApiModelProperty(value = "电话", name = "phone", required = true)
    private String phone;
    @ApiModelProperty(value = "性别", name = "gender", required = true)
    private String gender;
    @ApiModelProperty(value = "微信头像信息", name = "avatar", required = true)
    private String avatar;

    @ApiModelProperty(value = "创建时间", name = "createTime", required = true)
    private String createTime;


}
