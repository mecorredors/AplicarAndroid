package car.gov.co.carserviciociudadano.petcar.presenter;

import android.os.AsyncTask;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.ImageUtil;
import car.gov.co.carserviciociudadano.petcar.dataaccess.AdjuntosPetCar;
import car.gov.co.carserviciociudadano.petcar.dataaccess.Gestores;
import car.gov.co.carserviciociudadano.petcar.interfaces.IViewAdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.AdjuntoPetCar;
import car.gov.co.carserviciociudadano.petcar.model.Gestor;
import car.gov.co.carserviciociudadano.petcar.model.MaterialRecogido;

public class AdjuntoPetCarPresenter {
    private IViewAdjuntoPetCar iViewArchivoAdjunto;
    private boolean mPublicando = false;
    private  MaterialRecogido mMaterialRecogido;
    private Gestor mGestor;
    public AdjuntoPetCarPresenter(IViewAdjuntoPetCar iViewArchivoAdjunto){
        this.iViewArchivoAdjunto = iViewArchivoAdjunto;
    }

    public void setmPublicando(boolean mPublicando) {
        this.mPublicando = mPublicando;
    }


    public void publicarArchivosAdjuntos(MaterialRecogido materialRecogido){
        mMaterialRecogido = materialRecogido;
        mGestor = new Gestores().getLogin();
        if (mGestor == null){
            iViewArchivoAdjunto.onErrorArchivoAdjunto("No se encontró gestor, inicie sesión");
            return;
        }

        List<AdjuntoPetCar> lstArchivosAdjuntos = new AdjuntosPetCar().list(mMaterialRecogido.Id, Enumerator.Estado.PENDIENTE_PUBLICAR);
        publicarArchivosAdjuntos(lstArchivosAdjuntos);
    }

    private void publicarArchivosAdjuntos(List<AdjuntoPetCar> lstArchivosAdjuntos){
        if (mPublicando == false ){
            mPublicando = true;
            AdjuntoPetCarPresenter.SubirArchivosAdjuntosAsincrona subirImagenesAsincrona = new AdjuntoPetCarPresenter.SubirArchivosAdjuntosAsincrona(lstArchivosAdjuntos);
            subirImagenesAsincrona.execute();
        }else{
            iViewArchivoAdjunto.onErrorArchivoAdjunto("");
        }
    }



    class SubirArchivosAdjuntosAsincrona extends AsyncTask<Void, Integer, Boolean> {

        int numErrores = 0;
        List<AdjuntoPetCar> mLstArchivosAdjuntos;
        AdjuntosPetCar mArchivosAdjuntos = new AdjuntosPetCar();
        public  SubirArchivosAdjuntosAsincrona(List<AdjuntoPetCar> lstArchivosAdjuntos){
            this.mLstArchivosAdjuntos = lstArchivosAdjuntos;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int progress = 1;
            if (mLstArchivosAdjuntos != null && mLstArchivosAdjuntos.size() > 0) {
                for (AdjuntoPetCar imagen : mLstArchivosAdjuntos){

                    ImageUtil.Image imageScale = ImageUtil.scaledBitmap(imagen.Path, Enumerator.NAME_DIRECTORY_IMAGES,true,new ImageUtil.Size(1280,720));
                    if (imageScale != null && !imageScale.getPath().isEmpty()) {
                        imagen.Path = imageScale.getPath();
                        //imagen. = imageScale.getNewName();
                    }

                    int statusCode = mArchivosAdjuntos.publicar(imagen, mMaterialRecogido.IDMaterialRecogido, mGestor.Identificacion);

                    if (statusCode != 200){
                        numErrores++;
                        break;
                    }
                    progress++;
                    publishProgress(progress);
                    if (isCancelled())
                        break;
                }
            }
            return (numErrores == 0);
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            int progreso = values[0].intValue();
            // if (mProgressDialog != null) mProgressDialog.setProgress(progreso);
        }

        @Override
        protected void onPreExecute() {
            //  if (mProgressDialog != null) mProgressDialog.setMessage("Publicando imagenes");
        }

        @Override
        protected void onPostExecute(final Boolean result) {

            if (mPublicando) {
                mPublicando = false;
                if (result) {
                    int count = 0;
                    for ( AdjuntoPetCar imagen : mLstArchivosAdjuntos){
                        if (imagen.PathTemporal  != null && !imagen.PathTemporal.equals("")){
                            count ++;
                            imagen.Estado = Enumerator.Estado.PUBLICADO;
                            mArchivosAdjuntos.update(imagen);
                        }
                    }
                    if (count == mLstArchivosAdjuntos.size()) {
                        iViewArchivoAdjunto.onSuccessArchivosAdjunto(mMaterialRecogido);

                    }else{
                        iViewArchivoAdjunto.onErrorArchivoAdjunto("Hubo un inconveniente al publicar todas las fotos, vuelva intentar en unos minutos");
                    }
                }else{
                    iViewArchivoAdjunto.onErrorArchivoAdjunto("Hubo un inconveniente al publicar todas las fotos, vuelva intentar en unos minutos");
                }
            }
        }

        @Override
        protected void onCancelled() {
            iViewArchivoAdjunto.onErrorArchivoAdjunto("");
            mPublicando = false;
        }
    }
}
