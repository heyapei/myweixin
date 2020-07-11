package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteUserWorkMapper;
import com.hyp.myweixin.pojo.modal.*;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkSimpleVO;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.dateutil.MyDateUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/14 19:06
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteUserWorkServiceImpl implements WeixinVoteUserWorkService {

    @Autowired
    private WeixinVoteUserWorkMapper weixinVoteUserWorkMapper;
    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;
    @Autowired
    private WeixinVoteUserService weixinVoteUserService;
    @Autowired
    private WeixinVoteConfService weixinVoteConfService;


    /**
     * 判断当前投票的合法性
     *
     * @param weixinVoteUserWork
     * @return
     */
    @Override
    public String judgeVoteLegal(WeixinVoteUserWork weixinVoteUserWork) {
        int voteWorkId = weixinVoteUserWork.getWorkId();
        if (voteWorkId <= 0) {
            return "未发现活动数据ID";
        }

        VoteDetailCompleteVO weixinVoteWorkByUserWorkId = weixinVoteWorkService.getWeixinVoteWorkByUserWorkId(voteWorkId);
        if (weixinVoteWorkByUserWorkId == null) {
            return "未能查找到作品所属活动的具体数据";
        }
        voteWorkId = weixinVoteWorkByUserWorkId.getActiveVoteBaseId();
        // log.info("活动的主键："+voteWorkId);

        WeixinVoteBase weixinVoteBase = weixinVoteBaseService.getWeixinVoteBaseByWorkId(voteWorkId);
        if (weixinVoteBase == null) {
            return "未发现活动数据";
        }
        Date nowDate = new Date();
        Date activeStartTime = weixinVoteBase.getActiveStartTime();
        //log.info("活动开始时间："+activeStartTime);
        Date activeEndTime = weixinVoteBase.getActiveEndTime();
        //log.info("活动结束时间："+activeEndTime);
        /*时间判断 活动是否开始/结束*/
        boolean voteTimeLegal = activeStartTime.before(nowDate);
        if (!voteTimeLegal) {
            return "活动未开始";
        }
        voteTimeLegal = activeEndTime.before(nowDate);
        if (voteTimeLegal) {
            return "活动已结束";
        }

        /*读取配置项*/
        WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(voteWorkId);
        if (weixinVoteConf == null) {
            return "没有发现活动的配置项";
        }
        /*判断是否可以重复投票*/
        Integer activeConfRepeatVote = weixinVoteConf.getActiveConfRepeatVote();
        if (activeConfRepeatVote != null) {


            /*如果activeConfRepeatVote为0标识只允许投票一次
            等于别的标识可以投票多次 具体多少次 按照activeConfVoteType判断*/


            if (activeConfRepeatVote == 0) {
                List<WeixinVoteUserWork> weixinVoteUserWorkS = getWeixinVoteUserWorkS(weixinVoteUserWork.getOpenId(),
                        weixinVoteUserWork.getWorkId());
                if (weixinVoteUserWorkS != null && weixinVoteUserWorkS.size() >= 1) {
                    return "当前活动不允许重复投票";
                }
            } else {

                /*如果等于1 标识 每日投票限制*/
                if (activeConfRepeatVote == 1) {
                    List<WeixinVoteUserWork> weixinVoteUserWorkS =
                            getWeixinVoteUserWorkSByOpenIdTime(weixinVoteUserWork.getOpenId(),
                                    weixinVoteUserWork.getWorkId(), null, null);
                    Integer activeConfVoteType = weixinVoteConf.getActiveConfVoteType();
                    if (activeConfVoteType != null) {
                        if (weixinVoteUserWorkS != null && weixinVoteUserWorkS.size() >= 0) {
                            if (weixinVoteUserWorkS.size() >= activeConfVoteType) {
                                return "当前活动只允许每日投票" + activeConfVoteType + "次";
                            }
                        }
                    }
                } else {
                    List<WeixinVoteUserWork> weixinVoteUserWorkS = getWeixinVoteUserWorkS(weixinVoteUserWork.getOpenId(),
                            weixinVoteUserWork.getWorkId());
                    Integer activeConfVoteType = weixinVoteConf.getActiveConfVoteType();
                    if (weixinVoteUserWorkS != null && weixinVoteUserWorkS.size() >= 0) {
                        if (weixinVoteUserWorkS.size() >= activeConfVoteType) {
                            return "当前活动最多允许重复投票" + activeConfVoteType + "次数";
                        }
                    }
                }
            }
        }

        /*判断是否限制性别*/
        String activeConfSex = weixinVoteConf.getActiveConfSex();
        if (StringUtils.isNotBlank(activeConfSex) && !activeConfSex.equalsIgnoreCase("无")) {
            String gender = weixinVoteUserWork.getGender();
            if (activeConfSex.equalsIgnoreCase("男")) {
                if (!gender.equalsIgnoreCase("1")) {
                    return "当前活动只允许男性用户投票";
                }
            } else if (activeConfSex.equalsIgnoreCase("女")) {
                if (!gender.equalsIgnoreCase("2")) {
                    return "当前活动只允许女性用户投票";
                }
            }
        }


        /*判断是否需要地区限制  2020年7月1日 FIXME　目前没有精度更高的数据可以使用 所以暂时不适用 不能以微信用户数据为准*/
        String activeConfRegion = weixinVoteConf.getActiveConfRegion();
        if (StringUtils.isNotBlank(activeConfRegion) && !activeConfRegion.equalsIgnoreCase("无")) {
            String city = weixinVoteUserWork.getCity();
        }


        return "";
    }

    /**
     * 查询用户对某个作品的投票数据
     *
     * @param openId
     * @param userWorkId
     * @return
     */
    @Override
    public List<WeixinVoteUserWork> getWeixinVoteUserWorkS(String openId, Integer userWorkId) {
        Example example = new Example(WeixinVoteUserWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId", openId);
        criteria.andEqualTo("workId", userWorkId);
        List<WeixinVoteUserWork> weixinVoteUserWorks = null;
        try {
            weixinVoteUserWorks = weixinVoteUserWorkMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过openId以及workId获取用户的投票信息错误，错误原因：{}", e.toString());
        }
        return weixinVoteUserWorks;
    }

    /**
     * 按照时间范围查询用户对某个作品的投票数据 如果没有传入开始结束/结束时间 则直接取今天的时间开始/结束
     *
     * @param openId     用户唯一值
     * @param userWorkId 用户作品ID
     * @param startTime
     * @param endTime
     * @return
     */
    @Override
    public List<WeixinVoteUserWork> getWeixinVoteUserWorkSByOpenIdTime(String openId, Integer userWorkId, Date startTime, Date endTime) {
        Example example = new Example(WeixinVoteUserWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId", openId);
        criteria.andEqualTo("workId", userWorkId);

        if (startTime == null) {
            startTime = MyDateUtil.getStartTime(new Date());
        }
        if (endTime == null) {
            endTime = MyDateUtil.getEndTime(new Date());
        }

        criteria.andBetween("createTime", startTime, endTime);

        List<WeixinVoteUserWork> weixinVoteUserWorks = null;
        try {
            weixinVoteUserWorks = weixinVoteUserWorkMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过openId以及workId获取用户在时间范围内的投票信息错误，错误原因：{}", e.toString());
        }
        return weixinVoteUserWorks;
    }


    /**
     * 通过openId获取获取用户的信息
     *
     * @param openId     用户唯一值
     * @param userWorkId 用户作品ID
     * @return
     */
    @Override
    public List<WeixinVoteUserWork> getWeixinVoteUserWorkSByOpenId(String openId, Integer userWorkId) {
        Example example = new Example(WeixinVoteUserWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("openId", openId);
        criteria.andEqualTo("workId", userWorkId);
        List<WeixinVoteUserWork> weixinVoteUserWorks = null;
        try {
            weixinVoteUserWorks = weixinVoteUserWorkMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过openId获取获取用户的信息错误，错误原因：{}", e.toString());
        }
        return weixinVoteUserWorks;
    }


    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @return
     */
    @Override
    @Transactional(rollbackFor = Exception.class)
    public int addUserVote(WeixinVoteUserWork weixinVoteUserWork) {
        if (weixinVoteUserWork == null) {
            throw new MyDefinitionException(500, "服务类发现参数错误");
        }
        Integer workId = weixinVoteUserWork.getWorkId();
        /**
         * 先查询是不是有这个作品
         */
        WeixinVoteWork voteWorkByUserWorkId = weixinVoteWorkService.getVoteWorkByUserWorkId(workId);
        if (voteWorkByUserWorkId == null) {
            log.error("未能通过workId查询到作品，查询的workId：{}", workId);
            throw new MyDefinitionException(404, "未能通过workId查询到作品");
        }

        /**
         * 更新活动的投票数
         */
        int i = weixinVoteBaseService.updateVoteBaseVoteNum(voteWorkByUserWorkId.getActiveVoteBaseId());
        if (i <= 0) {
            log.error("更新活动的投票数量错误，查询的workId：{}", voteWorkByUserWorkId.getActiveVoteBaseId());
            throw new MyDefinitionException(404, "未能通过活动ID查询到活动的数据，查询的活动ID：" + voteWorkByUserWorkId.getActiveVoteBaseId());
        }
        /**
         * 添加作品的被投票数量
         */
        int i1 = weixinVoteWorkService.updateVoteWorkVoteNum(workId);
        if (i1 <= 0) {
            log.error("更新作品的投票数量错误，查询的userWorkId：{}", workId);
            throw new MyDefinitionException(404, "未能通过作品ID查询到用户作品的数据，查询的作品ID：" + workId);
        }

        /**
         * 添加投票记录
         */
        try {
            weixinVoteUserWorkMapper.insertUseGeneratedKeys(weixinVoteUserWork);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存用户投票信息错误，错误原因：{}", e.toString());
        }

        return weixinVoteUserWork.getId();
    }

    /**
     * 保存用户对某个作品的投票记录
     *
     * @param workId
     * @return
     */
    @Override
    public List<WeixinVoteUserWork> getWeixinVoteUserWorkByWorkId(Integer workId) {

        Example example = new Example(WeixinVoteUserWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("workId", workId);
        List<WeixinVoteUserWork> weixinVoteUserWorks = weixinVoteUserWorkMapper.selectByExample(example);
        return weixinVoteUserWorks;
    }

    /**
     * 保存用户对某个作品的投票记录
     *
     * @param weixinVoteUserWork
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteUserWorkByPage(WeixinVoteUserWork weixinVoteUserWork, PageInfo pageInfo) {


        Example example = new Example(WeixinVoteUserWork.class);
        example.orderBy("createTime").desc();
        Example.Criteria criteria = example.createCriteria();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteWork用于条件查询
        if (weixinVoteUserWork != null) {
            criteria.andEqualTo("workId", weixinVoteUserWork.getWorkId());
        }
        List<WeixinVoteUserWork> weixinVoteWorks = weixinVoteUserWorkMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteWorks);

        return pageInfo;
    }

    /**
     * 获取投票人的头像信息
     *
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getWeixinVoteUserWorkSimpleVOByPageInfo(PageInfo pageInfo) {

        List<WeixinVoteUserWork> list = pageInfo.getList();
        List<WeixinVoteUserWorkSimpleVO> list1 = new ArrayList<>();
        for (WeixinVoteUserWork voteUserWork : list) {
            String openId = voteUserWork.getOpenId();
            WeixinVoteUser userByOpenId = weixinVoteUserService.getUserByOpenId(openId);
            if (userByOpenId != null) {
                WeixinVoteUserWorkSimpleVO weixinVoteUserWorkSimpleVO = new WeixinVoteUserWorkSimpleVO();
                weixinVoteUserWorkSimpleVO.setAvatarUrl(voteUserWork.getAvatarUrl());
                list1.add(weixinVoteUserWorkSimpleVO);
            }
        }
        pageInfo.setList(list1);
        return pageInfo;
    }
}
