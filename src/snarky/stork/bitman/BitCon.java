package snarky.stork.bitman;

import java.util.ArrayList;

/**
 * Do various conversions.
 */
public class BitCon {

    /**
     * int to bits (8 bits). Convert decimal integer to binary.
     * this is really for int returned by IO Stream, etc. 0 - 255.
     * 2's complement.
     *
     * @param i the number to convert.
     * @return
     */
    public static byte[] intToBits(int i) {
        if (i < 0 || i > 255) {
            throw new BitManException("0 to 255 expected");
        }
        return make8bits(decimalToBase2(i));
    }

    /**
     * Byte to bits (8 bits). Convert a decimal (byte) into binary.
     *
     * @param b the number to convert
     * @return
     */
    public static byte[] byteToBits(byte b) {
        final byte[] rBits = make8bits(decimalToBase2(b));

        if (b < 0) // negative - so need to invert
        {
            byte[] onvert = to2sComplement(rBits);
            if (onvert.length > 8) {
                throw new BitManException("Too many bits");
            }
            return onvert;
        } else {
            return rBits;
        }
    }

    private static byte[] make8bits(byte[] bits) {
        if (bits.length > 8) {
            throw new BitManException("Too many bits");
        }

        final byte[] rBits;
        //1  2  3  4  5  6  7  8
        rBits = new byte[]{0, 0, 0, 0, 0, 0, 0, 0};

        final int offset = rBits.length - bits.length;
        System.arraycopy(bits, 0, rBits, offset, bits.length);
        return rBits;
    }

    private static byte[] to2sComplement(byte[] b) {
        // flip
        for (int i = 0; i < b.length; i++) {
            if (b[i] == 0) {
                b[i] = 1;
            } else {
                b[i] = 0;
            }
        }
        // add one ...
        return addOne(b.length - 1, b);
    }

    private static byte[] addOne(int start, byte[] b) {
        final ArrayList<Byte> tempByteStore = new ArrayList<>();
        for (byte cb : b) {
            tempByteStore.add(cb);
        }

        byte currentVal, reminder = 1;
        for (int i = start; i >= 0; i--) {
            currentVal = tempByteStore.get(i);
            if (reminder != 0) // we have something to add.
            {
                if (currentVal != 0) {
                    tempByteStore.set(i, (byte) 0);
                    reminder = 1;

                    if (i == 0) {
                        tempByteStore.add(0, (byte) 0);
                        i++;
                    }
                } else {
                    tempByteStore.set(i, (byte) 1);
                    reminder = 0;
                }
            }
        }

        byte[] bytes = new byte[tempByteStore.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = tempByteStore.get(i);
        }
        return bytes;
    }

    // convert decimal number to binary ..

    /**
     * convert decimal number to binary ..
     *
     * @param num the number to convert
     * @return
     */
    public static byte[] decimalToBase2(long num) {
        if (num < 0) {
            num = -(num);
        }

        final ArrayList<Byte> al = new ArrayList<>();
        while (num > 0) {
            if (num % 2 == 0) {
                al.add(0, (byte) 0);
            } else {
                al.add(0, (byte) 1);
            }
            num = num / 2;
        }

        if (al.size() == 0) {
            al.add((byte) 0);
        }

        final byte[] bits = new byte[al.size()];
        int i = 0;

        for (Byte b : al) {
            bits[i++] = b;
        }
        return bits;
        //return new byte[]{0};
    }

    /**
     * Convert int to byte...
     *
     * @param i
     * @return
     */
    public static byte intToByte(int i) {
        if (i < 0 || i > 255)
        {
            throw new BitManException("0 to 255 expected");
        }
        /// ALTERNATIVE WAY ...
        /*
        final byte b;
        if (i > 127)
        {
            b = (byte) - (256 - i);
        }
        else
        {
            b = (byte) i;
        }
        return b;
         */
        return int2ByteMap[i];
    }

    /**
     * Convert byte to int...
     *
     * @param b
     * @return
     */
    public static int byteToInt(byte b) {
        int i;
        if (b >= 0) {
            i = b;
        } else {
            i = 256 + b;
        }
        return i;
    }

