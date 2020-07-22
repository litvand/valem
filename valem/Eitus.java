class Eitus extends Tipp {
    Eitus(Tipp laps) {
        lapsed = new Tipp[1];
        lapsed[0] = laps;
        setVanemaks();
    }
    public String toString() { return "¬"; }
}