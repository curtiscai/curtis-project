package com.curtis.other.exec;

import org.apache.commons.io.IOUtils;
import org.junit.Test;

import java.io.*;
import java.nio.charset.Charset;
import java.util.concurrent.*;

public class ScriptTest {

    @Test
    public void testExec() {
        Runtime runtime = Runtime.getRuntime();

        ExecutorService executorService = new ThreadPoolExecutor(2, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        try {

            Process process = runtime.exec("ping 127.0.0.1");
            InputStream ins = process.getInputStream();
            InputStream ers = process.getErrorStream();

            executorService.submit(() -> {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins, Charset.forName("gb2312")))) {
                    String line = null;
                    line = bufferedReader.readLine();
                    while (line != null) {
                        System.out.println(line);
                        line = bufferedReader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Causes the current thread to wait, if necessary, until the
            //      * process represented by this {@code Process} object has
            //      * terminated.  This method returns immediately if the subprocess
            //      * has already terminated.  If the subprocess has not yet
            //      * terminated, the calling thread will be blocked until the
            //      * subprocess exits.
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }


    @Test
    public void testExec2() {
        Runtime runtime = Runtime.getRuntime();
        try {
            runtime.exec("notepad.exe");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void test2() {
        Runtime runtime = Runtime.getRuntime();
        ExecutorService executorService = new ThreadPoolExecutor(2, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        try {

            Process process = runtime.exec("cmd", new String[]{"/c", "echo hello"});
            InputStream ins = process.getInputStream();
            InputStream ers = process.getErrorStream();

            executorService.submit(() -> {
                try (BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(ins, Charset.forName("gb2312")))) {
                    String line = null;
                    line = bufferedReader.readLine();
                    while (line != null) {
                        System.out.println(line);
                        line = bufferedReader.readLine();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });

            // Causes the current thread to wait, if necessary, until the
            //      * process represented by this {@code Process} object has
            //      * terminated.  This method returns immediately if the subprocess
            //      * has already terminated.  If the subprocess has not yet
            //      * terminated, the calling thread will be blocked until the
            //      * subprocess exits.
            process.waitFor();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}