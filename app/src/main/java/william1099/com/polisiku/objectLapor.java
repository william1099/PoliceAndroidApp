package william1099.com.polisiku;


public class objectLapor {
    public String judul, gambar, desc, tanggal, lokasi, pengirim, type;
    public double lat, lon;
    public objectLapor(String judul, String gambar, String desc, String tanggal, String lokasi, String pengirim, double lat, double lon, String type) {
        this.judul = judul;
        this.gambar = gambar;
        this.desc = desc;
        this.tanggal = tanggal;
        this.lokasi = lokasi;
        this.pengirim = pengirim;
        this.lat = lat;
        this.lon = lon;
        this.type = type;
    }
}
