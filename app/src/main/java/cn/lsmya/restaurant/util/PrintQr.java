package cn.lsmya.restaurant.util;

import java.io.ByteArrayOutputStream;

public class PrintQr {
    private static byte GS = 0x1d;

    /**
     * 打印单个二维码 sunmi自定义指令
     *
     * @param code:       二维码数据
     * @param modulesize: 二维码块大小(单位:点, 取值 1 至 16 )
     * @param errorlevel: 二维码纠错等级(0 至 3)
     *                    0 -- 纠错级别L ( 7%)
     *                    1 -- 纠错级别M (15%)
     *                    2 -- 纠错级别Q (25%)
     *                    3 -- 纠错级别H (30%)
     */
    public static byte[] getPrintQRCode(String code, int modulesize, int errorlevel) {
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            buffer.write(setQRCodeSize(modulesize));
            buffer.write(setQRCodeErrorLevel(errorlevel));
            buffer.write(getQCodeBytes(code));
            buffer.write(getBytesForPrintQRCode());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    private static byte[] setQRCodeSize(int modulesize) {
        //二维码块大小设置指令
        byte[] dtmp = new byte[8];
        dtmp[0] = GS;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x43;
        dtmp[7] = (byte) modulesize;
        return dtmp;
    }


    private static byte[] setQRCodeErrorLevel(int errorlevel) {
        //二维码纠错等级设置指令
        byte[] dtmp = new byte[8];
        dtmp[0] = GS;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x45;
        dtmp[7] = (byte) (48 + errorlevel);
        return dtmp;
    }

    private static byte[] getQCodeBytes(String code) {
        //二维码存入指令
        ByteArrayOutputStream buffer = new ByteArrayOutputStream();
        try {
            byte[] d = code.getBytes("GB18030");
            int len = d.length + 3;
            if (len > 7092) len = 7092;
            buffer.write((byte) 0x1D);
            buffer.write((byte) 0x28);
            buffer.write((byte) 0x6B);
            buffer.write((byte) len);
            buffer.write((byte) (len >> 8));
            buffer.write((byte) 0x31);
            buffer.write((byte) 0x50);
            buffer.write((byte) 0x30);
            for (int i = 0; i < d.length && i < len; i++) {
                buffer.write(d[i]);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return buffer.toByteArray();
    }

    private static byte[] getBytesForPrintQRCode() {
        //打印已存入数据的二维码
        byte[] dtmp;
        dtmp = new byte[9];
        dtmp[0] = 0x1D;
        dtmp[1] = 0x28;
        dtmp[2] = 0x6B;
        dtmp[3] = 0x03;
        dtmp[4] = 0x00;
        dtmp[5] = 0x31;
        dtmp[6] = 0x51;
        dtmp[7] = 0x30;
        return dtmp;
    }
}
