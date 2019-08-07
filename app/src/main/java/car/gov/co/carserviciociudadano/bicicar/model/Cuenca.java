package car.gov.co.carserviciociudadano.bicicar.model;

import car.gov.co.carserviciociudadano.common.ModelBase;

public class Cuenca extends ModelBase {
    public int IDCuenca;
    public String Nombre;

    public Cuenca(int idCuenca, String nombre){
        this.IDCuenca = idCuenca;
        this.Nombre = nombre;
    }

    @Override
    public String  toString(){
        return this.Nombre;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Cuenca && IDCuenca == ((Cuenca) o).IDCuenca );
    }
}
