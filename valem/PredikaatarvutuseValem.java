import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

class PredikaatarvutuseValem extends LausearvutuseValem {
    
    private ArrayList<ArrayList<Integer>> predikaatArgumendid;
    private HashMap<Tipp, ArrayList<Integer>> allSeotud;
    int indiviidideArv;

    PredikaatarvutuseValem() { // Alguses valemil ei ole ühtegi indiviidi.
        predikaatArgumendid = new ArrayList<>();
        allSeotud = new HashMap<>();
        indiviidideArv = 0;
    }

    private int getIndiviid(Random rng) { // Tagasta olemasolev või loo uus indiviid.
        int indiviid = rng.nextInt(indiviidideArv + 1);
        if (indiviid == indiviidideArv) ++indiviidideArv;
        return indiviid;
    }

    private boolean onArgument(int indiviid, int mitmesPredikaat) {
    // Tagasta, kas indiviid on selle predikaadi argument.
        for (int argument : predikaatArgumendid.get(mitmesPredikaat)) {
            if (indiviid == argument) return true;
        }
        return false;
    }

    private int lisaVabaArgument(int mitmesPredikaat, Random rng) {
    // Lisa sellele predikaadile argument, mis on kas olemasolev või uus indiviid, aga
    // igal juhul predikaadis hetkel vaba indiviid.
        int indiviid;
        // Kõik argumendid, mis predikaadil hetkel on, on seotud, nii et
        // vali juhuslike indiviide, kuni üks ei ole predikaadi argumentide
        // hulgas ning seetõttu on vaba.
        do {
            indiviid = getIndiviid(rng);
        } while (onArgument(indiviid, mitmesPredikaat));
        predikaatArgumendid.get(mitmesPredikaat).add(indiviid); // Lisa vaba indiviid.
        return indiviid;
    }

    private void enneSidumist(Tipp tipp) {
    // Uuenda räsitabelit `allSeotud` enne uute indiviidide lisamist ja sidumist.
    // Indiviid on tipu all seotud parajasti siis, kui tipp on selle indiviidiga
    // kvantor, või üks tipu eellastest on selle indiviidiga kvantor, või üks
    // tipu lastest on selle indiviidiga kvantor, või üks laste lastest on selle
    // indiviidiga kvantor jne.
        if (!allSeotud.containsKey(tipp)) allSeotud.put(tipp, new ArrayList<>());
        ArrayList<Integer> tipuAllSeotud = allSeotud.get(tipp);
        for (int i = 0; i < tipp.lasteArv(); ++i) {
            Tipp laps = tipp.laps(i);
            enneSidumist(laps);
            ArrayList<Integer> lapseAllSeotud = allSeotud.get(laps);
            for (int seotudIndiviid : lapseAllSeotud) {
                if (!tipuAllSeotud.contains(seotudIndiviid)) {
                    tipuAllSeotud.add(seotudIndiviid);
                }
            }
        }
    }

    private static boolean onVabuArgumente(ArrayList<ArrayList<Integer>> vabadArgumendid) {
    // Tagastab false, kui valemi kõik indiviidid on seotud.
        for (ArrayList<Integer> vabad : vabadArgumendid) {
            if (vabad.size() > 0) return true;
        }
        return false;
    }

    private void eemaldaVaba(int indiviid,
                             ArrayList<ArrayList<Integer>> vabadArgumendid,
                             Tipp tipp) {
        // `indiviid` on nüüd seotud tipus `tipp`, nii et iga lehe korral
        // eemalda indiviid lehe vabadest argumentide listist, kui leht
        // asub tipu `tipp` all ehk `tipp` on lehe eellane või leht ise.

        if (!(tipp instanceof Predikaat)) { // Tipp ei ole leht.
            for (int i = 0; i < tipp.lasteArv(); ++i) {
                eemaldaVaba(indiviid, vabadArgumendid, tipp.laps(i));
            }
            return;
        }

        int leheIndeks = lehed.indexOf(tipp);
        boolean lehtLeiti = (leheIndeks >= 0);
        assert (lehtLeiti); // Kui tipp on predikaat, siis ta on leht.
        ArrayList<Integer> vabad = vabadArgumendid.get(leheIndeks);
        int vabaIndeks = vabad.indexOf(indiviid);
        if (vabaIndeks < 0) { // Indiviid ei olnud selles predikaadis vaba, sest
            return;           // ta ei olnud üldse selle predikaadi argument.
        }

        vabad.remove(vabaIndeks);            // Indiviid võis listis `vaba` olla
        assert !(vabad.contains(indiviid));  // maksimaalselt üks kord.
    }

