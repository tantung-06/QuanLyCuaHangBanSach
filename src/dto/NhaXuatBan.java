/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author DELL
 */
public class NhaXuatBan {
    private String maNXB;
    private String tenNXB;
    private String soDienThoai;
    private String diaChi;
    private String email;

    public NhaXuatBan() {}

    public NhaXuatBan(String maNXB, String tenNXB, String soDienThoai, String diaChi, String email) {
        this.maNXB = maNXB;
        this.tenNXB = tenNXB;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
    }

    public String getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public String getTenNXB() {
        return tenNXB;
    }

    public void setTenNXB(String tenNXB) {
        this.tenNXB = tenNXB;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public String toString() {
        return "NhaXuatBan{" +
                "maNXB=" + maNXB +
                ", tenNXB=" + tenNXB +
                ", soDienThoai=" + soDienThoai +
                ", diaChi=" + diaChi +
                ", email=" + email +
                '}';
    }
}