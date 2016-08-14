package com.lg.api;

/**
 * 用户相关操作
 * 
 * @author LuoYi
 *
 */
public interface UserAPI {

	/**
	 * 注册用户 <br>
	 * POST
	 * @param phoneFlag 是否手机号注册
	 * @param payload
	 *            <code>{"usercode":"${用户号}","userphone":"${用户手机号}" || "useremail":"${用户邮箱}","userpassword":"${密码}"}</code>
	 * @return
	 */
	Object createNewUserSingle(boolean phoneFlag, Object payload);

	/**
	 * 获取用户[单个] <br>
	 * GET
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object getUsersByUserCode(String userCode);

	/**
	 * 获取用户[用户号和密码] <br>
	 * GET
	 * 
	 * @param userCode
	 *            用户号
	 * @param userPassword
	 *            用户密码
	 * @return
	 */
	Object getUsersByUserCodeAndPassword(String userCode, String userPassword);

	/**
	 * 获取用户[批量]，参数为空时默认返回最早创建的10个用户 <br>
	 * GET
	 * 
	 * @param limit
	 *            单页获取数量
	 * @param cursor
	 *            游标，大于单页记录时会产生
	 * @return
	 */
	Object getUsersBatch(Long limit, String cursor);

	/**
	 * 删除用户[单个] <br>
	 * DELETE
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object deleteUserByUserCode(String userCode);

	/**
	 * 重置用户密码 <br>
	 * PUT
	 * 
	 * @param userCode
	 *            用户号
	 * @param payload
	 *            <code>{"newpassword" : "${新密码指定的字符串}"}</code>
	 * @return
	 */
	Object modifyUserPassword(String userCode, Object payload);

	/**
	 * 修改用户信息 <br>
	 * PUT
	 * 
	 * @param userCode
	 *            用户号
	 * @param payload
	 *            <code>{"username" : "${用户昵称}", "userphone":"手机号", ...}</code>
	 * @return
	 */
	Object modifyUserInfo(String userName, Object payload);

	/**
	 * 给用户添加好友 <br>
	 * POST
	 * 
	 * @param userCode
	 *            用户号
	 * @param friendCode
	 *            好友用户号
	 * @return
	 */
	Object addFriendSingle(String userCode, String friendCode);

	/**
	 * 解除用户的好友关系 <br>
	 * DELETE
	 * 
	 * @param userCode
	 *            用户号
	 * @param friendCode
	 *            好友用户号
	 * @return
	 */
	Object deleteFriendSingle(String userCode, String friendCode);

	/**
	 * 查看某个用户的好友信息 <br>
	 * GET
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object getFriends(String userCode);

	/**
	 * 获取用户的黑名单 <br>
	 * GET
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object getBlackList(String userCode);

	/**
	 * 往用户的黑名单中加人 <br>
	 * POST
	 * 
	 * @param userCode
	 *            用户号
	 * @param payload
	 *            <code>{"usercodes":["5cxhactgdj", "mh2kbjyop1"]}</code>
	 * @return
	 */
	Object addToBlackList(String userCode, Object payload);

	/**
	 * 从用户的黑名单中减人 <br>
	 * DELETE
	 * 
	 * @param userCode
	 *            用户号
	 * @param blackListCode
	 *            黑名单用户号
	 * @return
	 */
	Object removeFromBlackList(String userCode, String blackListCode);

	/**
	 * 用户账号禁用 <br>
	 * POST
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object deactivateUser(String userCode);

	/**
	 * 用户账号解禁 <br>
	 * POST
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object activateUser(String userCode);

	/**
	 * 强制用户下线 <br>
	 * GET
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object disconnectUser(String userCode);

	/**
	 * 获取用户参与的群组 <br>
	 * GET
	 * 
	 * @param userCode
	 *            用户号
	 * @return
	 */
	Object getUserAllChatGroups(String userCode);
}
