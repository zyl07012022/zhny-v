package com.alonginfo.project.component.entities;



public class InspectionDetails {

    /**
     * 主键
     */
    private String guid;
    /**
     * 巡检计划名称
     */
    private String patrolSchemeName;
    /**
     * 巡检计划编号
     */
    private String patrolSchemeNumber;
    /**
     * 巡检时间
     */
    private String patrolData;
    /**
     * 巡检人员名称
     */
    private String patrolPerson;
    /**
     * 绝缘地毯是否完好（0 否 1是）
     */

    private String jydt;
    /**
     * 门窗是否完好（0 否 1是）
     */
    private String mc;
    /**
     * 建筑物周围是否存在隐患（0 否 1是）
     */
    private String jzw;
    /**
     * 房屋/设备基础有无下沉（0 否 1是）
     */
    private String fwsb;

    /**
     * 户外设备箱体有无锈蚀、变形（0 否 1是）
     */
    private String hwsb;
    /**
     * 室内照明是否完好（0 否 1是）
     */
    private String snzm;

    /**
     * 门锁是否损坏（0 否 1是）
     */
    private String mssh;
    /**
     * 房屋是否有积水、渗水（0 否 1是）
     */
    private String fwjs;
    /**
     * 小动物挡板是否齐全（0 否 1是）
     */
    private String xdwdb;
    /**
     * 室内是否清洁（0 否 1是）
     */
    private String snqj;
    /**
     * 有无异声（0 否 1是）
     */
    private String ywys;
    /**
     * 是否有异味（0 否 1是）
     */
    private String sfyw;
    /**
     * 室内温度是否正常（0 否 1是）
     */
    private String snwd;
    /**
     * 封堵是否完好（0 否 1是）
     */
    private String fdwh;
    /**
     * 电缆盖板有无破损缺失（0 否 1是）
     */
    private String dlgb;
    /**
     * 电缆沟是否有积水（0 否 1是）
     */
    private String dlgjs;
    /**
     * 监控除湿空调等设备是否正常（0 否 1是）
     */
    private String jksb;
    /**
     * 是否有模拟图板（0 否 1是）
     */
    private String mntb;
    /**
     * 室内消防、安全工具是否齐全（0 否 1是）
     */
    private String snxfaq;
    /**
     * 其他异常
     */
    private String otheryc;
    /**
     * 开关实际运行状态是否与运行方式（图纸）、模拟图板相符（0 否 1是）
     */
    private String kgzt;
    /**
     * 开关柜编号、名称是否齐全（0 否 1是）
     */
    private String kgbh;
    /**
     * 各种仪表、保护装置、信号装置是否正常（0 否 1是）
     */
    private String ybzz;
    /**
     * 开关防误闭锁是否完好、柜门关闭是否正常、油漆有无剥落（0 否 1是）
     */
    private String kgfh;
    /**
     * 是否有异常放电声（写明编号），有无过热变色、烧熔现象（0 否 1 是）
     */
    private String ycfd;
    /**
     * 是否有异常气味（写明编号）
     */
    private String ycqw;
    /**
     * 设备有无凝露，加热器或除湿装置是否投入正常（0 否 1是）
     */
    private String sbsl;
    /**
     * 各类挂锁是否正确（0 否 1是）
     */
    private String gssfzc;
    /**
     * 外露接地装置是否完好（0 否 1是）
     */
    private String wljd;
    /**
     * 通过观察窗开关柜内电缆终端情况（0 否 1是）
     */
    private String dlzdqk;
    /**
     * 铭牌及各种标志是否齐全、清晰（0 否 1是）
     */
    private String mpbq;
    /**
     * 带电显示器是否完好（写明编号）（0 否 1是）
     */
    private String ddxsq;
    /**
     * 变压器标签、标志是否齐全（0 否 1是）
     */
    private String byqbz;
    /**
     * 风扇手动设置是否正常、有无异声（0 否 1是）
     */
    private String fssfsz;
    /**
     * 温控器是否完好（0 否 1是）
     */
    private String wkzswh;
    /**
     * 电缆接头处是否有放电、锈蚀、过热痕迹（0 否 1是）
     */
    private String dljt;
    /**
     * 螺栓是否完整（0 否 1是）
     */
    private String lswz;

