package william1099.com.polisiku.Model;

/**
 * Created by W I N D O W S   8 on 10/03/2018.
 */

public class CMarker {
    public String nama;
    public String jalan;
    public int idx;
    public String placeID;
    public CMarker(String nama, String jalan, int idx, String id) {
        this.jalan = jalan;
        this.nama = nama;
        this.idx = idx;
        placeID = id;
    }

    public String getNama() {
        return nama;
    }

    public String getJalan() {
        return jalan;
    }

    public int getIdx() { return idx; }

    public String getPlaceID() { return placeID; }
}
