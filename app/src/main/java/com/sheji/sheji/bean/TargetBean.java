package com.sheji.sheji.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

@Entity
public class TargetBean {
    //表示头靶
    public static final int TYPE_HEAD = 0x01;
    //表示身靶
    public static final int TYPE_BODY = 0x02;
    //表示胸靶
    public static final int TYPE_CHEST = 0x03;
    //表示精准靶
    public static final int TYPE_PRECISION = 0x04;

    @Id(autoincrement = true)
    private Long id;

    private int type;

    private int number;

    private boolean hit;

    private int RingNumber = 0;

    private String precisionRingNumber;

    private String shootingInterval;

    private String time;

    private String date;

    @Generated(hash = 1439249859)
    public TargetBean(Long id, int type, int number, boolean hit, int RingNumber,
                      String precisionRingNumber, String shootingInterval, String time,
                      String date) {
        this.id = id;
        this.type = type;
        this.number = number;
        this.hit = hit;
        this.RingNumber = RingNumber;
        this.precisionRingNumber = precisionRingNumber;
        this.shootingInterval = shootingInterval;
        this.time = time;
        this.date = date;
    }

    @Generated(hash = 196922752)
    public TargetBean() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getNumber() {
        return this.number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public boolean getHit() {
        return this.hit;
    }

    public void setHit(boolean hit) {
        this.hit = hit;
    }

    public int getRingNumber() {
        return this.RingNumber;
    }

    public void setRingNumber(int RingNumber) {
        this.RingNumber = RingNumber;
    }

    public String getPrecisionRingNumber() {
        return this.precisionRingNumber;
    }

    public void setPrecisionRingNumber(String precisionRingNumber) {
        this.precisionRingNumber = precisionRingNumber;
    }

    public String getShootingInterval() {
        return this.shootingInterval;
    }

    public void setShootingInterval(String shootingInterval) {
        this.shootingInterval = shootingInterval;
    }

    public String getTime() {
        return this.time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return this.date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
