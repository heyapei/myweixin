package com.hyp.myweixin.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.mapper.WeixinMusicMapper;
import com.hyp.myweixin.mapper.WeixinVoteBaseMapper;
import com.hyp.myweixin.mapper.WeixinVoteConfMapper;
import com.hyp.myweixin.mapper.WeixinVoteOrganisersMapper;
import com.hyp.myweixin.pojo.modal.*;
import com.hyp.myweixin.pojo.vo.page.ActiveWorkRankVO;
import com.hyp.myweixin.pojo.vo.page.IndexWorksVO;
import com.hyp.myweixin.pojo.vo.page.VoteDetailByWorkIdVO;
import com.hyp.myweixin.service.WeixinVoteBaseService;
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
 * @Date 2020/6/7 20:11
 * @Description: TODO
 */
@Service
@Slf4j
public class WeixinVoteBaseServiceImpl implements WeixinVoteBaseService {


    @Autowired
    private WeixinVoteBaseMapper weixinVoteBaseMapper;
    @Autowired
    private WeixinVoteWorkService weixinVoteWorkService;

    @Autowired
    private WeixinVoteConfMapper weixinVoteConfMapper;
    @Autowired
    private WeixinMusicMapper weixinMusicMapper;
    @Autowired
    private WeixinVoteOrganisersMapper weixinVoteOrganisersMapper;


    /**
     * 查询用户名下的活动按照活动的状态值
     *
     * @param userId 用户ID
     * @param status 活动状态值
     * @return
     */
    @Override
    public List<WeixinVoteBase> getWeixinVoteBaseByUserIdAndStatus(int userId, int status) {
        Example example = new Example(WeixinVoteBase.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("activeShowOrder").desc();
        if (userId > 0) {
            criteria.andEqualTo("createSysUserId", userId);
        }
        criteria.andEqualTo("status", status);
        List<WeixinVoteBase> weixinVoteBases = null;
        try {
            weixinVoteBases = weixinVoteBaseMapper.selectByExample(example);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("查询用户名下的活动按照活动的状态值错误，错误原因{}", e.toString());
        }
        return weixinVoteBases;
    }

    /**
     * 通过活动的ID查询活动的详情 完整实体类
     *
     * @param workId 活动ID
     * @return
     */
    @Override
    public WeixinVoteBase getWeixinVoteBaseByWorkId(Integer workId) {
        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseMapper.selectByPrimaryKey(workId);
        } catch (Exception e) {
            e.printStackTrace();
            log.info("通过活动的ID查询活动的详情错误，错误原因：{}", e.toString());
        }

        return weixinVoteBase;
    }

