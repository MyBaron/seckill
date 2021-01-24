package com.github.lyrric.model;

import com.alibaba.fastjson.annotation.JSONField;
import lombok.Data;

/**
 * Created on 2020-07-23.
 * 疫苗列表
 * @author wangxiaodong
 */
@Data
public class Vaccine {


    /**
     * 疫苗代码
     */
    private Integer id;
    /**
     * 诊所 id
     */
    private Integer clinicId;
    /**
     * 医院名称
     */
    private String cname;
    /**
     * 疫苗名称
     */
    @JSONField(name = "text")
    private String vaccineName;
    /**
     * 预约时间
     */
    @JSONField(name = "date")
    private String orderTime;
    /**
     * 是否可以预约
     */
    @JSONField(name = "enable")
    private boolean enableOrder;

}