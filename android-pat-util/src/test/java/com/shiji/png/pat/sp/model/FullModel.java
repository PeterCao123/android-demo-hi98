package com.shiji.png.pat.sp.model;

import com.shiji.png.pat.sp.annotation.Precision;

/**
 * @author bruce.wu
 * @date 2018/8/2
 */
public class FullModel {

    private String str;

    private Boolean bool1;

    private boolean bool2;

    private Integer int1;

    private int int2;

    private Long long1;

    private long long2;

    @Precision
    private Double double1;

    @Precision
    private double double2;

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Boolean getBool1() {
        return bool1;
    }

    public void setBool1(Boolean bool1) {
        this.bool1 = bool1;
    }

    public boolean isBool2() {
        return bool2;
    }

    public void setBool2(boolean bool2) {
        this.bool2 = bool2;
    }

    public Integer getInt1() {
        return int1;
    }

    public void setInt1(Integer int1) {
        this.int1 = int1;
    }

    public int getInt2() {
        return int2;
    }

    public void setInt2(int int2) {
        this.int2 = int2;
    }

    public Long getLong1() {
        return long1;
    }

    public void setLong1(Long long1) {
        this.long1 = long1;
    }

    public long getLong2() {
        return long2;
    }

    public void setLong2(long long2) {
        this.long2 = long2;
    }

    public Double getDouble1() {
        return double1;
    }

    public void setDouble1(Double double1) {
        this.double1 = double1;
    }

    public double getDouble2() {
        return double2;
    }

    public void setDouble2(double double2) {
        this.double2 = double2;
    }
}