    /**
     * 有无异味、异声（0 否 1是）
     */
    private String ywywys;
    /**
     * 低压开关、母联开关三锁两钥匙是否完好（0 否 1是）
     */
    private String dymlss;
    /**
     * 低压柜正反面标签是否缺失（0 否 1是）
     */
    private String dygzfm;
    /**
     * 仪表测量是否良好（0 否 1是）
     */
    private String ybcl;

    /**
     * 分合闸指示是否良好（0 否 1是）
     */
    private String fhzzs;
    /**
     * 低压电缆连接处是否有放电、锈蚀、过热现象（0 否 1是）
     */
    private String dydllj;
    /**
     * 有无异味、异声（0 否 1是）
     */
    private String ysywyw;

    public String getGuid() {
        return guid;
    }

    public void setGuid(String guid) {
        this.guid = guid;
    }

    public String getPatrolSchemeName() {
        return patrolSchemeName;
    }

    public void setPatrolSchemeName(String patrolSchemeName) {
        this.patrolSchemeName = patrolSchemeName;
    }

    public String getPatrolSchemeNumber() {
        return patrolSchemeNumber;
    }

    public void setPatrolSchemeNumber(String patrolSchemeNumber) {
        this.patrolSchemeNumber = patrolSchemeNumber;
    }

    public String getPatrolData() {
        return patrolData;
    }

    public void setPatrolData(String patrolData) {
        this.patrolData = patrolData;
    }

    public String getPatrolPerson() {
        return patrolPerson;
    }

    public void setPatrolPerson(String patrolPerson) {
        this.patrolPerson = patrolPerson;
    }

