package snarky.stork.bitman;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;

// todo: may not need this.
public class BitConv {
    /*
    byte to int
    int to byte
    ----------------------------
    binary to ... {?? format?}

     */

    public static void stringer(String... s) {
    }

    // ? fill, spaces
    private static void stringShit(String s) {
        StringBuilder sbs = new StringBuilder();
        if ("?".equalsIgnoreCase(s)) {
            s = "?0";
        }

        for (char c : s.toCharArray()) {
            switch (c) {
                case '0':
                case '1':
                    sbs.append(c);
                    break;
                case '-':
                    // ok - spacer
                    break;
                case '?':
                    // wild card?
                    break;
                default:
                    // throw exception here
                    break;
            }
        }

        if (s.startsWith("?") && s.endsWith("?")) {
            // throw
            sbs.append("Shit .. ? * 2");
        }

        if (s.startsWith("?")) {
            while (sbs.length() < 8) {
                sbs.insert(0, '0');
            }
        }

        if (s.endsWith("?")) {
            while (sbs.length() < 8) {
                sbs.append('0');
            }
        }

        if (sbs.length() != 8) {
            // exception
            sbs.append("Shit .. > 8");
        }
        System.out.println(sbs.toString());
    }

    // int -> 1|0, ...
    public static byte[] intToBits(int i) throws Exception {
        return make8bits(decToBin(i));
    }

    //turn byte b into bits (0, 1);
    public static byte[] byteToBits(byte b) throws Exception {
        final byte[] rBits = make8bits(decToBin(b));

        if (b < 0) // negative - so need to invert
        {
            byte[] onvert = to2sComplement(rBits);
            if (onvert.length > 8) {
                throw new Exception("Too long..");
            }
            return onvert;
        } else {
            return rBits;
        }
    }

