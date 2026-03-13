/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author DELL
 */
public class Sach {
    private String maSach;
    private String tenSach;
    private int namXuatBan;
    private double giaBan;
    private int soLuongTon;
    private String maTacGia;
    private String maTheLoai;
    private String maNXB;
    private String trangThai;

    public Sach() {}

    public Sach(String maSach, String tenSach, int namXuatBan, double giaBan, int soLuongTon, String maTacGia, String maTheLoai, String maNXB, String trangThai) {
        this.maSach = maSach;
        this.tenSach = tenSach;
        this.namXuatBan = namXuatBan;
        this.giaBan = giaBan;
        this.soLuongTon = soLuongTon;
        this.maTacGia = maTacGia;
        this.maTheLoai = maTheLoai;
        this.maNXB = maNXB;
        this.trangThai = trangThai;
    }

    public String getMaSach() {
        return maSach;
    }

    public void setMaSach(String maSach) {
        this.maSach = maSach;
    }

    public String getTenSach() {
        return tenSach;
    }

    public void setTenSach(String tenSach) {
        this.tenSach = tenSach;
    }

    public int getNamXuatBan() {
        return namXuatBan;
    }

    public void setNamXuatBan(int namXuatBan) {
        this.namXuatBan = namXuatBan;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public int getSoLuongTon() {
        return soLuongTon;
    }

    public void setSoLuongTon(int soLuongTon) {
        this.soLuongTon = soLuongTon;
    }

    public String getMaTacGia() {
        return maTacGia;
    }

    public void setMaTacGia(String maTacGia) {
        this.maTacGia = maTacGia;
    }

    public String getMaTheLoai() {
        return maTheLoai;
    }

    public void setMaTheLoai(String maTheLoai) {
        this.maTheLoai = maTheLoai;
    }

    public String getMaNXB() {
        return maNXB;
    }

    public void setMaNXB(String maNXB) {
        this.maNXB = maNXB;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "Sach{" +
                "maSach=" + maSach +
                ", tenSach=" + tenSach +
                ", namXuatBan=" + namXuatBan +
                ", giaBan=" + giaBan +
                ", soLuongTon=" + soLuongTon +
                ", maTacGia=" + maTacGia +
                ", maTheLoai=" + maTheLoai +
                ", maNXB=" + maNXB +
                ", trangThai=" + trangThai +
                '}';
    }
}