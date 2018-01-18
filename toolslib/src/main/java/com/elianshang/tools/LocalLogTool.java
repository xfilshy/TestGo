package com.elianshang.tools;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.StringReader;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 本地文件日志工具
 * */
public class LocalLogTool {

    /**
     * 同步锁
     */
    private final Object lock = new Object();

    /**
     * 打印线程池大小
     */
    private final int POOL_SIZE = 2;

    /**
     * 打印线程池
     */
    private ExecutorService executor = Executors.newFixedThreadPool(POOL_SIZE);

    /**
     * 单例实体
     */
    private static LocalLogTool mLetvLogTool = null;

    private synchronized static LocalLogTool getInstance() {
        if (mLetvLogTool == null) {
            mLetvLogTool = new LocalLogTool();
        }
        return mLetvLogTool;
    }

    /**
     * 打印
     */
    public synchronized static void log(String data, File file) {
        if (!LogTool.isDebug()) {
            return;
        }

        getInstance().sendHandler(data, file);
    }

    private void sendHandler(String data, File file) {
        executor.execute(new Handler(data, file));
    }

    class Handler implements Runnable {

        private String data;

        private File file;

        public Handler(String data, File file) {
            this.data = data;
            this.file = file;
        }

        @Override
        public void run() {
            synchronized (lock) {
                File dir = file.getParentFile();
                if (!dir.exists()) {
                    dir.mkdirs();
                }

                try {
                    if (!file.exists()) {
                        file.createNewFile();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                BufferedReader stringReader = null;
                FileWriter fileWriter = null;

                try {

                    stringReader = new BufferedReader(new StringReader(data));

                    fileWriter = new FileWriter(file, true);

                    String line = null;

                    while ((line = stringReader.readLine()) != null) {
                        fileWriter.write(line);
                        fileWriter.write("\r\n");
                    }

                    stringReader.close();
                    fileWriter.flush();
                    fileWriter.close();
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        stringReader.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

    }
}
