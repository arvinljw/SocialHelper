package net.arvin.socialhelper.entities;

/**
 * Created by arvinljw on 2018/10/24 09:44
 * Function：
 * Desc：
 */
public class QQUnionIdEntity {

    /**
     * client_id : YOUR_APPID
     * openid : YOUR_OPENID
     * unionid : YOUR_UNIONID
     */

    private String client_id;
    private String openid;
    private String unionid;
    /**
     * error : 100016
     * error_description : access token check failed
     */

    private int error;
    private String error_description;

    public String getClient_id() {
        return client_id;
    }

    public void setClient_id(String client_id) {
        this.client_id = client_id;
    }

    public String getOpenid() {
        return openid;
    }

    public void setOpenid(String openid) {
        this.openid = openid;
    }

    public String getUnionid() {
        return unionid;
    }

    public void setUnionid(String unionid) {
        this.unionid = unionid;
    }

    public int getError() {
        return error;
    }

    public void setError(int error) {
        this.error = error;
    }

    public String getError_description() {
        return error_description;
    }

    public void setError_description(String error_description) {
        this.error_description = error_description;
    }
}
