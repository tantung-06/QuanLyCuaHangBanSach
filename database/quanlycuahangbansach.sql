CREATE DATABASE IF NOT EXISTS quanlycuahangbansach;

USE quanlycuahangbansach;

-- Nhóm quyền
CREATE TABLE NhomQuyen (
    maNhomQuyen VARCHAR(50) NOT NULL,
    tenNhomQuyen VARCHAR(255) NOT NULL,
    CONSTRAINT pk_NhomQuyen PRIMARY KEY (maNhomQuyen),
    CONSTRAINT uq_NhomQuyen_ma UNIQUE (maNhomQuyen)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO NhomQuyen VALUES
    ('NQ001', 'Quản trị viên'),
    ('NQ002', 'Nhân viên bán hàng'),
    ('NQ003', 'Nhân viên kho');

-- Tài khoản
CREATE TABLE TaiKhoan (
    maTaiKhoan VARCHAR(50) NOT NULL,
    tenDangNhap VARCHAR(255) NOT NULL,
    matKhau VARCHAR(255) NOT NULL,
    maNhomQuyen VARCHAR(50) NOT NULL,
    trangThai ENUM('HOAT_DONG','NGUNG') NOT NULL,
    CONSTRAINT pk_TaiKhoan PRIMARY KEY (maTaiKhoan),
    CONSTRAINT uq_TaiKhoan_ma UNIQUE (maTaiKhoan),
    CONSTRAINT fk_TaiKhoan_NhomQuyen
        FOREIGN KEY (maNhomQuyen) REFERENCES NhomQuyen(maNhomQuyen)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO TaiKhoan VALUES
    ('TK001', 'admin', 123456, 'NQ001', 'HOAT_DONG'),
    ('TK002', 'tantung', 123456, 'NQ002', 'HOAT_DONG'),
    ('TK003', 'ngoctuan', 123456, 'NQ003', 'HOAT_DONG'),
    ('TK004', 'yennhi', 123456, 'NQ002', 'HOAT_DONG'),
    ('TK005', 'ductuan', 123456, 'NQ002', 'HOAT_DONG'),
    ('TK006', 'quangvu', 123456, 'NQ002', 'HOAT_DONG');

-- Nhân viên
CREATE TABLE NhanVien (
    maNhanVien VARCHAR(50) NOT NULL,
    ho VARCHAR(100) NOT NULL,
    ten VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL,
    diaChi VARCHAR(500) NOT NULL,
    email VARCHAR(255) NOT NULL,
    maTaiKhoan VARCHAR(50) NOT NULL,
    trangThai ENUM('HOAT_DONG','NGUNG') NOT NULL DEFAULT 'HOAT_DONG',
    CONSTRAINT pk_NhanVien PRIMARY KEY (maNhanVien),
    CONSTRAINT uq_NhanVien_ma UNIQUE (maNhanVien),
    CONSTRAINT uq_NhanVien_TK UNIQUE (maTaiKhoan),
    CONSTRAINT fk_NhanVien_TaiKhoan
        FOREIGN KEY (maTaiKhoan) REFERENCES TaiKhoan(maTaiKhoan)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO NhanVien VALUES
    ('NV001', 'Tiêu Tấn', 'Tùng', '0901234567', '123 Lê Lợi, Q1, TP.HCM', 'tantung@gmail.com', 'TK001', 'HOAT_DONG'),
    ('NV002', 'Trần', 'Thị Bình', '0912345678', '456 Nguyễn Huệ, Q1, TP.HCM', 'binh.tran@sach.vn', 'TK002', 'HOAT_DONG'),
    ('NV003', 'Lê', 'Văn Cường', '0923456789', '789 Hai Bà Trưng, Q3, TP.HCM', 'cuong.le@sach.vn', 'TK003', 'HOAT_DONG'),
    ('NV004', 'Phan', 'Thị Diệu', '0934111222', '15 Pasteur, Q1, TP.HCM', 'dieu.phan@sach.vn', 'TK004', 'HOAT_DONG'),
    ('NV005', 'Võ', 'Minh Đức', '0945222333', '88 Nam Kỳ Khởi Nghĩa, Q3, TP.HCM', 'duc.vo@sach.vn', 'TK005', 'NGUNG');

-- Khách hàng
CREATE TABLE KhachHang (
    maKhachHang VARCHAR(50) NOT NULL,
    ho VARCHAR(100) NOT NULL,
    ten VARCHAR(100) NOT NULL,
    soDienThoai VARCHAR(20) NOT NULL,
    diaChi VARCHAR(500) NOT NULL,
    email VARCHAR(255) NOT NULL,
    trangThai ENUM('HOAT_DONG','NGUNG') NOT NULL DEFAULT 'HOAT_DONG',
    CONSTRAINT pk_KhachHang PRIMARY KEY (maKhachHang),
    CONSTRAINT uq_KhachHang_ma UNIQUE (maKhachHang)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO KhachHang VALUES
    ('KH001', 'Phạm',   'Thị Dung',    '0934567890', '10 Đinh Tiên Hoàng, Q1, TP.HCM',          'dung.pham@gmail.com',    'HOAT_DONG'),
    ('KH002', 'Hoàng',  'Văn Em',      '0945678901', '20 Võ Văn Tần, Q3, TP.HCM',               'em.hoang@gmail.com',     'HOAT_DONG'),
    ('KH003', 'Đặng',   'Thị Phương',  '0956789012', '30 Cách Mạng Tháng 8, Q10, TP.HCM',       'phuong.dang@gmail.com',  'HOAT_DONG'),
    ('KH004', 'Bùi',    'Quốc Hùng',   '0967890123', '45 Trần Hưng Đạo, Q5, TP.HCM',            'hung.bui@gmail.com',     'HOAT_DONG'),
    ('KH005', 'Ngô',    'Thị Kim',     '0978901234', '60 Lý Tự Trọng, Q1, TP.HCM',              'kim.ngo@gmail.com',      'HOAT_DONG'),
    ('KH006', 'Đinh',   'Văn Long',    '0989012345', '75 Nguyễn Thị Minh Khai, Q3, TP.HCM',     'long.dinh@gmail.com',    'HOAT_DONG'),
    ('KH007', 'Trương',  'Thị Mai',    '0990123456', '90 Điện Biên Phủ, Q10, TP.HCM',            'mai.truong@gmail.com',   'NGUNG');

-- Tác giả
CREATE TABLE TacGia (
    maTacGia  VARCHAR(50)  NOT NULL,
    tenTacGia VARCHAR(255) NOT NULL,
    CONSTRAINT pk_TacGia           PRIMARY KEY (maTacGia),
    CONSTRAINT uq_TacGia_ma        UNIQUE      (maTacGia)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO TacGia VALUES
    ('TG001', 'Nguyễn Nhật Ánh'),
    ('TG002', 'Nam Quốc Chấn'),
    ('TG003', 'Tô Hoài'),
    ('TG004', 'J.K. Rowling'),
    ('TG005', 'Dale Carnegie'),
    ('TG006', 'Paulo Coelho'),
    ('TG007', 'Yuval Noah Harari'),
    ('TG008', 'Nguyễn Du');

-- Thể loại
CREATE TABLE TheLoai (
    maTheLoai  VARCHAR(50)  NOT NULL,
    tenTheLoai VARCHAR(255) NOT NULL,
    CONSTRAINT pk_TheLoai          PRIMARY KEY (maTheLoai),
    CONSTRAINT uq_TheLoai_ma       UNIQUE      (maTheLoai)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO TheLoai VALUES
    ('TL001', 'Văn học thiếu nhi'),
    ('TL002', 'Kỹ năng sống'),
    ('TL003', 'Tiểu thuyết'),
    ('TL004', 'Khoa học viễn tưởng'),
    ('TL005', 'Lịch sử'),
    ('TL006', 'Triết học'),
    ('TL007', 'Kinh tế'),
    ('TL008', 'Thơ ca - Văn học cổ điển');

-- Nhà xuất bản
CREATE TABLE NhaXuatBan (
    maNXB       VARCHAR(50)  NOT NULL,
    tenNXB      VARCHAR(255) NOT NULL,
    diaChi      VARCHAR(500) NOT NULL,
    soDienThoai VARCHAR(20)  NOT NULL,
    email       VARCHAR(255) NOT NULL,
    CONSTRAINT pk_NhaXuatBan       PRIMARY KEY (maNXB),
    CONSTRAINT uq_NhaXuatBan_ma    UNIQUE      (maNXB)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO NhaXuatBan VALUES
    ('NXB001', 'NXB Trẻ',           '161B Lý Chính Thắng, Q3, TP.HCM',  '02839316289', 'nxbtre@nxbtre.com.vn'),
    ('NXB002', 'NXB Kim Đồng',      '55 Quang Trung, Hà Nội',            '02439434730', 'kinhdong@nxbkimdong.vn'),
    ('NXB003', 'NXB Tổng Hợp HCM',  '62 Nguyễn Thị Minh Khai, Q3',      '02839225340', 'tonghop@nxbhcm.vn'),
    ('NXB004', 'NXB Hội Nhà Văn',   '65 Nguyễn Du, Hà Nội',              '02439433406', 'nxbhnv@hn.vnn.vn'),
    ('NXB005', 'NXB Dân Trí',       '9 Đinh Lễ, Hà Nội',                 '02439362878', 'nxbdantri@dantri.com.vn');

-- Nhà cung cấp
CREATE TABLE NhaCungCap (
    maNCC       VARCHAR(50)  NOT NULL,
    tenNCC      VARCHAR(255) NOT NULL,
    soDienThoai VARCHAR(20)  NOT NULL,
    diaChi      VARCHAR(500) NOT NULL,
    email       VARCHAR(255) NOT NULL,
    trangThai   ENUM('HOAT_DONG','NGUNG') NOT NULL DEFAULT 'HOAT_DONG',
    CONSTRAINT pk_NhaCungCap       PRIMARY KEY (maNCC),
    CONSTRAINT uq_NhaCungCap_ma    UNIQUE      (maNCC)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO NhaCungCap VALUES
    ('NCC001', 'Công ty TNHH Phát Hành Sách HCM',  '0281234567', '86 Lê Thánh Tôn, Q1, TP.HCM',      'phs@phshcm.com',    'HOAT_DONG'),
    ('NCC002', 'Công ty CP Đầu Tư & Phát Triển',   '0282345678', '234 Điện Biên Phủ, Q3, TP.HCM',    'info@cpdt.com.vn',  'HOAT_DONG'),
    ('NCC003', 'Công ty TNHH Sách Miền Nam',        '0283456789', '12 Trần Phú, Q5, TP.HCM',          'sachMN@smn.vn',     'HOAT_DONG'),
    ('NCC004', 'Công ty CP Văn Hóa Phương Nam',     '0284567890', '940 Đường 3/2, Q11, TP.HCM',       'phuongnam@pn.com',  'HOAT_DONG');

-- Sách
CREATE TABLE Sach (
    maSach     VARCHAR(50)  NOT NULL,
    tenSach    VARCHAR(500) NOT NULL,
    namXuatBan INT          NOT NULL,
    giaBan     DECIMAL(12,2) NOT NULL,
    soLuongTon INT           NOT NULL DEFAULT 0,
    maTacGia   VARCHAR(50)  NOT NULL,
    maTheLoai  VARCHAR(50)  NOT NULL,
    maNXB      VARCHAR(50)  NOT NULL,
    trangThai  ENUM('CON_HANG','HET_HANG') NOT NULL DEFAULT 'CON_HANG',
    CONSTRAINT pk_Sach             PRIMARY KEY (maSach),
    CONSTRAINT uq_Sach_ma          UNIQUE      (maSach),
    CONSTRAINT chk_Sach_giaBan     CHECK       (giaBan >= 0),
    CONSTRAINT chk_Sach_soLuongTon CHECK       (soLuongTon >= 0),
    CONSTRAINT fk_Sach_TacGia
        FOREIGN KEY (maTacGia)  REFERENCES TacGia(maTacGia)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_Sach_TheLoai
        FOREIGN KEY (maTheLoai) REFERENCES TheLoai(maTheLoai)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_Sach_NhaXuatBan
        FOREIGN KEY (maNXB)     REFERENCES NhaXuatBan(maNXB)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO Sach VALUES
    ('S001', 'Cho tôi xin một vé đi tuổi thơ',  2009, 85000.00,  50, 'TG001', 'TL001', 'NXB001', 'CON_HANG'),
    ('S002', 'Mắt biếc',                         2016, 95000.00,  30, 'TG001', 'TL003', 'NXB001', 'CON_HANG'),
    ('S003', 'Đắc nhân tâm',                     2018, 120000.00, 80, 'TG005', 'TL002', 'NXB003', 'CON_HANG'),
    ('S004', 'Dế Mèn phiêu lưu ký',              2020, 65000.00,  20, 'TG003', 'TL001', 'NXB002', 'CON_HANG'),
    ('S005', 'Harry Potter và Hòn đá phù thủy',  2015, 150000.00, 15, 'TG004', 'TL004', 'NXB002', 'CON_HANG'),
    ('S006', 'Nhà giả kim',                       2019, 110000.00, 40, 'TG006', 'TL003', 'NXB003', 'CON_HANG'),
    ('S007', 'Sapiens: Lược sử loài người',       2020, 185000.00, 25, 'TG007', 'TL005', 'NXB005', 'CON_HANG'),
    ('S008', 'Tôi tài giỏi, bạn cũng thế',       2017, 130000.00, 60, 'TG002', 'TL002', 'NXB003', 'CON_HANG'),
    ('S009', 'Truyện Kiều',                       2021,  75000.00, 35, 'TG008', 'TL008', 'NXB004', 'CON_HANG'),
    ('S010', 'Doraemon - Tập 1',                  2022,  35000.00,  0, 'TG002', 'TL001', 'NXB002', 'HET_HANG'),
    ('S011', 'Nghĩ giàu làm giàu',                2016, 145000.00, 10, 'TG005', 'TL007', 'NXB005', 'CON_HANG'),
    ('S012', 'Conan - Tập 1',                     2023,  38000.00,  0, 'TG002', 'TL001', 'NXB002', 'HET_HANG');

-- Khuyến mãi
CREATE TABLE KhuyenMai (
    maKhuyenMai     VARCHAR(50)  NOT NULL,
    tenKhuyenMai    VARCHAR(255) NOT NULL,
    donHangToiThieu DECIMAL(15,2) NOT NULL,
    phanTramGiam    DECIMAL(5,2)  NOT NULL,
    ngayBatDau      DATE         NOT NULL,
    ngayKetThuc     DATE         NOT NULL,
    trangThai       ENUM('AP_DUNG','HET_HAN') NOT NULL,
    CONSTRAINT pk_KhuyenMai        PRIMARY KEY (maKhuyenMai),
    CONSTRAINT uq_KhuyenMai_ma     UNIQUE      (maKhuyenMai),
    CONSTRAINT chk_KhuyenMai_phanTram CHECK (phanTramGiam BETWEEN 0 AND 100),
    CONSTRAINT chk_KhuyenMai_ngay    CHECK (ngayKetThuc >= ngayBatDau)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO KhuyenMai VALUES
    ('KM001', 'Giảm giá mùa hè',         200000.00, 10.00, '2025-06-01', '2025-08-31', 'HET_HAN'),
    ('KM002', 'Khuyến mãi tháng 3',      150000.00, 15.00, '2026-03-01', '2026-03-31', 'AP_DUNG'),
    ('KM003', 'Sinh nhật cửa hàng',      300000.00, 20.00, '2026-04-01', '2026-04-15', 'AP_DUNG'),
    ('KM004', 'Tri ân khách hàng thân',  100000.00,  5.00, '2025-12-01', '2025-12-31', 'HET_HAN');

-- Phiếu nhập
CREATE TABLE PhieuNhap (
    maPhieuNhap VARCHAR(50)  NOT NULL,
    ngayNhap    DATE         NOT NULL,
    maNhanVien  VARCHAR(50)  NOT NULL,
    maNCC       VARCHAR(50)  NOT NULL,
    tongTien    DECIMAL(15,2) NOT NULL DEFAULT 0,
    trangThai   ENUM('DA_NHAP','HUY') NOT NULL DEFAULT 'DA_NHAP',
    CONSTRAINT pk_PhieuNhap        PRIMARY KEY (maPhieuNhap),
    CONSTRAINT uq_PhieuNhap_ma     UNIQUE      (maPhieuNhap),
    CONSTRAINT chk_PhieuNhap_tong  CHECK       (tongTien >= 0),
    CONSTRAINT fk_PhieuNhap_NhanVien
        FOREIGN KEY (maNhanVien) REFERENCES NhanVien(maNhanVien)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_PhieuNhap_NhaCungCap
        FOREIGN KEY (maNCC)      REFERENCES NhaCungCap(maNCC)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO PhieuNhap VALUES
    ('PN001', '2026-01-10', 'NV003', 'NCC001',  9750000.00, 'DA_NHAP'),
    ('PN002', '2026-01-20', 'NV003', 'NCC002',  4800000.00, 'DA_NHAP'),
    ('PN003', '2026-02-05', 'NV003', 'NCC003',  7200000.00, 'DA_NHAP'),
    ('PN004', '2026-02-18', 'NV003', 'NCC001',  3500000.00, 'DA_NHAP'),
    ('PN005', '2026-03-01', 'NV003', 'NCC004',  6100000.00, 'DA_NHAP'),
    ('PN006', '2026-03-06', 'NV003', 'NCC002',  2800000.00, 'DA_NHAP');

-- Chi tiết phiếu nhập
CREATE TABLE ChiTietPhieuNhap (
    maPhieuNhap VARCHAR(50) NOT NULL,
    maSach      VARCHAR(50) NOT NULL,
    soLuong     INT         NOT NULL,
    giaNhap     DECIMAL(12,2) NOT NULL,
    thanhTien   DECIMAL(15,2) NOT NULL COMMENT 'thanhTien = giaNhap * soLuong',
    CONSTRAINT pk_ChiTietPhieuNhap  PRIMARY KEY (maPhieuNhap, maSach),
    CONSTRAINT chk_CTPN_soLuong     CHECK (soLuong > 0),
    CONSTRAINT chk_CTPN_giaNhap     CHECK (giaNhap >= 0),
    CONSTRAINT fk_CTPN_PhieuNhap
        FOREIGN KEY (maPhieuNhap) REFERENCES PhieuNhap(maPhieuNhap)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_CTPN_Sach
        FOREIGN KEY (maSach)      REFERENCES Sach(maSach)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ChiTietPhieuNhap VALUES
    ('PN001', 'S001', 50,  70000.00, 3500000.00),
    ('PN001', 'S002', 30,  78000.00, 2340000.00),
    ('PN001', 'S003', 40,  98000.00, 3910000.00),
    ('PN002', 'S004', 30,  52000.00, 1560000.00),
    ('PN002', 'S005', 20, 126000.00, 2520000.00),
    ('PN002', 'S010', 30,  28000.00,  840000.00),
    ('PN003', 'S006', 40,  90000.00, 3600000.00),
    ('PN003', 'S007', 20, 160000.00, 3200000.00),
    ('PN003', 'S008', 40, 110000.00, 4400000.00),
    ('PN004', 'S009', 35,  60000.00, 2100000.00),
    ('PN004', 'S011', 10, 120000.00, 1200000.00),
    ('PN005', 'S003', 30,  98000.00, 2940000.00),
    ('PN005', 'S008', 30, 110000.00, 3300000.00),
    ('PN006', 'S001', 20,  70000.00, 1400000.00),
    ('PN006', 'S002', 15,  78000.00, 1170000.00);

-- Phiếu xuất
CREATE TABLE PhieuXuat (
    maPhieuXuat VARCHAR(50)  NOT NULL,
    ngayLap     DATE         NOT NULL,
    maNhanVien  VARCHAR(50)  NOT NULL,
    maKhachHang VARCHAR(50)  NOT NULL,
    maKhuyenMai VARCHAR(50)  NULL,
    tongTien    DECIMAL(15,2) NOT NULL DEFAULT 0,
    trangThai   ENUM('DA_XUAT','HUY') NOT NULL DEFAULT 'DA_XUAT',
    CONSTRAINT pk_PhieuXuat        PRIMARY KEY (maPhieuXuat),
    CONSTRAINT uq_PhieuXuat_ma     UNIQUE      (maPhieuXuat),
    CONSTRAINT chk_PhieuXuat_tong  CHECK       (tongTien >= 0),
    CONSTRAINT fk_PhieuXuat_NhanVien
        FOREIGN KEY (maNhanVien)  REFERENCES NhanVien(maNhanVien)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_PhieuXuat_KhachHang
        FOREIGN KEY (maKhachHang) REFERENCES KhachHang(maKhachHang)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_PhieuXuat_KhuyenMai
        FOREIGN KEY (maKhuyenMai) REFERENCES KhuyenMai(maKhuyenMai)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO PhieuXuat VALUES
    ('PX001', '2026-01-15', 'NV002', 'KH001', NULL,    255000.00, 'DA_XUAT'),
    ('PX002', '2026-01-22', 'NV002', 'KH002', 'KM001', 323750.00, 'DA_XUAT'),
    ('PX003', '2026-02-10', 'NV004', 'KH003', NULL,    490000.00, 'DA_XUAT'),
    ('PX004', '2026-02-14', 'NV002', 'KH004', 'KM002', 382500.00, 'DA_XUAT'),
    ('PX005', '2026-02-20', 'NV004', 'KH005', NULL,    215000.00, 'DA_XUAT'),
    ('PX006', '2026-03-01', 'NV002', 'KH006', 'KM002', 348875.00, 'DA_XUAT'),
    ('PX007', '2026-03-05', 'NV004', 'KH001', NULL,    335000.00, 'DA_XUAT'),
    ('PX008', '2026-03-07', 'NV002', 'KH003', NULL,    150000.00, 'DA_XUAT'),
    ('PX009', '2026-03-08', 'NV004', 'KH002', NULL,    185000.00, 'HUY');

-- Chi tiết phiếu xuất
CREATE TABLE ChiTietPhieuXuat (
    maPhieuXuat VARCHAR(50) NOT NULL,
    maSach      VARCHAR(50) NOT NULL,
    soLuong     INT         NOT NULL,
    donGia      DECIMAL(12,2) NOT NULL,
    thanhTien   DECIMAL(15,2) NOT NULL COMMENT 'thanhTien = donGia * soLuong',
    CONSTRAINT pk_ChiTietPhieuXuat  PRIMARY KEY (maPhieuXuat, maSach),
    CONSTRAINT chk_CTPX_soLuong     CHECK (soLuong > 0),
    CONSTRAINT chk_CTPX_donGia      CHECK (donGia >= 0),
    CONSTRAINT fk_CTPX_PhieuXuat
        FOREIGN KEY (maPhieuXuat) REFERENCES PhieuXuat(maPhieuXuat)
        ON DELETE NO ACTION ON UPDATE NO ACTION,
    CONSTRAINT fk_CTPX_Sach
        FOREIGN KEY (maSach)      REFERENCES Sach(maSach)
        ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

INSERT INTO ChiTietPhieuXuat VALUES
    ('PX001', 'S001', 2,  85000.00, 170000.00),
    ('PX001', 'S004', 1,  65000.00,  65000.00),
    ('PX001', 'S009', 1,  75000.00,  75000.00),
    ('PX002', 'S002', 2,  95000.00, 190000.00),
    ('PX002', 'S005', 1, 150000.00, 150000.00),
    ('PX003', 'S003', 1, 120000.00, 120000.00),
    ('PX003', 'S006', 1, 110000.00, 110000.00),
    ('PX003', 'S007', 1, 185000.00, 185000.00),
    ('PX003', 'S009', 1,  75000.00,  75000.00),
    ('PX004', 'S003', 2, 120000.00, 240000.00),
    ('PX004', 'S008', 1, 130000.00, 130000.00),
    ('PX005', 'S001', 1,  85000.00,  85000.00),
    ('PX005', 'S004', 2,  65000.00, 130000.00),
    ('PX006', 'S007', 1, 185000.00, 185000.00),
    ('PX006', 'S008', 1, 130000.00, 130000.00),
    ('PX006', 'S011', 1, 145000.00, 145000.00),
    ('PX007', 'S002', 2,  95000.00, 190000.00),
    ('PX007', 'S009', 2,  75000.00, 150000.00),
    ('PX008', 'S005', 1, 150000.00, 150000.00),
    ('PX009', 'S006', 1, 110000.00, 110000.00),
    ('PX009', 'S007', 1, 185000.00, 185000.00);