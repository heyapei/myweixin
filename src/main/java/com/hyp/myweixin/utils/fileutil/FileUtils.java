package com.hyp.myweixin.utils.fileutil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

/**
 * 文件上传工具包
 */
@Slf4j
public class FileUtils {

    /**
     * @param file     文件
     * @param path     文件存放路径
     * @param fileName 源文件名
     * @return
     */
    public static boolean upload(MultipartFile file, String path, String fileName) {
        //生成新的文件名
        String realPath = path + "/" + FileNameUtils.getFileName(fileName);
        //使用原文件名
        //String realPath = path + "/" + fileName;
        File dest = new File(realPath);
        //判断文件父目录是否存在
        /*if (!dest.getParentFile().exists()) {
            dest.getParentFile().mkdir();
        }*/
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
}
