package org.hnust.constant;

public class ItemConstant {
    private Integer status;     // 0代表未审核，1代表审核失败，2代表未解决，3代表已解决

    public static final Integer WAITING = 0;
    public static final Integer FAILED = 1;
    public static final Integer NOT_SOLVED = 2;
    public static final Integer SOLVED = 3;
}