    private static byte[] make8bits(byte[] bits) {
        if (bits.length > 8) {
            // throw ..
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
    public static byte[] decToBin(long num) {
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

    public static String stupidTone(byte[] b) {
        String[] s = new String[]{"000", "001", "010", "011", "100", "101", "110", "111"};
        StringBuilder sbs = new StringBuilder();
        String s1 = b[0] + "" + b[1] + "" + b[2];
        for (int i = 0; i < s.length; i++) {
            if (s1.equalsIgnoreCase(s[i])) {
                //System.out.println("1@ " + i);
                sbs.append(i);
            }
        }

        String s2 = b[3] + "" + b[4] + "" + b[5];
        for (int i = 0; i < s.length; i++) {
            if (s2.equalsIgnoreCase(s[i])) {
                //System.out.println("2@ " + i);
                sbs.append(i);
            }
        }

        String s3 = b[6] + "" + b[7];
        String[] pls = new String[]{"00", "01", "10", "11"};
        for (int i = 0; i < pls.length; i++) {
            if (s3.equalsIgnoreCase(pls[i])) {
                //System.out.println("3@ " + i);
                sbs.append(i);
            }
        }
        return sbs.toString();
    }

    public static byte intToByte(int i) {
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

    // java byte value to int.
    public static int byteToInt(byte b) {
        int i;
        if (b >= 0) {
            i = b;
        } else {
            i = 256 + b;
        }
        return i;
    }

    public static long binaryToDecimal(byte[] b) {
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

    public static long power(long num, long power) throws BitManException {
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

    public static String getHexStringBuilt(byte[] bytes) {
        final StringBuilder sb = new StringBuilder();

        for (byte b : bytes)
        {
            final String s = Integer.toHexString(Byte.toUnsignedInt(b));
            if (s.length() == 1)
            {
                sb.append("0");
            }
            sb.append(s);
        }
        return sb.toString().toUpperCase();
    }

    public static String getHexString(byte[] b) {
        final StringBuilder sbs = new StringBuilder();

        for (byte cb : b) {
            String st;

            if (cb < 0) {
                st = negHexByte[-(cb)];
            } else {
                st = posHexByte[cb];
            }
            sbs.append(st);
        }
        return sbs.toString();
    }

    public static int _8_bitsToInt(byte[] b) {
        return (int) binaryToDecimal(b);
    }

    public static byte _8_bitsToByte(byte[] b) throws BitManException {
        if (b.length != 8) {
            throw new BitManException("Wrong number of bit, 8 required");
        }

        if (b[0] == 0) {
            return (byte) binaryToDecimal(b);
        } else {
            byte[] bss = to2sComplement(b);
            return (byte) -binaryToDecimal(bss);
        }
    }

    public static void tesT3() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i <= 255; i++) {
            bos.write(i);
        }

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ArrayList<Byte> ebs = new ArrayList<>();

        int o;
        while ((o = bis.read()) != -1) {
            byte[] bbbs = intToBits(o);

            for (byte ui : bbbs) {
                ebs.add(ui);
            }
        }

        final byte[] currentByte = new byte[8];
        int bitCounter = 0;

        if (ebs.size() % 8 != 0) {
            throw new BitManException("Not enough byte .. Nope.");
        }

        final ArrayList<Integer> tempByteStore = new ArrayList<>();
        for (byte b : ebs) // loop thorugh the bits
        {
            currentByte[bitCounter++] = b;
            if (bitCounter >= 8) {
                tempByteStore.add(_8_bitsToInt(currentByte));
                bitCounter = 0;
            }
        }

        bis.reset();
        ArrayList<Integer> int2 = new ArrayList<>();
        int eee;
        while ((eee = bis.read()) != -1) {
            int2.add(eee);
        }

        final Integer[] popo1 = tempByteStore.toArray(new Integer[0]);
        final Integer[] popo2 = int2.toArray(new Integer[0]);

        if (!Arrays.equals(popo1, popo2)) {
            System.out.println(".. FAILED ...");
        }
    }

    public static void tesToo() throws Exception {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i <= 255; i++) {
            bos.write(i);
        }

        byte[] fuckYouByts = bos.toByteArray();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());
        ArrayList<Byte> ebs = new ArrayList<>();

        int o;
        while ((o = bis.read()) != -1) {
            byte[] bbbs = intToBits(o);

            for (byte ui : bbbs) {
                ebs.add(ui);
            }
        }

        final byte[] currentByte = new byte[8];
        int bitCounter = 0;

        if (ebs.size() % 8 != 0) {
            throw new BitManException("Not enough byte .. Nope.");
        }

        final ArrayList<Byte> tempByteStore = new ArrayList<>();
        for (byte b : ebs) // loop thorugh the bits
        {
            currentByte[bitCounter++] = b;
            if (bitCounter >= 8) {
                tempByteStore.add(_8_bitsToByte(currentByte));
                bitCounter = 0;
            }
        }

        byte[] bytes = new byte[tempByteStore.size()];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = tempByteStore.get(i);
        }

        if (bytes.length != fuckYouByts.length) {
            System.out.println("OOOPS length not teh same");
        } else {
            for (int oo = 0; oo < fuckYouByts.length; oo++) {
                if (bytes[oo] != fuckYouByts[oo]) {
                    System.out.println("SOMETHING WRONG ...");
                    break;
                }
            }
        }
    }

    public static void testIntToByte() {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i <= 255; i++) {
            bos.write(i);
        }

        final ArrayList<Integer> intList = new ArrayList<>();
        final ArrayList<Byte> byteList = new ArrayList<>();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        int i;
        while ((i = bis.read()) != -1) {
            intList.add(i);
        }

        bis.reset();
        byte[] bs = bis.readAllBytes();
        for (byte b : bs) {
            byteList.add(b);
        }

        if (intList.size() != byteList.size()) {
            System.out.println("THIS IS WRONG SIZE NOT THE SAME ...");
        }

        boolean ok = true;
        for (int y = 0; y < intList.size(); y++) {
            // the original one ..
            int intVal = intList.get(y);
            int convertedVal = byteToInt(byteList.get(y));

            if (intVal != convertedVal) {
                ok = false;
                break;
            }
        }

        for (int y = 0; y < intList.size(); y++) {
            // the original one ..
            byte origByte = byteList.get(y);
            byte convertedVal = intToByte(intList.get(y));

            if (origByte != convertedVal) {
                ok = false;
                break;
            }
        }

        if (ok) {
            System.out.println("OK ...");
        } else {
            System.out.println("NOT OK, something is not right ...");
        }
    }

    public boolean[] getIntAsBits(int i) {
        return new boolean[]{};
    }

    public boolean[] getByteAsBits(byte b) {
        return new boolean[]{};
    }

    public boolean[] toBaseTwo(long l) {
        return new boolean[]{};
    }

