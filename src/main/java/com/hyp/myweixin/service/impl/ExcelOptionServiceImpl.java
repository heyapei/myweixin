package com.hyp.myweixin.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.excel.active.ActiveVoteWorkExcelExportVO;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.MyCommonUtil;
import com.hyp.myweixin.utils.MyEnDecryptionUtil;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.redis.MyRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 15:57
 * @Description: TODO
 */

@Slf4j
@Service
@PropertySource("classpath:excel-export.properties")
public class ExcelOptionServiceImpl implements ExcelOptionService {
    private static final String SEMICOLON_SEPARATOR = ";";
    @Autowired
    private MyRedisUtil myRedisUtil;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;
    @Autowired
    private AdministratorsOptionService administratorsOptionService;
    @Autowired
    private MyEnDecryptionUtil myEnDecryptionUtil;

    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;


    @Value("${excel.export.aes.key}")
    private String AES_KEY;
    @Value("${excel.export.redis.key}")
    private String EXCEL_EXPORT_REDIS_KEY;


    /**
     * 获取活动下的所有的作品并转换成excel的数据（ActiveVoteWorkExcelExport）
     *
     * @param activeId 活动ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public List<ActiveVoteWorkExcelExportVO> exportActiveVoteWorkExcelExportVOByActiveId(Integer activeId) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("获取excel数据必须填写活动ID");
        }
        List<WeixinVoteWork> weixinVoteWorkList = weixinVoteWorkService.getWeixinVoteWorkListByWorkStatus(activeId, null);
        List<ActiveVoteWorkExcelExportVO> activeVoteWorkExcelExportVOList = MyEntityUtil.entity2VMList(weixinVoteWorkList, ActiveVoteWorkExcelExportVO.class);
        List<ActiveVoteWorkExcelExportVO> activeVoteWorkExcelExportVOS = new ArrayList<>();
        for (ActiveVoteWorkExcelExportVO activeVoteWorkExcelExportVO : activeVoteWorkExcelExportVOList) {
            WeixinVoteWork.VoteWorkStatusEnum voteWorkStatusEnum = WeixinVoteWork.VoteWorkStatusEnum.getVoteWorkStatusEnum(activeVoteWorkExcelExportVO.getVoteWorkStatus());
            activeVoteWorkExcelExportVO.setVoteWorkStatusMsg(voteWorkStatusEnum.getMsg());
            activeVoteWorkExcelExportVOS.add(activeVoteWorkExcelExportVO);
        }
        return activeVoteWorkExcelExportVOS;
    }


    /**
     * 判断是否能够使用该地址进行导出处理
     *
     * @param excelExportKey 导出excel用的key 用于判断是否能够使用该地址进行导出处理
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer judgeExportRight(String excelExportKey) throws MyDefinitionException {

        if (excelExportKey == null) {
            throw new MyDefinitionException("导出excel必须携带请求密钥");
        }
        //log.info("请求的excel密钥front：{}", excelExportKey);
        /*try {
            excelExportKey = URLDecoder.decode(excelExportKey, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("excel导出请求地址解密失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("excel导出请求地址解密失败");
        }*/
        //log.info("请求的excel密钥：{}", excelExportKey);
        String s = null;
        try {
            s = myEnDecryptionUtil.aesDecrypt(excelExportKey, AES_KEY);
        } catch (Exception e) {
            log.error("aes解析excel内容错误，错误原因:{}", e.toString());
            throw new MyDefinitionException("aes解析excel内容错误");
        }
        Integer activeId = null;
        try {
            JSONObject jsonObject = JSONObject.parseObject(s);
            activeId = jsonObject.getInteger("activeId");
        } catch (Exception e) {
            log.error("解析excel的json内容错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("解析excel的json内容错误");
        }

        Object o = myRedisUtil.get(EXCEL_EXPORT_REDIS_KEY + activeId);
        if (o == null) {
            throw new MyDefinitionException("当前导出链接已过期，请再次获取导出链接！！");
        }
        return activeId;
    }

    /**
     * 通过活动id获取excel导出的地址
     *
     * @param activeId 活动id
     * @return 导出地址
     * @throws MyDefinitionException
     */
    @Override
    public String getExcelExportUrlByActiveId(Integer activeId, Integer userId) throws MyDefinitionException {

        if (activeId == null || userId == null) {
            throw new MyDefinitionException("当前接口要求用户登录且活动指向明确");
        }
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        } catch (Exception e) {
            throw new MyDefinitionException("查询操作错误，" + e.getMessage());
        }
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("想要查询的活动不存在");
        } else {
            /*判断是否为超级管理员 如果是就不做任何判断*/
            if (!administratorsOptionService.isSuperAdministrators(userId)) {
                if (!weixinVoteBase.getCreateSysUserId().equals(userId)) {
                    throw new MyDefinitionException("您不是该活动的管理员");
                }
            }
        }


        String excelExportValue = "";
        for (int i = 0; i < 5; i++) {
            excelExportValue += MyCommonUtil.getUUID();
        }
        long timeMillis = System.currentTimeMillis();
        JSONObject jsonObject = new JSONObject(1);
        jsonObject.put("activeId", activeId);
        jsonObject.put("urlValue", excelExportValue);
        jsonObject.put("timestamp", timeMillis);

        String urlKey = null;
        try {
            urlKey = myEnDecryptionUtil.aesEncrypt(jsonObject.toString(), AES_KEY);
        } catch (Exception e) {
            log.error("aes加密失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("aes加密失败");
        }
        myRedisUtil.set(EXCEL_EXPORT_REDIS_KEY + activeId, timeMillis, 10 * 60);

        try {
            urlKey = URLEncoder.encode(urlKey, "utf-8");
        } catch (UnsupportedEncodingException e) {
            log.error("excel导出请求地址加密失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("excel导出请求地址加密失败");
        }
        return "/data/export/exportExcel?sk=" + urlKey;
    }
}
