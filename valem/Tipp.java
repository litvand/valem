abstract class Tipp {
    protected Tipp[] lapsed;
    protected Tipp vanem;

    public int lasteArv() {
        return lapsed == null ? 0 : lapsed.length;
    }
 
    public int lapseIndeks(Tipp laps) {
        for (int i = 0; i < lapsed.length; ++i) {
            if (laps == lapsed[i]) return i;
        }
        return -1;
    }

    public void asendaLaps(Tipp laps, Tipp uus) {
        lapsed[lapseIndeks(laps)] = uus;
    }

    public Tipp laps(int i) { return lapsed[i]; }

    public Tipp vanem() { return vanem; }
    
    protected void setVanem(Tipp vanem) { this.vanem = vanem; }

    protected void setVanemaks() {
        if (lapsed != null) {
            for (Tipp laps : lapsed) laps.setVanem(this);
        }
    }

    public static String indiviidiString(int indiviid) { 
        return String.valueOf((char)('q' + indiviid));
    }
}