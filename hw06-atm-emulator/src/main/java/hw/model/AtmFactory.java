package hw.model;

public class AtmFactory {

    public static Atm createAtm () {
        return new AtmImpl(new AtmStorage(), new Dispenser());
    }

}
