package org.seckill.service.Impl;

import org.seckill.dao.SeckillDao;
import org.seckill.dao.SuccessKilledDao;
import org.seckill.dto.Exposer;
import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.entity.SucceddKilled;
import org.seckill.enums.SeckillStatEnum;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillException;
import org.seckill.exception.SeckillcloseException;
import org.seckill.service.SeckillService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;

import java.awt.geom.Ellipse2D;
import java.util.Date;
import java.util.List;

@Service
public class SeckillServiceImpl implements SeckillService {

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    private SeckillDao seckillDao;

    @Autowired
    private SuccessKilledDao successKilledDao;

    //md5盐值字符串，用于混淆MD5
    private final String slat = "fksajf39394r23kjkljlk4";




    //查询所有的秒杀记录
    @Override
    public List<Seckill> getSeckillList() {
        return seckillDao.queryAll(0,4);
    }

    //查询单个秒杀记录
    @Override
    public Seckill getById(long seckillId) {
        return seckillDao.queryById(seckillId);
    }

    //生成md5
    public String getMd5(long seckillId) {
        String base = seckillId + "/" + slat;
        String md5 = DigestUtils.md5DigestAsHex(base.getBytes());
        return md5;
    }

    //秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
    @Override
    public Exposer exportSeckillUrl(long seckillId) {

        Seckill seckill = seckillDao.queryById(seckillId);

        if(seckill == null) {
            return new Exposer(false,seckillId);
        }
        Date startTime = seckill.getStartTime();
        Date endTime = seckill.getEndTime();
        Date nowTime = new Date();
        if(nowTime.getTime() <startTime.getTime() ||
                nowTime.getTime() > endTime.getTime()){
            return new Exposer(false, seckillId,nowTime.getTime(),
                    startTime.getTime(),endTime.getTime());
        }
        //转化特定字符串的过程，不可逆。
        String md5 = getMd5(seckillId);
        return new Exposer(true,seckillId,md5);
    }


    @Override
    @Transactional
    //执行秒杀操作
    public SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillcloseException {
        if(md5 == null || !md5.equals(getMd5(seckillId))){
            throw new SeckillException("秒杀重写");
        }
        Date nowTime = new Date();
        //秒杀执行逻辑：记录购买行为 + 减库存
        try {
            //记录购买行为
            int insertCount = successKilledDao.insertSuccessKilled(seckillId,userPhone);
            if(insertCount <= 0){
                //重复秒杀
                throw new RepeatKillException("重复秒杀");
            } else {
                //减库存，热点商品竞争
                int reduceCount = seckillDao.reduceNumber(seckillId,nowTime);
                if (reduceCount <= 0){
                    //没有更新到记录，秒杀结束，rollback
                    throw new SeckillcloseException("秒杀结束");
                } else {
                    //秒杀成功 commit
                    SucceddKilled succeddKilled = successKilledDao.queryByIdwithSeckill(seckillId,userPhone);
                    return new SeckillExecution(seckillId, SeckillStatEnum.SUCCESS, succeddKilled);
                }
            }

        } catch(SeckillcloseException e1 ){
            throw e1;
        } catch (RepeatKillException e2){
            throw e2;
        }catch (Exception e) {
            logger.error(e.getMessage(), e);
            //所有编译器异常，转化为运行期异常
            throw new SeckillException("内部错误：" + e.getMessage());
        }
    }
}
