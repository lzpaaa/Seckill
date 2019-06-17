package org.seckill.service;

import org.seckill.dto.SeckillExecution;
import org.seckill.entity.Seckill;
import org.seckill.dto.Exposer;
import org.seckill.exception.RepeatKillException;
import org.seckill.exception.SeckillException;
import org.seckill.exception.SeckillcloseException;

import java.util.List;

/**
 * 站在使用者的角度去设计接口
 */
public interface SeckillService {

    //查询所有的秒杀记录
    List<Seckill> getSeckillList();

    //查询单个秒杀记录
    Seckill getById(long seckillId);

    //秒杀开启时输出秒杀接口的地址，否则输出系统时间和秒杀时间
    Exposer exportSeckillUrl(long seckillId);

    //执行秒杀操作
    SeckillExecution executeSeckill(long seckillId, long userPhone, String md5)
            throws SeckillException, RepeatKillException, SeckillcloseException;
}
