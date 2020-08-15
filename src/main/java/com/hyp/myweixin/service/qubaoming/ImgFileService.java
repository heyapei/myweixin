package com.hyp.myweixin.service.qubaoming;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.pojo.vo.result.Result;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/8/16 0:00
 * @Description: TODO
 */
public interface ImgFileService {

    /**
     * 保存图片资源
     *
     * @param file
     * @param type
     * @return
     * @throws MyDefinitionException
     */
    Result saveSingleRes(MultipartFile file, String type) throws MyDefinitionException;

}
