package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinVoteWorkMapper;
import com.hyp.myweixin.pojo.modal.WeixinVoteUser;
import com.hyp.myweixin.pojo.modal.WeixinVoteWork;
import com.hyp.myweixin.pojo.vo.page.VoteDetailCompleteVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailSimpleVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteUserWorkDiffVO;
import com.hyp.myweixin.pojo.vo.page.WeixinVoteWorkSimpleVO;
import com.hyp.myweixin.service.WeixinVoteUserService;
import com.hyp.myweixin.service.WeixinVoteWorkService;
import com.hyp.myweixin.utils.MyEntityUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import tk.mybatis.mapper.entity.Example;

import java.util.ArrayList;
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
            if (weixinVoteWork.getId() != null) {
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

            if (weixinVoteWork.getVoteWorkOr() != null) {
                if (weixinVoteWork.getVoteWorkOr() < 0) {
                    example.orderBy("voteWorkOr").asc();
                } else {
                    example.orderBy("voteWorkOr").desc();
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


}
