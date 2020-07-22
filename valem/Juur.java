class Juur extends Tipp {
    Juur(Tipp laps) {
        lapsed = new Tipp[1];
        lapsed[0] = laps;
        setVanemaks();
    }

    private static String vajaduselSulgudes(Tipp tipp) {
        String s = alampuuString(tipp);
        return (tipp instanceof Predikaat || 
                tipp instanceof Kvantor ||
                tipp instanceof Eitus) ? s : '(' + s + ')';
    }

    private static String alampuuString(Tipp tipp) {
        if (tipp.lasteArv() == 0) {
            assert (tipp instanceof Predikaat);
            return tipp.toString();
        }

        String s = vajaduselSulgudes(tipp.laps(0));
        if (tipp.lasteArv() == 1) return tipp.toString() + s;

        assert tipp.lasteArv() == 2;
        return s + tipp.toString() + vajaduselSulgudes(tipp.laps(1));
    }

    public String toString() { return alampuuString(lapsed[0]); }
}