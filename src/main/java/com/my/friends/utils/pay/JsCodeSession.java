package com.my.friends.utils.pay;


/**
 * 网页授权信息
 * @author Administrator
 *
 */
public class JsCodeSession {
	
	//会话密钥
	private  String session_key;

	//用户标识
	private String openId;

	public String getUnionid() {
		return unionid;
	}

	public void setUnionid(String unionid) {
		this.unionid = unionid;
	}

	//用户标识
	private String unionid;

	public String getSession_key() {
		return session_key;
	}

	public void setSession_key(String session_key) {
		this.session_key = session_key;
	}

	public String getOpenId() {
		return openId;
	}

	public void setOpenId(String openId) {
		this.openId = openId;
	}
}
