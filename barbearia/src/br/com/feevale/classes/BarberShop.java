public class BarberShop {
    public int sofa = 0;
    public int standingUp = 0;
    public int chairs = 0;
    public static final int MAX_SOFA = 4;
    public static final int MAX_CHAIRS = 3;
    public static final int MAX_STANDING_UP = 13;

    public boolean isBarberShopFull() {
        return sofa + standingUp + chairs == MAX_SOFA + MAX_STANDING_UP + MAX_CHAIRS;
    }
    public boolean isSofaFull() {
        return sofa == MAX_SOFA;
    }
    public boolean isChairsFull() {
        return chairs == MAX_CHAIRS;
    }
    public boolean isStandUpPlacesFull() {
        return standingUp == MAX_STANDING_UP;
    }
}
