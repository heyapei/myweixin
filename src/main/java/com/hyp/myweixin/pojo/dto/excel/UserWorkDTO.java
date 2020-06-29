package com.hyp.myweixin.pojo.dto.excel;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.metadata.BaseRowModel;
import lombok.Data;

import java.util.Date;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/29 23:39
 * @Description: TODO
 */
@Data
public class UserWorkDTO extends BaseRowModel {

    @ExcelProperty(value = {"选手编号"}, index = 0)
    private String workID;

    @ExcelProperty(value = {"参赛名称"}, index = 1)
    private String workName;

    @ExcelProperty(value = {"参赛介绍"}, index = 2)
    private String workDesc;

    @ExcelProperty(value = {"用户手机"}, index = 3)
    private String userPhone;

    @ExcelProperty(value = {"用户微信"}, index = 4)
    private String userWeixin;

    @ExcelProperty(value = {"状态（0待审核1上线2下线）"}, index = 5)
    private String workStatus;

    @ExcelProperty(value = {"总票数"}, index = 6)
    private String workVoteCount;

    @ExcelProperty(value = {"作品排名"}, index = 7)
    private String workRank;

    @ExcelProperty(value = {"创建时间"}, index = 8)
    private Date createTime;


}
