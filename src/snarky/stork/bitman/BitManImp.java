package snarky.stork.bitman;

import java.util.ArrayList;
import java.util.Arrays;

public class BitManImp extends BitMan {

    /**
     * see if we have enough bytes, if we don't allocate some.
     * @param index
     */
    private void extendIfNecessary(int index){
        if (index <= _bits.size()) return;

        for(int i = _bits.size(); i < index;i++){
            _bits.add((byte) 0);
        }
    }

    @Override
    public void insert(int index, String... bits) {
        extendIfNecessary(index);
        for (String s : bits){
            final byte[] b = StringBit.to8Bits(s); // each string must have 8 valid bits
            _bits.add(index++, BitCon.bitsToByte(b));
        }
    }

    @Override
    public void insert(int index, int[] b) {
        extendIfNecessary(index);
        for (int i : b){
            _bits.add(index++, BitCon.intToByte(i));
        }
    }

    @Override
    public void insert(int index, byte[] b) {
        extendIfNecessary(index);
        for (byte bb : b){
            _bits.add(index++, bb);
        }
    }

    @Override
    public void insert(int index, int b) {
        extendIfNecessary(index);
        _bits.add(index, BitCon.intToByte(b));
    }

    @Override
    public void insert(int index, byte b) {
        extendIfNecessary(index);
        _bits.add(index, b);
    }

    @Override
    public void append(String... bits) {
        for (String s : bits){
            final byte[] b = StringBit.to8Bits(s);
            _bits.add(BitCon.bitsToByte(b));
        }
    }

    @Override
    public void append(int[] b) {
        for (int i : b){
            _bits.add(BitCon.intToByte(i));
        }
    }

    @Override
    public void append(byte[] b) {
        for (byte eb : b){
            _bits.add(eb);
        }
    }

    @Override
    public void append(int b) {

        _bits.add(BitCon.intToByte(b));
    }

    @Override
    public void append(byte b) {
        _bits.add(b);
    }

    @Override
    public byte[] getBytes(int index) {
        return getBytes(index, _bits.size());
    }

    @Override
    public int[] getBytesAsInt(int index) {
        return getBytesAsInt(index, _bits.size());
    }

    @Override
    public byte getByte(int index) {
        return _bits.get(index);
    }

    @Override
    public int getByteAsInt(int index) {
        return BitCon.byteToInt(_bits.get(index));
    }

    @Override
    public byte[] getBytes(int index, int len) {

        if (index >= _bits.size()) {
            return new byte[]{};
        }

        final byte[] bytes = new byte[len];
        int bitCount = 0;

        while (index < _bits.size() && len-- > 0) {
            bytes[bitCount++] = _bits.get(index++);
        }
        return Arrays.copyOfRange(bytes, 0, bitCount);
    }

    @Override
    public int[] getBytesAsInt(int index, int len) {
        final byte[] bbs = getBytes(index, len);

        final int[] ints = new int[bbs.length];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = BitCon.byteToInt(bbs[i]);
        }

