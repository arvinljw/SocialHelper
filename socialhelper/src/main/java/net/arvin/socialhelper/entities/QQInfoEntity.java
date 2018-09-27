package net.arvin.socialhelper.entities;

/**
 * Created by arvinljw on 17/11/24 15:49
 * Function：
 * Desc：qq原生返回的数据信息
 */
public final class QQInfoEntity {
    private String is_yellow_year_vip;
    private int ret;
    private String figureurl_qq_1;
    private String figureurl_qq_2;
    private String nickname;
    private String yellow_vip_level;
    private String msg;
    private String figureurl_1;
    private String vip;
    private String level;
    private String figureurl_2;
    private String is_yellow_vip;
    private String gender;
    private String figureurl;

    private QQLoginResultEntity loginResultEntity;

    public String getIs_yellow_year_vip() {
        return is_yellow_year_vip;
    }

    public void setIs_yellow_year_vip(String is_yellow_year_vip) {
        this.is_yellow_year_vip = is_yellow_year_vip;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public String getFigureurl_qq_1() {
        return figureurl_qq_1;
    }

    public void setFigureurl_qq_1(String figureurl_qq_1) {
        this.figureurl_qq_1 = figureurl_qq_1;
    }

    public String getFigureurl_qq_2() {
        return figureurl_qq_2;
    }

    public void setFigureurl_qq_2(String figureurl_qq_2) {
        this.figureurl_qq_2 = figureurl_qq_2;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getYellow_vip_level() {
        return yellow_vip_level;
    }

    public void setYellow_vip_level(String yellow_vip_level) {
        this.yellow_vip_level = yellow_vip_level;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getFigureurl_1() {
        return figureurl_1;
    }

    public void setFigureurl_1(String figureurl_1) {
        this.figureurl_1 = figureurl_1;
    }

    public String getVip() {
        return vip;
    }

    public void setVip(String vip) {
        this.vip = vip;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getFigureurl_2() {
        return figureurl_2;
    }

    public void setFigureurl_2(String figureurl_2) {
        this.figureurl_2 = figureurl_2;
    }

    public String getIs_yellow_vip() {
        return is_yellow_vip;
    }

    public void setIs_yellow_vip(String is_yellow_vip) {
        this.is_yellow_vip = is_yellow_vip;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getFigureurl() {
        return figureurl;
    }

    public void setFigureurl(String figureurl) {
        this.figureurl = figureurl;
    }

    public QQLoginResultEntity getLoginResultEntity() {
        return loginResultEntity;
    }

    public void setLoginResultEntity(QQLoginResultEntity loginResultEntity) {
        this.loginResultEntity = loginResultEntity;
    }

    @Override
    public String toString() {
        return "QQInfoEntity{" +
                "is_yellow_year_vip='" + is_yellow_year_vip + '\'' +
                ", ret=" + ret +
                ", figureurl_qq_1='" + figureurl_qq_1 + '\'' +
                ", figureurl_qq_2='" + figureurl_qq_2 + '\'' +
                ", nickname='" + nickname + '\'' +
                ", yellow_vip_level='" + yellow_vip_level + '\'' +
                ", msg='" + msg + '\'' +
                ", figureurl_1='" + figureurl_1 + '\'' +
                ", vip='" + vip + '\'' +
                ", level='" + level + '\'' +
                ", figureurl_2='" + figureurl_2 + '\'' +
                ", is_yellow_vip='" + is_yellow_vip + '\'' +
                ", gender='" + gender + '\'' +
                ", figureurl='" + figureurl + '\'' +
                ", loginResultEntity=" + loginResultEntity +
                '}';
    }
}