    /**
     * 保存活动基础表
     *
     * @param weixinVoteBase
     * @return
     */
    @Override
    public Integer saveVoteBase(WeixinVoteBase weixinVoteBase) {
        Integer result = null;
        int i = 0;
        try {
            /*需要返回当前活动的主键*/
            i = weixinVoteBaseMapper.insertUseGeneratedKeys(weixinVoteBase);
            //i = weixinVoteBaseMapper.insertSelective(weixinVoteBase);
        } catch (Exception e) {
            e.printStackTrace();
            log.error("保存活动基础数据错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("保存活动基础数据错误");
        }
        if (i > 0) {
            result = weixinVoteBase.getId();
        }
        return result;
    }

    /**
     * 分页查询活动下作品的排行榜
     *
     * @param activeId 活动ID
     * @param pageInfo 分页信息
     * @return
     */
    @Override
    public ActiveWorkRankVO getActiveWorkRank(Integer activeId, PageInfo pageInfo) {
        WeixinVoteBase weixinVoteBase = weixinVoteBaseMapper.selectByPrimaryKey(activeId);
        if (weixinVoteBase == null) {
            throw new MyDefinitionException("未能查找到对应活动内容");
        }
        WeixinVoteWork weixinVoteWork = new WeixinVoteWork();
        weixinVoteWork.setActiveVoteBaseId(activeId);
        weixinVoteWork.setVoteWorkShowOrder(0);
        weixinVoteWork.setVoteWorkCountNum(0);
        weixinVoteWork.setVoteWorkCreateTime(null);
        weixinVoteWork.setVoteWorkCountViewNum(null);
        PageInfo voteWorkAllWorkByPage = weixinVoteWorkService.getVoteWorkAllWorkByPage(weixinVoteWork, pageInfo);
        ActiveWorkRankVO workRankVO = new ActiveWorkRankVO();
        workRankVO.setVoteWorkPageInfo(voteWorkAllWorkByPage);
        workRankVO.setWeixinVoteBase(weixinVoteBase);
        return workRankVO;
    }


    /**
     * 分页查询投票活动列表
     *
     * @param weixinVoteBase
     * @param pageInfo
     * @return
     */
    @Override
    public PageInfo getVoteWorkByPage(WeixinVoteBase weixinVoteBase, PageInfo pageInfo) {

        /*条件查询*/
        Example example = new Example(WeixinVoteBase.class);
        Example.Criteria criteria = example.createCriteria();
        example.orderBy("activeShowOrder").desc();
        /*example.orderBy("createTime").desc();
        example.orderBy("viewCountNum").desc();*/

        PageHelper.startPage(pageInfo.getPageNum(), pageInfo.getPageSize());
        //TODO　weixinVoteBase用于条件查询
        List<WeixinVoteBase> weixinVoteBases = weixinVoteBaseMapper.selectByExample(example);
        // 如果这里需要返回VO，那么这里一定先把查询值放进去，让分页信息存储成功。然后再setList加入VO信息
        pageInfo = new PageInfo(weixinVoteBases);
        /*组装VO进行数据返回*/
        List<IndexWorksVO> indexWorksVOS = new ArrayList<>(5);
        for (WeixinVoteBase weixinVoteBase2 : weixinVoteBases) {
            Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountWorkByVoteBaseId(weixinVoteBase2.getId());
            /*Integer countVoteByVoteBaseId = weixinVoteWorkService.getCountVoteByVoteBaseId(weixinVoteBase2.getId());
            if (countVoteByVoteBaseId == null) {
                countVoteByVoteBaseId = 0;
            }*/
            //IndexWorksVO indexWorksVO = new IndexWorksVO();
            //BeanUtils.copyProperties(weixinVoteBase2, indexWorksVO);
            // 使用实体转换类进行数据转换处理
            IndexWorksVO indexWorksVO = MyEntityUtil.entity2VM(weixinVoteBase2, IndexWorksVO.class);
            indexWorksVO.setVoteWorkVoteCount(weixinVoteBase2.getVoteCountNum());
            indexWorksVO.setVoteWorkJoinCount(countWorkByVoteBaseId);
            indexWorksVOS.add(indexWorksVO);
        }
        pageInfo.setList(indexWorksVOS);


        return pageInfo;
    }

    /**
     * 通过活动的ID查询活动的详情
     *
     * @param workId 活动ID
     * @return
     */
    @Override
    public VoteDetailByWorkIdVO getVoteWorkByWorkId(Integer workId) {

        VoteDetailByWorkIdVO voteDetailByWorkIdVO = null;
        WeixinVoteBase weixinVoteBase = null;
        IndexWorksVO indexWorksVO = null;
        try {
            weixinVoteBase = weixinVoteBaseMapper.selectByPrimaryKey(workId);
        } catch (Exception e) {
            log.error("通过活动ID查询活动错误，查询的活动ID为{},错误理由{}", workId, e.toString());
            throw new MyDefinitionException("通过活动ID查询活动错误，查询的活动ID为" + workId);
        }
        if (weixinVoteBase == null) {
            return null;
        } else {
            Integer countWorkByVoteBaseId = weixinVoteWorkService.getCountWorkByVoteBaseId(weixinVoteBase.getId());
            // Integer countVoteByVoteBaseId = weixinVoteWorkService.getCountVoteByVoteBaseId(weixinVoteBase.getId());
           /* if (countVoteByVoteBaseId == null) {
                countVoteByVoteBaseId = 0;
            }*/


            Example example = new Example(WeixinVoteConf.class);
            Example.Criteria criteria = example.createCriteria();
            criteria.andEqualTo("activeVoteBaseId", weixinVoteBase.getId());
            List<WeixinVoteConf> weixinVoteConfS = null;
            try {
                weixinVoteConfS = weixinVoteConfMapper.selectByExample(example);
            } catch (Exception e) {
                log.error("通过投票活动ID查询配置内容错误，查询的投票ID为{},错误理由{}", workId, e.toString());
                throw new MyDefinitionException("通过投票活动ID查询配置内容错误，查询的投票ID为" + workId);

            }
            WeixinVoteConf weixinVoteConf = null;
            if (weixinVoteConfS != null && weixinVoteConfS.size() > 0) {
                weixinVoteConf = weixinVoteConfS.get(0);
            }

            String activeMusic = null;
            if (weixinVoteConf != null) {
                int musicId = weixinVoteConf.getActiveConfMusicId();
                WeixinMusic weixinMusic = null;
                try {
                    weixinMusic = weixinMusicMapper.selectByPrimaryKey(musicId);
                } catch (Exception e) {
                    log.error("通过音乐ID查询音乐内容错误，查询的音乐ID为{},错误理由{}", workId, e.toString());
                    throw new MyDefinitionException("通过音乐ID查询音乐内容错误，查询的音乐ID为" + workId);

                }


                if (weixinMusic != null) {
                    activeMusic = weixinMusic.getMusicUrl();
                }
            }


            Example example1 = new Example(WeixinVoteOrganisers.class);
            Example.Criteria criteria1 = example1.createCriteria();
            criteria1.andEqualTo("voteBaseId", weixinVoteBase.getId());
            WeixinVoteOrganisers weixinVoteOrganisers = new WeixinVoteOrganisers();
            List<WeixinVoteOrganisers> weixinVoteOrganisersList = null;
            try {
                weixinVoteOrganisersList = weixinVoteOrganisersMapper.selectByExample(example1);
            } catch (Exception e) {
                log.error("通过活动ID查询创建单位内容错误，查询的活动ID为{},错误理由{}", workId, e.toString());
                throw new MyDefinitionException("通过活动ID查询创建单位内容错误，查询的活动ID为" + workId);

            }
            if (weixinVoteOrganisersList != null && weixinVoteOrganisersList.size() > 0) {
                weixinVoteOrganisers = weixinVoteOrganisersList.get(0);
            }


            voteDetailByWorkIdVO = new VoteDetailByWorkIdVO();
            voteDetailByWorkIdVO.setActiveBgImg(weixinVoteBase.getActiveDescImg());
            voteDetailByWorkIdVO.setActiveImg(weixinVoteBase.getActiveImg());
            voteDetailByWorkIdVO.setActiveEndTime(weixinVoteBase.getActiveEndTime());
            voteDetailByWorkIdVO.setActiveStartTime(weixinVoteBase.getActiveStartTime());
            voteDetailByWorkIdVO.setActiveName(weixinVoteBase.getActiveName());
            voteDetailByWorkIdVO.setActiveMusic(activeMusic);
            voteDetailByWorkIdVO.setActiveJoinCount(countWorkByVoteBaseId);
            voteDetailByWorkIdVO.setActiveVoteCount(weixinVoteBase.getVoteCountNum());
            voteDetailByWorkIdVO.setActiveViewCount(weixinVoteBase.getViewCountNum());
            voteDetailByWorkIdVO.setOrganisersName(weixinVoteOrganisers.getName());
            voteDetailByWorkIdVO.setOrganisersLogoImg(weixinVoteOrganisers.getLogoImg());
            voteDetailByWorkIdVO.setOrganisersWeixinQrCode(weixinVoteOrganisers.getWeixinQrCode());
            voteDetailByWorkIdVO.setOrganisersPhone(weixinVoteOrganisers.getPhone());
        }

        return voteDetailByWorkIdVO;
    }

    /**
     * 通过活动的ID更新被浏览次数
     *
     * @param workId 活动ID
     * @return
     */
    @Override
    public int updateVoteBaseViewNum(Integer workId) {

        WeixinVoteBase weixinVoteBase = null;
        try {
            weixinVoteBase = weixinVoteBaseMapper.selectByPrimaryKey(workId);
        } catch (Exception e) {
            log.error("通过活动ID查询活动错误，查询的活动ID为{},错误理由{}", workId, e.toString());
            throw new MyDefinitionException("通过活动ID查询活动错误，查询的活动ID为" + workId);
        }
        if (weixinVoteBase == null) {
            return 0;
        } else {
            Integer viewCountNum = weixinVoteBase.getViewCountNum();
            weixinVoteBase.setViewCountNum(viewCountNum + 1);
        }
        int i = 0;
        try {
            i = weixinVoteBaseMapper.updateByPrimaryKey(weixinVoteBase);
        } catch (Exception e) {
            log.error("通过活动ID更新浏览次数（数据加1）错误，更新的活动ID为{},错误理由{}", workId, e.toString());
            throw new MyDefinitionException("通过活动ID更新浏览次数（数据加1）错误，查询的活动ID为" + workId);
        }

        return i;
    }

    /**
     * 通过workId记录点赞数
     *
     * @param workId 活动ID
     * @return
     */
    @Override
    public int updateVoteBaseVoteNum(Integer workId) {
        WeixinVoteBase weixinVoteBase = weixinVoteBaseMapper.selectByPrimaryKey(workId);
        if (weixinVoteBase == null) {
            return 0;
        } else {
            Integer voteCountNum = weixinVoteBase.getVoteCountNum();
            weixinVoteBase.setVoteCountNum(voteCountNum + 1);
        }
        int i = 0;
        try {
            i = weixinVoteBaseMapper.updateByPrimaryKey(weixinVoteBase);
        } catch (Exception e) {
            log.error("通过活动ID更新投票次数（数据加1）错误，更新的活动ID为{},错误理由{}", workId, e.toString());
            throw new MyDefinitionException("通过活动ID更新投票次数（数据加1）错误，查询的活动ID为" + workId);
        }
        return i;
    }
}
