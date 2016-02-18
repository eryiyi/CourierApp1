package com.example.CourierApp.base;

/**
 * Created by liuzwei on 2015/1/12.
 */
public class InternetURL {
    /**
     * 1.获取快递公司
     * 调用地址:www.xxshequ.gov/appapi/getCompanyList.php
     * 错误码:000-成功 091-检测码不正确
     * 2.获取用户ID
     * 调用地址:www.xxshequ.gov/appapi/getUserId.php
     * 错误码:000-成功 011-账号密码不正确 091-检测码不正确
     * 3.保存快件
     * 调用地址:www.xxshequ.gov/appapi/saveEmsInfo.php
     * 错误码:000-成功 011-查询失败 021-订单号已存在 031-用户ID不存在 091-检测码不正确
     * 4.检测码为字符串"xiaoxiong008"的md5 32位小写加密,可以在程序文件中找到
     * 调用文件代码中注释部分为调用实例
     * 如有问题 请联系 18792455303 或 qq33696109 留言 或 email 33696109@qq.com 均可
     */
    //获得用户ID
    public static final String GET_USER_ID= "http://www.xxshequ.com/appapi/getUserId.php";
    //获得快递公司
    public static final String COMPANY_LIST= "http://www.xxshequ.com/appapi/getCompanyList.php";

    public static final String EMS_LIST= "http://www.xxshequ.com/appapi/getEmsList.php";
    //保存快递信息
    public static final String SAVE_INFO = "http://www.xxshequ.com/appapi/saveEmsInfo.php";
}
