package cn.lsmya.restaurant.util;

import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class FileUtils {
    private static FileUtils fileUtils;

    public static FileUtils getInstance() {
        if (fileUtils == null) {
            fileUtils = new FileUtils();
        }
        return fileUtils;
    }

    public String getFileType(File file) {
        if (!file.exists()) return null;
        /* 取得扩展名 */
        String end = file.getName().substring(file.getName().lastIndexOf(".") + 1, file.getName().length()).toLowerCase();
        /* 依扩展名的类型决定MimeType */
        if (end.equals("m4a") || end.equals("mp3") || end.equals("mid") ||
                end.equals("xmf") || end.equals("ogg") || end.equals("wav")) {
            return "audio/*";
        } else if (end.equals("3gp") || end.equals("mp4")) {
            return "audio/*";
        } else if (end.equals("jpg") || end.equals("gif") || end.equals("png") ||
                end.equals("jpeg") || end.equals("bmp")) {
            return "image/*";
        } else if (end.equals("apk")) {
            return "application/vnd.android.package-archive";
        } else if (end.equals("ppt")) {
            return "application/vnd.ms-powerpoint";
        } else if (end.equals("xls")) {
            return "application/vnd.ms-excel";
        } else if (end.equals("doc") || end.equals("docx")) {
            return "application/msword";
        } else if (end.equals("pdf")) {
            return "application/pdf";
        } else if (end.equals("chm")) {
            return "application/x-chm";
        } else if (end.equals("txt")) {
            return "";
        } else {
            return "*/*";
        }
    }

    private String path = Environment.getExternalStorageDirectory().toString() + "/智慧餐厅";


    public void saveToFile(String fileName, InputStream in) throws IOException {
        FileOutputStream fos;
        BufferedInputStream bis;
        byte[] buf = new byte[1024];//接受1024个字节
        int size;
        bis = new BufferedInputStream(in);
        fos = new FileOutputStream(fileName);
        while ((size = bis.read(buf)) != -1)
            fos.write(buf, 0, size);
        fos.close();
        bis.close();
    }


    public File makeDir() {
        File fileDir = new File(path);
        if (fileDir.exists()) {
            return fileDir;
        } else {
            fileDir.mkdirs();
            return fileDir;
        }
    }

    public boolean isFileExists(String fileName) {
        String path = getInstance().makeDir().getPath() + File.separator + fileName;
        File file = new File(path);
        boolean exists = file.exists();
        return exists;
    }

}
