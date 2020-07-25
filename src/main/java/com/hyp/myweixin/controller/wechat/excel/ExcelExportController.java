package com.hyp.myweixin.controller.wechat.excel;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.service.ExcelOptionService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/29 23:12
 * @Description: TODO
 */
@RestController
@RequestMapping("/data/export")
@Slf4j
public class ExcelExportController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private ExcelOptionService excelOptionService;


    /**
     * 获取指定活动的excel导出地址
     *
     * @param activeId 活动ID
     * @return
     */
    @PostMapping("/getActiveExcelUrl")
    @ResponseBody
    public String export(Integer activeId, Integer userId) {

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        String excelExportUrlByActiveId = excelOptionService.getExcelExportUrlByActiveId(activeId, userId);
        return excelExportUrlByActiveId;
    }


}
