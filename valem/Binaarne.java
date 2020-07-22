abstract class Binaarne extends Tipp {
    Binaarne(Tipp a, Tipp b) {
        lapsed = new Tipp[2];
        lapsed[0] = a;
        lapsed[1] = b;
        setVanemaks();
    }
}