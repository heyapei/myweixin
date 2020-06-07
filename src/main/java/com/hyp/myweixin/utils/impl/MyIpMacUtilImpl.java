package com.hyp.myweixin.utils.impl;

import com.hyp.myweixin.utils.MyIpMacUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @Author 何亚培
 * @Version V1.0
 * @Date 2020/6/7 0:26
 * @Description: TODO
 */
@Slf4j
@Service
public class MyIpMacUtilImpl implements MyIpMacUtil {

    // \b 是单词边界(连着的两个(字母字符 与 非字母字符) 之间的逻辑上的间隔),
    // 字符串在编译时会被转码一次,所以是 "\\b"
    // \B 是单词内部逻辑间隔(连着的两个字母字符之间的逻辑上的间隔)
    static String phoneReg = "\\b(ip(hone|od)|android|opera m(ob|in)i" + "|windows (phone|ce)|blackberry" + "|s(ymbian|eries60|amsung)|p(laybook|alm|rofile/midp"
            + "|laystation portable)|nokia|fennec|htc[-_]" + "|mobile|up.browser|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    static String tableReg = "\\b(ipad|tablet|(Nexus 7)|up.browser" + "|[1-4][0-9]{2}x[1-4][0-9]{2})\\b";
    // 移动设备正则匹配：手机端、平板
    static Pattern phonePat = Pattern.compile(phoneReg, Pattern.CASE_INSENSITIVE);
    static Pattern tablePat = Pattern.compile(tableReg, Pattern.CASE_INSENSITIVE);

    /**
     * 获取移动设备的类型
     *
     * @param userAgent
     * @return
     */
    @Override
    public String getMobileType(String userAgent) {


        if (null == userAgent) {
            userAgent = "";
        }
        // 匹配
        Matcher matcherPhone = phonePat.matcher(userAgent);
        Matcher matcherTable = tablePat.matcher(userAgent);
        if (matcherPhone.find()) {
            return matcherPhone.group();
        } else if (matcherTable.find()) {
            return matcherTable.group();
        } else {
            return "pc";
        }
    }


    /**
     * 识别移动设备的名称
     */
    Pattern p = Pattern.compile(";\\s?(\\S*?\\s?\\S*?)\\s?(Build)?/");

    /**
     * 获取移动设备的名称
     *
     * @param userAgent
     * @return
     */
    @Override
    public String getMobileName(String userAgent) {
        if (null == userAgent) {
            userAgent = "";
        }
        Matcher matcher = p.matcher(userAgent);
        String mobileType = null;
        if (matcher.find()) {
            mobileType = matcher.group(1).trim();
        } else {
            // 对于iPhone的特殊处理
            String iphoneOS = "iPhone OS";
            if (userAgent.contains(iphoneOS)) {
                mobileType = userAgent.replaceAll(" ", "");
                String iphoneOSIndex = iphoneOS.replaceAll(" ", "");
                mobileType = mobileType.substring(mobileType.indexOf(iphoneOSIndex) + 6, mobileType.indexOf(iphoneOSIndex) + 10);
            } else {
                mobileType = "未知型号";
            }
        }
        return mobileType;
    }

    /**
     * 获取IP地址
     *
     * @param request
     * @return
     */
    @Override
    public String getRealIP(HttpServletRequest request) {
        // 获取客户端ip地址
        String clientIp = request.getHeader("x-forwarded-for");

        if (clientIp == null || clientIp.length() == 0 || "unknown".equalsIgnoreCase(clientIp)) {
            clientIp = request.getRemoteAddr();
        }
        String[] clientIps = clientIp.split(",");
        if (clientIps.length <= 1) {
            return clientIp.trim();
        }
        // 判断是否来自CDN
        if (isComeFromCDN(request)) {
            if (clientIps.length >= 2) {
                return clientIps[clientIps.length - 2].trim();
            }
        }
        return clientIps[clientIps.length - 1].trim();
    }

    /**
     * 判断是否为CDN网络来源
     *
     * @param request
     * @return
     */
    @Override
    public boolean isComeFromCDN(HttpServletRequest request) {
        String host = request.getHeader("host");
        return host.contains("www.189.cn") || host.contains("shouji.189.cn") || host.contains(
                "image2.chinatelecom-ec.com") || host.contains(
                "image1.chinatelecom-ec.com");
    }

    /**
     * ip地址转long类型数据
     *
     * @param ipAddress
     * @return
     */
    @Override
    public long ipToLong(String ipAddress) {
        if (ipAddress.equalsIgnoreCase("0:0:0:0:0:0:0:1")) {
            ipAddress = "127.0.0.1";
        }
        String[] ip = ipAddress.split("\\.");
        long a = Integer.parseInt(ip[0]);
        long b = Integer.parseInt(ip[1]);
        long c = Integer.parseInt(ip[2]);
        long d = Integer.parseInt(ip[3]);
        return a * 256 * 256 * 256 + b * 256 * 256 + c * 256 + d;
    }

    /**
     * 判断是否为内网IP
     *
     * @param ipAddress
     * @return
     */
    @Override
    public boolean isInnerIP(String ipAddress) {
        boolean isInnerIp;
        long ipNum = ipToLong(ipAddress);
        /**
         私有IP：A类  10.0.0.0-10.255.255.255
         B类  172.16.0.0-172.31.255.255
         C类  192.168.0.0-192.168.255.255
         当然，还有127这个网段是环回地址
         **/
        long aBegin = ipToLong("10.0.0.0");
        long aEnd = ipToLong("10.255.255.255");

        long bBegin = ipToLong("172.16.0.0");
        long bEnd = ipToLong("172.31.255.255");

        long cBegin = ipToLong("192.168.0.0");
        long cEnd = ipToLong("192.168.255.255");
        isInnerIp = isInner(ipNum, aBegin, aEnd) || isInner(ipNum, bBegin, bEnd) || isInner(ipNum, cBegin, cEnd)
                || ipAddress.equals("127.0.0.1");
        return isInnerIp;
    }

    /**
     * long类型转IP
     *
     * @param longIp
     * @return
     */
    @Override
    public String longToIP(long longIp) {
        StringBuffer sb = new StringBuffer("");
        //直接右移24位
        sb.append(String.valueOf((longIp >>> 24)));
        sb.append(".");
        //将高8位置0，然后右移16位
        sb.append(String.valueOf((longIp & 0x00FFFFFF) >>> 16));
        sb.append(".");
        //将高16位置0，然后右移8位
        sb.append(String.valueOf((longIp & 0x0000FFFF) >>> 8));
        sb.append(".");
        //将高24位置0
        sb.append(String.valueOf((longIp & 0x000000FF)));
        return sb.toString();
    }

    private static boolean isInner(long userIp, long begin, long end) {
        return (userIp >= begin) && (userIp <= end);
    }


}
