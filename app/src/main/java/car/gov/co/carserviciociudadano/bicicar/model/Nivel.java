package car.gov.co.carserviciociudadano.bicicar.model;

import car.gov.co.carserviciociudadano.common.ModelBase;

public class Nivel  extends ModelBase {
    public int IDNivel;
    public String Nombre;

    public  Nivel(int idNivel, String nombre){
        this.IDNivel = idNivel;
        this.Nombre = nombre;
    }

    @Override
    public String  toString(){
        return this.Nombre;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Nivel && IDNivel == ((Nivel) o).IDNivel );
    }
}
