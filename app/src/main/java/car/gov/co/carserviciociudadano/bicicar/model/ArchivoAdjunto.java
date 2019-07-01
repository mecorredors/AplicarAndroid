package car.gov.co.carserviciociudadano.bicicar.model;

import android.database.Cursor;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.common.ModelBase;

public class ArchivoAdjunto extends ModelBase {
    public int Id;
    public int IDArchivoAdjunto;
    public int IDEvento;
    public String Nombre;
    public String Descripcion;
    public boolean EsFotoEvento;
    public boolean PublicarWeb;
    public String Path;
    public String PathTemporal;
    public int Estado;

    public int Type;

    public static final String TABLE_NAME = "ArchivosAdjuntos";

    public static final String ID = "Id";
    public static final String ID_ARCHIVO_ADJUNTO ="IDArchivoAdjunto";
    public static final String ID_EVENTO ="IDEvento";
    public static final String NOMBRE ="Nombre";
    public static final String DESCRIPCION ="Descripcion";
    public static final String ES_FOTO_EVENTO ="EsFotoEvento";
    public static final String PUBLICAR_WEB ="PublicarWeb";
    public static final String PATH ="Path";
    public static final String PATH_TEMPORAL ="PathTemporal";
    public static final String ESTADO ="Estado";


    public ArchivoAdjunto(){
    }

    public ArchivoAdjunto(Cursor c) {
        if (c.getColumnIndex(ID) >= 0) this.Id = c.getInt(c.getColumnIndex(ID));
        if (c.getColumnIndex(ID_ARCHIVO_ADJUNTO) >= 0) this.IDArchivoAdjunto = c.getInt(c.getColumnIndex(ID_ARCHIVO_ADJUNTO));
        if (c.getColumnIndex(ID_EVENTO) >= 0) this.IDEvento = c.getInt(c.getColumnIndex(ID_EVENTO));
        if (c.getColumnIndex(NOMBRE) >= 0) this.Nombre = c.getString(c.getColumnIndex(NOMBRE));
        if (c.getColumnIndex(DESCRIPCION) >= 0) this.Descripcion = c.getString(c.getColumnIndex(DESCRIPCION));
        if (c.getColumnIndex(ES_FOTO_EVENTO) >= 0) this.EsFotoEvento = c.getInt(c.getColumnIndex(ES_FOTO_EVENTO)) == 1;
        if (c.getColumnIndex(PUBLICAR_WEB) >= 0) this.PublicarWeb = c.getInt(c.getColumnIndex(PUBLICAR_WEB)) == 1;
        if (c.getColumnIndex(PATH) >= 0) this.Path = c.getString(c.getColumnIndex(PATH));
        if (c.getColumnIndex(PATH_TEMPORAL) >= 0) this.PathTemporal = c.getString(c.getColumnIndex(PATH_TEMPORAL));
        if (c.getColumnIndex(ESTADO) >= 0) this.Estado = c.getInt(c.getColumnIndex(ESTADO));
        Type = Enumerator.TipoFoto.FOTO;
    }

    @Override
    public boolean equals(Object o) {
        return (o instanceof ArchivoAdjunto && Id == ((ArchivoAdjunto) o).Id );
    }

}
