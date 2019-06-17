package org.seckill.dto;

import org.seckill.entity.SucceddKilled;
import org.seckill.enums.SeckillStatEnum;

/**
 * 执行秒杀操作
 */
public class SeckillExecution {

    //id
    private long seckillId;

    //秒杀执行结果状态
    private int state;

    //状态信息
    private String stateInfo;

    //秒杀成功对象
    private SucceddKilled succeddKilled;


    public SeckillExecution(long seckillId, SeckillStatEnum statEnum) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
    }

    public SeckillExecution(long seckillId, SeckillStatEnum statEnum, SucceddKilled succeddKilled) {
        this.seckillId = seckillId;
        this.state = statEnum.getState();
        this.stateInfo = statEnum.getStateInfo();
        this.succeddKilled = succeddKilled;
    }

    public long getSeckillId() {
        return seckillId;
    }

    public void setSeckillId(long seckillId) {
        this.seckillId = seckillId;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public String getStateInfo() {
        return stateInfo;
    }

    public void setStateInfo(String stateInfo) {
        this.stateInfo = stateInfo;
    }

    public SucceddKilled getSucceddKilled() {
        return succeddKilled;
    }

    public void setSucceddKilled(SucceddKilled succeddKilled) {
        this.succeddKilled = succeddKilled;
    }

    @Override
    public String toString() {
        return "SeckillExecution{" +
                "seckillId=" + seckillId +
                ", state=" + state +
                ", stateInfo='" + stateInfo + '\'' +
                ", succeddKilled=" + succeddKilled +
                '}';
    }
}
