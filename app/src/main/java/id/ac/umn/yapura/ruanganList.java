package id.ac.umn.yapura;

public class ruanganList {
    int id, maxCapacity;
    String nama, description, foto;

    public ruanganList(){}
    public ruanganList(int id, int maxCapacity, String nama, String description, String foto){
        this.id = id;
        this.maxCapacity = maxCapacity;
        this.nama = nama;
        this.description = description;
        this.foto = foto;

    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMaxCapacity(int maxCapacity) {
        this.maxCapacity = maxCapacity;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public int getId() {
        return id;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public String getNama() {
        return nama;
    }

    public String getDescription() {
        return description;
    }

    public String getFoto() {
        return foto;
    }
}
