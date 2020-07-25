package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.service.AdministratorsOptionService;
import com.hyp.myweixin.service.ExcelOptionService;
import com.hyp.myweixin.service.WeixinVoteBaseService;
import com.hyp.myweixin.service.WeixinVoteUserWorkService;
import com.hyp.myweixin.utils.MyCommonUtil;
import com.hyp.myweixin.utils.redis.MyRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 15:57
 * @Description: TODO
 */

@Slf4j
@Service
public class ExcelOptionServiceImpl implements ExcelOptionService {
    private static final String SEMICOLON_SEPARATOR = ";";
    private static final String EXCEL_PORT_KEY = "Active_excel_port_";
    @Autowired
    private MyRedisUtil myRedisUtil;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteUserWorkService weixinVoteUserWorkService;
    @Autowired
    private AdministratorsOptionService administratorsOptionService;

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
        myRedisUtil.set(EXCEL_PORT_KEY + activeId, excelExportValue, 10 * 60);

        /*拼接地址*/
        StringBuilder url = new StringBuilder("?key=");
        url.append(excelExportValue).append("&id=").append(activeId);


        return url.toString();
    }
}
