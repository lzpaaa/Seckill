package org.seckill.dao;


import org.apache.ibatis.annotations.Param;
import org.seckill.entity.SucceddKilled;

public interface SuccessKilledDao {

    /**
     * 插入购物明细
     * @param seckillId
     * @param userPhone
     * @return
     */
    int insertSuccessKilled(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);

    /**
     * 根据Id查询SuccessKilled，携带产品对象实体
     * @param seckillId
     * @param userPhone
     * @return
     */
    SucceddKilled queryByIdwithSeckill(@Param("seckillId") long seckillId, @Param("userPhone") long userPhone);
}
