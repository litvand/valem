import java.util.ArrayList;
import java.util.Random;

class LausearvutuseValem {
    protected Juur juur;
    // Lausearvutuse muutujad on põhimõtteliselt argumentideta predikaadid.
    protected ArrayList<String> predikaatNimed;
    protected ArrayList<Predikaat> lehed; // lehed.size() >= predikaatNimed.size()
    
    private Predikaat uusPredikaat() { // Kutsutakse konstruktorist.
        int mitmes = predikaatNimed.size();
        String nimi = String.valueOf((char)('A' + mitmes)); // "A", "B", "C", ...
        Predikaat uus = new Predikaat(mitmes, nimi);
        predikaatNimed.add(nimi);
        return uus;
    }

    LausearvutuseValem() {
        predikaatNimed = new ArrayList<>();
        juur = new Juur(uusPredikaat()); // Juur on eraldi klassi tipp.
        lehed = new ArrayList<>();
        lehed.add((Predikaat)juur.laps(0)); // Iga leht on predikaat.
    }

    private Predikaat getPredikaat(Random rng) {
        int hetkelOlemas = predikaatNimed.size();
        int mitmes = rng.nextInt(hetkelOlemas + 1);
        return (mitmes == hetkelOlemas) ? uusPredikaat() :
               new Predikaat(mitmes, predikaatNimed.get(mitmes));
    }

    private Tipp uusTipp(Tipp laps, Random rng) {
        Tipp uus = null;
        switch (rng.nextInt(5)) {
        case 0:
            uus = new Eitus(laps);
            break;
        case 1:
            Tipp teineLaps = getPredikaat(rng);
            // TODO: Suvaline pool ajast `Konjunktsioon(teineLaps, laps)`?
            uus = new Konjunktsioon(laps, teineLaps);
            break;
        case 2:
            uus = new Disjunktsioon(laps, getPredikaat(rng));
            break;
        case 3:
            uus = new Implikatsioon(laps, getPredikaat(rng));
            break;
        case 4:
            uus = new Ekvivalents(laps, getPredikaat(rng));
            break;
        default: // Kvantoreid ja indiviide võib hiljem lisada.
            assert false;
        }
        assert uus.lapseIndeks(laps) >= 0;
        return uus;
    }

    public void suurenda(Random rng) {
        Tipp leht = lehed.get(rng.nextInt(lehed.size()));
        leht.vanem().asendaLaps(leht, uusTipp(leht, rng));
    }

    public void suurenda(int teheteArv, Random rng) {
        for (int i = 0; i < teheteArv; ++i) suurenda(rng);
    }

    public Juur getJuur() { return juur; }

    public String toString() { return juur.toString(); }
}