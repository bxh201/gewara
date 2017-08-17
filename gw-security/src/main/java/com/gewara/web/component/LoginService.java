package com.gewara.web.component;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;

import com.gewara.model.acl.GewaraUser;
import com.gewara.support.ErrorCode;

public interface LoginService {
	
	/** �����û��������¼
	 * @param request
	 * @param response
	 * @param username
	 * @param password
	 * @return Map(id,realname)
	 */
	ErrorCode<Map> autoLogin(HttpServletRequest request, HttpServletResponse response, String username, String password);

	/** ����sessid �� ip��ַ��¼
	 * @param ip
	 * @param sessid
	 * @return
	 */
	GewaraUser getLogonGewaraUserBySessid(String ip, String sessid);

	/**����sessid �� ip��ַ��ȡ��֤��Ϣ
	 * @param ip
	 * @param sessid
	 * @return
	 */
	Authentication loadAuthentication(String ip, String sessid);

	/** ����sessid�ж��Ƿ���gewara�û���¼
	 * @param request
	 * @param sessid
	 * @return
	 */
	boolean isGewaraUserLogon(HttpServletRequest request, String sessid);

	/** �����¼
	 * @param request
	 * @param response
	 * @param sessid
	 * @return
	 */
	ErrorCode<Map> ssoAutoLogin(HttpServletRequest request, HttpServletResponse response, String sessid);

	/** ����sessid
	 * @param sessid
	 */
	void removeInvalidSessid(String sessid);
}
