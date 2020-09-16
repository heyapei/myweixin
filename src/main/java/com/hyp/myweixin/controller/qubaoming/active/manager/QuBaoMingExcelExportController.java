package com.hyp.myweixin.controller.qubaoming.active.manager;

import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.metadata.Sheet;
import com.alibaba.excel.support.ExcelTypeEnum;
import com.hyp.myweixin.config.secretkey.SecretKeyPropertiesValue;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.qubaoming.model.QubaomingActiveBase;
import com.hyp.myweixin.pojo.qubaoming.vo.active.manager.SignUpExcelExportVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.qubaoming.QuBaoMingExcelOptionService;
import com.hyp.myweixin.service.qubaoming.QubaomingActiveBaseService;
import com.hyp.myweixin.utils.MyRequestVailDateUtil;
import com.hyp.myweixin.utils.dateutil.MyDateStyle;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/22 10:26
 * @Description: TODO
 */
@RestController
@RequestMapping("qubaoming/data/export")
@Slf4j
public class QuBaoMingExcelExportController {

    @Autowired
    private MyRequestVailDateUtil myRequestVailDateUtil;
    @Autowired
    private HttpServletResponse httpServletResponse;
    @Autowired
    private HttpServletRequest httpServletRequest;
    @Autowired
    private SecretKeyPropertiesValue secretKeyPropertiesValue;
    @Autowired
    private QuBaoMingExcelOptionService quBaoMingExcelOptionService;
    @Autowired
    private QubaomingActiveBaseService qubaomingActiveBaseService;


    @GetMapping("/exportExcel")
    public void exportExcel(String sk) throws IOException {
        Integer activeId = quBaoMingExcelOptionService.judgeExportRight(sk);
        List<SignUpExcelExportVO> signUpExcelExportVOList = quBaoMingExcelOptionService.exportSignUpExcelExportVOByActiveId(activeId);

        log.info("查询结果：{}", signUpExcelExportVOList);

        ServletOutputStream out = httpServletResponse.getOutputStream();
        ExcelWriter writer = new ExcelWriter(out, ExcelTypeEnum.XLSX, true);
        QubaomingActiveBase qubaomingActiveBase = qubaomingActiveBaseService.selectByPkId(activeId);
        String fileName = qubaomingActiveBase.getActiveName() + "_" + MyDateUtil.DateToString(new Date(), MyDateStyle.YYYY_MM_DD_HH_MM);
        Sheet sheet = new Sheet(1, 0, SignUpExcelExportVO.class);
        //设置自适应宽度
        sheet.setAutoWidth(Boolean.TRUE);
        // 第一个 sheet 名称
        sheet.setSheetName("报名信息");
        writer.write(signUpExcelExportVOList, sheet);
        //通知浏览器以附件的形式下载处理，设置返回头要注意文件名有中文
        httpServletResponse.setHeader("Content-disposition", "attachment;filename=" + new String(fileName.getBytes("gb2312"), "ISO8859-1") + ".xlsx");
        writer.finish();
        httpServletResponse.setContentType("multipart/form-data");
        httpServletResponse.setCharacterEncoding("utf-8");
        out.flush();

        out.close();
    }


    /**
     * 获取指定活动的excel导出地址
     *
     * @param activeId 活动ID
     * @return
     */
    @PostMapping("/getActiveExcelUrl")
    @ResponseBody
    public Result getActiveExcelUrl(Integer activeId, Integer userId) {

        /*鉴权*/
        boolean b = myRequestVailDateUtil.validateSignMd5Date(httpServletRequest, secretKeyPropertiesValue.getMd5Key(), 10);
        if (!b) {
            throw new MyDefinitionException(401, "密钥验证错误");
        }

        String excelExportUrlByActiveId = null;
        try {
            excelExportUrlByActiveId = quBaoMingExcelOptionService.getExcelExportUrlByActiveId(activeId, userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("获取excel导出链接错误，" + e.getMessage());
        }
        if (excelExportUrlByActiveId == null) {
            return Result.buildResult(Result.Status.NOT_FOUND, "获取导出链接错误");
        }

        return Result.buildResult(Result.Status.OK, "请求成功", excelExportUrlByActiveId);
    }


}
