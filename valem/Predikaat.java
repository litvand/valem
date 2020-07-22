import java.util.ArrayList;

class Predikaat extends Tipp {
    private int mitmes;  // Pärast predikaadi loomist ei saa muuta.
    private String nimi; // Pärast predikaadi loomist ei saa muuta.
    public ArrayList<Integer> argumendid;

    Predikaat(int mitmes, String nimi) {
        this.mitmes = mitmes;
        this.nimi = nimi;
        argumendid = null;
    }

    public int getMitmes() { return mitmes; }

    public String toString() {
        String s = nimi;
        if (argumendid == null || argumendid.size() == 0) return s;

        s += '(';
        for (int i = 0; i < argumendid.size(); ++i) {
            s += indiviidiString(argumendid.get(i));
            if (i < argumendid.size() - 1) s += ", ";
        }
        s += ')';
        return s;
    }
}