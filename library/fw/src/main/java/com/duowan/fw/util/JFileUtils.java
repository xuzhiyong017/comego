package com.duowan.fw.util;

import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.provider.MediaStore.Images;
import android.util.Pair;

import com.duowan.fw.ThreadBus;
import com.duowan.fw.root.BaseContext;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;

public class JFileUtils {

    private static final String TEMP_DIR = "/temp";
    private static final String IMAGE_DIR = "/image";
    public static final String LOG_DIR = "/log";
    private static final String AUDIO_DIR = "/audio";
    private static final String VIDEO_DIR = "/video";
    private static final String BUBBLE_DIR = "/bubble";

    private static final String LOGO_DIR = "/logo";
    private static final String FORUM_LOGO_DIR = "/forum_logo";
    private static final String UPDATE_DIR = "/update";
    private static final String THUMB_PREFIX = "thumb-";
    public static final String TEMP_IM_IMAGE = "temp_sendpic.jpg";
    public static final String TEMP_IM_IMAGE_NOPOSTFIX = "temp_sendpic";
    private static final String TEMP_VOICE_FILE = "temp_voice.spx";
    private static final String ACT_RECOMMAND_FILE = "temp_act_recomm.txt";
    public static final String RECORD_EXT_HIGH_CPU = ".aac";
    public static final String RECORD_EXT_LOW_CPU = ".wav";
    public static final String RECORD_PUBLISH_EXT = ".m4a";

    private static final int MIN_LEN_OF_VALID_WAV = 128 * 1024;
    private static final int MIN_LEN_OF_VALID_AAC = 8 * 1024;

    private static final int MaxCacheFileCount = 100;
    private static final long MaxCacheFileSize = 100 * 1024 * 1024;

    public static final String[] AUDIO_EXTS = new String[]{
            RECORD_EXT_HIGH_CPU, RECORD_EXT_LOW_CPU, RECORD_PUBLISH_EXT, ".rec", ".mp4", ".rec2"
    };

    private FileOutputStream fos = null;
    private BufferedOutputStream bos = null;
    private File mFile;

