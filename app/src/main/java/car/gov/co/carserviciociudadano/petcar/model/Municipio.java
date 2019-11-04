package car.gov.co.carserviciociudadano.petcar.model;

import car.gov.co.carserviciociudadano.bicicar.model.Colegio;

/**
 * Created by apple on 23/09/18.
 */

public class Municipio {
    public  String Nombre;
    public  String ID;
    public  int Count;


    public Municipio(String id, String nommbre){
        this.ID = id;
        this.Nombre = nommbre;
    }

    @Override
    public String toString(){
        return  "("+ ID + ") " + Nombre;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof Municipio && ID == ((Municipio) o).ID );
    }
}
