import java.util.Random;
import java.util.Scanner;

class Genereerimine {
    static void genereeri(int valemiteArv, int teheteArv, int argumentideArv, Random rng) {
        for (int i = 0; i < valemiteArv; ++i) {
            PredikaatarvutuseValem valem = new PredikaatarvutuseValem();
            valem.suurenda(teheteArv, rng);
            valem.lisaArgumendid(argumentideArv, rng);
            System.out.println("\n");
            System.out.println(valem);
        }
    }

    public static void main(String[] args) {
        
        Scanner sc = new Scanner(System.in);

        System.out.println("Sisesta valemite arv:");
        int valemiteArv = sc.nextInt();

        System.out.println("Sisesta valemi tehete arv:");
        int teheteArv = sc.nextInt();

        System.out.println("Sisesta valemi predikaatide argumentide arv:");
        int argumentideArv = sc.nextInt();

        long seed = System.currentTimeMillis();
        System.out.println("Seed:\t" + seed);
        Random rng = new Random(seed);

        genereeri(valemiteArv, teheteArv, argumentideArv, rng);
    }
}