package glow.worm.common.utils;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import glow.worm.common.base.BaseCode;
import glow.worm.common.enums.DateFormatEnum;

/**
 * @author  wangyulong
 * @time    2019/04/30 11:44:34
 * @description 日期工具类
 */
public class DateUtil {  
	
	private final static Logger logger = LoggerFactory.getLogger(DateUtil.class);
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:05:25
	 * @param dateString
	 * @param formatRegex
	 * @return
	 * @description 校验字符串日期格式是否有误
	 */
	public static Boolean validateDateFormat(String dateString, String formatRegex) {
		if (StringUtils.isNotBlank(dateString)) {
			boolean regexResult = dateString.trim().matches(formatRegex);
		    if (!regexResult) {
		    	logger.error(">>>>>---------[" + dateString + "]日期格式有误---------<<<<<:{}", dateString);
		    	throw new RuntimeException("[" + dateString + "]日期格式有误");
			}else {
				return true;
			}
		}else {
			return false;
		}
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:06:18
	 * @param dateString
	 * @param dateFormatEnum
	 * @return
	 * @description 校验字符串日期格式是否有误
	 */
	public static Boolean validateDateFormat(String dateString, DateFormatEnum dateFormatEnum) {
		return validateDateFormat(dateString, dateFormatEnum.getRegex());
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:09:25
	 * @param dateString
	 * @return
	 * @description 校验并转换支持的日期到日期long值
	 *  支持的日期格式如下:
	 *  yyyy-MM-dd HH:mm:ss
	 *  yyyy-MM-dd HH:mm
	 *  yyyy-MM-dd
	 *  yyyy年MM月dd日 HH时mm分ss秒
	 *  yyyy年MM月dd日 HH时mm分
	 *  yyyy年MM月dd日
	 *  yyyy/MM/dd HH:mm:ss
	 *  yyyy/MM/dd HH:mm
	 *  yyyy/MM/dd
	 *  yyyy-MM-dd HH:mm:ss.SSS
	 *  1556606719696 - 如果本身是long值, 则返回原long值
	 */
	public static Long dateStringAutoToLongTime(String dateString) {
		if (StringUtils.isNotBlank(dateString)) {
			DateFormatEnum[] regexs = DateFormatEnum.getRegexs();
			if (regexs != null && regexs.length > 0) {
				Boolean result = false;
				for (DateFormatEnum item : regexs) {
					result = dateString.trim().matches(item.getRegex());
					if (result) {
						//如果是Long值的字符串直接转成Long类型返回
						if (StringUtils.isBlank(item.getFormat())) {
							return Long.valueOf(dateString);
						}
						return parseStringToLongTime(dateString, item.getFormat());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:17:59
	 * @param dateStr
	 * @return
	 * @description 校验并转换支持的日期到日期date
	 *  支持的日期格式如下:
	 *  yyyy-MM-dd HH:mm:ss
	 *  yyyy-MM-dd HH:mm
	 *  yyyy-MM-dd
	 *  yyyy年MM月dd日 HH时mm分ss秒
	 *  yyyy年MM月dd日 HH时mm分
	 *  yyyy年MM月dd日
	 *  yyyy/MM/dd HH:mm:ss
	 *  yyyy/MM/dd HH:mm
	 *  yyyy/MM/dd
	 *  yyyy-MM-dd HH:mm:ss.SSS
	 *  1556606719696 - 如果是long值, 则直接转date
	 */
	public static Date dateStringAutoToDate(String dateString) {
		if (StringUtils.isNotBlank(dateString)) {
			DateFormatEnum[] regexs = DateFormatEnum.getRegexs();
			if (regexs != null && regexs.length > 0) {
				Boolean result = false;
				for (DateFormatEnum item : regexs) {
					result = dateString.trim().matches(item.getRegex());
					if (result) {
						//如果是Long值的字符串直接转成Date类型返回
						if (StringUtils.isBlank(item.getFormat())) {
							return new Date(Long.valueOf(dateString));
						}
						return parseStringToDate(dateString, item.getFormat());
					}
				}
			}
		}
		return null;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:22:28
	 * @param dateString
	 * @param dateFormatEnum
	 * @return
	 * @description 日期字符串转日期long值
	 */
	public static Long dateStringToLongTime(String dateString, DateFormatEnum dateFormatEnum) {
		if (StringUtils.isNotBlank(dateString)) {
			Boolean result = validateDateFormat(dateString, dateFormatEnum);
			if (result) {
				return parseStringToLongTime(dateString, dateFormatEnum.getFormat());
			}
		}
		return null;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:23:52
	 * @param dateStr
	 * @param dateFormatEnum
	 * @return
	 * @description 日期字符串转date
	 */
	public static Date dateStringToDate(String dateString, DateFormatEnum dateFormatEnum) {
		if (StringUtils.isNotBlank(dateString)) {
			Boolean result = validateDateFormat(dateString, dateFormatEnum);
			if (result) {
				return parseStringToDate(dateString, dateFormatEnum.getFormat());
			}
		}
		return null;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:25:53
	 * @param dateString
	 * @param format
	 * @return
	 * @description 获取日期long值
	 */
	public static Long parseStringToLongTime(String dateString, String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	try {
    		if(StringUtils.isNotBlank(dateString)){
    			Date date = sdf.parse(dateString.trim());
    			return date.getTime();
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:26:08
	 * @param dateString
	 * @param dateFormatEnum
	 * @return
	 * @description 获取日期long值
	 */
	public static Long parseStringToLongTime(String dateString, DateFormatEnum dateFormatEnum) {
		 return parseStringToLongTime(dateString, dateFormatEnum.getFormat());
    }
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:26:44
	 * @param dateString
	 * @param format
	 * @return
	 * @description 获取日期Date
	 */
	public static Date parseStringToDate(String dateString, String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	try {
    		if(StringUtils.isNotBlank(dateString)){
    			return sdf.parse(dateString.trim());
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return null;
    }
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 15:27:11
	 * @param dateString
	 * @param dateFormatEnum
	 * @return
	 * @description 获取日期Date
	 */
	public static Date parseStringToDate(String dateString, DateFormatEnum dateFormatEnum) {
    	return parseStringToDate(dateString, dateFormatEnum.getFormat());
    }
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:05:02
	 * @param date
	 * @param format
	 * @return
	 * @description 解析日期到字符串
	 */
	public static String parseDateToString(Date date, String format) {
    	SimpleDateFormat sdf = new SimpleDateFormat(format);
    	String result = null;
    	try {
    		if(date != null && format != null){
    			result = sdf.format(date);
    		}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	return result;
    }
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:07:13
	 * @param date
	 * @param dateFormatEnum
	 * @return
	 * @description 日期转成字符串
	 */
	public static String parseDateToString(Date date, DateFormatEnum dateFormatEnum) {
		if (StringUtils.isBlank(dateFormatEnum.getFormat())) {
			return BaseCode.EMPTY_STRING + date.getTime();
		}
		return parseDateToString(date, dateFormatEnum.getFormat());
    }
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:11:07
	 * @param dateOne
	 * @param dateTwo
	 * @param compareUnit - 单位字段，从Calendar field取值
	 * @return 等于返回0, 大于返回大于0的值 小于返回小于0的值
	 * @description 根据单位字段比较两个日期
	 */
	public static int compareDate(Date dateOne, Date dateTwo, int compareUnit) {
		Calendar dateCalOne = Calendar.getInstance();
		dateCalOne.setTime(dateOne);
		Calendar dateCalTwo = Calendar.getInstance();
		dateCalTwo.setTime(dateTwo);
		switch (compareUnit) {
		case Calendar.YEAR:
			dateCalOne.clear(Calendar.MONTH);
			dateCalTwo.clear(Calendar.MONTH);
		case Calendar.MONTH:
			dateCalOne.set(Calendar.DATE, 1);
			dateCalTwo.set(Calendar.DATE, 1);
		case Calendar.DATE:
			dateCalOne.set(Calendar.HOUR_OF_DAY, 0);
			dateCalTwo.set(Calendar.HOUR_OF_DAY, 0);
		case Calendar.HOUR:
			dateCalOne.clear(Calendar.MINUTE);
			dateCalTwo.clear(Calendar.MINUTE);
		case Calendar.MINUTE:
			dateCalOne.clear(Calendar.SECOND);
			dateCalTwo.clear(Calendar.SECOND);
		case Calendar.SECOND:
			dateCalOne.clear(Calendar.MILLISECOND);
			dateCalTwo.clear(Calendar.MILLISECOND);
		case Calendar.MILLISECOND:
			break;
		default:
			throw new IllegalArgumentException("compareUnit 单位字段 [" + compareUnit + "] 不合法!");
		}
		return dateCalOne.compareTo(dateCalTwo);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:13:21
	 * @param timeOne
	 * @param timeTwo
	 * @param compareUnit - 单位字段，从Calendar field取值
	 * @return 等于返回0值, 大于返回大于0的值 小于返回小于0的值
	 * @description 根据单位字段比较两个时间
	 */
	public static int compareTime(Date timeOne, Date timeTwo, int compareUnit) {
		Calendar dateCalOne = Calendar.getInstance();
		dateCalOne.setTime(timeOne);
		Calendar dateCalTwo = Calendar.getInstance();
		dateCalTwo.setTime(timeTwo);

		dateCalOne.clear(Calendar.YEAR);
		dateCalOne.clear(Calendar.MONTH);
		dateCalOne.set(Calendar.DATE, 1);
		dateCalTwo.clear(Calendar.YEAR);
		dateCalTwo.clear(Calendar.MONTH);
		dateCalTwo.set(Calendar.DATE, 1);
		switch (compareUnit) {
		case Calendar.HOUR:
			dateCalOne.clear(Calendar.MINUTE);
			dateCalTwo.clear(Calendar.MINUTE);
		case Calendar.MINUTE:
			dateCalOne.clear(Calendar.SECOND);
			dateCalTwo.clear(Calendar.SECOND);
		case Calendar.SECOND:
			dateCalOne.clear(Calendar.MILLISECOND);
			dateCalTwo.clear(Calendar.MILLISECOND);
		case Calendar.MILLISECOND:
			break;
		default:
			throw new IllegalArgumentException("compareUnit 单位字段 [" + compareUnit + "] 不合法!");
		}
		return dateCalOne.compareTo(dateCalTwo);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:14:11
	 * @return
	 * @description 获得当前的时间戳
	 */
	public static Timestamp nowTimeStamp() {
		return new Timestamp(System.currentTimeMillis());
	}

	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:14:28
	 * @param tmp
	 * @param format
	 * @return
	 * @description 时间戳转日期字符串
	 */
	public static String timestampToDateString(Timestamp tmp, String format) {
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(tmp);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/05/05 16:15:11
	 * @param tmp
	 * @param dateFormatEnum
	 * @return
	 * @description 时间戳转日期字符串
	 */
	public static String timestampToDateString(Timestamp tmp, DateFormatEnum dateFormatEnum) {
		if (StringUtils.isBlank(dateFormatEnum.getFormat())) {
			return BaseCode.EMPTY_STRING + tmp.getTime();
		}
		SimpleDateFormat sdf = new SimpleDateFormat(dateFormatEnum.getFormat());
		return sdf.format(tmp);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/30 16:47:30
	 * @param date
	 * @return
	 * @description 获取day的起始时间点
	 */
	public static Date getDayStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 16:48:09
	 * @param date
	 * @return
	 * @description 获取day的终止时间点
	 */
	public static Date getDayEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 16:50:06
	 * @param date
	 * @param day
	 * @return
	 * @description 计算day天后的时间
	 */
	public static Date getAddDate(Date date, int day) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, day);
		return calendar.getTime();
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:10:52
	 * @param date
	 * @return
	 * @description 获取当前周起始时间
	 */
	public static Date getWeekStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.get(Calendar.WEEK_OF_YEAR);
		int firstDay = calendar.getFirstDayOfWeek();
		calendar.set(Calendar.DAY_OF_WEEK, firstDay);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:10:35
	 * @param date
	 * @return
	 * @description 获取当前周截止时间
	 */
	public static Date getWeekEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.get(Calendar.WEEK_OF_YEAR);
		int firstDay = calendar.getFirstDayOfWeek();
		calendar.set(Calendar.DAY_OF_WEEK, 8 - firstDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:10:13
	 * @param date
	 * @return
	 * @description 获取month的开始时间点
	 */
	public static Date getMonthStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/30 16:50:54
	 * @param date
	 * @return
	 * @description 获取month的终止时间点
	 */
	public static Date getMonthEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MONTH, 1);
		calendar.add(Calendar.MILLISECOND, -1);
		return calendar.getTime();
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:03:07
	 * @param dateStr
	 * @return
	 * @description 日期字符串转时间戳
	 */
	public static Timestamp dateStringToTimestamp(String dateString) {
		return Timestamp.valueOf(dateString);
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:06:30
	 * @return
	 * @description  获取当前日期的星期
	 */
	public static int getWeek() {
		Calendar cal = Calendar.getInstance();
		cal.setTime(new Date());
		int w = cal.get(Calendar.DAY_OF_WEEK);
		return w;
	}
	
	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:25:31
	 * @param date
	 * @return
	 * @description 获取当前年起始时间
	 */
	public static Date getYearStart(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:26:14
	 * @param date
	 * @return
	 * @description 获取当前年最后一天
	 */
	public static Date getYearEnd(Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.set(Calendar.YEAR, calendar.get(Calendar.YEAR));
		calendar.set(Calendar.MONTH, 11);
		calendar.set(Calendar.DAY_OF_MONTH, 31);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:27:40
	 * @param date
	 * @return
	 * @description 获取月天数
	 */
	public static int getDayOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getActualMaximum(Calendar.DAY_OF_MONTH);
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:28:01
	 * @param date
	 * @return
	 * @description 获取月第一天
	 */
	public static Date getFirstDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMinimum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

	/**
	 * @author wangyulong
	 * @time 2019/04/30 17:28:36
	 * @param date
	 * @return
	 * @description 获取月最后一天
	 */
	public static Date getLastDateOfMonth(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
		return c.getTime();
	}

}