    public static boolean isValidAudioFile(String path) {
        if (!JStringUtils.isNullOrEmpty(path)) {
            String ext = JFileUtils.getFileExtension(path);
            if (!JStringUtils.isNullOrEmpty(ext)) {
                for (String extItem : AUDIO_EXTS) {
                    if (JStringUtils.equal(ext, extItem, true)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static String getTxtFileContent(String fileName) {
        String path = fileName;
        String content = "";
        if (JStringUtils.isNullOrEmpty(fileName)) {
            return content;
        }
        File file = new File(path);
        if (file.isFile()) {
            InputStream instream = null;
            try {
                if (fileName.startsWith(BaseContext.gContext.getFilesDir().getPath())) {
                    instream = BaseContext.gContext.openFileInput(JFileUtils.getFileName(fileName));
                } else {
                    instream = new FileInputStream(file);
                }
                if (instream != null) {
                    InputStreamReader inputreader = new InputStreamReader(
                            instream);
                    BufferedReader buffreader = new BufferedReader(inputreader);
                    String line;
                    while ((line = buffreader.readLine()) != null) {
                        content += line + "\n";
                    }
                }
            } catch (Exception e) {
                JLog.error("getTxtFileContent", "read fail, e = " + e);
            } finally {
                if (instream != null) {
                    try {
                        instream.close();
                    } catch (Exception e) {
                    }
                }
            }
        }
        return content;
    }

    public static File createFileOnSD(String dirPath, String name) {
        File file = null;
        if (isSDCardMounted()) {
            createDir(dirPath, true);
            file = new File(dirPath + "/" + name);
            try {
                if (!file.exists() && !file.createNewFile()) {
                    file = null;
                }
            } catch (IOException e) {
                JLog.error("YYFileUtils", "can not create file on SD card");
                file = null;
            }
        }
        return file;
    }

    public static String getFileExtension(String filePath) {
        String fileName = getFileName(filePath);
        int index = fileName.lastIndexOf(".");
        if (index != -1) {
            return fileName.substring(index);
        }
        return null;
    }

    public static String getFileName(String filePath) {
        if (filePath != null) {
            final String slash = "/";
            final int pos = filePath.lastIndexOf(slash) + 1;
            if (pos > 0) {
                return filePath.substring(pos);
            }
        }
        return null;
    }

    /* drop the extesion of a filename */
    public static String dropExt(String fname) {
        if (!JFP.empty(fname)) {
            int pos = fname.lastIndexOf(".");
            if (pos != -1)
                return JFP.take(pos, fname);
        }
        return fname;
    }

    public static boolean isFileExisted(String filePath) {
        if (JStringUtils.isNullOrEmpty(filePath)) {
            return false;
        }
        try {
            File file = new File(filePath);
            return (file.exists() && file.length() > 0);
        } catch (Exception e) {
            return false;
        }
    }

    public static void renameFile(String oldFile, String newFile) {
        try {
            File file = new File(oldFile);
            file.renameTo(new File(newFile));
        } catch (Exception e) {
            return;
        }
    }

    public static boolean CopyFile(String inFileName, String outFileName) {
        try {
            File inFile = new File(inFileName);
            File outFile = new File(outFileName);

            FileInputStream fis = new FileInputStream(inFile);
            FileOutputStream fos = new FileOutputStream(outFile);
            byte[] buf = new byte[2048];
            int i = 0;
            while ((i = fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
            fis.close();
            fos.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static void removeFiles(List<Pair<Integer, String>> fileNames) {
        for (Pair<Integer, String> p : fileNames) {
            if (p.second != null) {
                removeFile(p.second);
            }
        }
    }

    public static void removeFile(String filename) {
        if (!JStringUtils.isNullOrEmpty(filename)) {
            try {
                File file = new File(filename);

                file.delete();
            } catch (Exception e) {
            }
        }
    }

    public static void removeDir(String dirPath) {
        File dir = new File(dirPath);
        try {
            removeDirRecursively(dir);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void removeDirRecursively(File file) throws Exception {
        if (file.exists()) {
            if (file.isFile()) {
                file.delete();
            } else if (file.isDirectory()) {
                File[] files = file.listFiles();
                if (files != null) {
                    for (int i = 0; i < files.length; i++) {
                        removeDirRecursively(files[i]);
                    }
                }
                file.delete();
            }
        }
    }

    public static File getFileFromURL(String url) {
        if (JStringUtils.isNullOrEmpty(url)) {
            return null;
        }
        int idx = url.lastIndexOf('/');
        return new File(getRootImImageDir() + url.substring(idx + 1));
    }

    public static File getFileFromURL(String base, String url) {
        if (JStringUtils.isNullOrEmpty(url)) {
            return null;
        }
        int idx = url.lastIndexOf('/');
        return new File(base, url.substring(idx + 1));
    }

    public static String getFilePathFromUri(Context context, Uri uri) {
        File file = new File(uri.getPath());
        if (file.isFile()) {
            return file.getPath();
        }
        if (uri.toString().indexOf("file://") == 0) {
            String ret = uri.toString().substring(7);
            ret = JUtils.decodeUri(ret);
            return ret;
        } else {
            ContentResolver cr = context.getContentResolver();
            Cursor cursor = cr.query(uri, new String[]{Images.Media.DATA}, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                String ret = cursor.getString(0);
                ret = JUtils.decodeUri(ret);
                return ret;
            }
        }
        return null;
    }

    public static boolean isSDCardMounted() {
        return availableMemInSDcard();
    }

    public static boolean availableMemInSDcard() {
        if (!externalStorageExist()) {
            return false;
        }
        File sdcard = Environment.getExternalStorageDirectory();
        StatFs statFs = new StatFs(sdcard.getPath());
        long blockSize = statFs.getBlockSize();
        long avaliableBlocks = statFs.getAvailableBlocks();
        long total = avaliableBlocks * blockSize / 1024;
        if (total < 10) {
            return false;
        }
        return true;
    }

    public static boolean externalStorageExist() {
        boolean ret = false;
        ret = Environment.getExternalStorageState().equalsIgnoreCase(
                Environment.MEDIA_MOUNTED);
        return ret;
    }

    public static boolean checkFileValidation(String filepath, String md5) {
        final String fileMd5 = JUtils.fileMd5(filepath);
        return fileMd5.equals(md5);
    }

    public static String getRootTempDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        TEMP_DIR, "/");
    }

    public static String getRootImageDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        IMAGE_DIR, "/");
    }

    public static String getRootBubbleDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        BUBBLE_DIR, "/");
    }

    public static String getRootLogoDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        LOGO_DIR, "/");
    }

    public static String getRootLogDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        LOG_DIR, "/");
    }

    public static String getRootAudioDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        AUDIO_DIR, "/");
    }

    public static String getRootVideoDir() {
        return JStringUtils.combineStr
                (Environment.getExternalStorageDirectory().getAbsolutePath(),
                        "/", BaseContext.gCompanyName, "/",
                        BaseContext.gAppName,
                        VIDEO_DIR, "/");
    }

    public static String getForumLogoPathFromUrl(String url) {
        String name = getFileName(url);
        if (name != null) {
            String dirPath = Environment.getExternalStorageDirectory().getPath();
            return dirPath + rootDir() + FORUM_LOGO_DIR + "/" + name;
        }
        return null;
    }

    public static String imageOf(String url) {
        String name = getFileName(url);
        if (name != null)
            return concatPath(getRootImageDir(), name);
        return null;
    }

    public static String dropPrefix(String s, String prefix) {
        return s.startsWith(prefix) ? JFP.drop(JFP.length(prefix), s) : s;
    }

    /**
     * Safe concatenate paths no matter the first one ends with / or the second one starts with /.
     */
    public static String concatPath(String p1, String p2) {
        return p1.endsWith("/") ? p1 + dropPrefix(p2, "/") : p1 + "/" + dropPrefix(p2, "/");
    }

    public static String concatPaths(String... ss) {
        String path = "";
        for (String s : ss)
            path = concatPath(path, s);
        return path;
    }

    public static String getRootUpdateDir() {
        String dirPath = Environment.getExternalStorageDirectory().getPath();
        return dirPath + rootDir() + UPDATE_DIR + "/";
    }

    public static String getRootImTempImagePath() {
        return getRootImImageDir() + TEMP_IM_IMAGE;
    }

    public static String getRootImTempImagePath(String postfix) {
        return getRootImImageDir() + TEMP_IM_IMAGE_NOPOSTFIX + postfix;
    }

    public static String getRootImImageDir() {
        String dirPath = Environment.getExternalStorageDirectory().getPath();
        return dirPath + rootDir() + TEMP_DIR + "/";
    }

    public static String getRootImageFilePath(String filename) {
        return getRootImageDir() + filename;
    }

    public static String getRootTempVoiceFilePath() {
        return getRootVoicePath(TEMP_VOICE_FILE);
    }

    public static String getRootActRecommFilename() {
        File file = BaseContext.gContext.getFileStreamPath(ACT_RECOMMAND_FILE);
        return file.getPath();
    }


    public static String getRootImVoiceFilePathFromUrl(String url) {
        final String fileName = JFileUtils.getFileName(url);
        if (!JStringUtils.isNullOrEmpty(fileName)) {
            return getRootAudioDir() + fileName;
        }
        return null;
    }

    public static String getRootVoicePath(String name) {
        String filename = name;
        if (filename != null) {
            int index = name.lastIndexOf('/');
            if (index != -1) {
                filename = name.substring(index + 1);
            }
        } else {
            filename = "";
        }
        return getRootAudioDir() + filename;
    }

    public static String getRootLogoFilePath(String filename) {
        return getRootLogoDir() + filename;
    }

    // translate image name from URL to local thumb nail path
    public static String getRootThumbImageLocalPath(String name) {
        String filename = name;
        int index = name.lastIndexOf('/');
        if (index != -1) {
            filename = name.substring(index + 1);
        }
        return getRootImImageDir() + THUMB_PREFIX + filename;
    }

    public static JFileUtils createTempFile(Context context, String name) throws Exception {
        return createTempFile(context, name, false);
    }

    public static JFileUtils createTempFile(Context context, String name, boolean onlyOnSD) throws Exception {
        File file = createFileOnSD(rootDir() + TEMP_DIR, name);
        FileOutputStream fileos = null;
        if (file == null && context != null && !onlyOnSD) {
            try {
                fileos = context.openFileOutput(name, Context.MODE_PRIVATE);
                String filePath = context.getFilesDir() + "/" + name;
                file = new File(filePath);
            } catch (Exception e) {
                JLog.error("YYFileUtils", "can not open private file");
            }
        }
        return new JFileUtils(file, fileos);
    }

    public static boolean hasImageFile(String name) {
        if (isSDCardMounted()) {
            String dirPath = getRootImageDir();
            File file = new File(dirPath + File.separatorChar + name);
            return file.exists();
        } else return false;
    }

    public static File getImageFile(String name) {
        File file = createFileOnSD(getRootImageDir(), name);
        return file;
    }

    public static JFileUtils createImageFile(String name) throws Exception {
        File file = createFileOnSD(getRootImageDir(), name);
        return new JFileUtils(file, null);
    }

    public static JFileUtils createLogoFile(String name) throws Exception {
        File file = createFileOnSD(rootDir() + LOGO_DIR, name);
        return new JFileUtils(file, null);
    }

    private static File openUpdateFile(File file) {
        if (file == null || !file.delete()) {
            JLog.error("openUpdateFile", (file == null ? "file is null" : "file " + file.getPath() + " can not be deleted"));
            return null;
        }

        try {
            if (!file.createNewFile()) {
                JLog.error("openUpdateFile", "createNewFile " + file.getPath() + " failed");
                return null;
            }
        } catch (Exception e) {
            JLog.error("openUpdateFile", e);
            return null;
        }

        return file;
    }

    public static JFileUtils openFile(String filePath) throws Exception {
        String dirPath = filePath.substring(0, filePath.lastIndexOf("/"));
        createDir(dirPath, true);

        File file = new File(filePath);
        if (!file.exists() && !file.createNewFile()) {
            file = null;
        }
        return new JFileUtils(file, null);
    }

    private JFileUtils(File file, FileOutputStream fileos) throws Exception {
        mFile = file;
        fos = fileos;
        if (mFile != null) {
            if (fos == null) {
                fos = new FileOutputStream(mFile);
            }
            bos = new BufferedOutputStream(fos);
        } else {
            throw new Exception("YYFileOutput, can not create file output stream");
        }
    }

    public static void createDir(String dirPath, boolean nomedia) {
        ensureDirExists(dirPath);
        if (nomedia) {
            File nomediafile = new File(dirPath + "/.nomedia");
            try {
                nomediafile.createNewFile();
            } catch (IOException e) {
            }
        }
    }

    public static void ensureDirExists(String dirPath) {
        File dirFile = new File(dirPath);
        if (!dirFile.exists()) {
            dirFile.mkdirs();
        }
    }

    /**
     * Ensure the parent directory of given file path exists.
     * make directories if need.
     *
     * @param filePath A file path.
     * @return True for success, false otherwise.
     */
    public static boolean ensureFileDirExists(String filePath) {
        String dir = getDirOfFilePath(filePath);
        if (JStringUtils.isNullOrEmpty(dir)) {
            return false;
        }
        ensureDirExists(dir);
        return true;
    }

    public static String getDirOfFilePath(String filePath) {
        if (JStringUtils.isNullOrEmpty(filePath)) {
            return null;
        }
        int sepPos = filePath.lastIndexOf(File.separatorChar);
        if (sepPos == -1) {
            return null;
        }
        return filePath.substring(0, sepPos);
    }

    private static Set<String> FOLDERS = new TreeSet<String>() {
        /**
         *
         */
        private static final long serialVersionUID = 1L;

        {
            add(getRootTempDir());
            add(getRootImageDir());
            add(getRootAudioDir());
            add(getRootVideoDir());
            add(getRootLogDir());
        }
    };

    public static void asyncSetupFolders() {
        new AsyncTask<Void, Void, Void>() {

            private void touch(String folder) {
                ensureDirExists(folder);
            }

            @Override
            protected Void doInBackground(Void... args) {
                for (String f : FOLDERS)
                    touch(f);
                return null;
            }
        }.execute();
    }

    public void write(Bitmap bmp) {
        write(bmp, 80);
    }

    public void write(Bitmap bmp, int compressRate) {
        bmp.compress(Bitmap.CompressFormat.JPEG, compressRate, bos);
    }

    public void write(InputStream is) {
        int bytes = 0;
        byte[] buffer = new byte[4096];
        try {
            while ((bytes = is.read(buffer)) != -1) {
                bos.write(buffer, 0, bytes);
            }
        } catch (IOException e) {
            JLog.error(this, e);
        }
    }

    public void write(String fileName) {
        try {
            FileInputStream fis = new FileInputStream(fileName);
            write(fis);
            fis.close();
        } catch (Exception e) {
            JLog.error(this, e);
        }
    }

    public void write(byte[] buffer) {
        try {
            bos.write(buffer);
        } catch (IOException e) {
            JLog.error(this, e);
        }
    }

    public void write(byte[] buffer, int offset, int length) {
        try {
            bos.write(buffer, offset, length);
        } catch (IOException e) {
            JLog.error(this, e);
        }
    }

    public void close() {
        try {
            bos.flush();
            bos.close();
            fos.close();
        } catch (IOException e) {
            JLog.error(this, e);
        }
    }

    public File getFile() {
        return mFile;
    }

    public static long getFileLength(String file) {
        File tmp = new File(file);
        return tmp.length();
    }

    public static String fallbackFile(String file) {
        String ext = JUtils.getFileExt(file);
        int i = file.lastIndexOf(".");
        return i == -1 ? "" : file.substring(0, i + 1) + "bak" + ext;
    }

    public static class Delist {
        public static final String FLOG = concatPath(getRootAudioDir(), "delist.txt");

        /**
         * Add a file to delete list, so that it will be delete later.
         */
        public static void add(String fname) {
            PrintWriter w = null;
            try {
                w = new PrintWriter(new FileWriter(FLOG, true));
                w.printf("%s\n", fname);
            } catch (Exception e) {
            } finally {
                if (w != null)
                    w.close();
            }
        }

        public static void clear() {
            new AsyncTask<Void, Void, Void>() {
                @Override
                protected Void doInBackground(Void... args) {
                    return delete();
                }
            }.execute();
        }

        private static Void delete() {
            Scanner s = null;
            boolean traversed = false;
            try {
                for (s = new Scanner(new FileReader(FLOG)); s.hasNextLine(); JFileUtils.removeFile(s.nextLine()))
                    ;
                traversed = true;
            } catch (Exception e) {
            } finally {
                if (s != null)
                    s.close();
                if (traversed)
                    JFileUtils.removeFile(FLOG);
            }
            //Collector.collect(); //remove all record files not refered in db
            return null;
        }
    }

    /**
     * Create a fake log for media file
     */
    public static void fakelog(String fname) {
        if (!isFileExisted(fname))
            return;
        try {
            PrintWriter w = new PrintWriter(new FileWriter(fname + ".log", false));
            w.printf("%d", getFileLength(fname));
            w.close();
        } catch (Exception e) {
        }
    }

    /**
     * Check validity of record file,
     * currently only .aac and .wav file are supported,
     * check is based on the file length.
     *
     * @param filePath Must be end with .aac or .wav.
     */
    public static boolean isValidRecordFile(String filePath) {
        if (!isFileExisted(filePath)) {
            return false;
        }

        String ext = getFileExtension(filePath);
        if (ext == null) {
            return false;
        }

        JLog.verbose(JFileUtils.class, "lcy file extension is %s", ext);

        boolean aac = false;
        if (!(aac = ext.equalsIgnoreCase(RECORD_EXT_HIGH_CPU))
                && !ext.equalsIgnoreCase(RECORD_EXT_LOW_CPU)) {
            JLog.debug(JFileUtils.class, "lcy record extension check failed.");
            return false;
        }

        final long len = JFileUtils.getFileLength(filePath);
        final long minLen = aac ? MIN_LEN_OF_VALID_AAC : MIN_LEN_OF_VALID_WAV;
        boolean ret = (len >= minLen);
        JLog.debug(JFileUtils.class, "lcy file length invalid %d, %d, %s.", len, minLen, ext);
        return ret;
    }

    public static long getFileSize(File file) {
        long size = 0;
        if (file.isDirectory()) {
            File fileList[] = file.listFiles();
            for (int i = 0; i < fileList.length; i++) {
                if (fileList[i].isDirectory()) {
                    size = size + getFileSize(fileList[i]);
                } else {
                    // JLog.verbose("Simon", "file: " + fileList[i] + "  size: " + fileList[i].length());
                    size = size + fileList[i].length();
                }
            }
        } else {
            size = file.length();
        }
        return size;
    }

    public static String getFormatSize(double size) {
        double kiloByte = size / 1024;
        if (kiloByte == 0) {
            return size + "M";
        }
        if (kiloByte < 1) {
            return size + "B";
        }

        double megaByte = kiloByte / 1024;
        if (megaByte < 1) {
            BigDecimal result1 = new BigDecimal(Double.toString(kiloByte));
            return result1.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "KB";
        }

        double gigaByte = megaByte / 1024;
        if (gigaByte < 1) {
            BigDecimal result2 = new BigDecimal(Double.toString(megaByte));
            return result2.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "MB";
        }

        double teraBytes = gigaByte / 1024;
        if (teraBytes < 1) {
            BigDecimal result3 = new BigDecimal(Double.toString(gigaByte));
            return result3.setScale(2, BigDecimal.ROUND_HALF_UP)
                    .toPlainString() + "GB";
        }
        return "";
    }

    public static byte[] readFile(String file) throws IOException {
        return readFile(new File(file));
    }

    public static byte[] readFile(File file) throws IOException {
        // Open file
        RandomAccessFile f = new RandomAccessFile(file, "r");
        try {
            // Get and check length
            long longlength = f.length();
            int length = (int) longlength;
            if (length != longlength)
                throw new IOException("File size >= 2 GB");
            // Read file and return data
            byte[] data = new byte[length];
            f.readFully(data);
            return data;
        } finally {
            f.close();
        }
    }

    public static String rootDir() {
        return "/" + BaseContext.gCompanyName;
    }


    public static void clearOldCache() {
        ThreadBus.bus().post(ThreadBus.Working, new Runnable() {
            @Override
            public void run() {

                try {
                    clearOldCacheFileCauseFromCount(JFileUtils.getRootAudioDir());
                    clearOldCacheFileCauseFromSize(JFileUtils.getRootAudioDir());
                    clearOldCacheFileCauseFromCount(JFileUtils.getRootVideoDir());
                    clearOldCacheFileCauseFromSize(JFileUtils.getRootVideoDir());

                } catch (Exception e) {
                    e.printStackTrace();
                    JLog.error(this, e);
                }
            }
        });

    }

    public static void clearOldCacheFileCauseFromCount(String path) {
        File file = new File(path);

        File[] files = file.listFiles();

        if (null == files || files.length < MaxCacheFileCount) {
            return;
        }
        List<File> fileList = Arrays.asList(files);

        //排序
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(fileList, modifyDayComparator);

        //删除
        for (int i = 0; i <= files.length - MaxCacheFileCount - 1; i++) {
            fileList.get(i).delete();
        }
    }

    public static void clearOldCacheFileCauseFromSize(String path) throws Exception {

        File file = new File(path);

        long totolFileSize = JFileUtils.getFileSize(file);

        File[] files = file.listFiles();

        if (null == files || totolFileSize < MaxCacheFileSize) {
            return;
        }

        List<File> fileList = Arrays.asList(files);

        //排序
        System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");
        Collections.sort(fileList, modifyDayComparator);

        //删除
        long currentCachedSize = totolFileSize;
        int fileCount = fileList.size();
        for (int i = 0; i < fileCount && currentCachedSize > MaxCacheFileSize; i++) {
            File temp = fileList.get(i);
            currentCachedSize -= getFileSize(temp);
            temp.delete();
        }

    }

    static Comparator<File> modifyDayComparator = new Comparator<File>() {
        @Override
        public int compare(File lhs, File rhs) {

            long diff = lhs.lastModified() - rhs.lastModified();
            if (diff > 0)
                return 1;
            else if (diff == 0)
                return 0;
            else
                return -1;
        }
    };
    
    // if so do not exits just to make copy from apk
    public static void initNativeLib(Context context, String libName) {
        try {
            // Try loading our native lib, see if it works...
            System.loadLibrary(libName);
        } catch (UnsatisfiedLinkError er) {
            ApplicationInfo appInfo = context.getApplicationInfo();
            String solibName = "lib" + libName + ".so";
            String destPath = context.getFilesDir().toString();
            try {
                String soName = destPath + File.separator + solibName;
                new File(soName).delete();
                JUnzipUtil.extractFile(appInfo.sourceDir, "lib/" + Build.CPU_ABI + "/" + solibName, destPath);
                System.load(soName);
            } catch (IOException e) {
                // extractFile to app files dir did not work. Not enough space? Try elsewhere...
                destPath = context.getExternalCacheDir().toString();
                // Note: location on external memory is not secure, everyone can read/write it...
                // However we extract from a "secure" place (our apk) and instantly load it,
                // on each start of the app, this should make it safer.
                String soName = destPath + File.separator + solibName;
                new File(soName).delete(); // this copy could be old, or altered by an attack
                try {
                    JUnzipUtil.extractFile(appInfo.sourceDir, "lib/" + Build.CPU_ABI + "/" + solibName, destPath);
                    System.load(soName);
                } catch (IOException e2) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static final File EXTERNAL_DCIM = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM);
    /**
     * @return 返回DCIM(拍照默认存储位置)
     * @throws FileNotFoundException
     */
    public static File getExternalDCIM() throws IOException {
        if (!isSDCardMounted()) {
            throw new FileNotFoundException("sd card not found");
        }
        return EXTERNAL_DCIM;
    }

    /**
     * 保存Bitmap 到 拍照
     *
     * @param bitmap
     * @return bitmap路径
     */
    public static String saveBitmapDCIM(Bitmap bitmap) {
        String dcim = null;
        try {
            dcim = getExternalDCIM().getAbsolutePath();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return saveBitmap(bitmap, dcim);
    }

    /**
     * 保存Bitmap到sdcard 随机文件名
     *
     * @param bitmap
     * @return 文件路径
     */
    public static String saveBitmap(Bitmap bitmap, String path) {
        if (bitmap == null) {
            return null;
        }
        String jpegName = concatPath(path, "/" + "IMG" + System.currentTimeMillis() + ".jpg");
        try {
            FileOutputStream bos = new FileOutputStream(jpegName);
            // BufferedOutputStream bos = new BufferedOutputStream(fout);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            bos.flush();
            bos.close();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return jpegName;
    }

    public static Uri getLocalFileUri(String path) {
        String uri = path;
        if (null != path && !path.startsWith("file://") && path.startsWith("/")) {
            uri = "file://" + path;
        }
        if (null != uri && uri.startsWith("file:///")) {
            return Uri.parse(uri);
        }
        return null;
    }

}
