package com.gatsby.note.filter;

import cn.hutool.core.util.StrUtil;
import lombok.experimental.FieldDefaults;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @PACKAGE_NAME: com.gatsby.note.filter
 * @NAME: EncodingFilter
 * @AUTHOR: Jonah
 * @DATE: 2023/5/18
 */

/**
 * 处理请求乱码
 * 服务器默认的解析编码为ISO-8859-1，不支持中文
 *
 */
@WebFilter("/*")
public class EncodingFilter implements Filter {

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void destroy() {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;

        req.setCharacterEncoding("UTF-8");

        String method = req.getMethod();

        if("GET".equalsIgnoreCase(method)){
            String serverInfo = req.getServletContext().getServerInfo();
            String version = serverInfo.substring(serverInfo.lastIndexOf("/")+1,serverInfo.indexOf("."));

            if(version!=null && Integer.parseInt(version)<8){
                MyWapper myRequest = new MyWapper(req);
                filterChain.doFilter(myRequest,resp);
                return;
            }
        }
        filterChain.doFilter(req,resp);
    }

    class MyWapper extends HttpServletRequestWrapper{

        private HttpServletRequest request;

        /**
         * 带参构造
         * 得到需要处理的req对象
         * @param req
         */
        public MyWapper(HttpServletRequest req){
            super(req);
            this.request = req;
        }

        /**
         * 重写getParameter方法，处理乱码问题
         * @param name
         * @return
         */
        @Override
        public String getParameter(String name) {
            String value = request.getParameter(name);
            if(StrUtil.isBlank(value)){
                return value;
            }

            try{
                value = new String(value.getBytes("ISO-8859-1"),"UTF-8");
            }catch (Exception e){
                e.printStackTrace();
            }

            return value;
        }
    }
}
