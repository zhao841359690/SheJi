package com.sheji.sheji.util;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.Message;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.InvalidParameterException;

import android_serialport_api.SerialPort;

public class SerialPortUtils {
    private static SerialPortUtils sInstance = null;

    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Thread receiveThread = null;
    private boolean flag = false;

    private StringBuffer stringBuffer = new StringBuffer();
    private String recInfo;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String action = recInfo.substring(6, 8);
            if ("FB".equals(action)) {
                onLoginDataReceiveListener.onFBReceive("01".equals(recInfo.substring(15, 17)));
            } else if ("FE".equals(action)) {
                onMainDataReceiveListener.onFEReceive(recInfo.substring(17, recInfo.length() - 4));
            } else if ("DB".equals(action) || "DC".equals(action)) {
                boolean hit = "01".equals(recInfo.substring(recInfo.length() - 6, recInfo.length() - 4));
                String position;
                int precisionNumber = 0;
                if (hit) {
                    position = recInfo.substring(recInfo.length() - 17, recInfo.length() - 6);
                    if ("00 00 00 01".equals(position)) {
                        position = "10环上";
                        precisionNumber = 10;
                    } else if ("00 00 00 02".equals(position)) {
                        position = "10环右";
                        precisionNumber = 10;
                    } else if ("00 00 00 04".equals(position)) {
                        position = "10环下";
                        precisionNumber = 10;
                    } else if ("00 00 00 08".equals(position)) {
                        position = "10环左";
                        precisionNumber = 10;
                    } else if ("00 00 00 10".equals(position)) {
                        position = "9环上";
                        precisionNumber = 9;
                    } else if ("00 00 00 20".equals(position)) {
                        position = "9环右";
                        precisionNumber = 9;
                    } else if ("00 00 00 40".equals(position)) {
                        position = "9环下";
                        precisionNumber = 9;
                    } else if ("00 00 00 80".equals(position)) {
                        position = "9环左";
                        precisionNumber = 9;
                    } else if ("00 00 01 00".equals(position)) {
                        position = "8环上";
                        precisionNumber = 8;
                    } else if ("00 00 02 00".equals(position)) {
                        position = "8环右上";
                        precisionNumber = 8;
                    } else if ("00 00 04 00".equals(position)) {
                        position = "8环右";
                        precisionNumber = 8;
                    } else if ("00 00 08 00".equals(position)) {
                        position = "8右下";
                        precisionNumber = 8;
                    } else if ("00 00 10 00".equals(position)) {
                        position = "8环下";
                        precisionNumber = 8;
                    } else if ("00 00 20 00".equals(position)) {
                        position = "8左下";
                        precisionNumber = 8;
                    } else if ("00 00 40 00".equals(position)) {
                        position = "8环左";
                        precisionNumber = 8;
                    } else if ("00 00 80 00".equals(position)) {
                        position = "8环左上";
                        precisionNumber = 8;
                    } else if ("00 01 00 00".equals(position)) {
                        position = "7环上";
                        precisionNumber = 7;
                    } else if ("00 02 00 00".equals(position)) {
                        position = "7环右上";
                        precisionNumber = 7;
                    } else if ("00 04 00 00".equals(position)) {
                        position = "7环右";
                        precisionNumber = 7;
                    } else if ("00 08 00 00".equals(position)) {
                        position = "7环右下";
                        precisionNumber = 7;
                    } else if ("00 10 00 00".equals(position)) {
                        position = "7环下";
                        precisionNumber = 7;
                    } else if ("00 20 00 00".equals(position)) {
                        position = "7环左下";
                        precisionNumber = 7;
                    } else if ("00 40 00 00".equals(position)) {
                        position = "7环左";
                        precisionNumber = 7;
                    } else if ("00 80 00 00".equals(position)) {
                        position = "7环左上";
                        precisionNumber = 7;
                    } else if ("01 00 00 00".equals(position)) {
                        position = "6环上";
                        precisionNumber = 6;
                    } else if ("02 00 00 00".equals(position)) {
                        position = "6环右上";
                        precisionNumber = 6;
                    } else if ("04 00 00 00".equals(position)) {
                        position = "6环右";
                        precisionNumber = 6;
                    } else if ("08 00 00 00".equals(position)) {
                        position = "6环右下";
                        precisionNumber = 6;
                    } else if ("10 00 00 00".equals(position)) {
                        position = "6环下";
                        precisionNumber = 6;
                    } else if ("20 00 00 00".equals(position)) {
                        position = "6环左下";
                        precisionNumber = 6;
                    } else if ("40 00 00 00".equals(position)) {
                        position = "6环左";
                        precisionNumber = 6;
                    } else if ("80 00 00 00".equals(position)) {
                        position = "6环左上";
                        precisionNumber = 6;
                    }
                } else {
                    position = "无记录";
                    precisionNumber = 0;
                }
                onMainDataReceiveListener.onDBOrDCReceive(action, position, precisionNumber, hit);
            }
        }
    };

    public static SerialPortUtils getInstance() {
        if (sInstance == null) {
            synchronized (SerialPortUtils.class) {
                if (sInstance == null) {
                    sInstance = new SerialPortUtils();
                }
            }
        }
        return sInstance;
    }

    /**
     * 打开串口
     *
     * @return serialPort串口对象
     */
    public SerialPort openSerialPort() {
        try {
            serialPort = new SerialPort(new File("/dev/ttyUSB0"), 9600, 0);
            if (serialPort == null) {
                serialPort = new SerialPort(new File("/dev/ttyUSB1"), 9600, 0);
            }
            //获取打开的串口中的输入输出流，以便于串口数据的收发
            if (serialPort != null) {
                inputStream = serialPort.getInputStream();
                outputStream = serialPort.getOutputStream();

                flag = true;
                receiveSerialPort();
            } else {
                return null;
            }
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return serialPort;
    }

    /**
     * 关闭串口的方法
     * 关闭串口中的输入输出流
     */
    public void closeSerialPort() {
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
    public void receiveSerialPort() {
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
                            recInfo = new String(readData, 0, size);
                            stringBuffer.append(recInfo);
                            if (stringBuffer.length() > 4 && "0A0D".equals(stringBuffer.substring(stringBuffer.length() - 4, stringBuffer.length()))) {
                                recInfo = stringBuffer.toString();
                                stringBuffer = new StringBuffer();
                                handler.sendEmptyMessage(1);
                            }
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

    public OnLoginDataReceiveListener onLoginDataReceiveListener = null;

    public OnMainDataReceiveListener onMainDataReceiveListener = null;

    public interface OnLoginDataReceiveListener {
        //总控台发送Pad枪和计数器绑定的数据协议-反馈报文
        public void onFBReceive(boolean success);
    }

    public interface OnMainDataReceiveListener {
        //计数器同时发送PAD和总控台的数据协议(开火 单发/双发/三连发  电量百分比)
        public void onFEReceive(String electricQuantity);

        //靶机同时发送PAD和总控台的命中数据协议(精准环数 命中)
        public void onDBOrDCReceive(String type, String position, int precisionNumber, boolean hit);
    }

    public void setOnLoginDataReceiveListener(OnLoginDataReceiveListener loginDataReceiveListener) {
        this.onLoginDataReceiveListener = loginDataReceiveListener;
    }

    public void setOnMainDataReceiveListener(OnMainDataReceiveListener mainDataReceiveListener) {
        this.onMainDataReceiveListener = mainDataReceiveListener;
    }
}
