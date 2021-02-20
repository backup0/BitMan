package snarky.stork.bitman;

import java.util.Arrays;

/**
 * Operations on bits. Bit operations.
 */
// todo: not tested, TEST
public class BitOps {

    public static boolean eql(byte[] a, byte[] b) {
        return Arrays.equals(a, b);
    }
    
    public static boolean eql(String a, String b) {
        return a.equalsIgnoreCase(b);
    }

    public static byte[] and(String a, String b) {
        byte[][] eb = String0(a, b);
        return and(eb[0], eb[1]);
    }

    public static byte[] or(String a, String b) {
        byte[][] eb = String0(a, b);
        return or(eb[0], eb[1]);
    }

    public static byte[] xor(String a, String b) {
        byte[][] eb = String0(a, b);
        return xor(eb[0], eb[1]);
    }
    
    public static byte[] and(byte[] a, byte[] b) {

        byte[][] bbs = Byte0(a, b);

        byte[] aa = bbs[0];
        byte[] bb = bbs[1];

        byte[] c = new byte[aa.length];

        for (int i = 0; i < aa.length; i++) {
            if (aa[i] == 1 && bb[i] == 1) {
                c[i] = 1;
            }
            else {
                c[i] = 0;
            }
        }
        return c;
    }

    public static byte[] or(byte[] a, byte[] b) {
        byte[][] bbs = Byte0(a, b);

        byte[] aa = bbs[0];
        byte[] bb = bbs[1];

        byte[] c = new byte[aa.length];

        for (int i = 0; i < aa.length; i++) {
            if (aa[i] == 0 && bb[i] == 0) {
                c[i] = 0;
            }
            else {
                c[i] = 1;
            }
        }
        return c;
    }

    public static byte[] xor(byte[] a, byte[] b) {
        byte[][] bbs = Byte0(a, b);

        byte[] aa = bbs[0];
        byte[] bb = bbs[1];

        byte[] c = new byte[aa.length];

        for (int i = 0; i < aa.length; i++) {
            if (aa[i] == 1 && bb[i] == 1) {
                c[i] = 0;
            }
            else if (aa[i] == 0 && bb[i] == 0) {
                c[i] = 0;
            }
            else {
                c[i] = 1;
            }
        }
        return c;
    }

    private static byte[][] Byte0(byte[] a, byte[] b) {

        final byte[] ba;
        final byte[] bb;

        if (a.length < b.length) {
            ba = fillUp(a, b.length);
            bb = b;
        }
        else if (a.length > b.length) {
            ba = a;
            bb = fillUp(b, a.length);
        }
        else {
            ba = a;
            bb = b;
        }
        return new byte[][]{ba, bb};
    }
    
    private static byte[][] String0(String a, String b) {

        final byte[] ba;
        final byte[] bb;

        if (a.length() < b.length()) {
            ba = fillUp(a, b.length());
            bb = StringBit.toBits(b);
        }
        else if (a.length() > b.length()) {
            ba = StringBit.toBits(a);
            bb = fillUp(b, a.length());
        }
        else {
            ba = StringBit.toBits(a);
            bb = StringBit.toBits(b);
        }
        return new byte[][]{ba, bb};
    }

    private static byte[] fillUp(byte[] a, int len) {
        byte[] nb = new byte[len];
        Arrays.fill(nb, (byte) 0);
        System.arraycopy(a, 0, nb, (len-a.length), a.length);
        return nb;
    }
    
    private static byte[] fillUp(String bits, int len) {
        StringBuilder sbs = new StringBuilder(bits);
        while (sbs.length() < len) {
            sbs.insert(0, "0");
        }
        return StringBit.toBits(sbs.toString());
    }
}
