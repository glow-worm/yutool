package glow.worm.common.enums;
/**
 * @author   wangyulong
 * @time    2019/05/05 16:59:10
 * @description 日期格式枚举 	
 */
public enum DateFormatEnum {

	/**
	 * 长日期格式 2019-04-30 12:00:00
	 */
	LONG_DATE("yyyy-MM-dd HH:mm:ss", "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}$"),
	
	/**
	 * 长日期无秒格式 2019-04-30 12:00
	 */
	LONG_DATE_NO_SECOND("yyyy-MM-dd HH:mm", "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}$"),
	
	/**
	 * 短日期格式 2019-04-30
	 */
	SHORT_DATE("yyyy-MM-dd", "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2}$"),
	
	/**
	 * 中国长日期格式 2019年04月30日 12时00分00秒
	 */
	CHINA_LONG_DATE("yyyy年MM月dd日 HH时mm分ss秒", "^[0-9]{4}[\u4E00-\u9FA5][0-9]{1,2}[\u4E00-\u9FA5][0-9]{1,2}[\u4E00-\u9FA5] [0-9]{2}[\u4E00-\u9FA5][0-9]{2}[\u4E00-\u9FA5][0-9]{2}[\u4E00-\u9FA5]$"),
	
	/**
	 * 中国长日期格式 2019年04月30日 12时00分
	 */
	CHINA_LONG_DATE_NO_SECOND("yyyy年MM月dd日 HH时mm分", "^[0-9]{4}[\u4E00-\u9FA5][0-9]{1,2}[\u4E00-\u9FA5][0-9]{1,2}[\u4E00-\u9FA5] [0-9]{2}[\u4E00-\u9FA5][0-9]{2}[\u4E00-\u9FA5]$"),

	/**
	 * 中国短日期格式 2019年04月30日
	 */
	CHINA_SHORT_DATE("yyyy年MM月dd日", "^[0-9]{4}[\u4E00-\u9FA5][0-9]{1,2}[\u4E00-\u9FA5][0-9]{1,2}[\u4E00-\u9FA5]$"),
	
	/**
	 * 长日期格式 2019/04/30 12:00:00
	 */
	SLASH_LONG_DATE("yyyy/MM/dd HH:mm:ss", "^[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}$"),
	
	/**
	 * 长日期无秒格式 2019/04/30 12:00
	 */
	SLASH_LONG_DATE_NO_SECOND("yyyy/MM/dd HH:mm", "^[0-9]{4}/[0-9]{1,2}/[0-9]{1,2} [0-9]{2}:[0-9]{2}$"),
	
	/**
	 * 短日期格式 2019/04/30 12:00
	 */
	SLASH_SHORT_DATE("yyyy/MM/dd", "^[0-9]{4}/[0-9]{1,2}/[0-9]{1,2}$"),
	
	/**
	 * 时间戳格式 2019-04-30 13:41:07.925
	 */
	TIME_STAMP("yyyy-MM-dd HH:mm:ss.SSS", "^[0-9]{4}-[0-9]{1,2}-[0-9]{1,2} [0-9]{2}:[0-9]{2}:[0-9]{2}:[0-9]{3}$"),
	
	/**
	 * 日期long值 1556606719696
	 */
	LONG_TIME("", "^[1-9][0-9]{12,}$");

	/*
	 * 日期格式化
	 */
	private String format;

	/*
	 * 日期格式化正则表达式
	 */
	private String regex;

	private DateFormatEnum(String format, String regex) {
		this.format = format;
		this.regex = regex;
	}
	
    /**
     * @author wangyulong
     * @time 2019/04/30 14:29:47
     * @param format
     * @return
     * @description 返回 具体的枚举
     */
    public static DateFormatEnum getEnumByCode(String format) {
        for (DateFormatEnum formatEnum : DateFormatEnum.values()) {
            if (formatEnum.getFormat().equals(format)) {
                return formatEnum;
            }
        }
        return null;
    }
    
    /**
     * @author wangyulong
     * @time 2019/04/30 15:43:23
     * @return
     * @description 返回支持的正则数组
     */
    public static DateFormatEnum[] getRegexs() {
    	return DateFormatEnum.values();
    }

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getRegex() {
		return regex;
	}

	public void setRegex(String regex) {
		this.regex = regex;
	}
}
