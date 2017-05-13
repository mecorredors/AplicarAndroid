package car.gov.co.carserviciociudadano.denunciaambiental.model;

/**
 * Created by Olger on 13/05/2017.
 */

public class Foto {

    private String Path;
    private int Estado;
    private int Type;

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