    /**
     * Convert base 2 number to decimal number.
     *
     * @param b
     * @return
     */
    public static long base2toDecimal(byte[] b) {
        try {
            long t = 0;

            int power = 0;
            for (int i = b.length - 1; i >= 0; i--) {
                t = t + (b[i] == 1 ? (power(2, power)) : 0);
                power++;
            }
            return t;
        } catch (BitManException e) {
            // this shouldn't happen, but what can you do.
            return 0;
        }
    }

    /**
     * Get exponent
     *
     * @param num
     * @param power
     * @return
     */
    public static long power(long num, long power) {
        long val = num;

        if (power < 0) {
            throw new BitManException("Negative exponent not supported.");
        } else if (power == 0) {
            val = 1;
        } else {
            int i = 0;
            while (i++ < power - 1) {
                val = val * num;
            }
        }
        return val;
    }

    /**
     * The values in i array as hex.
     *
     * @param i
     * @return
     */
    public static String[] getHexString(int[] i) {
        final String[] s = new String[i.length];

        for (int o = 0; o < i.length; o++) {
            s[o] = Integer.toHexString(i[o]);
        }
        return s;
    }

    /**
     * The values in b array as hex.
     *
     * @param b
     * @return
     */
    public static String[] getHexString(byte[] b) {
        final String[] s = new String[b.length];

        for (int i = 0; i < b.length; i++) {
            s[i] = Integer.toHexString(Byte.toUnsignedInt(b[i]));
        }
        return s;
    }

    /**
     * [1 | 0, ... x8] to int, 8 bits exactly.
     *
     * @param b the bits
     * @return values in int
     */
    public static int bitsToInt(byte[] b) {
        if (b.length != 8) {
            throw new BitManException("Wrong number of bit, 8 required");
        }
        return (int) base2toDecimal(b);
    }

    /**
     * [1 | 0, ... x8] to byte, 8 bits exactly.
     *
     * @param b the bits
     * @return values in byte
     */
    public static byte bitsToByte(byte[] b) {
        if (b.length != 8) {
            throw new BitManException("Wrong number of bit, 8 required");
        }

        if (b[0] == 0) {
            return (byte) base2toDecimal(b);
        } else {
            byte[] bss = to2sComplement(b);
            return (byte) -base2toDecimal(bss);
        }
    }

    private static final byte[] int2ByteMap = new byte[]{0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10,
            11, 12, 13, 14, 15, 16, 17, 18, 19, 20, 21, 22, 23, 24, 25, 26, 27, 28, 29, 30,
            31, 32, 33, 34, 35, 36, 37, 38, 39, 40, 41, 42, 43, 44, 45, 46, 47, 48, 49, 50,
            51, 52, 53, 54, 55, 56, 57, 58, 59, 60, 61, 62, 63, 64, 65, 66, 67, 68, 69, 70,
            71, 72, 73, 74, 75, 76, 77, 78, 79, 80, 81, 82, 83, 84, 85, 86, 87, 88, 89, 90,
            91, 92, 93, 94, 95, 96, 97, 98, 99, 100, 101, 102, 103, 104, 105, 106, 107, 108,
            109, 110, 111, 112, 113, 114, 115, 116, 117, 118, 119, 120, 121, 122, 123, 124,
            125, 126, 127, -128, -127, -126, -125, -124, -123, -122, -121, -120, -119, -118,
            -117, -116, -115, -114, -113, -112, -111, -110, -109, -108, -107, -106, -105,
            -104, -103, -102, -101, -100, -99, -98, -97, -96, -95, -94, -93, -92, -91,
            -90, -89, -88, -87, -86, -85, -84, -83, -82, -81, -80, -79, -78, -77, -76,
            -75, -74, -73, -72, -71, -70, -69, -68, -67, -66, -65, -64, -63, -62, -61,
            -60, -59, -58, -57, -56, -55, -54, -53, -52, -51, -50, -49, -48, -47, -46,
            -45, -44, -43, -42, -41, -40, -39, -38, -37, -36, -35, -34, -33, -32, -31,
            -30, -29, -28, -27, -26, -25, -24, -23, -22, -21, -20, -19, -18, -17, -16,
            -15, -14, -13, -12, -11, -10, -9, -8, -7, -6, -5, -4, -3, -2, -1};
}
