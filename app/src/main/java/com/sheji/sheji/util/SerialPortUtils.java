package com.sheji.sheji.util;

import android.os.Bundle;
import android.util.Log;

import com.sheji.sheji.activity.MainActivity;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import android_serialport_api.SerialPort;

/**
 * Created by WangChaowei on 2017/12/7.
 */

public class SerialPortUtils {
    private String port = "ttyS1";//串口号
    private int baudrate = 9600;//波特率

    public static SerialPort serialPort = null;
    public static InputStream inputStream = null;
    public static OutputStream outputStream = null;
    public static Thread receiveThread = null;
    public static boolean flag = false;

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort() {
        try {
            serialPort = new SerialPort(new File("/dev/" + port), baudrate, 0);
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            inputStream = serialPort.getInputStream();
            outputStream = serialPort.getOutputStream();
            flag = true;
            receiveSerialPort();
        } catch (IOException e) {
            return serialPort;
        }
        return serialPort;
    }

    /**
     * 关闭串口的方法
     * 关闭串口中的输入输出流
     */
    public static void closeSerialPort() {
        try {
            if (inputStream != null) {
                inputStream.close();
            }
            if (outputStream != null) {
                outputStream.close();
            }
            if (serialPort != null) {
                serialPort.close();
            }
            flag = false;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 发送串口数据的方法
     *
     * @param data 要发送的数据
     */
    public void sendSerialPort(String data) {
        try {
            byte[] sendData = data.getBytes();
            if (sendData.length > 0) {
                outputStream.write(sendData);
                outputStream.write('\n');
                outputStream.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * 接收串口数据的方法
     */
    public static void receiveSerialPort() {
        if (receiveThread != null) {
            return;
        }
        /*创建子线程接收串口数据
         */
        receiveThread = new Thread() {
            @Override
            public void run() {
                while (flag) {
                    try {
                        byte[] readData = new byte[1024];
                        if (inputStream == null) {
                            return;
                        }
                        int size = inputStream.read(readData);
                        if (size > 0 && flag) {
                            String recinfo = new String(readData, 0, size);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        //启动接收线程
        receiveThread.start();
    }
}
