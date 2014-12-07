package com.jes.emu6502;

/**
 * Created by Piotr Kulma on 2014-11-22.
 */
public class StatusRegister {
    //sign flag
    public int N = 0;
    //overflow flag
    public int V = 0;
    //break flag
    public int B = 0;
    //decimal flag
    public int D = 0;
    //interrupt flag
    public int I = 1;
    //zero flag
    public int Z = 0;
    //carry flag
    public int C = 0;

    @Override
    public String toString() {
        StringBuffer stringBuff = new StringBuffer("");

        stringBuff.append("SR=[ ");
        stringBuff.append(" N: ");
        stringBuff.append(N);
        stringBuff.append(", V: ");
        stringBuff.append(V);
        stringBuff.append(", #: ");
        stringBuff.append(1);
        stringBuff.append(", B: ");
        stringBuff.append(B);
        stringBuff.append(", D: ");
        stringBuff.append(D);
        stringBuff.append(", I: ");
        stringBuff.append(I);
        stringBuff.append(", Z: ");
        stringBuff.append(Z);
        stringBuff.append(", C: ");
        stringBuff.append(C);
        stringBuff.append(" ] ");
        return stringBuff.toString();
    }
}