    public void lisaArgumendid(int lisamisteArv, Random rng) {
    // Lisa predikaatidele argumendid ning seejärel lisa vajadusel valemile
    // kvantorid suvalistesse kohtadesse, et iga predikaadi kõik argumendid
    // oleksid seotud.

        enneSidumist(juur);

        while (predikaatArgumendid.size() < predikaatNimed.size()) {
        // Ühe predikaati argumendid ja nende järjekord on samad selle
        // predikaati iga tipu korral, nii et on listis `predikaatArgumendid`
        // on omakorda list iga predikaati kohta, mitte iga lehe kohta.
        // Seevastu sama nimega argument võib olla vaba predikaati ühes
        // tipus, aga mitte teises, nii et on eraldi vabade argumentide
        // list iga lehe kohta, mitte ainult iga predikaati kohta.
            predikaatArgumendid.add(new ArrayList<>());
        }

        // Hetkel valemis olemasolevad argumendid on kõik seotud, nii et
        // loome tühjade listidega listi uute argumentide jaoks.
        ArrayList<ArrayList<Integer>> vabadArgumendid = new ArrayList<>();
        while (vabadArgumendid.size() < lehed.size()) {
            vabadArgumendid.add(new ArrayList<>());
        }

        for (int i = 0; i < lisamisteArv; ++i) {

            // Valime suvalise predikaadi.
            int mitmesPredikaat = rng.nextInt(predikaatNimed.size());
            
            // Lisame selle argumendi, mis on kas uus või olemasolev indiviid.
            int argument = lisaVabaArgument(mitmesPredikaat, rng);

            // Jätame meelde selle predikaadi iga lehe korral, et lehel on
            // nüüd vaba argument.
            for (int j = 0; j < lehed.size(); ++j) {
                if (lehed.get(j).getMitmes() == mitmesPredikaat) {
                    vabadArgumendid.get(j).add(argument);
                }
            }
        }

        while (onVabuArgumente(vabadArgumendid)) {

            // Vali suvaline leht, millel on veel vabu argumente.
            int leheIndeks = rng.nextInt(lehed.size());
            ArrayList<Integer> vabad = vabadArgumendid.get(leheIndeks);
            if (vabad.size() == 0) continue; // Lehel ei ole vabu indiviide; proovi uuesti.

            // Vali lehe suvaline vaba argument.
            int vabaIndeks = rng.nextInt(vabad.size());
            int vaba = vabad.get(vabaIndeks); // Tsükli selles iteratsioonis
            vabad.remove(vabaIndeks);         // seotakse see vaba indiviid.

            // Tee list lehe enda ja lehe eellastega, mille all lehes vaba
            // indiviid ei ole seotud. (Indiviidi ei saa kahekordselt siduda,
            // näiteks "on olemas x, nii et iga x-i puhul, A(x)".)
            Tipp leht = lehed.get(leheIndeks);
            Tipp oksal = leht;
            ArrayList<Tipp> oks = new ArrayList<>(); // Lehe eellased mingi kõrguseni
            assert leht != null;
            while (!(oksal instanceof Juur || allSeotud.get(oksal).contains(vaba))) {
                oks.add(oksal);
                oksal = oksal.vanem();
                assert oksal != null;
            }
            assert oks.size() >= 1; // Leht on alati oksal.
        
            // Vali suvaline koht, kuhu kvantor lisada indiviidi sidumiseks.
            Tipp kvantoriLaps = oks.get(rng.nextInt(oks.size()));
            
            // Vali suvaline kvantor.
            Kvantor kvantor;
            if (rng.nextInt(2) == 0) {
                kvantor = new Olemasolu(kvantoriLaps, vaba);
            } else {
                kvantor = new Universaalsus(kvantoriLaps, vaba);
            }
            assert kvantor.lapseIndeks(kvantoriLaps) >= 0;
            kvantoriLaps.vanem().asendaLaps(kvantoriLaps, kvantor);
            eemaldaVaba(vaba, vabadArgumendid, kvantoriLaps);

            // Jäta meelde järgmiste argumentide lisamiseks, et varem
            // vaba indiviid on nüüd selle oksa iga tipu all seotud.
            for (Tipp tipp : oks) {
                assert !allSeotud.get(tipp).contains(vaba);
                allSeotud.get(tipp).add(vaba); // `vaba` ei ole enam vaba.
            }
            allSeotud.put(kvantor, new ArrayList<>()); // Kvantor on uus tipp, nii et
            allSeotud.get(kvantor).add(vaba);          // seda ei olnud listis `oks`.
        }

        // Nüüd on `lisamisteArv` uut argumenti ja nad on kõik seotud igas lehes ehk
        // valemis ei ole vabu indiviide.
    }
}