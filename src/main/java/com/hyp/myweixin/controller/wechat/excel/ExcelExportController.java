package com.hyp.myweixin.controller.wechat.excel;

import com.hyp.myweixin.pojo.vo.result.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

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


    /**
     * 通过活动Id导出活动用户信息
     * @param voteWorkId 活动ID
     * @return
     */
    @PostMapping("userwork/voteworkid")
    public Result getOpenId( String voteWorkId) {


        return null;
    }



}
