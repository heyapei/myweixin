package com.hyp.myweixin.controller.qubaoming.line;

import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.ImgFileService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/9/2 21:19
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/weChat/online")
@Slf4j
public class SmallOnLineController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private ImgFileService imgFileService;


    @ApiOperation(value = "是否显示内容", tags = {"程序上线"})
    @GetMapping("rightOrNo")
    public Result OnLineRight() {
        Integer result = 0;
        return Result.buildResult(Result.Status.OK, result);
    }
}
