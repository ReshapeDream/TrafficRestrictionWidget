package com.neil.trafficrestrictionwidget;

public class TrMsg {


    /**
     * msg : {"tr":{"t0":{"dw":"周四","tr":"2和7"},"t1":{"dw":"周五","tr":"3和8"},"t2":{"dw":"周六","tr":"不限"}},"pm":{"area":"北京","pm":"36","pmt":"优","pub":"11:00更新"}}
     * code : 0
     */

    private MsgBean msg;
    private int code;

    public MsgBean getMsg() {
        return msg;
    }

    public void setMsg(MsgBean msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public static class MsgBean {
        /**
         * tr : {"t0":{"dw":"周四","tr":"2和7"},"t1":{"dw":"周五","tr":"3和8"},"t2":{"dw":"周六","tr":"不限"}}
         * pm : {"area":"北京","pm":"36","pmt":"优","pub":"11:00更新"}
         */

        private TrBean tr;
        private PmBean pm;

        public TrBean getTr() {
            return tr;
        }

        public void setTr(TrBean tr) {
            this.tr = tr;
        }

        public PmBean getPm() {
            return pm;
        }

        public void setPm(PmBean pm) {
            this.pm = pm;
        }

        public static class TrBean {
            /**
             * t0 : {"dw":"周四","tr":"2和7"}
             * t1 : {"dw":"周五","tr":"3和8"}
             * t2 : {"dw":"周六","tr":"不限"}
             */

            private T0Bean t0;
            private T1Bean t1;
            private T2Bean t2;

            public T0Bean getT0() {
                return t0;
            }

            public void setT0(T0Bean t0) {
                this.t0 = t0;
            }

            public T1Bean getT1() {
                return t1;
            }

            public void setT1(T1Bean t1) {
                this.t1 = t1;
            }

            public T2Bean getT2() {
                return t2;
            }

            public void setT2(T2Bean t2) {
                this.t2 = t2;
            }

            public static class T0Bean {
                /**
                 * dw : 周四
                 * tr : 2和7
                 */

                private String dw;
                private String tr;

                public String getDw() {
                    return dw;
                }

                public void setDw(String dw) {
                    this.dw = dw;
                }

                public String getTr() {
                    return tr;
                }

                public void setTr(String tr) {
                    this.tr = tr;
                }
            }

            public static class T1Bean {
                /**
                 * dw : 周五
                 * tr : 3和8
                 */

                private String dw;
                private String tr;

                public String getDw() {
                    return dw;
                }

                public void setDw(String dw) {
                    this.dw = dw;
                }

                public String getTr() {
                    return tr;
                }

                public void setTr(String tr) {
                    this.tr = tr;
                }
            }

            public static class T2Bean {
                /**
                 * dw : 周六
                 * tr : 不限
                 */

                private String dw;
                private String tr;

                public String getDw() {
                    return dw;
                }

                public void setDw(String dw) {
                    this.dw = dw;
                }

                public String getTr() {
                    return tr;
                }

                public void setTr(String tr) {
                    this.tr = tr;
                }
            }
        }

        public static class PmBean {
            /**
             * area : 北京
             * pm : 36
             * pmt : 优
             * pub : 11:00更新
             */

            private String area;
            private String pm;
            private String pmt;
            private String pub;

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }

            public String getPm() {
                return pm;
            }

            public void setPm(String pm) {
                this.pm = pm;
            }

            public String getPmt() {
                return pmt;
            }

            public void setPmt(String pmt) {
                this.pmt = pmt;
            }

            public String getPub() {
                return pub;
            }

            public void setPub(String pub) {
                this.pub = pub;
            }
        }
    }
}
