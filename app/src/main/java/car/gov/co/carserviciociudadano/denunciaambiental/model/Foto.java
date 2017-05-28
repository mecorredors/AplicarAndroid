package car.gov.co.carserviciociudadano.denunciaambiental.model;

/**
 * Created by Olger on 13/05/2017.
 */

public class Foto {
    private String Descripcion;
    private String DirLocal;
    private String Nombre;
    private String Size;

    private String Path;
    private int Estado;
    private int Type;


    public String getDescripcion() {
        return Descripcion;
    }

    public void setDescripcion(String descripcion) {
        Descripcion = descripcion;
    }

    public String getDirLocal() {
        return DirLocal;
    }

    public void setDirLocal(String dirLocal) {
        DirLocal = dirLocal;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getSize() {
        return Size;
    }

    public void setSize(String size) {
        Size = size;
    }


    public String getPath() {
        return Path;
    }

    public void setPath(String path) {
        Path = path;
    }

    public int getEstado() {
        return Estado;
    }

    public void setEstado(int estado) {
        Estado = estado;
    }

    public int getType() {
        return Type;
    }

    public void setType(int type) {
        Type = type;
    }
}
