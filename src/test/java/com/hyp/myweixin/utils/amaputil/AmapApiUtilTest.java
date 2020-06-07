package com.hyp.myweixin.utils.amaputil;

import com.hyp.myweixin.pojo.dto.AmapIpToAddressDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 11:32
 * @Description: TODO
 */
@RunWith(SpringRunner.class)
@SpringBootTest
@Slf4j
public class AmapApiUtilTest {


    @Autowired
    private AmapApiUtil amapApiUtil;


    @Test
    public void getIpPosition() {

        AmapIpToAddressDTO ipPosition = amapApiUtil.getIpPositionNoAsync("42.235.189.27");
        System.out.println("普通地址信息：" + ipPosition);
        String geocodeByIpAddressGeneral = amapApiUtil.getGeocodeByIpAddressGeneralNoAsync(ipPosition.getRectangle());
        System.out.println("街道信息：" + geocodeByIpAddressGeneral);

        //主线程sleep() 的确是方便了 异步方法的调用
        try {
            Thread.sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
}