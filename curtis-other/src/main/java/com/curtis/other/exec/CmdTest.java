package com.curtis.other.exec;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.*;

public class CmdTest {

    @Test
    public void test() {
        Runtime runtime = Runtime.getRuntime();


        ExecutorService executorService = new ThreadPoolExecutor(2, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        try {

            String[] cmd = new String[]{"cmd.exe", "/C", "wmic process get name"};
            // Process process = runtime.exec("cmd.exe", new String[]{"/C", "wmic process get name"});
            // Process process = runtime.exec(cmd);
            Process process = runtime.exec("cmd.exe /C wmic process get name");
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

    public static String getOsCmd() {
        //获得系统属性集
        Properties props = System.getProperties();
        //操作系统名称
        String osName = props.getProperty("os.name");
        if (osName.toLowerCase().contains("linux")) {
            return "/bin/sh -c";
        } else if (osName.toLowerCase().contains("windows")) {
            return "cmd /c";
        } else {
            throw new RuntimeException("服务器不是linux|windows操作系统");
        }
    }

    @Test
    public void test2() {
        Runtime runtime = Runtime.getRuntime();


        ExecutorService executorService = new ThreadPoolExecutor(2, 10, 0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10), Executors.defaultThreadFactory(),
                new ThreadPoolExecutor.DiscardPolicy());
        try {

            // String[] envp = { "JAVA_HOME=D:\\Program Files\\Java\\jdk1.8.0_271"};
            // String[] cmd = new String[]{"cmd.exe", "/C", "G:\\test.bat", "11", "22", "33"};
            String[] cmd = new String[]{"G:\\test.bat", "11", "22", "33"};
            // Process process = runtime.exec("cmd.exe", new String[]{"/C", "wmic process get name"});
            // Process process = runtime.exec(cmd);
            Process process = runtime.exec(cmd);
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
