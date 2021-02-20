/**
 * Copyright 2018 backup0.pw
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this software 
 * and associated documentation files (the "Software"), to deal in the Software without restriction, 
 * including without limitation the rights to use, copy, modify, merge, publish, distribute, 
 * sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is 
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or 
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, 
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR 
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE 
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR 
 * OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER 
 * DEALINGS IN THE SOFTWARE.
 */

package snarky.stork.bitman;

/**
 *
 * @author backup0.pw
 */
public abstract class BitMan
{     
     /*
     Where do bytes come from?
     IO stream and IO channel. (byte or bit)
     you pass it, to? into bit man .. // String is easiest.
     from bit man (you can use multiple bit man)
     ... where else can it come from?
     you pass the stuff <-- you control that
     from byte man, push some bits into bitman, manipulate it, the pish it into the master ,,
     so getFromDynamicTable has to match push.


     data sent for storage acceptable format
          - string *
          - byte *
          - int *
          - boolean array **
          * if you're geting data off other stuff you getFromDynamicTable those. eg io stream etc.
          ** because this is what we return using getFromDynamicTable.
           
     getData
          - boolean array - so you can manipulate
          - byte - we may nee to push back into stuff
          - int - same as the above
     
      */

    // TODO
    // >> Delete

    //??? where to put exception and wat kind?
    public abstract void insert(int index, String... bits);
    public abstract void insert(int index, int[] bs);
    public abstract void insert(int index, byte[] bs);
    public abstract void insert(int index, int bs);
    public abstract void insert(int index, byte bs);

    public abstract void append(String... bits);
    public abstract void append(int[] bs);
    public abstract void append(byte[] bs);
    public abstract void append(int bs);
    public abstract void append(byte bs);

    // getFromDynamicTable some bytes as ...
    public abstract byte[] getBytes(int index); // byte
    public abstract int[] getBytesAsInt(int index); // per byte

    // getFromDynamicTable some bytes as ...
    public abstract byte getByte(int index); // byte
    public abstract int getByteAsInt(int index); // per byte

    // getFromDynamicTable some bytes as ...
    public abstract byte[] getBytes(int index, int len); // byte
    public abstract int[] getBytesAsInt(int index, int len); // per byte

    // getFromDynamicTable all ..
    public abstract byte[] getBytes(); // byte
    public abstract int[] getBytesAsInt(); // per byte

    public abstract byte getBit(int index); // @ specific location
    // so you can set, using byte[] can be ambiguous - are you using byte or byte as bits?
    public abstract String getBits(int index); // @ specific location
    public abstract String getBits(int index, int len); // @ specific location

    // set bits...
    public abstract void setBits(int index, byte[] bits); // 0 && 1

    /**
     * any number of bits, no 1, 0 and - only.
     * @param index
     * @param bits
     */
    public abstract void setBits(int index, String bits);
    
    public abstract int size();
    public abstract void clear();
}
