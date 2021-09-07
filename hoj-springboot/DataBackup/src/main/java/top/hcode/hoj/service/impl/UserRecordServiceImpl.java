package top.hcode.hoj.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Autowired;
import top.hcode.hoj.pojo.vo.ACMRankVo;
import top.hcode.hoj.pojo.entity.UserRecord;
import top.hcode.hoj.dao.UserRecordMapper;
import top.hcode.hoj.pojo.vo.OIRankVo;
import top.hcode.hoj.pojo.vo.UserHomeVo;
import top.hcode.hoj.service.UserRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import top.hcode.hoj.utils.Constants;
import top.hcode.hoj.utils.RedisUtils;

import java.util.List;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author Himit_ZH
 * @since 2020-10-23
 */
@Service
public class UserRecordServiceImpl extends ServiceImpl<UserRecordMapper, UserRecord> implements UserRecordService {

    @Autowired
    private UserRecordMapper userRecordMapper;

    @Autowired
    private RedisUtils redisUtils;

    // 排行榜缓存时间 60s
    private static final long cacheRankSecond = 60;

    @Override
    public Page<ACMRankVo> getACMRankList(int limit, int currentPage) {

        Page<ACMRankVo> page = new Page<>(currentPage, limit);
        String key = Constants.Account.ACM_RANK_CACHE.getCode() + "_" + limit + "_" + currentPage;

        List<ACMRankVo> data = (List<ACMRankVo>) redisUtils.get(key);

        if (data == null) {
            data = userRecordMapper.getACMRankList(page);
            redisUtils.set(key, data, cacheRankSecond);
        }

        return page.setRecords(data);
    }


    @Override
    public List<ACMRankVo> getRecent7ACRank() {
        return userRecordMapper.getRecent7ACRank();
    }

    @Override
    public Page<OIRankVo> getOIRankList(int limit, int currentPage) {

        Page<OIRankVo> page = new Page<>(currentPage, limit);
        String key = Constants.Account.OI_RANK_CACHE.getCode() + "_" + limit + "_" + currentPage;

        List<OIRankVo> data = (List<OIRankVo>) redisUtils.get(key);

        if (data == null) {
            data = userRecordMapper.getOIRankList(page);
            redisUtils.set(key, data, cacheRankSecond);
        }

        return page.setRecords(data);
    }

    @Override
    public UserHomeVo getUserHomeInfo(String uid) {
        return userRecordMapper.getUserHomeInfo(uid);
    }


}
