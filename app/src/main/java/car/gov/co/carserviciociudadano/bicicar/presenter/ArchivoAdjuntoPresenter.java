package car.gov.co.carserviciociudadano.bicicar.presenter;

import android.os.AsyncTask;

import java.util.List;

import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.ImageUtil;
import car.gov.co.carserviciociudadano.bicicar.dataaccess.ArchivosAdjuntos;
import car.gov.co.carserviciociudadano.bicicar.model.ArchivoAdjunto;

public class ArchivoAdjuntoPresenter {
    private IViewArchivoAdjunto iViewArchivoAdjunto;
    private boolean mPublicando = false;
    public  ArchivoAdjuntoPresenter(IViewArchivoAdjunto iViewArchivoAdjunto){
        this.iViewArchivoAdjunto = iViewArchivoAdjunto;
    }

    public void setmPublicando(boolean mPublicando) {
        this.mPublicando = mPublicando;
    }


    public void publicarArchivosAdjuntos(int idEvento){
        List<ArchivoAdjunto> lstArchivosAdjuntos = new ArchivosAdjuntos().list(idEvento, Enumerator.Estado.PENDIENTE_PUBLICAR);
        publicarArchivosAdjuntos(lstArchivosAdjuntos);
    }

    private void publicarArchivosAdjuntos(List<ArchivoAdjunto> lstArchivosAdjuntos){
        if (mPublicando == false ){
            mPublicando = true;
            ArchivoAdjuntoPresenter.SubirArchivosAdjuntosAsincrona subirImagenesAsincrona = new ArchivoAdjuntoPresenter.SubirArchivosAdjuntosAsincrona(lstArchivosAdjuntos);
            subirImagenesAsincrona.execute();
        }else{
            iViewArchivoAdjunto.onErrorArchivoAdjunto("");
        }
    }



    class SubirArchivosAdjuntosAsincrona extends AsyncTask<Void, Integer, Boolean> {

        int numErrores = 0;
        List<ArchivoAdjunto> mLstArchivosAdjuntos;
        ArchivosAdjuntos mArchivosAdjuntos = new ArchivosAdjuntos();
        public  SubirArchivosAdjuntosAsincrona(List<ArchivoAdjunto> lstArchivosAdjuntos){
            this.mLstArchivosAdjuntos = lstArchivosAdjuntos;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int progress = 1;
            if (mLstArchivosAdjuntos != null && mLstArchivosAdjuntos.size() > 0) {
                for (ArchivoAdjunto imagen : mLstArchivosAdjuntos){

                    ImageUtil.Image imageScale = ImageUtil.scaledBitmap(imagen.Path, Enumerator.NAME_DIRECTORY_IMAGES,true,new ImageUtil.Size(1280,720));
                    if (imageScale != null && !imageScale.getPath().isEmpty()) {
                        imagen.Path = imageScale.getPath();
                        imagen.Nombre = imageScale.getNewName();
                    }

                    int statusCode = mArchivosAdjuntos.publicar(imagen);

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
                    for ( ArchivoAdjunto imagen : mLstArchivosAdjuntos){
                        if (imagen.PathTemporal  != null && !imagen.PathTemporal.equals("")){
                            count ++;
                            imagen.Estado = Enumerator.Estado.PUBLICADO;
                            mArchivosAdjuntos.update(imagen);
                        }
                    }
                    if (count == mLstArchivosAdjuntos.size()) {
                        iViewArchivoAdjunto.onSuccessArchivosAdjunto(mLstArchivosAdjuntos);

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