    public static void main(String[] args) throws Exception {
        BitConv s = new BitConv();
        //testIntToByte();
        tesT3();
        /*
        to8Bits("");
        to8Bits("?01");
        to8Bits("?0101-0000");
        to8Bits("00000000");
        to8Bits("0001 1000");
        to8Bits("00000000?");
        to8Bits("?00000000?");
        to8Bits("00-10-11-?");
        to8Bits("?-10-11-");
        to8Bits("?");
        to8Bits("??");
        */

        /*
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        for (int i = 0; i  <= 255; i++)
        {
            bos.write(i);
        }

        final ArrayList<byte[]> intList = new ArrayList<>();
        final ArrayList<byte[]> byteList = new ArrayList<>();

        ByteArrayInputStream bis = new ByteArrayInputStream(bos.toByteArray());

        int i;
        while ((i = bis.read()) != -1)
        {
            intList.add(convertIntToBits(i));
        }

        bis.reset();
        byte[] bs = bis.readAllBytes();
        for (byte b : bs)
        {
            byteList.add(convertByteToBits(b));
        }

        if (intList.size() != byteList.size())
        {
            System.out.println("THIS IS WRONG SIZE NOT THE SAME ...");
        }

        for (int o = 0; o < intList.size(); o++)
        {
            if (!Arrays.equals(intList.getFromDynamicTable(o), byteList.getFromDynamicTable(o)))
            {
                System.out.println("Poorly sized.");
            }
        }
        */

        /*
        int o = 0;
        for(byte i = Byte.MIN_VALUE; i <= Byte.MAX_VALUE; i++)
        {
            //System.out.println(i + " - " + Byte.toUnsignedInt(i));
            byte[] bbs = s.convertByteToBits(i);
            System.out.println(i + " - " + Arrays.toString(bbs) + " - " + bbs.length + " - " + s.stupidTone(bbs));
            /*
            ArrayList<Byte> b = s.iknowThis(i);
            Byte[] bbs = b.toArray(new Byte[b.size()]);

            System.out.println(i + " - " + Arrays.toString(bbs));
            if (i == 127)
            {
                break;
            }
            */
        /*
            if (i == 127)
            {
                break;
            }
        }
        */
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

    private static final String[] posHexByte = new String[]{"00", "01", "02", "03", "04", "05", "06", "07", "08",
            "09", "0a", "0b", "0c", "0d", "0e", "0f", "10", "11", "12", "13", "14", "15", "16", "17", "18",
            "19", "1a", "1b", "1c", "1d", "1e", "1f", "20", "21", "22", "23", "24", "25", "26", "27",
            "28", "29", "2a", "2b", "2c", "2d", "2e", "2f", "30", "31", "32", "33", "34", "35", "36",
            "37", "38", "39", "3a", "3b", "3c", "3d", "3e", "3f", "40", "41", "42", "43", "44", "45",
            "46", "47", "48", "49", "4a", "4b", "4c", "4d", "4e", "4f", "50", "51", "52", "53", "54",
            "55", "56", "57", "58", "59", "5a", "5b", "5c", "5d", "5e", "5f", "60", "61", "62", "63",
            "64", "65", "66", "67", "68", "69", "6a", "6b", "6c", "6d", "6e", "6f", "70", "71", "72",
            "73", "74", "75", "76", "77", "78", "79", "7a", "7b", "7c", "7d", "7e", "7f"};

    private static final String[] negHexByte = new String[]{"00", "ff", "fe", "fd", "fc", "fb", "fa", "f9",
            "f8", "f7", "f6", "f5", "f4", "f3", "f2", "f1", "f0", "ef", "ee", "ed", "ec", "eb", "ea",
            "e9", "e8", "e7", "e6", "e5", "e4", "e3", "e2", "e1", "e0", "df", "de", "dd", "dc", "db",
            "da", "d9", "d8", "d7", "d6", "d5", "d4", "d3", "d2", "d1", "d0", "cf", "ce", "cd", "cc",
            "cb", "ca", "c9", "c8", "c7", "c6", "c5", "c4", "c3", "c2", "c1", "c0", "bf", "be", "bd",
            "bc", "bb", "ba", "b9", "b8", "b7", "b6", "b5", "b4", "b3", "b2", "b1", "b0", "af", "ae",
            "ad", "ac", "ab", "aa", "a9", "a8", "a7", "a6", "a5", "a4", "a3", "a2", "a1", "a0", "9f",
            "9e", "9d", "9c", "9b", "9a", "99", "98", "97", "96", "95", "94", "93", "92", "91", "90",
            "8f", "8e", "8d", "8c", "8b", "8a", "89", "88", "87", "86", "85", "84", "83", "82", "81",
            "80"};
}
