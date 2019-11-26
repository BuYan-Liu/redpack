package com.zhenglong.global;

public interface GlobalCode {
    class AStatus{
        public static final int UNMATCHED=1;//未匹配
        public static final int MATCHED=2;//已匹配
    }
    class Sex {
        public static final int FEMALE=1;//女
        public static final int MALE=2;//男
    }
    class Subscribe{
        public static final int SUBSCRIBE=1;//已订阅
        public static final int UNSUBSCRIBE=0;//未订阅
    }

    class RedPackStatus {
        public static final int UNSENDED=1;//未发
        public static final int SENDED=2;//已发
    }

    class Commission {
        public static final int NOTCOMMISSION=1;
        public static final int COMMISSION=2;
        public static int getCommission(String key) {
            if ("分佣".equals(key)) return COMMISSION;
            return NOTCOMMISSION;
        }
    }

}
