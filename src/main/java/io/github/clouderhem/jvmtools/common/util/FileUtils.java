package io.github.clouderhem.jvmtools.common.util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

/**
 * @author Aaron Yeung
 * @date 8/18/2023 9:24 PM
 */
public final class FileUtils {

    private static final Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static byte[] downloadFileFromUrl(String urlStr) {

        if (StringUtils.isEmpty(urlStr)) {
            throw new RuntimeException("文件链接不能为空: " + urlStr);
        }

        try {
            URL url = new URL(urlStr);
            URLConnection conn = url.openConnection();
            InputStream inStream = conn.getInputStream();

            byte[] buffer = new byte[1204];
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();

            int length;
            while ((length = inStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, length);
            }

            return byteArrayOutputStream.toByteArray();
        } catch (Exception e) {
            log.error("", e);
            throw new RuntimeException(e);
        }
    }
}
