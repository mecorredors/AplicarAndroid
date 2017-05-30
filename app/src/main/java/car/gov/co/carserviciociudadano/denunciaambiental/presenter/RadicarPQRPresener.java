package car.gov.co.carserviciociudadano.denunciaambiental.presenter;

import android.os.AsyncTask;
import java.util.ArrayList;
import java.util.List;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.ImageUtil;
import car.gov.co.carserviciociudadano.denunciaambiental.dataacces.RadicarPQR;
import car.gov.co.carserviciociudadano.denunciaambiental.interfaces.IRadicarPQR;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Foto;
import car.gov.co.carserviciociudadano.denunciaambiental.model.Denuncia;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;

/**
 * Created by Olger on 27/05/2017.
 */

public class RadicarPQRPresener {
    private IViewRadicarPQR viewRadicarPQR;
    private boolean mPublicando = false;
    public  RadicarPQRPresener(IViewRadicarPQR iViewRadicarPQR){
        this.viewRadicarPQR = iViewRadicarPQR;
    }

    public void setmPublicando(boolean mPublicando) {
        this.mPublicando = mPublicando;
    }

    public void publicarImagenes(List<Foto> lstArchivosAdjuntos, String usuario){
        if (mPublicando == false ){
            mPublicando = true;
            SubirImagenesAsincrona subirImagenesAsincrona = new SubirImagenesAsincrona(lstArchivosAdjuntos,usuario);
            subirImagenesAsincrona.execute();
        }else{
            viewRadicarPQR.onErrorImages("");
        }
    }

    public void radicarPQR(Denuncia denuncia){
        new  RadicarPQR().insert(denuncia, new IRadicarPQR() {
            @Override
            public void onSuccess(Denuncia denuncia) {
                viewRadicarPQR.onSuccessRadicarPQR(denuncia);
            }

            @Override
            public void onError(ErrorApi errorApi) {
                viewRadicarPQR.onErrorRadicarPQR(errorApi);
            }
        });
    }


    class SubirImagenesAsincrona extends AsyncTask<Void, Integer, Boolean> {

        int numErrores = 0;
        String usuario;
        List<Foto> mLstImagenes;
        RadicarPQR radicarPQR = new RadicarPQR();
        public  SubirImagenesAsincrona(List<Foto> lstImagenes, String usuario){
            this.mLstImagenes = lstImagenes;
            this.usuario = usuario;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            int progress = 1;
            if (mLstImagenes != null && mLstImagenes.size() > 0) {
                for (Foto imagen : mLstImagenes){

                    ImageUtil.Image imageScale = ImageUtil.scaledBitmap(imagen.getPath(), Enumerator.NAME_DIRECTORY_IMAGES,true);
                    if (imageScale != null && !imageScale.getPath().isEmpty()) {
                        imagen.setDirLocal(imageScale.getPath());
                        imagen.setNombre(imageScale.getNewName());
                        imagen.setDescripcion(imageScale.getName());
                        imagen.setSize(String.valueOf(imageScale.getSize()));
                    }

                    int statusCode = radicarPQR.publicarImagenTemporal(imagen,usuario);

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
                    List<String> pathImages = new ArrayList<>();
                    int count = 0;
                    for ( Foto imagen : mLstImagenes){
                        if (imagen.getDirLocal() != null && !imagen.getDirLocal().equals("")){
                            count ++;
                        }
                    }
                    if (count == mLstImagenes.size()) {
                        viewRadicarPQR.onSuccessImages(mLstImagenes);

                    }else{
                        viewRadicarPQR.onErrorImages("Hubo un inconveniente al enviar la denuncia, vuelva intentar en unos minutos");
                    }
                }else{
                    viewRadicarPQR.onErrorImages("Hubo un inconveniente al enviar la denuncia, vuelva intentar en unos minutos");
                }
            }
        }

        @Override
        protected void onCancelled() {
            viewRadicarPQR.onErrorImages("");
            mPublicando = false;
        }
    }
}
