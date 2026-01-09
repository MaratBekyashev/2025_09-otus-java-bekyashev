package hw;

import hw.model.Atm;
import hw.model.AtmFactory;
import hw.model.Banknote;
import java.util.Map;
import java.util.Scanner;

public class Application {

    public static void main(String[] args) {
        Atm atm = AtmFactory.createAtm();

        Scanner scan = new Scanner(System.in);
        int banknoteCount;
        do {
            System.out.println(">>> Enter quantity of deposited banknotes( 0 - to cancel):");
            banknoteCount = scan.nextInt();
            if (banknoteCount == 0) {
                break;
            }
            if (banknoteCount > 0) {
                System.out.println(">>> Enter the banknote nominal to deposit:");
                int banknoteNominal = scan.nextInt();

                var banknote = Banknote.findBanknoteByNominal(banknoteNominal);
                atm.deposit(banknote, banknoteCount);
            }
        } while (true);

        System.out.println("Current ATM Balance: " + atm.getBalance());

        System.out.println("How many money you want to withDraw:");
        int amountToWithDraw = scan.nextInt();

        Map<Banknote, Integer> result = atm.withdraw(amountToWithDraw);

        System.out.println("Withdrawn: " + result);
        System.out.println("Current ATM balance is " + atm.getBalance());
    }
}
