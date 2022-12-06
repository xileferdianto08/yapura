package id.ac.umn.yapura;

public class barangList {
    int id, maxQty;
    String nama, description, foto;

    public barangList(){}
    public barangList(int id, int maxQty, String nama, String description, String foto) {
        this.id = id;
        this.maxQty = maxQty;
        this.nama = nama;
        this.description = description;
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaxQty() {
        return maxQty;
    }

    public void setMaxQty(int maxQty) {
        this.maxQty = maxQty;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }
}
