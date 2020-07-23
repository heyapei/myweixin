package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteWorkMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteBase;
import com.hyp.myweixin.pojo.modal.WeixinVoteConf;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.query.voteuserwork.ActiveUserWorkQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.SaveVoteUserQuery;
import com.hyp.myweixin.pojo.query.voteuserwork.UpdateUserWorkStatusQuery;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailSimpleVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkDiffVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteWorkSimpleVO;
import com.hyp.myweixin.pojo.vo.result.Result;
import com.hyp.myweixin.service.*;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/8 17:36
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteWorkServiceImpl implements WeixinVoteWorkService {

    @Autowired
    private WeixinVoteWorkMapper weixinVoteWorkMapper;

    @Autowired
    private WeixinVoteUserService weixinVoteUserService;

    @Autowired
    private WeixinVoteBaseService weixinVoteBaseService;

    @Autowired
    private WeixinVoteConfService weixinVoteConfService;

    private static final String SEMICOLON_SEPARATOR = ";";


    @Autowired
    private AdministratorsOptionService administratorsOptionService;


    /**
     * 更新作品的状态
     *
     * @param updateUserWorkStatusQuery
     * @return 返回更新的行数
     * @throws MyDefinitionException
     */
    @Override
    public Integer updateUserWorkStatus(UpdateUserWorkStatusQuery updateUserWorkStatusQuery) throws MyDefinitionException {

        WeixinVoteWork voteWorkByUserWorkId = getVoteWorkByUserWorkId(updateUserWorkStatusQuery.getUserWorkId());

        if (voteWorkByUserWorkId == null) {
            throw new MyDefinitionException("没有找到当前作品信息");
        }
        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(voteWorkByUserWorkId.getActiveVoteBaseId());
        if (weixinVoteBaseByWorkId == null) {
            throw new MyDefinitionException("没有找到当前活动信息");
        }

        /*判断是否为超级管理员 如果是就不做任何判断*/
        if (!administratorsOptionService.isSuperAdministrators(updateUserWorkStatusQuery.getUserId())) {
            if (!weixinVoteBaseByWorkId.getCreateSysUserId().equals(updateUserWorkStatusQuery.getUserId())) {
                throw new MyDefinitionException("您不是当前活动的管理员");
            }
        }


        if (voteWorkByUserWorkId.getVoteWorkStatus().equals(updateUserWorkStatusQuery.getWorkStatus())) {
            throw new MyDefinitionException("无需重复更改当前作品的状态");
        }

        voteWorkByUserWorkId.setVoteWorkStatus(updateUserWorkStatusQuery.getWorkStatus());
        Integer rowAffect = null;
        try {
            int i = weixinVoteWorkMapper.updateByPrimaryKeySelective(voteWorkByUserWorkId);
            if (i > 0) {
                rowAffect = i;
            }
        } catch (Exception e) {
            log.error("更新作品状态操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("更新作品状态操作过程错");
        }
        return rowAffect;
    }

    /**
     * 通过查询条件获取当前活动下面的数据 当然分页查询
     *
     * @param activeUserWorkQuery 活动下作品的数据
     * @return 作品列表
     * @throws MyDefinitionException
     */
    @Override
    public PageInfo<WeixinVoteWork> getUserWorkListByTypePage(ActiveUserWorkQuery activeUserWorkQuery) throws MyDefinitionException {

        if (activeUserWorkQuery == null) {
            throw new MyDefinitionException("查询参数不能为空");
        }

        WeixinVoteBase weixinVoteBaseByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(activeUserWorkQuery.getActiveId());
        if (weixinVoteBaseByWorkId == null) {
            throw new MyDefinitionException("未发现当前活动项");
        }

        /*判断是否为超级管理员 如果是就不做任何判断*/
        if (!administratorsOptionService.isSuperAdministrators(activeUserWorkQuery.getUserId())) {
            if (!activeUserWorkQuery.getUserId().equals(weixinVoteBaseByWorkId.getCreateSysUserId())) {
                throw new MyDefinitionException("您不是当前活动的管理员");
            }
        }
        PageHelper.startPage(activeUserWorkQuery.getPageNum(), activeUserWorkQuery.getPageSize());
        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("voteWorkShowOrder").desc();
        example.orderBy("voteWorkCreateTime").desc();
        Integer workStatus = activeUserWorkQuery.getWorkStatus();
        criteria.andEqualTo("activeVoteBaseId", activeUserWorkQuery.getActiveId());
        if (workStatus != null && workStatus != -1) {
            criteria.andEqualTo("voteWorkStatus", activeUserWorkQuery.getWorkStatus());
        }

        List<WeixinVoteWork> weixinVoteWorks = null;
        try {
            weixinVoteWorks = weixinVoteWorkMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询活动下的作品操作过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("查询活动下的作品操作过程失败");
        }
        PageInfo<WeixinVoteWork> pageInfo = new PageInfo(weixinVoteWorks);
        List<WeixinVoteWork> list = pageInfo.getList();
        List<WeixinVoteWork> list1 = new ArrayList<>();
        for (WeixinVoteWork weixinVoteWork : list) {

            String[] split = weixinVoteWork.getVoteWorkImg().split(SEMICOLON_SEPARATOR);
            weixinVoteWork.setVoteWorkImg(split[0]);
            list1.add(weixinVoteWork);
        }
        pageInfo.setList(list1);
        return pageInfo;
    }

    /**
     * 通过userID和activeId查询某个人在某个活动中提交作品的内容
     *
     * @param userId   用户ID
     * @param activeId 活动ID
     * @return 作品列表
     * @throws MyDefinitionException
     */
    @Override
    public List<WeixinVoteWork> getWeiXinVoteWorkListByUserId(Integer userId, Integer activeId) throws MyDefinitionException {
        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("voteWorkUserId", userId);
        criteria.andEqualTo("activeVoteBaseId", activeId);


        List<WeixinVoteWork> weixinVoteWorks = null;
        try {
            weixinVoteWorks = weixinVoteWorkMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("查询用户在某个活动中的作品内容列表操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询用户在某个活动中的作品内容列表操作过程错误");
        }
        return weixinVoteWorks;
    }

    /**
     * 用户上传个人的作品  需要完整属性
     *
     * @param saveVoteUserQuery 前端上传回来的用户作品数据
     * @return 如果有错误返回错误信息
     * @throws MyDefinitionException
     */
    @Override
    public Result createWeixinVoteWorkReturnPK(SaveVoteUserQuery saveVoteUserQuery) throws MyDefinitionException {

        /*查询活动是否存在*/
        WeixinVoteBase voteWorkByWorkId = weixinVoteBaseService.getWeixinVoteBaseByWorkId(saveVoteUserQuery.getActiveId());
        if (voteWorkByWorkId == null) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "作品所属活动不明确");
        } else {
            /*如果存在判断是否允许用户上传*/
            WeixinVoteConf weixinVoteConfByVoteWorkId = weixinVoteConfService.getWeixinVoteConfByVoteWorkId(saveVoteUserQuery.getActiveId());
            if (weixinVoteConfByVoteWorkId == null) {
                return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "没有查找到活动配置数据");
            } else {
                if (weixinVoteConfByVoteWorkId.getActiveConfSignUp().
                        equals(WeixinVoteConf.ActiveConfSignUpEnum.CAN_SIGN_UP.getCode())) {
                    /*如果允许个人用户上传则先判断是否已经上传过了*/
                    List<WeixinVoteWork> weiXinVoteWorkListByUserId = null;
                    try {
                        weiXinVoteWorkListByUserId = getWeiXinVoteWorkListByUserId(saveVoteUserQuery.getUserId(), saveVoteUserQuery.getActiveId());
                    } catch (MyDefinitionException e) {
                        return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
                    }
                    /*如果是创建人则不进行是否有活动的判断*/
                    if (weiXinVoteWorkListByUserId != null) {
                        if (!voteWorkByWorkId.getCreateSysUserId().equals(saveVoteUserQuery.getUserId())) {
                            if (weiXinVoteWorkListByUserId.size() >= 1 &&
                                    !weiXinVoteWorkListByUserId.get(0).getVoteWorkStatus().equals(WeixinVoteWork.VoteWorkStatusEnum.OFFLINE.getCode())) {
                                return Result.buildResult(Result.Status.UNAUTHORIZED, "已有作品在该活动中");
                            }
                        }
                    }

                    /*如果允许个人用户上传则再判断是否在允许上传时间范围内*/
                    Date nowDate = new Date();
                    Date activeUploadStartTime = weixinVoteConfByVoteWorkId.getActiveUploadStartTime();
                    Date activeUploadEndTime = weixinVoteConfByVoteWorkId.getActiveUploadEndTime();
                    boolean voteTimeLegal = activeUploadStartTime.before(nowDate);
                    if (!voteTimeLegal) {
                        return Result.buildResult(Result.Status.UNAUTHORIZED, "作品上传时间未开始");
                    }
                    voteTimeLegal = activeUploadEndTime.before(nowDate);
                    if (voteTimeLegal) {
                        return Result.buildResult(Result.Status.UNAUTHORIZED, "作品上传时间已结束");
                    }

                } else if (weixinVoteConfByVoteWorkId.getActiveConfSignUp().
                        equals(WeixinVoteConf.ActiveConfSignUpEnum.CANT_SIGN_UP.getCode())) {
                    /*不要允许用户上传数据*/
                    if (!voteWorkByWorkId.getCreateSysUserId().equals(saveVoteUserQuery.getUserId())) {
                        return Result.buildResult(Result.Status.UNAUTHORIZED, "当前活动不允许用户上传作品");
                    }
                }
            }

        }

        /*查询用户是否存在 如果存在判断用户状态是否是可以使用状态*/
        WeixinVoteUser userById = weixinVoteUserService.getUserById(saveVoteUserQuery.getUserId());
        if (userById == null) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, "作品所属用户不明确");
        } else {
            Integer enable = userById.getEnable();
            if (!enable.equals(WeixinVoteUser.ENABLEENUM.ENABLE.getCode())) {
                return Result.buildResult(Result.Status.UNAUTHORIZED, "当前账户已被禁用");
            }
        }
        /*查询当前作品下面有多少作品了 然后用户给新创建的作品排序*/
        Integer countWorkByVoteBaseId = null;
        try {
            countWorkByVoteBaseId = getUserWorkCountByActiveId(saveVoteUserQuery.getActiveId());
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }

        /*创建作品*/
        /*获取当前该作品在活动中的序号*/
        Integer voteWorkOr = ++countWorkByVoteBaseId;
        /*新作品实例化*/
        WeixinVoteWork weixinVoteWork = WeixinVoteWork.init();
        weixinVoteWork.setVoteWorkOr(voteWorkOr);
        weixinVoteWork.setActiveVoteBaseId(saveVoteUserQuery.getActiveId());
        weixinVoteWork.setVoteWorkImg(saveVoteUserQuery.getVoteWorkImgS());
        weixinVoteWork.setVoteWorkUserId(saveVoteUserQuery.getUserId());
        weixinVoteWork.setVoteWorkUserPhone(saveVoteUserQuery.getUserPhone());
        weixinVoteWork.setVoteWorkUserWeixin(saveVoteUserQuery.getUserWeixin());
        weixinVoteWork.setVoteWorkName(saveVoteUserQuery.getVoteWorkName());
        weixinVoteWork.setVoteWorkUserName(userById.getNickName());
        weixinVoteWork.setVoteWorkDesc(saveVoteUserQuery.getVoteWorkDesc());
        Integer saveWeixinVoteWorkReturnPK = null;
        try {
            saveWeixinVoteWorkReturnPK = saveWeixinVoteWorkReturnPK(weixinVoteWork);
        } catch (MyDefinitionException e) {
            return Result.buildResult(Result.Status.INTERNAL_SERVER_ERROR, e.getMessage());
        }
        return Result.buildResult(Result.Status.OK, saveWeixinVoteWorkReturnPK);
    }

    /**
     * 查询一个活动下面有多少作品数量了 通过活动ID查询
     *
     * @param voteWorkId 活动的主键
     * @return 作品数量
     * @throws MyDefinitionException
     */
    @Override
    public Integer getUserWorkCountByActiveId(Integer voteWorkId) throws MyDefinitionException {
        if (voteWorkId == null) {
            throw new MyDefinitionException("查询一个活动下面有多少作品数量的参数不能为空");
        }
        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", voteWorkId);
        int i = 0;
        try {
            i = weixinVoteWorkMapper.selectCountByExample(example);
        } catch (Exception e) {
            log.error("查询一个活动下面有多少作品数量操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("查询一个活动下面有多少作品数量操作过程错误");
        }
        return i;
    }

    /**
     * 获取作品点赞比当前作品多的作品
     *
     * @param activeId
     * @param workId
     * @return
     */
    @Override
    public List<WeixinVoteWork> getThanWorkWeixinVoteWork(Integer activeId, Integer workId) {

        WeixinVoteWork voteWorkByUserWorkId = getVoteWorkByUserWorkId(workId);
        if (voteWorkByUserWorkId == null) {
            log.error("获取作品点赞比当前作品多的作品失败，未能通过workId查询到作品，查询的workId：{}", workId);
            throw new MyDefinitionException(404, "未能通过workId查询到作品");
        }

        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", activeId);
        criteria.andGreaterThan("voteWorkCountNum", voteWorkByUserWorkId.getVoteWorkCountNum());
        example.orderBy("voteWorkCountNum").desc();
        return weixinVoteWorkMapper.selectByExample(example);
    }

    /**
     * 获取作品点赞比当前作品少的作品
     *
     * @param activeId
     * @param workId
     * @return
     */
    @Override
    public List<WeixinVoteWork> getLessWorkWeixinVoteWork(Integer activeId, Integer workId) {

        WeixinVoteWork voteWorkByUserWorkId = getVoteWorkByUserWorkId(workId);
        if (voteWorkByUserWorkId == null) {
            log.error("获取作品点赞比当前作品少的作品失败，未能通过workId查询到作品，查询的workId：{}", workId);
            throw new MyDefinitionException(404, "未能通过workId查询到作品");
        }

        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", activeId);
        criteria.andLessThan("voteWorkCountNum", voteWorkByUserWorkId.getVoteWorkCountNum());
        example.orderBy("voteWorkCountNum").desc();
        return weixinVoteWorkMapper.selectByExample(example);
    }

    /**
     * 获取作品在活动中的排名
     *
     * @param activeId
     * @param workId
     * @return
     */
    @Override
    public Integer getRankNumByUserWorkId(Integer activeId, Integer workId) {
        Integer rankNumByUserWorkId = null;
        try {
            rankNumByUserWorkId = weixinVoteWorkMapper.getRankNumByUserWorkId(activeId, workId);
        } catch (Exception e) {
            log.error("查询当前作品排名错误，错误原因：{}", e.toString());
        }
        return rankNumByUserWorkId;
    }

    /**
     * 查询当前作品的差距
     *
     * @param workId
     * @return
     */
    @Override
    public WeixinVoteUserWorkDiffVO getUserWorkDiff(Integer workId) {
        WeixinVoteWork voteWorkByUserWorkId = getVoteWorkByUserWorkId(workId);
        if (voteWorkByUserWorkId == null) {
            log.error("未能通过workId查询到作品，查询的workId：{}", workId);
            throw new MyDefinitionException(404, "未能通过workId查询到作品");
        }
        WeixinVoteUserWorkDiffVO weixinVoteWorkDiffVo = new WeixinVoteUserWorkDiffVO();
        /*weixinVoteWorkDiffVo.setViewCount(voteWorkByUserWorkId.getVoteWorkCountViewNum());
        weixinVoteWorkDiffVo.setVoteCount(voteWorkByUserWorkId.getVoteWorkCountNum());
        weixinVoteWorkDiffVo.setVoteWorkOr(voteWorkByUserWorkId.getVoteWorkOr());
        weixinVoteWorkDiffVo.setVoteWorkRank(getRankNumByUserWorkId(voteWorkByUserWorkId.getActiveVoteBaseId(), workId));*/
        /*自己的数据*/
        weixinVoteWorkDiffVo.setMyWeixinVoteWorkSimpleVO(MyEntityUtil.entity2VM(voteWorkByUserWorkId, WeixinVoteWorkSimpleVO.class));

        /*上一个名次*/
        WeixinVoteWorkSimpleVO weixinVoteWorkSimpleVOThan = null;
        List<WeixinVoteWork> thanWorkWeixinVoteWork = getThanWorkWeixinVoteWork(voteWorkByUserWorkId.getActiveVoteBaseId(), workId);
        if (thanWorkWeixinVoteWork != null && thanWorkWeixinVoteWork.size() > 0) {
            weixinVoteWorkSimpleVOThan = MyEntityUtil.entity2VM(thanWorkWeixinVoteWork.get(thanWorkWeixinVoteWork.size() - 1), WeixinVoteWorkSimpleVO.class);
        }
        /*下一个名次*/
        WeixinVoteWorkSimpleVO weixinVoteWorkSimpleVOLess = null;
        thanWorkWeixinVoteWork = getLessWorkWeixinVoteWork(voteWorkByUserWorkId.getActiveVoteBaseId(), workId);
        if (thanWorkWeixinVoteWork != null && thanWorkWeixinVoteWork.size() > 0) {
            weixinVoteWorkSimpleVOLess = MyEntityUtil.entity2VM(thanWorkWeixinVoteWork.get(0), WeixinVoteWorkSimpleVO.class);
        }
        /*最高名次*/
        WeixinVoteWorkSimpleVO weixinVoteWorkSimpleVOBest = null;
        thanWorkWeixinVoteWork = getThanWorkWeixinVoteWork(voteWorkByUserWorkId.getActiveVoteBaseId(), workId);
        if (thanWorkWeixinVoteWork != null && thanWorkWeixinVoteWork.size() > 0) {
            weixinVoteWorkSimpleVOBest = MyEntityUtil.entity2VM(thanWorkWeixinVoteWork.get(0), WeixinVoteWorkSimpleVO.class);
        }



        /**/
        weixinVoteWorkDiffVo.setPreDiff(weixinVoteWorkSimpleVOThan.getVoteWorkCountNum() - voteWorkByUserWorkId.getVoteWorkCountNum());
        weixinVoteWorkDiffVo.setPreWeixinVoteWorkSimpleVO(weixinVoteWorkSimpleVOThan);

        weixinVoteWorkDiffVo.setNextDiff(voteWorkByUserWorkId.getVoteWorkCountNum() - weixinVoteWorkSimpleVOLess.getVoteWorkCountNum());
        weixinVoteWorkDiffVo.setNextWeixinVoteWorkSimpleVO(weixinVoteWorkSimpleVOLess);

        weixinVoteWorkDiffVo.setBestDiff(weixinVoteWorkSimpleVOBest.getVoteWorkCountNum() - voteWorkByUserWorkId.getVoteWorkCountNum());
        weixinVoteWorkDiffVo.setBestWeixinVoteWorkSimpleVO(weixinVoteWorkSimpleVOBest);

        return weixinVoteWorkDiffVo;
    }


    /**
     * 通过voteBaseId获取当前这个活动有多少人参加
     *
     * @param voteBaseId
     * @return
     */
    @Override
    public Integer getCountWorkByVoteBaseId(Integer voteBaseId) {
        int i = 0;
        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("activeVoteBaseId", voteBaseId);
        List<WeixinVoteWork> weixinVoteWorks = null;
        try {
            weixinVoteWorks = weixinVoteWorkMapper.selectByExample(example);
        } catch (Exception e) {
            log.error("通过voteBaseId获取当前这个活动有多少人参加失败，错误原因{}", e.toString());
            throw new MyDefinitionException("通过voteBaseId获取当前这个活动有多少人参加失败");
        }
        if (weixinVoteWorks != null) {
            i = weixinVoteWorks.size();
        }
        return i;
    }

    /**
     * 通过voteBaseId获取当前这个活动有多少人投票
     *
     * @param voteBaseId
     * @return
     */
    @Override
    public Integer getCountVoteByVoteBaseId(Integer voteBaseId) {
        try {
            return weixinVoteWorkMapper.getCountVoteByVoteBaseId(voteBaseId);
        } catch (Exception e) {
            log.error("通过voteBaseId获取当前这个活动有多少人投票查询失败，错误原因{}", e.toString());
            throw new MyDefinitionException("通过voteBaseId获取当前这个活动有多少人投票查询失败");
        }
    }

    /**
     * 分页查询活动的所有的作品
     *
     * @param weixinVoteWork
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getVoteWorkAllWorkByPage(WeixinVoteWork weixinVoteWork, PageInfo pageInfo) {

        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteWork用于条件查询
        if (weixinVoteWork != null) {
            if (weixinVoteWork.getActiveVoteBaseId() != null) {
                criteria.andEqualTo("activeVoteBaseId", weixinVoteWork.getActiveVoteBaseId());
            }

            if (weixinVoteWork.getVoteWorkStatus() != null && weixinVoteWork.getVoteWorkStatus() > -1) {
                criteria.andEqualTo("voteWorkStatus", weixinVoteWork.getVoteWorkStatus());
            }

            /*排序*/
            if (weixinVoteWork.getVoteWorkShowOrder() != null) {
                if (weixinVoteWork.getVoteWorkShowOrder() < 0) {
                    example.orderBy("voteWorkShowOrder").asc();
                } else {
                    example.orderBy("voteWorkShowOrder").desc();
                }
            }

            if (weixinVoteWork.getVoteWorkOr() != null) {
                if (weixinVoteWork.getVoteWorkOr() < 0) {
                    example.orderBy("voteWorkOr").asc();
                } else {
                    example.orderBy("voteWorkOr").desc();
                }
            }
            if (weixinVoteWork.getVoteWorkCountNum() != null) {
                if (weixinVoteWork.getVoteWorkCountNum() < 0) {
                    example.orderBy("voteWorkCountNum").asc();
                } else {
                    example.orderBy("voteWorkCountNum").desc();
                }
            }

            if (weixinVoteWork.getVoteWorkCountViewNum() != null) {
                if (weixinVoteWork.getVoteWorkCountViewNum() < 0) {
                    example.orderBy("voteWorkCountViewNum").asc();
                } else {
                    example.orderBy("voteWorkCountViewNum").desc();
                }
            }


            if (weixinVoteWork.getVoteWorkCreateTime() != null) {
                example.orderBy("voteWorkCreateTime").desc();
            }


        }


        List<WeixinVoteWork> weixinVoteWorks = weixinVoteWorkMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteWorks);
        /*组装VO进行数据返回*/
        List<VoteDetailSimpleVO> voteDetailSimpleVOS = MyEntityUtil.entity2VMList(weixinVoteWorks, VoteDetailSimpleVO.class);
        List<VoteDetailSimpleVO> weixinVoteWorksTemp = new ArrayList<>();
        for (VoteDetailSimpleVO voteWork : voteDetailSimpleVOS) {
            String[] split = voteWork.getVoteWorkImg().split(";");
            voteWork.setVoteWorkImg(split[0]);
            weixinVoteWorksTemp.add(voteWork);
        }
        pageInfo.setList(voteDetailSimpleVOS);
        return pageInfo;
    }

    /**
     * 分页查询活动的人气作品
     *
     * @param weixinVoteWork
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getVoteWorkHotWorkByPage(WeixinVoteWork weixinVoteWork, PageInfo pageInfo) {

        Example example = new Example(WeixinVoteWork.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("voteWorkShowOrder").desc();
        example.orderBy("voteWorkCountNum").desc();
        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteWork用于条件查询
        if (weixinVoteWork != null) {
            criteria.andEqualTo("activeVoteBaseId", weixinVoteWork.getId());
        }
        List<WeixinVoteWork> weixinVoteWorks = weixinVoteWorkMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteWorks);
        /*组装VO进行数据返回*/
        List<VoteDetailSimpleVO> voteDetailSimpleVOS = MyEntityUtil.entity2VMList(weixinVoteWorks, VoteDetailSimpleVO.class);
        List<VoteDetailSimpleVO> weixinVoteWorksTemp = new ArrayList<>();
        for (VoteDetailSimpleVO voteWork : voteDetailSimpleVOS) {
            String[] split = voteWork.getVoteWorkImg().split(";");
            voteWork.setVoteWorkImg(split[0]);
            weixinVoteWorksTemp.add(voteWork);
        }
        pageInfo.setList(voteDetailSimpleVOS);
        return pageInfo;
    }

    /**
     * 通过userWorkId查询当前的详细信息
     *
     * @param userWorkId
     * @return
     */
    @Override
    public VoteDetailCompleteVO getWeixinVoteWorkByUserWorkId(Integer userWorkId) {
        WeixinVoteWork weixinVoteWork = weixinVoteWorkMapper.selectByPrimaryKey(userWorkId);
        if (weixinVoteWork == null) {
            throw new MyDefinitionException(404, "未发现当前ID:" + userWorkId + "的作品");
        }

        String voteWorkImg = weixinVoteWork.getVoteWorkImg();
        String[] voteImgS = voteWorkImg.split(";");
        VoteDetailCompleteVO voteDetailCompleteVO = MyEntityUtil.entity2VM(weixinVoteWork, VoteDetailCompleteVO.class);
        Integer rankNumByUserWorkId = null;
        try {
            rankNumByUserWorkId = weixinVoteWorkMapper.getRankNumByUserWorkId(weixinVoteWork.getActiveVoteBaseId(), userWorkId);
        } catch (Exception e) {
            log.error("查询当前作品排名错误，错误原因：{}", e.toString());
        }
        voteDetailCompleteVO.setVoteWorkImgS(voteImgS);
        voteDetailCompleteVO.setRankNum(rankNumByUserWorkId);
        WeixinVoteUser userById = weixinVoteUserService.getUserById(weixinVoteWork.getVoteWorkUserId());
        if (userById != null) {
            voteDetailCompleteVO.setVoteWorkUserAvatar(userById.getAvatarUrl());
        }
        return voteDetailCompleteVO;
    }


    /**
     * 通过作品的ID更新被浏览次数
     *
     * @param userWorkId 用户作品ID
     * @return
     */
    @Override
    public int updateVoteWorkViewNum(Integer userWorkId) {

        WeixinVoteWork weixinVoteWork = null;
        try {
            weixinVoteWork = weixinVoteWorkMapper.selectByPrimaryKey(userWorkId);
        } catch (Exception e) {
            log.error("通过作品ID查询活动错误，查询的作品ID为{},错误理由{}", userWorkId, e.toString());
            throw new MyDefinitionException("通过作品ID查询活动错误，查询的作品ID为" + userWorkId);
        }
        if (weixinVoteWork == null) {
            return 0;
        } else {
            Integer viewCountNum = weixinVoteWork.getVoteWorkCountViewNum();
            weixinVoteWork.setVoteWorkCountViewNum(viewCountNum + 1);
        }
        int i = 0;
        try {
            i = weixinVoteWorkMapper.updateByPrimaryKey(weixinVoteWork);
        } catch (Exception e) {
            log.error("通过作品ID更新浏览次数（数据加1）错误，更新的活动ID为{},错误理由{}", userWorkId, e.toString());
            throw new MyDefinitionException("通过作品ID更新浏览次数（数据加1）错误，查询的活动ID为" + userWorkId);
        }
        return i;
    }

    /**
     * 通过作品的ID查询作品
     *
     * @param userWorkId 用户作品ID
     * @return
     */
    @Override
    public WeixinVoteWork getVoteWorkByUserWorkId(Integer userWorkId) {
        WeixinVoteWork weixinVoteWork = null;
        try {
            weixinVoteWork = weixinVoteWorkMapper.selectByPrimaryKey(userWorkId);
        } catch (Exception e) {
            log.error("通过作品ID查询活动错误，查询的作品ID为{},错误理由{}", userWorkId, e.toString());
            throw new MyDefinitionException("通过作品ID查询活动错误，查询的作品ID为" + userWorkId);
        }
        return weixinVoteWork;
    }

    /**
     * 通过作品的ID更新被投票次数
     *
     * @param userWorkId 用户作品ID
     * @return
     */
    @Override
    public int updateVoteWorkVoteNum(Integer userWorkId) {
        WeixinVoteWork weixinVoteWork = null;
        try {
            weixinVoteWork = weixinVoteWorkMapper.selectByPrimaryKey(userWorkId);
        } catch (Exception e) {
            log.error("通过作品ID查询活动错误，查询的作品ID为{},错误理由{}", userWorkId, e.toString());
            throw new MyDefinitionException("通过作品ID查询活动错误，查询的作品ID为" + userWorkId);
        }
        if (weixinVoteWork == null) {
            return 0;
        } else {
            Integer viewCountNum = weixinVoteWork.getVoteWorkCountNum();
            weixinVoteWork.setVoteWorkCountNum(viewCountNum + 1);
        }
        int i = 0;
        try {
            i = weixinVoteWorkMapper.updateByPrimaryKey(weixinVoteWork);
        } catch (Exception e) {
            log.error("通过作品ID更新投票次数（数据加1）错误，更新的活动ID为{},错误理由{}", userWorkId, e.toString());
            throw new MyDefinitionException("通过作品ID更新投票次数（数据加1）错误，查询的活动ID为" + userWorkId);
        }
        return i;
    }

    /**
     * 保存用户作品 返回主键 需要完整属性
     *
     * @param weixinVoteWork
     * @return 创建完成后的主键
     * @throws MyDefinitionException
     */
    @Override
    public Integer saveWeixinVoteWorkReturnPK(WeixinVoteWork weixinVoteWork) throws MyDefinitionException {
        if (weixinVoteWork == null) {
            throw new MyDefinitionException("保存用户作品实体类参数不能为空");
        }
        Integer pk = null;
        try {
            int i = weixinVoteWorkMapper.insertUseGeneratedKeys(weixinVoteWork);
            if (i > 0) {
                pk = weixinVoteWork.getId();
            }
        } catch (Exception e) {
            log.error("保存用户作品错误操作过程错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("保存用户作品错误操作过程错误");
        }
        return pk;
    }
}
