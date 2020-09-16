package com.hyp.myweixin.service;

import com.alibaba.fastjson.JSONObject;
import com.hyp.myweixin.pojo.vo.excel.active.ActiveVoteWorkExcelExportVO;
import com.hyp.myweixin.utils.MyEnDecryptionUtil;
import com.hyp.myweixin.utils.redis.MyRedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/25 17:30
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class QuBaoMingExcelOptionServiceTest {

    @Autowired
    private ExcelOptionService excelOptionService;
    @Autowired
    private MyRedisUtil myRedisUtil;
    @Autowired
    private MyEnDecryptionUtil myEnDecryptionUtil;

    @Test
    public void testExcelVO() {

        List<ActiveVoteWorkExcelExportVO> activeVoteWorkExcelExportVOS = excelOptionService.exportActiveVoteWorkExcelExportVOByActiveId(90);
        log.info("查询结果：{}", activeVoteWorkExcelExportVOS.toString());
    }

    @Test
    public void getExcelExportUrlByActiveId() throws NoSuchPaddingException, UnsupportedEncodingException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, InvalidKeyException {
        String excelExportUrlByActiveId = excelOptionService.getExcelExportUrlByActiveId(89, 25);
        log.info("查询结果：{}", excelExportUrlByActiveId);
        String rsaKey = "33969c786f086d2716bcc14bb4bec567a1036d1396cc2898d4603180c014dc8c";
        Object o = myRedisUtil.get("active_excel_port_" + 89);
        log.info("redis查询结果：" + String.valueOf(o));

        String s = myEnDecryptionUtil.aesDecrypt(excelExportUrlByActiveId, rsaKey);
        log.info("aes解密结果：{}", s);
        log.info("aes解密结果JSON：{}", JSONObject.parseObject(s));

    }
}