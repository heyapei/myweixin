package com.hyp.myweixin.utils.fileutil;

import com.hyp.myweixin.utils.MyCommonUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

/**
 * 文件上传工具包
 *
 * @author
 */
@Slf4j
public class MyFileUtil {


    /*可以获取文件MD5的值*/

    /**
     * 获取上传文件的md5
     *
     * @param file
     * @return
     * @throws NoSuchAlgorithmException
     * @throws IOException
     */
    public static String getFileMd5(MultipartFile file) {

        try {
            byte[] uploadBytes = file.getBytes();
            MessageDigest md5 = MessageDigest.getInstance("MD5");
            byte[] digest = md5.digest(uploadBytes);
            String hashString = new BigInteger(1, digest).toString(16);
            return hashString;
        } catch (Exception e) {
            e.printStackTrace();
            log.error("获取文件MD5值错误，错误原因：{}", e.toString());
        }
        return null;

    }


    /**
     * @param file     文件
     * @param path     文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {
        //生成新的文件名
        String realPath = path + "/" + fileName;
        File dest = new File(realPath);
        try {
            isExitsPath(realPath);
        } catch (InterruptedException e) {
            log.error("创建目录出现错误，目录：{}，错误原因：{}", realPath, e.toString());
            return false;
        }
        try {
            //保存文件
            file.transferTo(dest);
            return true;
        } catch (IllegalStateException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            log.error("保存文件失败，目录：{}，错误原因：{}", realPath, e.toString());
            return false;
        } catch (IOException e) {
            log.error("保存文件失败2，目录：{}，错误原因：{}", realPath, e.toString());
            // TODO Auto-generated catch block
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 是否创建目录
     *
     * @param path
     * @return
     */
    private static boolean isExitsPath(String path) throws InterruptedException {
        String[] paths = path.split("\\\\");
        StringBuffer fullPath = new StringBuffer();
        for (int i = 0; i < paths.length; i++) {
            fullPath.append(paths[i]).append("\\\\");
            File file = new File(fullPath.toString());
            if (paths.length - 1 != i) {
                if (!file.exists()) {
                    file.mkdir();
                }
            }
        }
        File file = new File(fullPath.toString());
        if (!file.exists()) {
            return true;
        } else {
            return false;
        }
    }


    /**
     * 获取文件后缀
     *
     * @param fileName
     * @return
     */
    public static String getSuffix(String fileName) {
        return fileName.substring(fileName.lastIndexOf("."));
    }

    /**
     * 生成新的文件名
     *
     * @param fileOriginName 源文件名
     * @return
     */
    public static String getFileName(String fileOriginName) {
        return MyCommonUtil.getUUID() + FileNameUtils.getSuffix(fileOriginName);
    }


    /**
     * 为防止文件覆盖的现象发生，要为上传文件产生一个唯一的文件名
     *
     * @param filename
     * @return
     */
    public static String makeFileName(String filename) {
        return UUID.randomUUID().toString().replace("-", "") + "_" + filename;
    }

    /**
     * @param file     文件
     * @param filePath 文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static void upload(byte[] file, String filePath, String fileName) throws Exception {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        FileOutputStream out = new FileOutputStream(filePath + "/" + fileName);
        out.write(file);
        out.flush();
        out.close();
    }

    /**
     * 为防止一个目录下面出现太多文件，要使用hash算法打散存储
     *
     * @param filename 文件名，要根据文件名生成存储目录
     * @param savePath 文件存储路径
     * @return 新的存储目录
     * @Method: makePath
     * @Description:
     * @author: heyapei
     */
    public static String makePath(String filename, String savePath) {
        //得到文件名的hashCode的值，得到的就是filename这个字符串对象在内存中的地址
        int hashcode = filename.hashCode();
        //0--15
        int dir1 = hashcode & 0xf;
        //0-15
        int dir2 = (hashcode & 0xf0) >> 4;
        //构造新的保存目录 //upload\2\3  upload\3\5
        String dir = savePath + "/" + dir1 + "/" + dir2;
        //File既可以代表文件也可以代表目录
        File file = new File(dir);
        //如果目录不存在
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }
        return dir;
    }

    /**
     * @param filename     要下载的文件名
     * @param saveRootPath 上传文件保存的根目录，也就是/WEB-INF/upload目录
     * @return 要下载的文件的存储目录
     * @Method: findFileSavePathByFileName
     * @Description: 通过文件名和存储上传文件根目录找出要下载的文件的所在路径
     * @author: heyapei
     */
    public static String findFileSavePathByFileName(String filename, String saveRootPath) {
        int hashcode = filename.hashCode();
        int dir1 = hashcode & 0xf;  //0--15
        int dir2 = (hashcode & 0xf0) >> 4;  //0-15
        String dir = saveRootPath + "\\" + dir1 + "\\" + dir2;  //upload\2\3  upload\3\5
        File file = new File(dir);
        if (!file.exists()) {
            //创建目录
            file.mkdirs();
        }
        return dir;
    }


}
