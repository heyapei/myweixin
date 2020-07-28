package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteUserWorkMapper;
import com.hyp.myweixin.pojo.modal.*;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkSimpleVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.UserUploadRightVO;
import com.hyp.myweixin.pojo.vo.page.activeeditor.UserWorkDetailVO;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.MyErrorList;
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
    @Autowired
    private AdministratorsOptionService administratorsOptionService;
    @Autowired
    private MyErrorList myErrorList;


    private static final String SEMICOLON_SEPARATOR = ";";


    /**
     * 通过主键查询用户投票信息
     *
     * @param pkId 主键ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public WeixinVoteUserWork getWeixinVoteUserWorkByPkId(Integer pkId) throws MyDefinitionException {

        WeixinVoteUserWork weixinVoteUserWork = null;
        try {
            weixinVoteUserWork = weixinVoteUserWorkMapper.selectByPrimaryKey(pkId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过主键查询用户投票信息错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("通过主键查询用户投票信息错误");
        }


        return weixinVoteUserWork;
    }

    /**
     * 获取用户点赞的作品信息
     *
     * @param userId             用户ID
     * @param distinctUserWorkId 是否按照作品ID筛出 true筛出 false不筛除
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public List<WeixinVoteUserWork> getUserJoinActiveWorkList(Integer userId, boolean distinctUserWorkId) throws MyDefinitionException {

        if (userId == null) {
            throw new MyDefinitionException("用户ID必填");
        }

        WeixinVoteUser weixinVoteUser = weixinVoteUserService.getUserById(userId);

        if (weixinVoteUser == null) {
            throw new MyDefinitionException("当前查询的用户不存在");
        }

        List<Integer> userVoteWorkId = null;
        try {
            userVoteWorkId = weixinVoteUserWorkMapper.getUserVoteWorkId(weixinVoteUser.getOpenId(), distinctUserWorkId);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户投票的作品ID操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询用户投票的作品ID操作过程错误");
        }

        List<WeixinVoteUserWork> weixinVoteUserWorkList = new ArrayList<>();
        for (Integer userWorkId : userVoteWorkId) {
            WeixinVoteUserWork weixinVoteUserWorkByPkId = getWeixinVoteUserWorkByPkId(userWorkId);
            weixinVoteUserWorkList.add(weixinVoteUserWorkByPkId);
        }
        return weixinVoteUserWorkList;
    }

    /**
     * 用户上传作品前判断需要什么信息以及是否允许上传
     *
     * @param activeId 活动ID
     * @param userId   用户ID
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public UserUploadRightVO judgeUserUploadRight(Integer userId, Integer activeId) throws MyDefinitionException {
        if (activeId == null) {
            throw new MyDefinitionException("参数不可以为空");
        }
        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeId);
        if (weixinVoteBaseByWorkId == null) {
            throw new MyDefinitionException("未能查找到活动信息");
        }
        WeixinVoteConf weixinVoteConfByVoteWorkId = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(weixinVoteBaseByWorkId.getId());
        if (weixinVoteConfByVoteWorkId == null) {
            throw new MyDefinitionException("未能查找到活动配置信息");
        }

        WeixinVoteUser weixinVoteUser = weixinVoteUserService.getUserById(userId);
        if (weixinVoteUser == null) {
            throw new MyDefinitionException("未能查找到用户信息");
        }


        UserUploadRightVO userUploadRightVO = new UserUploadRightVO();
        userUploadRightVO.setMessage("允许上传");
        userUploadRightVO.setAllowUploadResult(true);
        Integer activeConfSignUp = weixinVoteConfByVoteWorkId.getActiveConfSignUp();
        if (activeConfSignUp.equals(WeixinVoteConf.ActiveConfSignUpEnum.CAN_SIGN_UP.getCode())) {
            userUploadRightVO.setAllowUploadConf(true);
        } else if (activeConfSignUp.equals(WeixinVoteConf.ActiveConfSignUpEnum.CANT_SIGN_UP.getCode())) {
            userUploadRightVO.setAllowUploadConf(false);
            userUploadRightVO.setAllowUploadResult(false);
            myErrorList.add("当前活动不允许自主上传作品，不允许上传");
        }
        userUploadRightVO.setActiveStartTime(weixinVoteBaseByWorkId.getActiveStartTime());
        userUploadRightVO.setActiveEndTime(weixinVoteBaseByWorkId.getActiveEndTime());

        userUploadRightVO.setAllowUploadStartTimeConf(weixinVoteConfByVoteWorkId.getActiveUploadStartTime());
        userUploadRightVO.setAllowUploadEndTimeConf(weixinVoteConfByVoteWorkId.getActiveUploadEndTime());


        List<WeixinVoteWork> weiXinVoteWorkListByUserId = null;
        try {
            weiXinVoteWorkListByUserId = weixinVoteWorkService.getWeiXinVoteWorkListByUserId(userId, activeId);
        } catch (MyDefinitionException e) {
            throw new MyDefinitionException(e.getMessage());
        }

        userUploadRightVO.setHasUploaded(false);
        /*如果是创建人则不进行是否有活动的判断*/
        if (weiXinVoteWorkListByUserId != null) {
            if (weiXinVoteWorkListByUserId.size() >= 1 &&
                    !weiXinVoteWorkListByUserId.get(0).getVoteWorkStatus().equals(WeixinVoteWork.VoteWorkStatusEnum.OFFLINE.getCode())) {
                userUploadRightVO.setHasUploaded(true);
                userUploadRightVO.setAllowUploadResult(false);
                //userUploadRightVO.setMessage("该活动中已有作品，不允许上传");
                myErrorList.add("该活动中已有作品，不允许上传");
            }
        }

        Integer activeConfNeedWeixin = weixinVoteConfByVoteWorkId.getActiveConfNeedWeixin();
        if (activeConfNeedWeixin.equals(WeixinVoteConf.ActiveConfNeedWeixinEnum.NEED_WEIXIN.getCode())) {
            userUploadRightVO.setNeedUserWeixin(true);
        } else if (activeConfNeedWeixin.equals(WeixinVoteConf.ActiveConfNeedWeixinEnum.NOT_NEED_WEIXIN.getCode())) {
            userUploadRightVO.setNeedUserWeixin(false);
        }
        Integer activeConfNeedPhone = weixinVoteConfByVoteWorkId.getActiveConfNeedPhone();
        if (activeConfNeedPhone.equals(WeixinVoteConf.ActiveConfNeedPhoneEnum.NEED_PHONE.getCode())) {
            userUploadRightVO.setNeedUserPhone(true);
        } else if (activeConfNeedPhone.equals(WeixinVoteConf.ActiveConfNeedPhoneEnum.NOT_NEED_PHONE.getCode())) {
            userUploadRightVO.setNeedUserPhone(false);
        }


        Date nowDate = new Date();
        if (!userUploadRightVO.getAllowUploadStartTimeConf().before(nowDate)) {
            // userUploadRightVO.setMessage("未到作品上传开始时间，不允许上传");
            myErrorList.add("未到作品上传开始时间，不允许上传");
            userUploadRightVO.setAllowUploadResult(false);
        } else if (userUploadRightVO.getAllowUploadEndTimeConf().before(nowDate)) {
            //userUploadRightVO.setMessage("作品上传结束时间已到，不允许上传");
            myErrorList.add("作品上传结束时间已结束，不允许上传");
            userUploadRightVO.setAllowUploadResult(false);
        }


        if (weixinVoteUser.getEnable().equals(WeixinVoteUser.ENABLEENUM.UN_ENABLE.getCode())) {
            //userUploadRightVO.setMessage("用户已被禁用，不允许上传");
            myErrorList.add("用户已被禁用，不允许上传");
            userUploadRightVO.setAllowUploadResult(false);
        }

        if (myErrorList.hasErrors()) {
            userUploadRightVO.setMessage(myErrorList.toPlainString());
        }

        return userUploadRightVO;
    }

    /**
     * 管理员查看作品详情
     *
     * @param userId
     * @param userWorkId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public UserWorkDetailVO getUserWorkDetailByWorkId(Integer userId, Integer userWorkId) throws MyDefinitionException {

        if (userId == null || userWorkId == null) {
            throw new MyDefinitionException("参数不能为空");
        }

        WeixinVoteWork weixinVoteWork = weixinVoteWorkService.getVoteWorkByUserWorkId(userWorkId);
        if (weixinVoteWork == null) {
            throw new MyDefinitionException("未发现作品数据");
        }
        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(weixinVoteWork.getActiveVoteBaseId());
        if (weixinVoteBaseByWorkId == null) {
            throw new MyDefinitionException("未找到对应的活动数据");
        }
        if (!administratorsOptionService.isSuperAdministrators(userId)) {
            if (!weixinVoteBaseByWorkId.getCreateSysUserId().equals(userId)) {
                throw new MyDefinitionException("您不是当前作品的管理员");
            }
        }
        UserWorkDetailVO userWorkDetailVO = new UserWorkDetailVO();

        userWorkDetailVO.setUserPhone(weixinVoteWork.getVoteWorkUserPhone());
        userWorkDetailVO.setUserWeixin(weixinVoteWork.getVoteWorkUserWeixin());
        userWorkDetailVO.setUserWorkDesc(weixinVoteWork.getVoteWorkDesc());
        userWorkDetailVO.setUserWorkId(weixinVoteWork.getId());
        userWorkDetailVO.setUserWorkName(weixinVoteWork.getVoteWorkName());
        userWorkDetailVO.setUserWorkOr(weixinVoteWork.getVoteWorkOr());
        String voteWorkImg = weixinVoteWork.getVoteWorkImg();
        if (voteWorkImg != null && voteWorkImg.contains(SEMICOLON_SEPARATOR)) {
            userWorkDetailVO.setUserWorkImgS(voteWorkImg.split(SEMICOLON_SEPARATOR));
        } else {
            userWorkDetailVO.setUserWorkImgS(new String[0]);
        }
        return userWorkDetailVO;
    }

    /**
     * 获取剩余票数
     *
     * @param baseId
     * @param openId
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Integer getRemainderByOpenIdTime(Integer baseId, String openId) throws MyDefinitionException {

        log.info("当前请求参数：{}，{}", baseId, openId);
        try {
            WeixinVoteConf weixinVoteConf = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(baseId);
            WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(baseId);

            String activeConfVoteType = weixinVoteConf.getActiveConfVoteType();
            String[] split = activeConfVoteType.split(";");
            Integer activeVoteType = Integer.parseInt(split[0]);
            Integer activeConfVoteTypeNum = Integer.parseInt(split[1]);

            if (activeVoteType == 1) {
                Integer weixinVoteUserWorkNumByOpenIdTime =
                        getWeixinVoteUserWorkNumByOpenIdTime(baseId,
                                openId, null,
                                MyDateUtil.getStartTime(new Date()), MyDateUtil.getEndTime(new Date()));
                return activeConfVoteTypeNum - weixinVoteUserWorkNumByOpenIdTime;
            } else if (activeVoteType == 2) {
                Integer weixinVoteUserWorkNumByOpenIdTime =
                        getWeixinVoteUserWorkNumByOpenIdTime(baseId,
                                openId, null,
                                MyDateUtil.getStartTime(weixinVoteBaseByWorkId.getActiveStartTime()), MyDateUtil.getEndTime(weixinVoteBaseByWorkId.getActiveEndTime()));
                return activeConfVoteTypeNum - weixinVoteUserWorkNumByOpenIdTime;
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
            return null;
        } catch (MyDefinitionException e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }

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

        if (weixinVoteBase.getStatus() != null) {
            if (!weixinVoteBase.getStatus().equals(WeixinVoteBase.ActiveStatusEnum.ONLINE.getCode())) {
                if (weixinVoteBase.getStatus().equals(WeixinVoteBase.ActiveStatusEnum.OFFLINE.getCode())) {
                    return "活动已下线";
                } else if (weixinVoteBase.getStatus().equals(WeixinVoteBase.ActiveStatusEnum.UN_COMPLETE.getCode())) {
                    return "活动未完成";
                } else if (weixinVoteBase.getStatus().equals(WeixinVoteBase.ActiveStatusEnum.END.getCode())) {
                    return "活动已结束";
                } else if (weixinVoteBase.getStatus().equals(WeixinVoteBase.ActiveStatusEnum.PAUSE.getCode())) {
                    return "活动已暂停";
                } else if (weixinVoteBase.getStatus().equals(WeixinVoteBase.ActiveStatusEnum.UN_VERIFY.getCode())) {
                    return "活动待审核";
                }
            }
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

            log.info("判断是否允许重复投票");


            /*如果activeConfRepeatVote为0标识只允许投票一次
            等于别的标识可以投票多次 具体多少次 按照activeConfVoteType判断*/
            String activeConfVoteType = weixinVoteConf.getActiveConfVoteType();
            String[] split = activeConfVoteType.split(";");
            Integer activeVoteType = Integer.parseInt(split[0]);
            Integer activeConfVoteTypeNum = Integer.parseInt(split[1]);


            if (activeConfRepeatVote == 0) {
                //log.info("不允许");
                //log.info("不允许{}；{}",activeVoteType,activeConfVoteTypeNum);
                /*List<WeixinVoteUserWork> weixinVoteUserWorkS = getWeixinVoteUserWorkS(weixinVoteUserWork.getOpenId(),
                        weixinVoteUserWork.getWorkId());
                if (weixinVoteUserWorkS != null && weixinVoteUserWorkS.size() >= 1) {
                    return "当前活动不允许重复投票";
                }*/

                /**
                 * 因为不允许重复投票所以这部分可以直接先做一下是否已经投票了
                 *
                 */
                List<WeixinVoteUserWork> weixinVoteUserWorkS = getWeixinVoteUserWorkS(weixinVoteUserWork.getOpenId(),
                        weixinVoteUserWork.getWorkId());
                if (weixinVoteUserWorkS != null && weixinVoteUserWorkS.size() >= 1) {
                    Integer remainder = 0;
                    if (activeConfVoteTypeNum - weixinVoteUserWorkS.size() >= 0) {
                        remainder = activeConfVoteTypeNum - weixinVoteUserWorkS.size();
                    }
                    if (activeVoteType == 1) {
                        // 是1的话标识每天
                        return "当前活动不允许重复对作品进行投票，您今天还可以投票：" + remainder + "次";
                    } else if (activeVoteType == 2) {
                        // 是2的话标识总共
                        return "当前活动不允许重复对作品进行投票，您还可以投票：" + remainder + "次";
                    }

                }


                /*如果还能到这里说明用户还没有投票 那这里就要判断用户是否还有票*/
                if (activeVoteType == 1) {
                    /*是1说明是每天*/
                    Integer weixinVoteUserWorkNumByOpenIdTime =
                            getWeixinVoteUserWorkNumByOpenIdTime(weixinVoteBase.getId(),
                                    weixinVoteUserWork.getOpenId(), null,
                                    MyDateUtil.getStartTime(new Date()), MyDateUtil.getEndTime(new Date()));
                    if (weixinVoteUserWorkNumByOpenIdTime != null && weixinVoteUserWorkNumByOpenIdTime > 0) {
                        if (weixinVoteUserWorkNumByOpenIdTime >= activeConfVoteTypeNum) {
                            return "当前活动不允许重复对选手投票且每人每天限制投票数为：" + activeConfVoteTypeNum + "票，您已用完投票次数";
                        }
                    }
                } else if (activeVoteType == 2) {
                    Integer weixinVoteUserWorkNumByOpenIdTime =
                            getWeixinVoteUserWorkNumByOpenIdTime(weixinVoteBase.getId(),
                                    weixinVoteUserWork.getOpenId(), null,
                                    weixinVoteBase.getActiveStartTime(), weixinVoteBase.getActiveEndTime());
                    if (weixinVoteUserWorkNumByOpenIdTime != null && weixinVoteUserWorkNumByOpenIdTime > 0) {
                        if (weixinVoteUserWorkNumByOpenIdTime >= activeConfVoteTypeNum) {
                            return "当前活动不允许重复对选手投票且每人限制投票总数为：" + activeConfVoteTypeNum + "票，您已用完投票次数";
                        }
                    }
                }
            } else if (activeConfRepeatVote == 1) {

                log.info("允许{}；{}", activeVoteType, activeConfVoteTypeNum);





                /*这里是允许重复投票的*/
                if (activeVoteType == 1) {
                    log.info("允许1");
                    /*是1说明是每天*/
                    Integer weixinVoteUserWorkNumByOpenIdTime =
                            getWeixinVoteUserWorkNumByOpenIdTime(weixinVoteBase.getId(),
                                    weixinVoteUserWork.getOpenId(), null,
                                    MyDateUtil.getStartTime(new Date()), MyDateUtil.getEndTime(new Date()));

                    log.info("允许1{};{}", weixinVoteUserWorkNumByOpenIdTime, weixinVoteUserWorkNumByOpenIdTime);
                    if (weixinVoteUserWorkNumByOpenIdTime != null && weixinVoteUserWorkNumByOpenIdTime > 0) {
                        if (weixinVoteUserWorkNumByOpenIdTime >= activeConfVoteTypeNum) {
                            return "当前活动允许重复对选手投票但每人每天限制投票数为：" + activeConfVoteTypeNum + "票，您已用完投票次数";
                        }
                    }
                } else if (activeVoteType == 2) {


                    log.info("允许2");
                    /*如果是2说明是总共*/
                    Integer weixinVoteUserWorkNumByOpenIdTime =
                            getWeixinVoteUserWorkNumByOpenIdTime(weixinVoteBase.getId(),
                                    weixinVoteUserWork.getOpenId(), null,
                                    weixinVoteBase.getActiveStartTime(), weixinVoteBase.getActiveEndTime());
                    log.info("允许2{};{}", weixinVoteUserWorkNumByOpenIdTime, weixinVoteUserWorkNumByOpenIdTime);
                    if (weixinVoteUserWorkNumByOpenIdTime != null && weixinVoteUserWorkNumByOpenIdTime > 0) {
                        if (weixinVoteUserWorkNumByOpenIdTime >= activeConfVoteTypeNum) {
                            return "当前活动每天允许重复对选手投票但每人限制投票总数为：" + activeConfVoteTypeNum + "票，您已用完投票次数";
                        }
                    }
                }

                /*如果等于1 标识 每日投票限制*/
                /*if (activeConfRepeatVote == 1) {
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
                }*/


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
     * 通过活动基础表 用户openId 查询用户在这个活动中的投票情况 有时间范围
     *
     * @param baseId    活动ID
     * @param openId    用户标识
     * @param workId    活动ID
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 在时间范围内容 某个用户在一定时间范围内的投票总数
     * @throws MyDefinitionException
     */
    @Override
    public Integer getWeixinVoteUserWorkNumByOpenIdTime(Integer baseId, String openId, Integer workId, Date startTime, Date endTime) throws MyDefinitionException {
        Integer weixinVoteUserWorkNumByOpenIdTime = null;
        try {
            weixinVoteUserWorkNumByOpenIdTime = weixinVoteUserWorkMapper.getWeixinVoteUserWorkNumByOpenIdTime(baseId, openId, workId, startTime, endTime);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("通过活动基础表用户openId查询用户在这个活动时间范围中的投票情况操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("通过活动基础表用户openId查询用户在这个活动时间范围中的投票情况操作过程错误");
        }
        return weixinVoteUserWorkNumByOpenIdTime;
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
     * 分页获取作品的投票信息
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
