package com.qjq.crawler.download.zk;

public enum ZkParam {

    DEGRADE("degrad", "降级服务总开关"),
    
    WHEN_IS_NEWPRODUCT("new.product.day","N天内属于新品(单位：天数)"),

    OPEN_GOODSDETAIL("open_goodsdetail", "走老详情页接口"),

    OPEN_SEARCH("open_search", "走老搜索列表"),

    LIKEWHO("bi.bubugao-inc.com/Api/Recom/appItemGood", "猜你喜欢开关"),

    ZHLCOMMENT("ZHLComment", "黄致列评论开关"), COMMENT("comment", ""),

    PROFILEIMGURL("profileImgUrl", "头像图片"),

    NICKNAME("nickName", "昵称"),

    IMGURLCOMURLONE("imgUrlComUrlOne", "评论图片第一张图"),

    IMGURLCOMURLTWO("imgUrlComUrlTwo", "评论图片第二张图"),

    IMGURLCOMURLTHREE("imgUrlComUrlThree", "评论图片第三张图"),

    DATE("date", "2016-01-18"),

    TIME_JOB_RUN_MASTER("timeJob.model.masterRun", "使用master跑定时任务"),

    TIME_JOB_ACTIVE_ALL("timeJob.active.all", "定时任务总开关"),

    CACHE_ACTIVE_ALL("cache.active.all", "缓存总开关"),

    PRINT_DEBUG_LOG("print.debug.log", "打印外部服务调用链日志"),
    
    PRINT_RESULT_LOG("print.result.log", "打印外部服务返回数据"),

    PRINT_JOB_DEBUG_LOG("print.job.debug.log", "打印定时任务调用链日志"),

    TOTALPAGE("bmms.product.totalPage", "缓存分页总数"),

    FLUSH_SYSPARAMS("job.flush.sysparams.fixedrate", "系统参数定时任务刷新间隔时间"),

    FLUSH_BMMS("job.flush.bmms.fixedrate", "bmms定时任务刷新间隔时间"),

    CACHE_GLOBAL_IDNEX_EXTENDS("cache.global.index.extends", "全球购首页缓存失效时间"),

    CACHE_FRESH_IDNEX_EXTENDS("cache.fresh.index.extends", "生鲜首页缓存失效时间"),

    CACHE_COMMENT_INFO("cache.comment.info", "评论缓存时间"),

    CACHE_GOODS_EXTENDS("cache.goods.extends", "商品扩展信息缓存时间"),

    CACHE_GOODS_PLATE_SHOP("cache.goods.plate.shop", "店铺信息缓存时间"),

    CACHE_MEMBER_PRAISE("cache.member.praise", "会员点赞信息缓存时间"),

    CACHE_PRODUCT_DETAIL_GOODSID("cache.product.detail.goodid", "商品详细新缓存时间"),

    CACHE_PRODUCT_DETAIL_PRODUCTID("cache.product.detail.productid", "货品详细信息缓存时间"),

    CACHE_PRODUCT_INFO("cache.product.info", "货品信息缓存时间"),

    CAHCE_SHOP_INFO("cache.shop.info", "店铺信息缓存时间"),

    BUSSINESS_UMPSKUBATCHPROMITIONEXP("bussiness_umpskubatchpromitionexp", "biz批量查询ump信息缓存时间"),

    CACHE_CATEGORY_LIST("cache.category.list", "分类列表缓存时间"),

    EXPRESSION_BAOSHUI_STR("taxes.baoshui.str", "保税仓公式文案表达式"),
    
    EXPRESSION_HAIWAI_STR("taxes.haiwai.str", "海外直邮公式文案表达式"),
    
    EXPRESSION_BAOSHUI("taxes.baoshui.expression", "保税仓公式表达式"),
    
    EXPRESSION_HAIWAI("taxes.haiwai.expression", "海外直邮公式表达式"),
    
    POPUP_SWITCH("popup.switch","弹框开关"),
    
    POPUP_SIZE("popup.size","弹幕条数"),
	
    SHOP_IDS("yunhou.shop.id.list", "自营店铺ID"),

    POPUP_CACHE_UPDATE_JOB_TIME("popup.cache.update.job.time", "弹框缓存更新定时任务时间"),
    
    REPORT_WRITCH("report.switch","举报开关"),
	
    COMMENT_SWTICH("comment.switch","未购买是否可以评论"),
    
    REPUTATION_TOPICS_SHOW_NUM("reputation.topics.show.num","口碑首页热门话题显示个数"),
    
    REPUTATION_PRODUCTS_SHOW_NUM("reputation.products.show.num","口碑首页热门单品显示个数"),
    
    SECRT_TIMEOUT("secrt.timout","600000"),
    
    EMPTY_CART_HOT_PRODUCTS_NUM("empty.cart.hot.products.num","购物车为空时热门单品个数"),
    
    ACCESS_INTERFACE_NAME("access.interface.name","防刷接口列表"),

    ACCESS_INTERFACE_SWITCH("access.interface.switch","防刷接口开关"),
    
    ACCESS_INTERFACE_REDIS_NAME("access.interface.redis.name","防刷接口保存UUID接口列表"),
    
    ACCESS_INTERFACE_TIMEOUT("access.interface.timeout","防刷url在redis的过期时间");
    
    private String key;

    private String desc;

    private ZkParam(String key, String desc) {
        this.key = key;
        this.desc = desc;
    }

    public static ZkParam parseZkParam(String key) {
        for (ZkParam zkParam : ZkParam.values()) {
            if (zkParam.getKey().equals(key)) {
                return zkParam;
            }
        }
        throw new IllegalArgumentException(key + "无法解析为ZkParam类型.");
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }
}
