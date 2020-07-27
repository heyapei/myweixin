package com.hyp.myweixin.service.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinUserInfoSupplyMapper;
import com.hyp.myweixin.pojo.modal.WeixinUserInfoSupply;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.query.user.supply.AddUserSupplyQuery;
import com.hyp.myweixin.pojo.vo.page.user.UserInfoSupplyDetailVO;
import com.hyp.myweixin.service.UserInfoSupplyService;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.smallwechatapi.WeixinSmallContentDetectionApiService;
import com.hyp.myweixin.utils.MyEntityUtil;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/27 21:00
 * @Description: TODO
 */
@Service
@Slf4j
public class UserInfoSupplyServiceImpl implements UserInfoSupplyService {
    @Autowired
    private WeixinUserInfoSupplyMapper weixinUserInfoSupplyMapper;
    @Autowired
    private WeixinVoteUserService weixinVoteUserService;


    @Autowired
    private WeixinSmallContentDetectionApiService weixinSmallContentDetectionApiService;


    /**
     * 更新微信补充信息
     *
     * @param addUserSupplyQuery 微信小程序提交上来的用户补充信息
     * @return 返回影响的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateWeixinUserInfoSupplyByAddUserSupplyQuery(AddUserSupplyQuery addUserSupplyQuery) throws MyDefinitionException {

        if (addUserSupplyQuery == null) {
            throw new MyDefinitionException("更新用户的数据不可以为空");
        }
        WeixinUserInfoSupply weixinUserInfoSupply = null;
        try {
            weixinUserInfoSupply = selectByUserId(addUserSupplyQuery.getUserId());
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        Boolean aBoolean = null;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getAddress(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("地址内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("地址内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getEmail(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("Email内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("Email内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getPhone(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("手机号内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("手机号内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getRealName(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("姓名内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("姓名内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getWechatNumber(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("微信号内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("微信号内容存在违规内容，请重新输入");
        }

        weixinUserInfoSupply.setUserId(addUserSupplyQuery.getUserId());
        if (StringUtils.isNotBlank(addUserSupplyQuery.getAddress())) {
            weixinUserInfoSupply.setAddress(addUserSupplyQuery.getAddress());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getEmail())) {
            weixinUserInfoSupply.setEmail(addUserSupplyQuery.getEmail());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getPhone())) {
            weixinUserInfoSupply.setPhone(addUserSupplyQuery.getPhone());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getRealName())) {
            weixinUserInfoSupply.setRealName(addUserSupplyQuery.getRealName());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getWechatNumber())) {
            weixinUserInfoSupply.setWechatNumber(addUserSupplyQuery.getWechatNumber());
        }
        if (addUserSupplyQuery.getBirthday() != null) {
            weixinUserInfoSupply.setBirthday(Long.parseLong(MyDateUtil.numberDateFormatToDate(addUserSupplyQuery.getBirthday(), 13)));
        }
        if (addUserSupplyQuery.getGender() != null) {
            weixinUserInfoSupply.setGender(addUserSupplyQuery.getGender());
        }
        weixinUserInfoSupply.setUpdateDate(System.currentTimeMillis());
        Integer integer = null;
        try {
            integer = updateSelectiveByUserId(weixinUserInfoSupply);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return integer;
    }

    /**
     * 通过用户Id查找到用户的补充信息
     *
     * @param userId 用户ID
     * @return 返回主键
     * @throws MyDefinitionException
     */
    @Override
    public UserInfoSupplyDetailVO getUserInfoSupplyDetailVOByUserId(Integer userId) throws MyDefinitionException {

        if (userId == null) {
            throw new MyDefinitionException("请指定需要查询的用户ID");
        }
        WeixinUserInfoSupply weixinUserInfoSupply = null;
        try {
            weixinUserInfoSupply = selectByUserId(userId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        if (weixinUserInfoSupply == null) {
            throw new MyDefinitionException("未能查找到指定用户的补充信息");
        }
        UserInfoSupplyDetailVO userInfoSupplyDetailVO = null;
        try {
            userInfoSupplyDetailVO = MyEntityUtil.entity2VM(weixinUserInfoSupply, UserInfoSupplyDetailVO.class);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("补充数据转视图实体失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("补充数据转视图实体失败");
        }
        return userInfoSupplyDetailVO;
    }

    /**
     * 添加微信补充信息 要求全部数据
     * 该操作只适用于第一次提交数据
     * 不允许重复提交
     *
     * @param addUserSupplyQuery 微信小程序提交上来的用户补充信息
     * @return 返回主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer addWeixinUserInfoSupply(AddUserSupplyQuery addUserSupplyQuery) throws MyDefinitionException {
        if (addUserSupplyQuery == null) {
            throw new MyDefinitionException("用户补充信息不能为空");
        }

        Boolean aBoolean = null;
        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getAddress(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("地址内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("地址内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getEmail(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("Email内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("Email内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getPhone(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("手机号内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("手机号内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getRealName(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("姓名内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("姓名内容存在违规内容，请重新输入");
        }

        try {
            aBoolean = weixinSmallContentDetectionApiService.checkMsgSecCheckApi(
                    addUserSupplyQuery.getWechatNumber(),
                    null);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException("微信号内容违规检查未通过:" + e.getMessage());
        }
        if (aBoolean == null || aBoolean == false) {
            throw new MyDefinitionException("微信号内容存在违规内容，请重新输入");
        }
        
        WeixinUserInfoSupply weixinUserInfoSupply = WeixinUserInfoSupply.init();
        weixinUserInfoSupply.setUserId(addUserSupplyQuery.getUserId());
        if (StringUtils.isNotBlank(addUserSupplyQuery.getAddress())) {
            weixinUserInfoSupply.setAddress(addUserSupplyQuery.getAddress());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getEmail())) {
            weixinUserInfoSupply.setEmail(addUserSupplyQuery.getEmail());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getPhone())) {
            weixinUserInfoSupply.setPhone(addUserSupplyQuery.getPhone());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getRealName())) {
            weixinUserInfoSupply.setRealName(addUserSupplyQuery.getRealName());
        }
        if (StringUtils.isNotBlank(addUserSupplyQuery.getWechatNumber())) {
            weixinUserInfoSupply.setWechatNumber(addUserSupplyQuery.getWechatNumber());
        }
        if (addUserSupplyQuery.getBirthday() != null) {
            weixinUserInfoSupply.setBirthday(Long.parseLong(MyDateUtil.numberDateFormatToDate(addUserSupplyQuery.getBirthday(), 13)));
        }
        if (addUserSupplyQuery.getGender() != null) {
            weixinUserInfoSupply.setGender(addUserSupplyQuery.getGender());
        }

        Integer pkId = null;
        try {
            pkId = addWeixinUserInfoSupply(weixinUserInfoSupply);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }
        return pkId;
    }

    /**
     * 添加微信补充信息 要求全部数据
     *
     * @param weixinUserInfoSupply 微信补充信息全部数据
     * @return 返回主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer addWeixinUserInfoSupply(WeixinUserInfoSupply weixinUserInfoSupply) throws MyDefinitionException {

        if (weixinUserInfoSupply == null) {
            throw new MyDefinitionException("补充信息必须不为空");
        }
        WeixinVoteUser weixinVoteUser = weixinVoteUserService.getUserById(weixinUserInfoSupply.getUserId());
        if (weixinVoteUser == null) {
            throw new MyDefinitionException("没有找到当前用户信息");
        }

        Integer pkId = null;
        try {
            pkId = null;
            int i = weixinUserInfoSupplyMapper.insertUseGeneratedKeys(weixinUserInfoSupply);
            if (i > 0) {
                pkId = weixinUserInfoSupply.getId();
            }
        } catch (Exception e) {
            e.printStackTrace();
            log.error("添加微信补充信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("添加微信补充信息操作过程错误");
        }

        //log.info("主键Id:{}",pkId);

        return pkId;
    }

    /**
     * 按照主键删除数据
     *
     * @param pkId 含主键
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer deleteByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("要查询的数据未指定");
        }

        int i = 0;
        try {
            i = weixinUserInfoSupplyMapper.deleteByPrimaryKey(pkId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按照主键删除用户补充信息操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("按照主键删除用户补充信息操作过程错误");
        }
        return i;
    }

    /**
     * 按照主键更新数据 只更新有值的数据
     *
     * @param weixinUserInfoSupply 要更新的数据（含主键）
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveByPkId(WeixinUserInfoSupply weixinUserInfoSupply) throws MyDefinitionException {
        if (weixinUserInfoSupply == null) {
            throw new MyDefinitionException("补充信息必须不为空");
        }
        int i = 0;
        try {
            i = weixinUserInfoSupplyMapper.updateByPrimaryKeySelective(weixinUserInfoSupply);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按照主键更新有效数据操作过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("按照主键更新有效数据操作过程失败");
        }

        return i;
    }

    /**
     * 按照UserId更新数据 只更新有值的数据
     *
     * @param weixinUserInfoSupply 要更新的数据（含userId）
     * @return 影响行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateSelectiveByUserId(WeixinUserInfoSupply weixinUserInfoSupply) throws MyDefinitionException {

        Example example = new Example(WeixinUserInfoSupply.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", weixinUserInfoSupply.getUserId());

        if (weixinUserInfoSupply == null) {
            throw new MyDefinitionException("补充信息必须不为空");
        }
        int i = 0;
        try {
            i = weixinUserInfoSupplyMapper.updateByExampleSelective(weixinUserInfoSupply, example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按照UserId更新有效数据操作过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("按照UserId更新有效数据操作过程失败");
        }
        return i;
    }

    /**
     * 按照主键查找数据
     *
     * @param pkId 主键
     * @return 数据
     * @throws MyDefinitionException
     */
    @Override
    public WeixinUserInfoSupply selectByPkId(Integer pkId) throws MyDefinitionException {
        if (pkId == null) {
            throw new MyDefinitionException("要查询的数据未指定");
        }
        WeixinUserInfoSupply weixinUserInfoSupply = null;
        try {
            weixinUserInfoSupply = weixinUserInfoSupplyMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按照主键查找数据操作过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("按照主键查找数据操作过程失败");
        }

        return weixinUserInfoSupply;
    }

    /**
     * 按照用户ID查找数据
     *
     * @param userId 用户ID
     * @return 数据
     * @throws MyDefinitionException
     */
    @Override
    public WeixinUserInfoSupply selectByUserId(Integer userId) throws MyDefinitionException {
        if (userId == null) {
            throw new MyDefinitionException("用户ID未指定");
        }
        Example example = new Example(WeixinUserInfoSupply.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("userId", userId);
        WeixinUserInfoSupply weixinUserInfoSupply = null;
        try {
            weixinUserInfoSupply = weixinUserInfoSupplyMapper.selectOneByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("按照用户ID查找数据操作过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("按照用户ID查找数据操作过程失败");
        }
        return weixinUserInfoSupply;
    }
}
