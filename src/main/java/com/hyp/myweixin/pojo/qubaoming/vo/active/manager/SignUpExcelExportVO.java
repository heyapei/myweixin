package com.hyp.myweixin.pojo.qubaoming.vo.active.manager;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.converters.string.StringImageConverter;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.io.File;
import java.io.Serializable;
import java.net.URL;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020年7月25日 18点29分
 * @Description: TODO 活动下作品数据导出EXCEL
 */

@Data
public class SignUpExcelExportVO extends BaseRowModel implements Serializable {
    public static final long serialVersionUID = 1L;


    @ExcelProperty(value = {"姓名"}, index = 0)
    private String name;

    @ExcelProperty(value = {"手机号"}, index = 1)
    private String phone;

    @ExcelProperty(value = {"性别"}, index = 2)
    private String gender;

    @ExcelProperty(value = {"性别"}, index = 3)
    private String age;

    @ExcelProperty(value = {"头像"}, index = 4)
    private URL avatar;

    @ExcelProperty(value = {"报名时间"}, index = 5)
    private Date createTime;


}

