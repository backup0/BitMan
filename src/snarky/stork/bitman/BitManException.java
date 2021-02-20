package snarky.stork.bitman;

public class BitManException extends RuntimeException
{
    public BitManException()
    {
        this("BitMan exception");
    }

    public BitManException(String s)
    {
        super(s);
    }
}
