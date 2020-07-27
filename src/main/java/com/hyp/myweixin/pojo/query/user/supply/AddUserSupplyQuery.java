package com.hyp.myweixin.pojo.query.user.supply;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/27 20:44
 * @Description: TODO 保存用户补充信息
 */
@Data
public class AddUserSupplyQuery {

    /**
     * 数据实例化 不对userId赋值
     *
     * @return
     */
    public static AddUserSupplyQuery init() {
        AddUserSupplyQuery addUserSupplyQuery = new AddUserSupplyQuery();
        addUserSupplyQuery.setRealName("");
        addUserSupplyQuery.setAddress("");
        addUserSupplyQuery.setEmail("");
        addUserSupplyQuery.setWechatNumber("");
        addUserSupplyQuery.setBirthday(new Date());
        addUserSupplyQuery.setPhone("");
        addUserSupplyQuery.setGender(0);
        return addUserSupplyQuery;
    }


    /**
     * 用户ID
     */
    @ApiModelProperty(value = "用户ID", name = "userId", required = true)
    @NotNull
    private Integer userId;

    /**
     * 真实姓名
     */
    @ApiModelProperty(value = "真实姓名", name = "realName", required = true)
    private String realName;

    /**
     * 收件地址
     */
    @ApiModelProperty(value = "收件地址", name = "address", required = true)
    private String address;

    /**
     * 邮件地址
     */
    @ApiModelProperty(value = "邮件地址", name = "email", required = true)
    private String email;

    /**
     * 微信号
     */
    @ApiModelProperty(value = "微信号", name = "wechatNumber", required = true)
    private String wechatNumber;

    /**
     * 生日 采用时间戳格式
     */
    @ApiModelProperty(value = "生日", name = "birthday", required = true)
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    private Date birthday;

    /**
     * 真实的手机号
     */
    @ApiModelProperty(value = "手机号", name = "phone", required = true)
    private String phone;

    /**
     * 性别 1男 2女 0未知 默认未知
     */
    @ApiModelProperty(value = "性别 1男 2女 0未知 默认未知", name = "gender", required = true)
    private Integer gender;


}
