package com.hyp.myweixin.pojo.vo.excel.active;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020年7月25日 18点29分
 * @Description: TODO 活动下作品数据导出EXCEL
 */

@Data
public class ActiveVoteWorkExcelExportVO extends BaseRowModel implements Serializable {
    public static final long serialVersionUID = 1L;

    @ExcelProperty(value = {"作品序号"}, index = 0)
    private Integer voteWorkOr;

    @ExcelProperty(value = {"姓名"}, index = 1)
    private String voteWorkUserName;

    @ExcelProperty(value = {"手机号"}, index = 2)
    private String voteWorkUserPhone;

    @ExcelProperty(value = {"微信号"}, index = 3)
    private String voteWorkUserWeixin;

    @ExcelProperty(value = {"票数"}, index = 4)
    private Integer voteWorkCountNum;
    @ExcelProperty(value = {"浏览次数"}, index = 5)
    private Integer voteWorkCountViewNum;
    @ExcelProperty(value = {"作品状态"}, index = 6)
    private String voteWorkStatusMsg;

    @ExcelProperty(value = {"作品名称"}, index = 7)
    private String voteWorkName;
    @ExcelProperty(value = {"作品描述"}, index = 8)
    private String voteWorkDesc;
    @ExcelProperty(value = {"作品提交时间"}, index = 9)
    private Date voteWorkCreateTime;

    private Integer voteWorkStatus;


}

