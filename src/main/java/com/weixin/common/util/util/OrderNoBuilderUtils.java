package com.weixin.common.util.util;



/**
 * title:订单ID生成器
 **/
public class OrderNoBuilderUtils {
    private static SnowflakeIdWorker snowflakeIdWorker=new SnowflakeIdWorker(10,10);
    private static long id = 0L;
//    private static String datePrefix = DateUtils.date2String(DateUtils.DATE_STRING_FORMAT_DAY3, new Date());//20170812

    /**
     * 生成正常订单ID
     *
     * @return
     */
//    public static String normalOrderNo() {
//        String flag = "00";
//        id = snowflakeIdWorker.nextId();
//        return datePrefix + flag + id;
//    }

    /**
     * 生成商品唯一编码
     *
     * @return
     */
    public static String getNewId() {
        id = snowflakeIdWorker.nextId();
        return id+"";
    }

    /**
     * 生成退单ID
     *
     * @return
     */
//    public static String returnOrderNo() {
//        String flag = "01";
//        id = snowflakeIdWorker.nextId();
//        return datePrefix + flag + id;
//    }

}
