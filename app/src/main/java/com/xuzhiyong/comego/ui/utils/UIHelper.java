package com.xuzhiyong.comego.ui.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Dialog;
import android.content.ClipData;
import android.content.Context;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.duowan.ada.module.AdaConfig;
import com.duowan.ada.module.DConst;
import com.duowan.ada.module.http.HttpHelper;
import com.duowan.fw.Module;
import com.duowan.fw.ThreadBus;
import com.duowan.fw.util.JFileUtils;
import com.duowan.fw.util.JLog;
import com.duowan.fw.util.JStringUtils;
import com.duowan.fw.util.JVer;
import com.duowan.fw.util.JVersionUtil;

import org.xmlpull.v1.XmlPullParserException;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.duowan.ada.module.AdaConfig.config;

/**
 * ui的一些辅助方法
 *
 * @author yujian
 */
public class UIHelper {

	@SuppressWarnings("deprecation")
	@TargetApi(11)
	public static void copyText(String str) {
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager) Module.gMainContext.
					getSystemService(Context.CLIPBOARD_SERVICE);
			clipboard.setText(str);
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
					Module.gMainContext.
							getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clip = ClipData.newPlainText("text label", str);
			clipboard.setPrimaryClip(clip);
		}
	}

	@SuppressWarnings("deprecation")
	@TargetApi(11)
	private String getClipboardText() {
		String str = null;
		int sdk = android.os.Build.VERSION.SDK_INT;
		if (sdk < android.os.Build.VERSION_CODES.HONEYCOMB) {
			android.text.ClipboardManager clipboard = (android.text.ClipboardManager)
					Module.gMainContext.getSystemService(Context.CLIPBOARD_SERVICE);
			if (null != clipboard.getText()) {
				str = clipboard.getText().toString();
			}
		} else {
			android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
					Module.gMainContext.getSystemService(Context.CLIPBOARD_SERVICE);
			ClipData clipData = clipboard.getPrimaryClip();
			if (null != clipData && clipData.getItemCount() > 0) {
				str = clipData.getItemAt(0).coerceToText(Module.gMainContext).toString();
			}
		}
		return str;
	}

	public static DisplayMetrics getDispalyMetrics(Context context) {
		return context.getResources().getDisplayMetrics();
	}

	public static float getDensity(Context context) {
		return context.getResources().getDisplayMetrics().density;
	}

	public static int getStatusBarHeight() {
		try {
			Class<?> cls = Class.forName("com.android.internal.R$dimen");

			Object obj = cls.newInstance();

			Field field = cls.getField("status_bar_height");

			int x = Integer.parseInt(field.get(obj).toString());

			return Module.gMainContext.getResources().getDimensionPixelSize(x);
		} catch (Exception e) {
			JLog.error(null, "get status bar height fail");
		}
		return 0;
	}

	public static int dip2px(float value) {
		final float f = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
				value, getDispalyMetrics(Module.gMainContext));
		final int res = (int) (f + 0.5f);
		if (res != 0) return res;
		if (value == 0) return 0;
		if (value > 0) return 1;
		return -1;
	}

	public static int px2dip(float pxValue) {
		final float scale = getDensity(Module.gMainContext);
		return (int) (pxValue / scale + 0.5f);
	}

	public static int sp2px(float spValue) {
		final float fontScale = Module.gMainContext.getResources().getDisplayMetrics().scaledDensity;
		return (int) (spValue * fontScale + 0.5f);
	}

	@SuppressWarnings("deprecation")
	public static void measureView(View child) {
		ViewGroup.LayoutParams p = child.getLayoutParams();
		if (p == null) {
			p = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.FILL_PARENT,
					ViewGroup.LayoutParams.WRAP_CONTENT);
		}

		int childWidthSpec = ViewGroup.getChildMeasureSpec(0, 0 + 0, p.width);
		int lpHeight = p.height;
		int childHeightSpec;

		if (lpHeight > 0) {
			childHeightSpec = MeasureSpec.makeMeasureSpec(lpHeight, MeasureSpec.EXACTLY);
		} else {
			childHeightSpec = MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED);
		}
		child.measure(childWidthSpec, childHeightSpec);
	}

	public static void setListViewHeightBasedOnChildrenCount(ListView listView, int showItemCount) {
		ListAdapter listAdapter = listView.getAdapter();
		if (listAdapter == null) {
			return;
		}

		int totalHeight = 0;
		int realCount = Math.min(showItemCount, listAdapter.getCount());
		for (int i = 0; i < realCount; i++) {
			View listItem = listAdapter.getView(i, null, listView);
			measureView(listItem);
			totalHeight += listItem.getMeasuredHeight();
		}

		ViewGroup.LayoutParams params = listView.getLayoutParams();
		params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
		listView.setLayoutParams(params);
	}

	public static final boolean sIsMiui;

	static {
		sIsMiui = isMiui();
	}

	private static boolean isMiui() {
		String line = null;
		BufferedReader input = null;
		try {
			Process p = Runtime.getRuntime().exec("getprop ro.miui.ui.version.name");

			input = new BufferedReader(new InputStreamReader(p.getInputStream()), 1024);

			line = input.readLine();

			input.close();
		} catch (IOException ex) {
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
				}
			}
		}
		return !TextUtils.isEmpty(line) && line.startsWith("V");
	}

	public static int getStringLength(String s) {
		if (s == null) {
			return 0;
		}

		String chineseRex = "[\u4e00-\u9fa5]";

		int valueLength = 0;

		for (int i = 0; i < s.length(); i++) {
			String temp = s.substring(i, i + 1);
			if (temp.matches(chineseRex)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}

		return valueLength;
	}

	public static AnimationDrawable createAnimationDrawable(int animRes) {
		Resources resources = Module.gMainContext.getResources();
		AnimationDrawable drawable = null;
		try {
			drawable = AnimationDrawable.class.cast(
					Drawable.createFromXml(resources, resources.getAnimation(animRes)));
		} catch (XmlPullParserException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassCastException e) {
			e.printStackTrace();
		}
		return drawable;
	}

	public static void hideKeyboard(Activity act) {
		try {
			InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(act.getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (NullPointerException exception) {
			JLog.error(null, "InputMethodManager can't find focus");
		}
	}

	public static void showKeyboard(Activity act, View view) {
		try {
			InputMethodManager imm = (InputMethodManager) act.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT);
		} catch (NullPointerException exception) {
			JLog.error(null, "InputMethodManager can't find focus");
		}
	}

	public static void hideKeyboard(View view) {
		try {
			InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(view.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		} catch (NullPointerException exception) {
			JLog.error(null, "InputMethodManager can't find focus");
		}
	}

	public static <T extends View> T getView(View parent, int resId) {
		return (T) (parent.findViewById(resId));
	}

	public static <T extends View> T getView(Dialog dialog, int resId) {
		return (T) (dialog.findViewById(resId));
	}

	public static <T extends View> T getView(Activity activity, int resId) {
		return (T) (activity.findViewById(resId));
	}


	public static void loadFromFile(Activity activity, String path, ImageView imageView) {
//        Glide.with(activity).load(new File(path)).asBitmap().diskCacheStrategy(DiskCacheStrategy.NONE).placeholder(R.drawable
// .img_default).into(imageView);

	}

	/**
	 * 获取系统所有图片路径
	 *
	 * @param context
	 * @return
	 */
	public static List<String> getAllPhotoPaths(Context context) {
		List<String> photos = new ArrayList<>();
		Cursor cursor = context.getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
				new String[]{MediaStore.Images.ImageColumns.DATA}, MediaStore.Images.ImageColumns.SIZE + ">5120", //所有照片屏蔽5k以下照片
				// (防垃圾空图)
				null, MediaStore.Images.ImageColumns.DATE_MODIFIED + " DESC");
		if (null != cursor) {
			while (cursor.moveToNext()) {
				int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
				if (index > -1) {
					String path = cursor.getString(index);
					photos.add(path);
				}
			}
			cursor.close();
		}
		return photos;
	}

	public static final Pattern SPLASH_URL_SIZE_REGEX, LOGO_URL_REGEX;

	static {
		SPLASH_URL_SIZE_REGEX = Pattern.compile("^(http://)(\\S+)(/)(\\S+)(\\.)([0-9]+)(\\*)([0-9]+)(\\.jpg|\\.png)$");
		LOGO_URL_REGEX = Pattern.compile("^(http://)(\\S+)(/)(more_logo_\\d_\\d_\\d_)(\\S+)(_)([0-9]+)(x)([0-9]+)(\\.jpg|\\.png)$");
	}

	public static Bitmap getSplashBitmap() {
		if (config.splash == null) {
			return null;
		}
		if (!isValid(config.splash.date)) {
			return null;
		}

		Bitmap b = null;
		final AdaConfig.ConfigSplashAct config = AdaConfig.config.splash;
		if (config != null && config.imgs != null && config.imgs.length > 0) {
			String filePath = getSplashFilePathFromUrl(config.imgs[0]);
			if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
				b = BitmapFactory.decodeFile(filePath);
			} else {
				ThreadBus.bus().post(ThreadBus.Working, new Runnable() {

					@Override
					public void run() {
						try {
							downloadConfigImage(config.imgs);
						} catch (Exception e) {
							JLog.error(this, "download splash image error : " + e);
						}
					}
				});
			}
		}

		return b;
	}

	@SuppressLint("SimpleDateFormat")
	public static boolean isValid(AdaConfig.ConfigDate date) {
		if (date == null) {
			return true;
		}

		long current = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		if (date.validdate != null) {
			long validate;
			try {
				validate = sdf.parse(date.validdate).getTime();
				if (current < validate) {
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}

		}

		if (date.invaliddate != null) {
			long invalidDate;
			try {
				invalidDate = sdf.parse(date.invaliddate).getTime();
				if (current > invalidDate) {
					return false;
				}
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		return true;
	}

	public static void downloadConfigImage(final String[] urls) {
		String finalUrl = urls[0];

		int screenSize = UIConst.SCREEN_HEIGHT * UIConst.SCREEN_WIDTH;
		int minDiff = Integer.MAX_VALUE;
		for (String url : urls) {
			Matcher m = SPLASH_URL_SIZE_REGEX.matcher(url);
			if (m.matches()) {
				int diff = Math.abs(Integer.valueOf(m.group(6)) * Integer.valueOf(m.group(8)) - screenSize);
				if (diff < minDiff) {
					minDiff = diff;
					finalUrl = url;
				}
			}
		}

		final String finalFilePath = getSplashFilePathFromUrl(finalUrl);

		if (TextUtils.isEmpty(finalFilePath)) {
			JLog.error(null, "get splash filepath from url failed url : " + finalUrl);
			return;
		}

		HttpHelper.downloadImage(finalUrl, JStringUtils.combineStr(finalFilePath, ".tmp"), new HttpHelper.ImageDownloadListener() {
			@Override
			public void onResult(final boolean success, final String path) {
				ThreadBus.bus().callThreadSafe(ThreadBus.Main, new Runnable() {

					@Override
					public void run() {
						if (success) {
							JFileUtils.renameFile(path, finalFilePath);
						} else {
							JLog.error(this, "download splash image failed");
						}
					}
				});
			}
		});
	}

	private static String getSplashFilePathFromUrl(String url) {
		Matcher m = SPLASH_URL_SIZE_REGEX.matcher(url);
		if (m.matches()) {
			return JStringUtils.combineStr(Module.gMainContext.
					getCacheDir(), "/", m.group(4));
		}
		return null;
	}

	public static Bitmap getActivityLogoBitmap() {
		if (AdaConfig.config.splash == null) {
			return null;
		}

		Bitmap b = null;
		final AdaConfig.ConfigSplashAct config = AdaConfig.config.splash;
		if (config != null && config.marketlogo != null && config.marketlogo.length > 0) {

			JVer currentVer = JVersionUtil.getLocalVer(Module.gMainContext);
			String versionStr = JStringUtils.combineStr(currentVer.mMajor,
					"_", currentVer.mMinor, "_", currentVer.mBuild);

			final ArrayList<String> matchedUrls = new ArrayList<String>();
			for (String url : config.marketlogo) {
				if (!TextUtils.isEmpty(url) && url.contains(versionStr)
						&& url.contains(JStringUtils.combineStr("_", DConst.sChannelID, "_"))) {
					matchedUrls.add(url);
				}
			}

			if (matchedUrls.isEmpty()) {
				return null;
			}

			String filePath = getActivityLogoFilePathFromUrl(matchedUrls.get(0));

			if (!TextUtils.isEmpty(filePath) && new File(filePath).exists()) {
				b = BitmapFactory.decodeFile(filePath);
			} else {
				ThreadBus.bus().post(ThreadBus.Working, new Runnable() {

					@Override
					public void run() {
						try {
							downloadActivityLogoImage(matchedUrls);
						} catch (Exception e) {
							JLog.error(this, "download splash image error : " + e);
						}
					}
				});
			}
		}
		return b;
	}

	private static String getActivityLogoFilePathFromUrl(String url) {
		Matcher m = LOGO_URL_REGEX.matcher(url);
		if (m.matches()) {
			StringBuilder sb = new StringBuilder();
			sb.append(Module.gMainContext.getCacheDir()).append("/");
			return JStringUtils.combineStr(Module.gMainContext.
					getCacheDir(), "/", m.group(4), m.group(5));
		}
		return null;
	}

	public static void downloadActivityLogoImage(List<String> urls) {
		String finalUrl = urls.get(0);

		int screenSize = UIConst.SCREEN_HEIGHT * UIConst.SCREEN_WIDTH;
		int minDiff = Integer.MAX_VALUE;
		for (String url : urls) {
			Matcher m = LOGO_URL_REGEX.matcher(url);
			if (m.matches()) {
				int diff = Math.abs(Integer.valueOf(m.group(7)) * Integer.valueOf(m.group(9)) - screenSize);
				if (diff < minDiff) {
					minDiff = diff;
					finalUrl = url;
				}
			}
		}

		final String finalFilePath = getActivityLogoFilePathFromUrl(finalUrl);

		if (TextUtils.isEmpty(finalFilePath)) {
			JLog.error(null, "get splash filepath from url failed url : " + finalUrl);
			return;
		}

		// FIXME: 2016/3/30
//        HttpHelper.downloadImage(finalUrl, JStringUtils.
//                combineStr(finalFilePath, ".tmp"), new DownloadHandler() {
//
//	        @Override
//	        public void onResult(final boolean isSuccess, final String content) {
//		        ThreadBus.bus().post(ThreadBus.Main, new Runnable() {
//
//			        @Override
//			        public void run() {
//				        if (isSuccess) {
//					        JFileUtils.renameFile(content, finalFilePath);
//				        } else {
//					        JLog.error(this, "download activity logo image failed");
//				        }
//			        }
//		        });
//	        }
//        });
	}
}
