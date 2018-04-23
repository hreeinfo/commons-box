package commons.box.util;

import commons.box.app.AppLog;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;

/**
 * <p>创建作者：xingxiuyi </p>
 * <p>创建日期：2018/4/22 </p>
 * <p>版权所属：xingxiuyi </p>
 */
public class Servlets {
    private static final AppLog LOG = Logs.get(Servlets.class);

    private static final String[] REMOTE_ADDR_HEADER_NAMES = {
            "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP", "HTTP_CLIENT_IP", "HTTP_X_FORWARDED_FOR"
    };

    /**
     * 获取真实IP地址 TODO 验证逻辑是否有效
     *
     * @param request
     * @return
     */
    public static final String remoteAddr(HttpServletRequest request) {
        String ip = Strs.trim(request.getHeader("X-Forwarded-For"));

        try {
            if (Strs.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) {
                Enumeration<String> rhs = request.getHeaderNames();
                while ((Strs.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) && rhs.hasMoreElements()) {
                    String hn = rhs.nextElement();
                    for (String dn : REMOTE_ADDR_HEADER_NAMES) {
                        if (Strs.equalsIgnoreCase(hn, dn)) {
                            ip = Strs.trim(request.getHeader(hn));
                            break;
                        }
                    }
                }
            }

            if (Strs.contains(ip, ",")) {
                String[] ips = ip.split(",");
                for (String ip1 : ips) {
                    String tip1 = Strs.trim(ip1);
                    if (!("unknown".equalsIgnoreCase(tip1))) {
                        ip = tip1;
                        break;
                    }
                }
            }
        } catch (Exception ignored) {
        }

        if (Strs.isBlank(ip) || "unknown".equalsIgnoreCase(ip)) ip = request.getRemoteAddr();

        return ip;
    }
}
