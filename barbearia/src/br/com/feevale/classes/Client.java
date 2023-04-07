public class Clients extends Thread {
    public static final int STANDING_UP = 1;
    public static final int SEATED_IN_SOFA = 2;
    public static final int CUTTING_HAIR = 3;
    public static final int PAYNG = 4;
    public static final int LEAVING = 5;

    public int id;
    public int actionType;

    public BarberShop barberShop;

    public Client(int id, BarberShop barberShop) {
        this.id = id;
        this.barberShop = barberShop;
        this.actionType = STANDING_UP;
        start();
    }

    @Override
    public void start() {
        if(barberShop.isBarberShopFull()) return;
        super.start();
        setPriority(1);
        run();
    }

    @Override
    public void run() {
        super.run();
        while(actionType != LEAVING) {
            try {
                locateClientInBarberShop();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private void locateClientInBarberShop() throws InterruptedException {
        if(actionType == STANDING_UP && !barberShop.isSofaFull()) goToSofa();
        else if(actionType == SEATED_IN_SOFA && !barberShop.isChairsFull()) goToChair();
        else if(actionType == CUTTING_HAIR) goPay();
        else if(actionType == PAYNG) leave();

        performAction();
    }

    private void goToChair() {
        actionType = CUTTING_HAIR;
        barberShop.chairs++;
        setPriority(1);
        System.out.println("Cliente " + id + " Sentou na cadeira para corte");
    }
    private void goToSofa() {
        barberShop.standingUp--;
        actionType = SEATED_IN_SOFA;
        barberShop.sofa++;
        setPriority(5);
        System.out.println("Cliente " + id + " Sentou no Sofa");
    }
    private void standUp() {
        actionType = STANDING_UP;
        barberShop.standingUp++;
        setPriority(1);
    }
    private void goPay() {
        actionType = PAYNG;
        barberShop.standingUp++;
        setPriority(1);
        System.out.println("Cliente " + id + " Foi Pagar");
    }
    private void leave() {
        actionType = LEAVING;
        barberShop.standingUp--;
        System.out.println("Cliente " + id + " Foi embora");
    }
    private void performAction() {
        try {
            Thread.sleep(1000);
            setPriority(getPriority()+1);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
