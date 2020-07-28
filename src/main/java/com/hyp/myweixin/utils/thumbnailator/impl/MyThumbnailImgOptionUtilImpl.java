package com.hyp.myweixin.utils.thumbnailator.impl;

import com.hyp.myweixin.exception.MyDefinitionException;
import com.hyp.myweixin.utils.MyCommonUtil;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgOptionUtil;
import com.hyp.myweixin.utils.thumbnailator.MyThumbnailImgType;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.apache.commons.io.FileUtils;
import org.apache.http.entity.ContentType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.channels.FileChannel;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/7/26 9:55
 * @Description: TODO
 */
@Slf4j
@Service
public class MyThumbnailImgOptionUtilImpl implements MyThumbnailImgOptionUtil {


    /**
     * 压缩图片
     *
     * @param multipartFile      图片文件
     * @param myThumbnailImgType 需要转换的类型 填写null为不转换图片类型
     * @param width              压缩宽度
     * @param length             压缩高度
     * @param outputQuality      压缩质量比 outputQuality是图片的质量，值也是在0到1，越接近于1质量越好，越接近于0质量越差
     *                           //@param scale   不可以和size同时使用           压缩比例 缩放比例，大于1就是变大，小于1就是缩小 可使用该公式计算 1.0 / (num / (1024 * 1024) + 1.0)
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public InputStream compressImage(MultipartFile multipartFile,
                                     MyThumbnailImgType myThumbnailImgType,
                                     Integer width, Integer length,
                                     Float outputQuality)
            throws MyDefinitionException {

        //*************对不是jpg格式的图片转换成jpg格式***************
        //获取文件名后缀，判断其格式
        int begin = multipartFile.getOriginalFilename().lastIndexOf(".");
        int last = multipartFile.getOriginalFilename().length();
        //获得文件后缀名
        String houzuiFileName = multipartFile.getOriginalFilename().substring(begin, last);

        //创建临时文件
        File tempFile = new File(multipartFile.getOriginalFilename());
        //写入临时File文件 tempFile，将multipartFile转换成File
        try {
            //import org.apache.commons.io.FileUtils;
            FileUtils.copyInputStreamToFile(multipartFile.getInputStream(), tempFile);
        } catch (IOException e) {
            log.error("压缩图片过程写临时文件失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("压缩图片过程写临时文件失败");
        }
        String covertType = null;
        if (myThumbnailImgType != null) {
            covertType = myThumbnailImgType.getImgType();
        }
        //如果文件不是对应的格式,转换其格式
        if (covertType != null && !covertType.equalsIgnoreCase(houzuiFileName)) {
            //ImageUtils是一个工具类，下面给出
            //将png格式转换成jpg，输出到tempFile
            convert(multipartFile.getOriginalFilename(), covertType, tempFile.getAbsolutePath());//测试OK
            //*************对不是jpg格式的图片转换成jpg格式***************
        }
        try {
            float qualityTemp = 1f;
            if (outputQuality != null && outputQuality > 0) {
                qualityTemp = outputQuality;
            }

            //压缩图片
            BufferedImage bufferedImage = Thumbnails.of(tempFile)
                    .size(width, length)//指定压缩之后的图片尺寸
                    .outputQuality(qualityTemp)//图片压缩质量
                    .asBufferedImage();
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ImageIO.write(bufferedImage, MyThumbnailImgType.IMAGE_TYPE_PNG.getImgType(), os);
            InputStream inputStream = new ByteArrayInputStream(os.toByteArray());
            //System.out.println(inputStream);
            return inputStream;
        } catch (IOException e) {
            log.error("压缩图片过程压缩图片失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("压缩图片过程压缩图片失败");
        } finally {
            //会在本地产生临时文件，用完后需要删除
            if (tempFile.exists()) {
                tempFile.delete();
            }
        }
    }

    /**
     * 当前只提供给图片压缩使用
     * 图像类型转换：GIF->JPG、GIF->PNG、PNG->JPG、PNG->GIF(X)、BMP->PNG
     *
     * @param srcImageFile  源图像地址
     * @param formatName    包含格式非正式名称的 String：如JPG、JPEG、GIF等
     * @param destImageFile 目标图像地址
     */
    public void convert(String srcImageFile, String formatName, String destImageFile) throws MyDefinitionException {
        try {
            File f = new File(srcImageFile);
            f.canRead();
            f.canWrite();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(destImageFile));
        } catch (Exception e) {
            log.error("图片数据转换失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("图片数据转换失败");
        }
    }

    /**
     * inputStream转成File
     *
     * @param ins
     * @param file
     * @throws MyDefinitionException
     */
    @Override
    public void inputStreamToFile(InputStream ins, File file) throws MyDefinitionException {
        try {
            OutputStream os = new FileOutputStream(file);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.close();
            ins.close();
        } catch (IOException e) {
            log.error("InputStream转File过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("InputStream转File过程失败");
        }
    }

    /**
     * file转换为inputStream
     *
     * @param file
     * @return
     * @throws FileNotFoundException
     */
    public InputStream fileToInputStream(File file) throws MyDefinitionException {
        try {
            return new FileInputStream(file);
        } catch (FileNotFoundException e) {
            log.error("file转换为inputStream过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("file转换为inputStream过程失败");
        }
    }

    /**
     * 将str转换为inputStream
     *
     * @param str
     * @return
     */
    public InputStream stringToInputStream(String str) {
        ByteArrayInputStream is = new ByteArrayInputStream(str.getBytes());
        return is;
    }

    /**
     * 将inputStream转换为str
     *
     * @param is
     * @return
     * @throws IOException
     */
    public String inputStreamToStr(InputStream is) {
        try {
            StringBuffer sb;
            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(is));

                sb = new StringBuffer();

                String data;
                while ((data = br.readLine()) != null) {
                    sb.append(data);
                }
            } finally {
                br.close();
            }
            return sb.toString();
        } catch (IOException e) {
            log.error("inputStream转换为string过程失败，失败原因：{}", e.toString());
            throw new MyDefinitionException("inputStream转换为string过程失败");
        }
    }

    /**
     * file转MultipartFile
     *
     * @param file
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public MultipartFile fileToMultipartFile(File file) throws MyDefinitionException {
        // MockMultipartFile(String name, @Nullable String originalFilename, @Nullable String contentType, InputStream contentStream)
        // 其中originalFilename,String contentType 旧名字，类型  可为空
        // ContentType.APPLICATION_OCTET_STREAM.toString() 需要使用HttpClient的包
        MultipartFile multipartFile = null;
        try {
            FileInputStream fileInputStream = new FileInputStream(file);
            multipartFile = new MockMultipartFile(MyCommonUtil.getUUID2() + file.getName(), file.getName(), ContentType.APPLICATION_OCTET_STREAM.toString(), fileInputStream);
        } catch (IOException e) {
            log.error("file转MultipartFile错误，错误原因：{}", e.toString());
            throw new MyDefinitionException("file转MultipartFile错误");
        }
        return multipartFile;
    }


    /**
     * 获取文件大小
     *
     * @param file
     * @return
     * @throws MyDefinitionException
     */
    @Override
    public Long getFileSize(File file) throws MyDefinitionException {
        long size = 0;
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel fc = fis.getChannel();
            size = fc.size();
        } catch (IOException e) {
            log.error("计算文件大小错误,错误原因:{}", e.toString());
            throw new MyDefinitionException("计算文件大小错误");
        }
        return size;
    }


}
