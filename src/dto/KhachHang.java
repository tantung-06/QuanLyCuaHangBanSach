/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dto;

/**
 *
 * @author DELL
 */
public class KhachHang {
    private String maKhachHang;
    private String ho;
    private String ten;
    private String soDienThoai;
    private String diaChi;
    private String email;
    private String trangThai;

    public KhachHang() {}

    public KhachHang(String maKhachHang, String ho, String ten, String soDienThoai, String diaChi, String email, String trangThai) {
        this.maKhachHang = maKhachHang;
        this.ho = ho;
        this.ten = ten;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
        this.email = email;
        this.trangThai = trangThai;
    }

    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHo() {
        return ho;
    }

    public void setHo(String ho) {
        this.ho = ho;
    }

    public String getTen() {
        return ten;
    }

    public void setTen(String ten) {
        this.ten = ten;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(String trangThai) {
        this.trangThai = trangThai;
    }

    @Override
    public String toString() {
        return "KhachHang{" +
                "maKhachHang=" + maKhachHang +
                ", ho=" + ho +
                ", ten=" + ten +
                ", soDienThoai=" + soDienThoai +
                ", diaChi=" + diaChi +
                ", email=" + email +
                ", trangThai=" + trangThai +
                '}';
    }
}