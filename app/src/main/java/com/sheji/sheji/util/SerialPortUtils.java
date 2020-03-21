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

/**
 * @author kk-zhaoqingfeng
 */
public class SerialPortUtils {
    //CC 23 DD FB 00 00 00 01 01 0A 0D
    //CC 23 BB FE 00 00 00 01 01 70 0A 0D     100是64 255是FF
    //CC 23 BB DB 00 00 00 01 00 00 00 01 01 0A 0D
    private static SerialPortUtils sInstance = null;

    private String port = "ttyUSB0";

    private SerialPort serialPort = null;
    private InputStream inputStream = null;
    private OutputStream outputStream = null;
    private Thread receiveThread = null;
    private boolean flag = false;

    private StringBuffer stringBuffer = new StringBuffer();
    private String recInfo;

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            String action = recInfo.substring(6, 8);
            if ("FB".equals(action)) {
                onLoginDataReceiveListener.onFBReceive("01".equals(recInfo.substring(16, 18)));
            } else if ("FE".equals(action)) {
                onMainDataReceiveListener.onFEReceive(recInfo.substring(16, 18), recInfo.substring(18, 20));
            } else if ("DB".equals(action) || "DC".equals(action)) {
                boolean hit = "01".equals(recInfo.substring(24, 26));
                String position;
                int precisionNumber = 0;
                if (hit && "DC".equals(action)) {
                    position = recInfo.substring(16, 24);
                    if ("F50AFF00".equals(position)) {
                        position = "0点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AFE01".equals(position)) {
                        position = "1点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AFD02".equals(position)) {
                        position = "2点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AFC03".equals(position)) {
                        position = "3点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AFB04".equals(position)) {
                        position = "4点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AFA05".equals(position)) {
                        position = "5点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AF906".equals(position)) {
                        position = "6点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AF807".equals(position)) {
                        position = "7点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AF708".equals(position)) {
                        position = "8点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AF609".equals(position)) {
                        position = "9点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AEF10".equals(position)) {
                        position = "10点钟10环";
                        precisionNumber = 10;
                    } else if ("F50AEE11".equals(position)) {
                        position = "11点钟10环";
                        precisionNumber = 10;
                    } else if ("F609FF00".equals(position)) {
                        position = "0点钟9环";
                        precisionNumber = 10;
                    } else if ("F609FE01".equals(position)) {
                        position = "1点钟9环";
                        precisionNumber = 10;
                    } else if ("F609FD02".equals(position)) {
                        position = "2点钟9环";
                        precisionNumber = 10;
                    } else if ("F609FC03".equals(position)) {
                        position = "3点钟9环";
                        precisionNumber = 10;
                    } else if ("F609FB04".equals(position)) {
                        position = "4点钟9环";
                        precisionNumber = 10;
                    } else if ("F609FA05".equals(position)) {
                        position = "5点钟9环";
                        precisionNumber = 10;
                    } else if ("F609F906".equals(position)) {
                        position = "6点钟9环";
                        precisionNumber = 10;
                    } else if ("F609F807".equals(position)) {
                        position = "7点钟9环";
                        precisionNumber = 10;
                    } else if ("F609F708".equals(position)) {
                        position = "8点钟9环";
                        precisionNumber = 10;
                    } else if ("F609F609".equals(position)) {
                        position = "9点钟9环";
                        precisionNumber = 10;
                    } else if ("F609EF10".equals(position)) {
                        position = "10点钟9环";
                        precisionNumber = 10;
                    } else if ("F609EE11".equals(position)) {
                        position = "11点钟9环";
                        precisionNumber = 10;
                    }else if ("F708FF00".equals(position)) {
                        position = "0点钟8环";
                        precisionNumber = 10;
                    } else if ("F708FE01".equals(position)) {
                        position = "1点钟8环";
                        precisionNumber = 10;
                    } else if ("F708FD02".equals(position)) {
                        position = "2点钟8环";
                        precisionNumber = 10;
                    } else if ("F708FC03".equals(position)) {
                        position = "3点钟8环";
                        precisionNumber = 10;
                    } else if ("F708FB04".equals(position)) {
                        position = "4点钟8环";
                        precisionNumber = 10;
                    } else if ("F708FA05".equals(position)) {
                        position = "5点钟8环";
                        precisionNumber = 10;
                    } else if ("F708F906".equals(position)) {
                        position = "6点钟8环";
                        precisionNumber = 10;
                    } else if ("F708F807".equals(position)) {
                        position = "7点钟8环";
                        precisionNumber = 10;
                    } else if ("F708F708".equals(position)) {
                        position = "8点钟8环";
                        precisionNumber = 10;
                    } else if ("F708F609".equals(position)) {
                        position = "9点钟8环";
                        precisionNumber = 10;
                    } else if ("F708EF10".equals(position)) {
                        position = "10点钟8环";
                        precisionNumber = 10;
                    } else if ("F708EE11".equals(position)) {
                        position = "11点钟8环";
                        precisionNumber = 10;
                    }else if ("F807FF00".equals(position)) {
                        position = "0点钟7环";
                        precisionNumber = 10;
                    } else if ("F807FE01".equals(position)) {
                        position = "1点钟7环";
                        precisionNumber = 10;
                    } else if ("F807FD02".equals(position)) {
                        position = "2点钟7环";
                        precisionNumber = 10;
                    } else if ("F807FC03".equals(position)) {
                        position = "3点钟7环";
                        precisionNumber = 10;
                    } else if ("F807FB04".equals(position)) {
                        position = "4点钟7环";
                        precisionNumber = 10;
                    } else if ("F807FA05".equals(position)) {
                        position = "5点钟7环";
                        precisionNumber = 10;
                    } else if ("F807F906".equals(position)) {
                        position = "6点钟7环";
                        precisionNumber = 10;
                    } else if ("F807F807".equals(position)) {
                        position = "7点钟7环";
                        precisionNumber = 10;
                    } else if ("F807F708".equals(position)) {
                        position = "8点钟7环";
                        precisionNumber = 10;
                    } else if ("F807F609".equals(position)) {
                        position = "9点钟7环";
                        precisionNumber = 10;
                    } else if ("F807EF10".equals(position)) {
                        position = "10点钟7环";
                        precisionNumber = 10;
                    } else if ("F807EE11".equals(position)) {
                        position = "11点钟7环";
                        precisionNumber = 10;
                    }else if ("F906FF00".equals(position)) {
                        position = "0点钟6环";
                        precisionNumber = 10;
                    } else if ("F906FE01".equals(position)) {
                        position = "1点钟6环";
                        precisionNumber = 10;
                    } else if ("F906FD02".equals(position)) {
                        position = "2点钟6环";
                        precisionNumber = 10;
                    } else if ("F906FC03".equals(position)) {
                        position = "3点钟6环";
                        precisionNumber = 10;
                    } else if ("F906FB04".equals(position)) {
                        position = "4点钟6环";
                        precisionNumber = 10;
                    } else if ("F906FA05".equals(position)) {
                        position = "5点钟6环";
                        precisionNumber = 10;
                    } else if ("F906F906".equals(position)) {
                        position = "6点钟6环";
                        precisionNumber = 10;
                    } else if ("F906F807".equals(position)) {
                        position = "7点钟6环";
                        precisionNumber = 10;
                    } else if ("F906F708".equals(position)) {
                        position = "8点钟6环";
                        precisionNumber = 10;
                    } else if ("F906F609".equals(position)) {
                        position = "9点钟6环";
                        precisionNumber = 10;
                    } else if ("F906EF10".equals(position)) {
                        position = "10点钟6环";
                        precisionNumber = 10;
                    } else if ("F906EE11".equals(position)) {
                        position = "11点钟6环";
                        precisionNumber = 10;
                    }else if ("FA05FF00".equals(position)) {
                        position = "0点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05FE01".equals(position)) {
                        position = "1点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05FD02".equals(position)) {
                        position = "2点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05FC03".equals(position)) {
                        position = "3点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05FB04".equals(position)) {
                        position = "4点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05FA05".equals(position)) {
                        position = "5点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05F906".equals(position)) {
                        position = "6点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05F807".equals(position)) {
                        position = "7点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05F708".equals(position)) {
                        position = "8点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05F609".equals(position)) {
                        position = "9点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05EF10".equals(position)) {
                        position = "10点钟5环";
                        precisionNumber = 10;
                    } else if ("FA05EE11".equals(position)) {
                        position = "11点钟5环";
                        precisionNumber = 10;
                    } else {
                        position = "";
                        precisionNumber = 0;
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
            serialPort = new SerialPort(new File("/dev/" + port), 1200, 0);
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
     * @param sendData 要发送的数据
     */
    public void sendSerialPort(byte[] sendData) {
        try {
            if (sendData.length > 0) {
                outputStream.write(sendData);
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
                            recInfo = HexToString(readData, 0, size);
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
        /**
         * 总控台发送Pad枪和计数器绑定的数据协议-反馈报文
         *
         * @param success 成功/失败
         */
        public void onFBReceive(boolean success);
    }

    public interface OnMainDataReceiveListener {
        /**
         * 计数器同时发送PAD和总控台的数据协议(开火 单发/双发/三连发  电量百分比)
         *
         * @param fire             开火 单发/双发/三连发
         * @param electricQuantity 电量百分比
         */
        public void onFEReceive(String fire, String electricQuantity);


        /**
         * 靶机同时发送PAD和总控台的命中数据协议(精准环数 命中)
         *
         * @param type            靶子类型(普通靶/精准靶)
         * @param position        精准环数
         * @param precisionNumber 环数
         * @param hit             命中/未命中
         */
        public void onDBOrDCReceive(String type, String position, int precisionNumber, boolean hit);
    }

    public void setOnLoginDataReceiveListener(OnLoginDataReceiveListener loginDataReceiveListener) {
        this.onLoginDataReceiveListener = loginDataReceiveListener;
    }

    public void setOnMainDataReceiveListener(OnMainDataReceiveListener mainDataReceiveListener) {
        this.onMainDataReceiveListener = mainDataReceiveListener;
    }

    private String HexToString(byte[] data, int start, int end) {
        char[] HEX_CODE = {'0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

        if (data == null) {
            return null;
        }
        int length = data.length;
        if (start < 0 || end < 0 || end < start ||
                start > length || end > length) {
            return null;
        }
        String mystring = "";
        for (int i = start; i < end; i++) {
            byte b = data[i];
            mystring += HEX_CODE[(b >> 4) & 0x0F];
            mystring += HEX_CODE[b & 0x0F];
        }
        return mystring;
    }
}
