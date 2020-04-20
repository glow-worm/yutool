package glow.worm.common.enums;

/**
 * @author  wangyulong
 * @time    2019/04/29 15:55:27
 * @description 过期时间类型枚举
 */
public enum TimeTypeEnum {
    
	/**
	 * 秒钟
	 */
	SECONDS(1),
	
	/**
	 * 分钟
	 */
	MINUTES(60),
	
	/**
	 * 小时
	 */
	HOURS(3600),
	
	/**
	 * 天
	 */
	DAY(86400),
	
	/**
	 * 周-默认7天
	 */
	WEEKS(604800),

	/**
	 * 月-默认30天
	 */
	MONTH(2592000),
	
	/**
	 * 年-默认365天
	 */
	YEARS(31536000);

	/*
	 * 类型value
	 */
	private int value;

	private TimeTypeEnum(int value) {
		this.value = value;
	}

	public int getValue() {
		return value;
	}
	
}
