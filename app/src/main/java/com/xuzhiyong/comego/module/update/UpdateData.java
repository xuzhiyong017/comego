package com.xuzhiyong.comego.module.update;

import com.duowan.fw.KeepAnnotation;

@KeepAnnotation
public class UpdateData {
    // for json
    @KeepAnnotation
    public class PatchInfo {
        @KeepAnnotation
        String patch_version;
        @KeepAnnotation
        int version_code;
        @KeepAnnotation
        long patch_size;
        @KeepAnnotation
        String apk_md5;
        @KeepAnnotation
        String target_md5;
        @KeepAnnotation
        String patch_url;
        @KeepAnnotation
        String patch_md5;
    }

    @KeepAnnotation
    String version;
    @KeepAnnotation
    int version_code;
    @KeepAnnotation
    String url;
    @KeepAnnotation
    long size; //kilobytes
    @KeepAnnotation
    String md5;
    @KeepAnnotation
    String description;
    @KeepAnnotation
    int force;
    @KeepAnnotation
    int ignore;
    @KeepAnnotation
    int ignore_auto_check;
    @KeepAnnotation
    String uid_pattern;
    @KeepAnnotation
    PatchInfo[] patches;

    static class Patch {
        PatchInfo patchInfo;
        String path;

        Patch(PatchInfo patchInfo, String path) {
            this.patchInfo = patchInfo;
            this.path = path;
        }
    }
}
