package com.duowan.jni;

import java.io.File;

import android.os.AsyncTask;

import com.duowan.fw.util.JMD5Utils;

public class JBsDiffTool {

	// diff [patch] 任务
	public static class BsApllyPatchTask{
		// task id
		public int id;
		// 旧的apk 绝对路径
		public String oldPath;
		public String oldMd5;
		
		// 生成新的apk绝对路径
		public String newPath;
		public String newMd5;
		
		// 补丁版本绝对路径
		public String patchPath;
		public String patchMd5;
	}
	
	// 执行错误码
	public enum BsErrCode{
		BsErrCode_OK,
		BsErrCode_PatchFailed,
		
		BsErrCode_PatchMd5Wrong,
		BsErrCode_OldMd5Wrong,
		BsErrCode_NewMd5Wrong,
		
		BsErrCode_IOException,
		
		BsErrCode_Shit,
	}
	
	// 阶段
	public enum BsProgressPhase{
		BsProgress_Check, 	// 检查阶段
		BsProgress_Aplly,	// 应用补丁
		BsProgress_GenNew,	// 生成新文件
	}
	
	// diff [patch] 应用回调
	public static interface BsApllyPatchCallback{
		public void onPatchResult(BsApllyPatchTask task, BsErrCode code);
		public void onPatchProgress(BsApllyPatchTask task, BsProgressPhase phase);
	}
	
	// 应用 patch
	public static void applyPatch(final BsApllyPatchTask task, final BsApllyPatchCallback callback){
		new AsyncTask<BsApllyPatchTask, BsProgressPhase, BsErrCode>() {

			@Override
			protected BsErrCode doInBackground(BsApllyPatchTask... params) {
				if (params.length == 0) {
					return BsErrCode.BsErrCode_Shit;
				}
				// should not be null
				return doPatch(params[0]);
			}
			
			BsErrCode doPatch(BsApllyPatchTask task){
				if (task == null) {
					return BsErrCode.BsErrCode_Shit;
				}
				
				BsErrCode result = BsErrCode.BsErrCode_OK;
				
				// check md5
				try {
					while (true) {
						publishProgress(BsProgressPhase.BsProgress_Check);
						
						// 检查 patch 的 MD5
						if (task.patchMd5 != null 
								&& !checkMd5(task.patchMd5, 
										JMD5Utils.md5(task.patchPath), 
										result, 
										BsErrCode.BsErrCode_PatchMd5Wrong)) {
							break;
						}
						// 检查 旧的 包的MD5
						if (task.oldMd5 != null 
							&& !checkMd5(task.oldMd5, 
									JMD5Utils.md5(task.oldPath), 
									result, 
									BsErrCode.BsErrCode_PatchMd5Wrong)){
							break;
						}
						
						publishProgress(BsProgressPhase.BsProgress_Aplly);
						// 应用包，生成到临时文件
						String tmp = task.newPath + ".tmp";
						try {
							if(JBsDiff.applyPatch(task.oldPath, tmp, task.patchPath) != 0){
								result = BsErrCode.BsErrCode_PatchFailed;
								break;
							}
						} catch (Exception e) {
							result = BsErrCode.BsErrCode_PatchFailed;
							break;
						}
						
						publishProgress(BsProgressPhase.BsProgress_GenNew);
						// 检查新的文件MD5
						if (task.newMd5 != null 
								&&!checkMd5(task.newMd5, 
									JMD5Utils.md5(tmp), 
									result, 
									BsErrCode.BsErrCode_NewMd5Wrong)) {
							new File(tmp).delete();
							break;
						}else {
							new File(tmp).renameTo(new File(task.newPath));
						}
						
						break;
					}

				} catch (Exception e) {
					// 读取文件异常
					result = BsErrCode.BsErrCode_IOException;
					e.printStackTrace();
				}
				
				return result;
			}
			
			@Override
			protected void onPostExecute(BsErrCode result) {
				callback.onPatchResult(task, result);
		    }
			
			@Override
		    protected void onProgressUpdate(BsProgressPhase... values) {
				if (values.length > 0) {
					callback.onPatchProgress(task, values[0]);
				}
		    }
			
			boolean checkMd5(String shouldbe, String current, BsErrCode result, BsErrCode code){
				if (shouldbe != null) {
					if (!shouldbe.equalsIgnoreCase(current)) {
						result = code;
						return false;
					}
				}
				return true;
			}
		}.execute(task);
	}
}