        return ints;
    }

    @Override
    public byte[] getBytes() {
        return getBytes(0, _bits.size());
    }

    @Override
    public int[] getBytesAsInt() {
        return getBytesAsInt(0, _bits.size());
    }

    @Override
    public byte getBit(int index) {

        int byteLoc = getByteBlock(index);
        if (byteLoc >= _bits.size()) {
            throw new BitManException("Out of bound.");
        }

        final byte[] byteTodo = getAsBits(byteLoc); // 'tis the first byte (as bit in byte)
        final int bitIndex = index - getByteBoundaries(byteLoc)[0];
        return byteTodo[bitIndex];
    }

    @Override
    public String getBits(int index) {
        // todo: test
        return getBits(index, (_bits.size() * 8) - index);
    }

    @Override
    public String getBits(int index, int len) {
        final StringBuilder sbs = new StringBuilder();
        int byteLoc = getByteBlock(index);

        byte[] byteTodo = getAsBits(byteLoc); // 'tis the first byte (as bit in byte)
        int bitIndex = index - getByteBoundaries(byteLoc)[0];

        while(len-- > 0 && byteTodo.length > 0) {

            if (byteTodo[bitIndex++] == 0) {
                sbs.append("0");
            }
            else {
                sbs.append("1");
            }

            if (bitIndex >= 8){
                // @ the end
                bitIndex = 0;
                int nextByte = byteLoc + 1;
                if (nextByte >= _bits.size()){
                    byteTodo = new byte[]{};
                }
                else{
                    byteTodo = getAsBits(++byteLoc);
                }
            }
        }
        return sbs.toString();
    }

    @Override
    public void setBits(int index, byte[] bits) {
        setBits(index, StringBit.getAsString(bits));
    }

    @Override
    public void setBits(int index, String bits) {

        int byteLoc = getByteBlock(index);

        final byte[] modificatorBits = StringBit.toBits(bits);
        int mbc = 0; // use this to keep track of the bit we're dealing with

        byte[] byteTodo = getAsBits(byteLoc);
        int bitIndex = index - getByteBoundaries(byteLoc)[0];

        while(mbc < modificatorBits.length && byteTodo.length > 0) {
            // modify the bit @ location
            byteTodo[bitIndex++] = modificatorBits[mbc++];

            // we reach the end of this byte
            // push it back into ...
            if (bitIndex >= 8) {
                // @ the end
                bitIndex = 0;
                _bits.set(byteLoc, BitCon.bitsToByte(byteTodo));
                int nextByte = byteLoc + 1;
                if (nextByte >= _bits.size()) {
                    byteTodo = new byte[]{};
                }
                else {
                    byteTodo = getAsBits(++byteLoc);
                }
            }
        }

        if (byteTodo.length == 8) {
            _bits.set(byteLoc, BitCon.bitsToByte(byteTodo));
        }
    }

    @Override
    public int size() {
        return _bits.size();
    }

    @Override
    public void clear() {
        _bits.clear();
    }

    /**
     * Grab a byte @ index, convert to bits.
     * @param index
     * @return byte as bits
     */
    private byte[] getAsBits(int index) {
        if (index >= _bits.size()){
            return new byte[]{};
        }

        final byte theByte = _bits.get(index);
        if (theByte >= 0) {
            return decode(poz[theByte]);
        }
        else {
            return decode(negz[-(theByte)]);
        }
    }

    private int getByteBlock(int i){
        return i/8;
    }

    private int[] getByteBoundaries(int index)
    {
        return new int[]{index * 8, (index * 8) + 7};
    }

    /**
     * This convert the 3 char string into [0 | 1, ... x8]
     * @param s
     * @return
     */
    private byte[] decode(String s)
    {
        final char[] cs =  s.toCharArray();
        final byte[] b = new byte[8];

        int i0 = 0, i1 = 1, i2 = 2;
        switch (cs[0]) {
            case '0':
                b[i0] = 0; b[i1] = 0; b[i2] = 0;
                break;
            case '1':
                b[i0] = 0; b[i1] = 0; b[i2] = 1;
                break;
            case '2':
                b[i0] = 0; b[i1] = 1; b[i2] = 0;
                break;
            case '3':
                b[i0] = 0; b[i1] = 1; b[i2] = 1;
                break;
            case '4':
                b[i0] = 1; b[i1] = 0; b[i2] = 0;
                break;
            case '5':
                b[i0] = 1; b[i1] = 0; b[i2] = 1;
                break;
            case '6':
                b[i0] = 1; b[i1] = 1; b[i2] = 0;
                break;
            case '7':
                b[i0] = 1; b[i1] = 1; b[i2] = 1;
                break;
            default:
                break;
        }

        i0 = 3; i1 = 4; i2 = 5;
        switch (cs[1]) {
            case '0':
                b[i0] = 0; b[i1] = 0; b[i2] = 0;
                break;
            case '1':
                b[i0] = 0; b[i1] = 0; b[i2] = 1;
                break;
            case '2':
                b[i0] = 0; b[i1] = 1; b[i2] = 0;
                break;
            case '3':
                b[i0] = 0; b[i1] = 1; b[i2] = 1;
                break;
            case '4':
                b[i0] = 1; b[i1] = 0; b[i2] = 0;
                break;
            case '5':
                b[i0] = 1; b[i1] = 0; b[i2] = 1;
                break;
            case '6':
                b[i0] = 1; b[i1] = 1; b[i2] = 0;
                break;
            case '7':
                b[i0] = 1; b[i1] = 1; b[i2] = 1;
                break;
            default:
                break;
        }

        i0 = 6; i1 = 7;
        switch (cs[2]) {
            case '0':
                b[i0] = 0; b[i1] = 0;
                break;
            case '1':
                b[i0] = 0; b[i1] = 1;
                break;
            case '2':
                b[i0] = 1; b[i1] = 0;
                break;
            case '3':
                b[i0] = 1; b[i1] = 1;
                break;
            default:
                break;
        }
        return b;
    }

    public String dump() {
        final StringBuilder sbs = new StringBuilder();

        for (byte b : _bits) {
            sbs.append(Byte.valueOf(b)).append(", ");
        }
        return sbs.toString();
    }

    public String dumpBits() {
        final StringBuilder sbs = new StringBuilder();

        for (byte b : _bits) {
            byte[] bytes = BitCon.byteToBits(b);
            sbs.append(StringBit.getAsString(bytes)).append(" ");
        }
        return sbs.toString();
    }

    private ArrayList<Byte> _bits = new ArrayList<>();

    private static final String[] poz = new String[] {
            "000", "001", "002", "003", "010", "011", "012", "013", "020", "021",
            "022", "023", "030", "031", "032", "033", "040", "041", "042", "043", "050", "051", "052",
            "053", "060", "061", "062", "063", "070", "071", "072", "073", "100", "101", "102", "103",
            "110", "111", "112", "113", "120", "121", "122", "123", "130", "131", "132", "133", "140",
            "141", "142", "143", "150", "151", "152", "153", "160", "161", "162", "163", "170", "171",
            "172", "173", "200", "201", "202", "203", "210", "211", "212", "213", "220", "221", "222",
            "223", "230", "231", "232", "233", "240", "241", "242", "243", "250", "251", "252", "253",
            "260", "261", "262", "263", "270", "271", "272", "273", "300", "301", "302", "303", "310",
            "311", "312", "313", "320", "321", "322", "323", "330", "331", "332", "333", "340", "341",
            "342", "343", "350", "351", "352", "353", "360", "361", "362", "363", "370", "371", "372",
            "373"};

    // negative valuez... (0 / -1 to -128)
    private static final String[] negz = new String[] {
            "000", "773", "772", "771", "770", "763", "762", "761", "760", "753",
            "752", "751", "750", "743", "742", "741", "740", "733", "732", "731", "730", "723", "722",
            "721", "720", "713", "712", "711", "710", "703", "702", "701", "700", "673", "672", "671",
            "670", "663", "662", "661", "660", "653", "652", "651", "650", "643", "642", "641", "640",
            "633", "632", "631", "630", "623", "622", "621", "620", "613", "612", "611", "610", "603",
            "602", "601", "600", "573", "572", "571", "570", "563", "562", "561", "560", "553", "552",
            "551", "550", "543", "542", "541", "540", "533", "532", "531", "530", "523", "522", "521",
            "520", "513", "512", "511", "510", "503", "502", "501", "500", "473", "472", "471", "470",
            "463", "462", "461", "460", "453", "452", "451", "450", "443", "442", "441", "440", "433",
            "432", "431", "430", "423", "422", "421", "420", "413", "412", "411", "410", "403", "402",
            "401", "400"};
}
