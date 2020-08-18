package com.zhcw.lib.utils.manager;


import com.zhcw.lib.base.BaseActivity;

/**
 *  activity 栈
 */
public class ActivityStackManager {

	private volatile java.util.Stack<BaseActivity> activityStack = new java.util.Stack<>();
	private static volatile ActivityStackManager instance;

	private ActivityStackManager() {

	}

	public static ActivityStackManager getScreenManager() {
		if (instance == null) {
			instance = new ActivityStackManager();
		}
		return instance;

	}

	public BaseActivity getActivity(Class<?> cls) {

		for (int i = 0; i < activityStack.size(); i++) {
			BaseActivity activity = activityStack.get(i);
			if (activity.getClass().equals(cls)) {
				return activity;
			}
		}
		return null;

	}

	// 退出栈顶Activity
	public void popActivity() {
		popActivity(currentActivity());
	}

	// 默认清栈
	public void popActivity(BaseActivity activity) {
		popActivity(activity ,false);
	}

	// 手动清栈，finish activity
	public void popActivity(BaseActivity activity, boolean quit) {
		if (activity != null) {
			if(quit) activity.finish();
			activityStack.remove(activity);
			activity = null;
		}
	}

	// 获得当前栈顶Activity
	public BaseActivity currentActivity() {
		BaseActivity activity = null;
		if (!activityStack.empty())
		{
			activity = activityStack.lastElement();
		}
		return activity;
	}

	// 将当前Activity推入栈中
	public void pushActivity(BaseActivity activity) {
		activityStack.add(activity);
	}

	public boolean isHave(Class<?> cls) {
		if (activityStack == null) {
			return false;
		}
		boolean ishave = false;
		for (int i = 0; i < activityStack.size(); i++) {
			BaseActivity activity = activityStack.get(i);
			if (activity.getClass().equals(cls)) {
				return true;
			}
		}
		return ishave;
	}

	public BaseActivity popAllActivityExceptOne(Class<?> cls) {
		boolean bquit = true;
		while (bquit) {
			BaseActivity activity = currentActivity();
			if (null != activity) {
				if (activity.getClass().equals(cls)) {
					return activity;
				}
				if (bquit) {
					popActivity(activity, true);
				}
			} else {
				bquit = false;
			}
		}
		return null;
	}



}