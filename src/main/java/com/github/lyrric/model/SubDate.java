package com.github.lyrric.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * 预约日期
 */
@Data
public class SubDate {

    /**
     * 目测为YYYY-MM-DD格式
     */
    private String date;
    /**
     * 当日还可以预约的数量
     */
    @JSONField(name = "enable")
    private boolean enableSub;


}
