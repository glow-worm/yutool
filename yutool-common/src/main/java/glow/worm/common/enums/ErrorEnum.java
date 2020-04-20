package glow.worm.common.enums;

/**
 * @author  wangyulong
 * @time    2019/04/29 15:51:39
 * @description  错误枚举	
 */
public enum ErrorEnum {
	
	SYSTEM_ERROR(100010, "设置缓存记录出错");


    /**
     * @author  wangyulong
     * @params  Integer code
     * @return  ErrEnum
     * @功能描述    返回 具体的枚举，例如：WECHAT_PROXY_REQUEST_SUCCESS
     */
    public static ErrorEnum getEnumByCode(Integer code) {
        for (ErrorEnum statusEnum : ErrorEnum.values()) {
            if (statusEnum.getCode().equals(code)) {
                return statusEnum;
            }
        }
        return null;
    }

	/**
	 * 状态码
	 */
	private Integer code;

	/**
	 * 消息
	 */
	private String msg;

    private ErrorEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

}
