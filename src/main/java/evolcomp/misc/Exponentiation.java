package evolcomp.misc;

public class Exponentiation {
    public static int getClosest2ToThePowerOf(int value) {
        int i = 1;
        while (i <= value) {
            i *= 2;
        }
        return i;
    }
}
