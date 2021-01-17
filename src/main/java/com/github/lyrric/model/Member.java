package com.github.lyrric.model;

import lombok.Data;

/**
 * Created on 2020-07-24.
 * 接种人信息
 * @author wangxiaodong
 */
@Data
public class Member {


    private Integer id;

    /**
     * 姓名
     */
    private String cname;

    /**
     * 身份证号码
     */
    private String idcard;

    private String tel;


}
