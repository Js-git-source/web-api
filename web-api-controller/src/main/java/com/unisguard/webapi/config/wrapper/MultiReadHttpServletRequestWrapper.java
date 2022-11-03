package com.unisguard.webapi.config.wrapper;

import javax.servlet.ReadListener;
import javax.servlet.ServletInputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * @author zemel
 * @date 2021/12/26 16:49
 */
public class MultiReadHttpServletRequestWrapper extends HttpServletRequestWrapper {
    private final String body;

    public MultiReadHttpServletRequestWrapper(HttpServletRequest request) throws IOException {
        super(request);
        if (request.getContentType() == null) {
            body = "";
        } else if (request.getContentType().contains("multipart/form-data")) {
            body = "";
        } else {
            StringBuilder builder = new StringBuilder();
            BufferedReader bufferedReader = request.getReader();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                builder.append(line);
            }
            body = builder.toString();
        }
    }

    public String getBody() {
        return body;
    }

    @Override
    public ServletInputStream getInputStream() {
        final ByteArrayInputStream bais = new ByteArrayInputStream(body.getBytes());
        return new ServletInputStream() {

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {

            }

            @Override
            public int read() {
                return bais.read();
            }
        };
    }

    @Override
    public BufferedReader getReader() {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }
}