    public String getJydt() {
        return jydt;
    }
    public void setJydt(String jydt) {
        this.jydt = jydt;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getJzw() {
        return jzw;
    }

    public void setJzw(String jzw) {
        this.jzw = jzw;
    }

    public String getFwsb() {
        return fwsb;
    }

    public void setFwsb(String fwsb) {
        this.fwsb = fwsb;
    }

    public String getHwsb() {
        return hwsb;
    }

    public void setHwsb(String hwsb) {
        this.hwsb = hwsb;
    }

    public String getSnzm() {
        return snzm;
    }

    public void setSnzm(String snzm) {
        this.snzm = snzm;
    }

    public String getMssh() {
        return mssh;
    }

    public void setMssh(String mssh) {
        this.mssh = mssh;
    }

    public String getFwjs() {
        return fwjs;
    }

    public void setFwjs(String fwjs) {
        this.fwjs = fwjs;
    }

    public String getXdwdb() {
        return xdwdb;
    }

    public void setXdwdb(String xdwdb) {
        this.xdwdb = xdwdb;
    }

    public String getSnqj() {
        return snqj;
    }

    public void setSnqj(String snqj) {
        this.snqj = snqj;
    }

    public String getYwys() {
        return ywys;
    }

    public void setYwys(String ywys) {
        this.ywys = ywys;
    }

    public String getSfyw() {
        return sfyw;
    }

    public void setSfyw(String sfyw) {
        this.sfyw = sfyw;
    }

    public String getSnwd() {
        return snwd;
    }

    public void setSnwd(String snwd) {
        this.snwd = snwd;
    }

    public String getFdwh() {
        return fdwh;
    }

    public void setFdwh(String fdwh) {
        this.fdwh = fdwh;
    }

    public String getDlgb() {
        return dlgb;
    }

    public void setDlgb(String dlgb) {
        this.dlgb = dlgb;
    }

    public String getDlgjs() {
        return dlgjs;
    }

    public void setDlgjs(String dlgjs) {
        this.dlgjs = dlgjs;
    }

    public String getJksb() {
        return jksb;
    }

    public void setJksb(String jksb) {
        this.jksb = jksb;
    }

    public String getMntb() {
        return mntb;
    }

    public void setMntb(String mntb) {
        this.mntb = mntb;
    }

    public String getSnxfaq() {
        return snxfaq;
    }

    public void setSnxfaq(String snxfaq) {
        this.snxfaq = snxfaq;
    }

    public String getOtheryc() {
        return otheryc;
    }

    public void setOtheryc(String otheryc) {
        this.otheryc = otheryc;
    }

    public String getKgzt() {
        return kgzt;
    }

    public void setKgzt(String kgzt) {
        this.kgzt = kgzt;
    }

    public String getKgbh() {
        return kgbh;
    }

    public void setKgbh(String kgbh) {
        this.kgbh = kgbh;
    }

    public String getYbzz() {
        return ybzz;
    }

    public void setYbzz(String ybzz) {
        this.ybzz = ybzz;
    }

    public String getKgfh() {
        return kgfh;
    }

    public void setKgfh(String kgfh) {
        this.kgfh = kgfh;
    }

    public String getYcfd() {
        return ycfd;
    }

    public void setYcfd(String ycfd) {
        this.ycfd = ycfd;
    }

    public String getYcqw() {
        return ycqw;
    }

    public void setYcqw(String ycqw) {
        this.ycqw = ycqw;
    }

    public String getSbsl() {
        return sbsl;
    }

    public void setSbsl(String sbsl) {
        this.sbsl = sbsl;
    }

    public String getGssfzc() {
        return gssfzc;
    }

    public void setGssfzc(String gssfzc) {
        this.gssfzc = gssfzc;
    }

    public String getWljd() {
        return wljd;
    }

    public void setWljd(String wljd) {
        this.wljd = wljd;
    }

    public String getDlzdqk() {
        return dlzdqk;
    }

    public void setDlzdqk(String dlzdqk) {
        this.dlzdqk = dlzdqk;
    }

    public String getMpbq() {
        return mpbq;
    }

    public void setMpbq(String mpbq) {
        this.mpbq = mpbq;
    }

    public String getDdxsq() {
        return ddxsq;
    }

    public void setDdxsq(String ddxsq) {
        this.ddxsq = ddxsq;
    }

    public String getByqbz() {
        return byqbz;
    }

    public void setByqbz(String byqbz) {
        this.byqbz = byqbz;
    }

    public String getFssfsz() {
        return fssfsz;
    }

    public void setFssfsz(String fssfsz) {
        this.fssfsz = fssfsz;
    }

    public String getWkzswh() {
        return wkzswh;
    }

    public void setWkzswh(String wkzswh) {
        this.wkzswh = wkzswh;
    }

    public String getDljt() {
        return dljt;
    }

    public void setDljt(String dljt) {
        this.dljt = dljt;
    }

    public String getLswz() {
        return lswz;
    }

    public void setLswz(String lswz) {
        this.lswz = lswz;
    }

    public String getYwywys() {
        return ywywys;
    }

    public void setYwywys(String ywywys) {
        this.ywywys = ywywys;
    }

    public String getDymlss() {
        return dymlss;
    }

    public void setDymlss(String dymlss) {
        this.dymlss = dymlss;
    }

    public String getDygzfm() {
        return dygzfm;
    }

    public void setDygzfm(String dygzfm) {
        this.dygzfm = dygzfm;
    }

    public String getYbcl() {
        return ybcl;
    }

    public void setYbcl(String ybcl) {
        this.ybcl = ybcl;
    }

    public String getFhzzs() {
        return fhzzs;
    }

    public void setFhzzs(String fhzzs) {
        this.fhzzs = fhzzs;
    }

    public String getDydllj() {
        return dydllj;
    }

    public void setDydllj(String dydllj) {
        this.dydllj = dydllj;
    }

    public String getYsywyw() {
        return ysywyw;
    }

    public void setYsywyw(String ysywyw) {
        this.ysywyw = ysywyw;
    }

}

