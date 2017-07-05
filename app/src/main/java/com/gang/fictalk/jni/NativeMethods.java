package com.gang.fictalk.jni;

public class NativeMethods {

	public static native int InitJniEnv(String dbDir, String dbPath);
	public static native int IsListenRun();
	/**
	 * @param lan : 0 中文；1英文
	 */
	public static native void SetLanguage(int lan);
	
	public static native String Login(String name, String pass, String uuid, int loginType, boolean mustpush);
	public static native String Logout();
	public static native String	Relogin();
	public static native void ToReConnect();
	public static native String	Register(String name, String pass, String phone, String reference);
	public static native String SearchUsers(String key);
	public static native String	UpdateUserInfo(long ID);
	public static native String AddFriend(long ID, String msg);
	public static native String AccpetFriend(long ID);
	public static native String RemoveFriend(long ID);
	public static native String GetFriends();
	public static native String GetUserLocal(long id);
	/**
	 * 获取user信息 本地不存在网络更新
	 * @param id
	 * @return
	 */
	public static native String GetUserInfo(long id);
	public static native String NewFriends();
	public static native int DeleteNewFriend(long msgID);
	public static native String GetNearbyPeople(float lon, float lat, int sex);
	public static native String ClearLocation();
	public static native String ChangeMark(long userID, String mark);
	public static native String ChangeHead(byte[] buf, int len);
	public static native String ChangeNick(String nick);
	public static native String ChangeSex(int sex);
	public static native String ChangePhone(String phone);
	public static native String ChangeQQ(long uID, String qq);
	public static native String ChangeEmail(long uID, String email);
	
	public static native String QueryWallet();
	public static native String ChangeUserCircleBackImg(byte[] photoBuf, int len);
	
	public static native String AddPushInfo(String token);
	
	/**
	 * 付款
	 * @param uID 用户ID
	 * @param money 金额
	 * @return
	 */
	public static native String PayMoney(int uID, float money, String pass);
	
	/**
	 * 清理缓存
	 * @return
	 */
	public static native int ClearCache();
	
	/**
	 * 
	 */
	public static native String GetChatList();
	public static native String GetChatMsg(long targetID, boolean isGroup, int offset, int len);
	public static native String GetChatMsgLast(long targetID, boolean isGroup, long tick);
	public static native void DeleteChatList(long chatID);
	public static native void ChatSetTop(long chatID, boolean top);
	public static native String SendMsg(long recvID, String msg, boolean isGroup);
	public static native String SendImageMsg(long recvID, boolean isGroup, byte[] imgbuf, int len);
	
	public static native int SetReaded(long targetID, boolean isGroup);
	public static native int SetReaded2(long msgID);
	
	/**
	 * 
	 */
	public static native String SendRedbag(String title, String payPass, float money,
			long recvID, int num, int type, boolean isGroup);
	public static native String RobRedbag(long bagID);
	public static native String GetRedbag(long bagID, long sendID);
	public static native String GetRedbagDetail(long bagID);
	public static native String SendAdBag(String title, String content, String pass, byte[] imgBuf, int imgLen, float money, int num,
			byte[] imgBuf2, int imgLen2, byte[] imgBuf3, int imgLen3);
	public static native String SendRedbagToActive(long bagID);
	public static native String GetRedBagSend(int time);
	public static native String GetRedBagRecv(int time);
	
	/**
	 * 
	 */
	public static native String CreateGroup(String name, String users);
	public static native String GetGroups();
	public static native String GetGroupInfoAndMember(long groupID);
	public static native String GetGroupInfoLocal(long groupID);
	public static native String GetGroupMember(long groupID);
	public static native String RenameGroup(long groupID, String name);
	public static native String GroupInvite(long groupID, String idlist);
	public static native String GroupRemove(long groupID, String idlist);
	public static native String GroupMute(long groupID, boolean isMute);
	public static native String GroupExit(long groupID);
	
	/**
	 * 
	 */
	public static native String SendCircle(String msg, Object[] itemArray, int itemLen);
	/**
	 * 查询朋友圈更新
	 * @return 0 ：无更新； >0 更新userID
	 */
	public static native long GetCircleLastUpdateUser();
	public static native String GetCircleList(int offset);
	public static native String GetCircleListByUser(long userID, int offset);
	public static native String GetCircleComment(long circleID);
	public static native String CommentCircle(long circleID, String msg, long userID, int type);
	public static native String RemoveCircle(long circleID);
	public static native String RemoveCircleComment(long commentID);
	
	/**
	 * gift
	 */
	public static native String GetMyGift();
	public static native String GetStoreGift();
	public static native String BuyGift(long giftID, int num, float money, String pass);
	public static native String SendGift(long giveTo, long giftID, int num, String msg);
	public static native String AcceptGift(long giftID);
	public static native String GetGiftInfo(long listID);
	
	/**
	 * 
	 */
	public static native String GetRes(String extra);
	
	
	/**
	 * 
	 */
	public static native String GetPhoneCards(int state, int offset, int len);
	public static native String SetPhoneCardUsed(int cardID);
	
	static {
		System.loadLibrary("hyc");
		System.loadLibrary("hydb");
		System.loadLibrary("fictalk");
	}
	
}
