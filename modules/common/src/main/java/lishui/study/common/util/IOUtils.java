/*
 * Copyright (C) 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package lishui.study.common.util;

import android.content.Context;

import java.io.ByteArrayOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.nio.charset.Charset;
import java.util.UUID;

/**
 * Supports various IO utility functions
 */
public class IOUtils {

    private static final int BUF_SIZE = 0x1000; // 4K
    static final Charset US_ASCII = Charset.forName("US-ASCII");
    static final Charset UTF_8 = Charset.forName("UTF-8");

    private IOUtils() {
    }

    static String readFully(Reader reader) throws IOException {
        try {
            StringWriter writer = new StringWriter();
            char[] buffer = new char[1024];
            int count;
            while ((count = reader.read(buffer)) != -1) {
                writer.write(buffer, 0, count);
            }
            return writer.toString();
        } finally {
            reader.close();
        }
    }

    /**
     * Deletes the contents of {@code dir}. Throws an IOException if any file
     * could not be deleted, or if {@code dir} is not a readable directory.
     */
    static void deleteContents(File dir) throws IOException {
        File[] files = dir.listFiles();
        if (files == null) {
            throw new IOException("not a readable directory: " + dir);
        }
        for (File file : files) {
            if (file.isDirectory()) {
                deleteContents(file);
            }
            if (!file.delete()) {
                throw new IOException("failed to delete file: " + file);
            }
        }
    }

    static void closeQuietly(/*Auto*/Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException rethrown) {
                throw rethrown;
            } catch (Exception ignored) {
            }
        }
    }

    public static byte[] toByteArray(File file) throws IOException {
        try (InputStream in = new FileInputStream(file)) {
            return toByteArray(in);
        }
    }

    public static byte[] toByteArray(InputStream in) throws IOException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        copy(in, out);
        return out.toByteArray();
    }

    public static long copy(InputStream from, OutputStream to) throws IOException {
        byte[] buf = new byte[BUF_SIZE];
        long total = 0;
        int r;
        while ((r = from.read(buf)) != -1) {
            to.write(buf, 0, r);
            total += r;
        }
        return total;
    }

    /**
     * Utility method to debug binary data
     */
    public static String createTempFile(Context context, byte[] data) {

        String name = UUID.randomUUID().toString();
        File file = new File(context.getCacheDir(), name);
        try (FileOutputStream fo = new FileOutputStream(file)) {
            fo.write(data);
            fo.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return file.getAbsolutePath();
    }
}
