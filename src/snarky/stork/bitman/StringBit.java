package snarky.stork.bitman;

import java.util.Arrays;

public class StringBit {

    public static byte[] to8Bits(String s) {

        if (s.length() == 0) {
            s = "?";
        }

        if ("?".equalsIgnoreCase(s)) {
            s = "?0";
        }

        final char[] chars = s.toCharArray();
        int qmCount = 0;
        for (char c : chars) {
            if (c == '?') {
                qmCount++;
            }
            if (qmCount > 1) {
                throw new BitManException("Only one ? is allowed");
            }
        }

        final boolean misplacedQM;
        misplacedQM = (!s.startsWith("?") && !s.endsWith("?")) && s.contains("?");

        if (misplacedQM) {
            throw new BitManException("? can only be @ start or end");
        }

        final StringBuilder sbs = new StringBuilder();
        for (char c : chars) {

            if (c == '0' || c == '1') {
                sbs.append(c);
            }
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
            throw new BitManException("Incorrect length: " + sbs.length());
        }
        else{
            return toBits(sbs.toString());
        }
    }

    public static byte[] toBits(String s)
    {
        final char[] chars = s.toCharArray();
        final byte[] bytes = new byte[chars.length];

        int i = 0;
        for (char c : chars) {
            if (c == '0') {
                bytes[i++] = 0;
            } else if (c == '1') {
                bytes[i++] = 1;
            }
        }
        return Arrays.copyOf(bytes, i);
    }

    public static String getAsString(byte[] b)
    {
        final StringBuilder sbs = new StringBuilder();
        for (byte bb : b){
            if (bb == 0){
                sbs.append("0");
            }else{
                sbs.append("1");
            }
        }
        return sbs.toString();
    }

    /**
     * Put 0 at the front of s, until length = len
     * @param s
     * @param len
     * @return
     */
    public static String padBits(String s, int len) {
        final StringBuilder sb = new StringBuilder(s);

        while (sb.length() < len) {
            sb.insert(0, "0");
        }
        return sb.toString();
    }
}
