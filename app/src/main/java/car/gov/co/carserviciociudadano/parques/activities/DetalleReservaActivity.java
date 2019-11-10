package car.gov.co.carserviciociudadano.parques.activities;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Environment;

import com.google.android.material.snackbar.Snackbar;
import androidx.core.content.ContextCompat;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import car.gov.co.carserviciociudadano.BuildConfig;
import car.gov.co.carserviciociudadano.R;
import car.gov.co.carserviciociudadano.Utils.Enumerator;
import car.gov.co.carserviciociudadano.Utils.Utils;
import car.gov.co.carserviciociudadano.common.BaseActivity;
import car.gov.co.carserviciociudadano.parques.adapter.AbonosAdapter;
import car.gov.co.carserviciociudadano.parques.presenter.ParquePresenter;
import car.gov.co.carserviciociudadano.parques.presenter.IViewParque;
import car.gov.co.carserviciociudadano.parques.dataaccess.Abonos;
import car.gov.co.carserviciociudadano.parques.dataaccess.DetalleReservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Reservas;
import car.gov.co.carserviciociudadano.parques.dataaccess.Usuarios;
import car.gov.co.carserviciociudadano.parques.interfaces.IAbono;
import car.gov.co.carserviciociudadano.parques.interfaces.IReserva;
import car.gov.co.carserviciociudadano.parques.model.Abono;
import car.gov.co.carserviciociudadano.parques.model.DetalleReserva;
import car.gov.co.carserviciociudadano.parques.model.ErrorApi;
import car.gov.co.carserviciociudadano.parques.model.Parque;
import car.gov.co.carserviciociudadano.parques.model.ServicioReserva;
import car.gov.co.carserviciociudadano.parques.model.Usuario;

public class DetalleReservaActivity extends BaseActivity {

    @BindView(R.id.lblTitulo)  TextView mLblTitulo;
    @BindView(R.id.lblFecha)  TextView mLblFecha;
    @BindView(R.id.lblTotal)  TextView mLblTotal;
    @BindView(R.id.lblEstado)  TextView mLblEstado;
    @BindView(R.id.imageView)   ImageView mImageView;
    @BindView(R.id.lblServicio)  TextView mLblServicio;
    @BindView(R.id.lblNroNoches)  TextView mLblNroNoches;
    @BindView(R.id.lblPrecio)  TextView mLblPrecio;
    @BindView(R.id.lblFechaDesde)  TextView mLblFechaDesde;
    @BindView(R.id.lblFechaHasta)  TextView mLblFechaHasta;
    @BindView(R.id.lblNroReserva)  TextView mLblNroReserva;
    @BindView(R.id.btnCancelarReserva)  Button mBtnCancelarReserva;
    @BindView(R.id.btnGenerarTiquete)  Button mBtnGenerarTiquete;
    @BindView(R.id.btnEnviarConsignacion)  Button mBtnEnviarConsignacion;
    @BindView(R.id.activity_detalle_reserva)  View mActivity_detalle_reserva;
    @BindView(R.id.recycler_view)  RecyclerView mRecyclerView;
    @BindView(R.id.progressView)  ProgressBar mProgressView;
    @BindView(R.id.lblAbonos)  TextView mLblAbonos;
    @BindView(R.id.lyPagoElectronico)  View mLyPagoElectronico;
    @BindView(R.id.btnPagoElectronico)    ImageButton mBtnPagoElectronico;
    @BindView(R.id.lblEnviarConsignacion)    TextView mLblEnviarConsignacion;

    private FirebaseAnalytics mFirebaseAnalytics;
    private DetalleReserva mDetalleReserva;
    ProgressDialog mProgressDialog;
    AbonosAdapter mAdaptador;
    List<Abono> mLstAbonos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_reserva);
        ButterKnife.bind(this);
        ActionBar bar = getSupportActionBar();
        bar.setDisplayHomeAsUpEnabled(true);

        mDetalleReserva = (DetalleReserva) IntentHelper.getObjectForKey(DetalleReservas.TAG);
        if (mDetalleReserva != null) {
            mImageView.setVisibility(View.GONE);
            mLblTitulo.setText(mDetalleReserva.getNombreParque());
            mLblEstado.setText(mDetalleReserva.getEstadoNombre());
            mLblFecha.setText(Utils.toStringLargeFromDate(mDetalleReserva.getFechaSistemaReserva()));
            mLblTotal.setText(Utils.formatoMoney(mDetalleReserva.getTotalValorReserva()));
            mLblFechaDesde.setText(Utils.toStringLargeFromDate(mDetalleReserva.getFechaInicialReserva()));
            mLblFechaHasta.setText(Utils.toStringLargeFromDate(mDetalleReserva.getFechaFinalReserva()));
            mLblServicio.setText(mDetalleReserva.getNombreServicio());
            mLblNroNoches.setText(String.valueOf(mDetalleReserva.getCantidadReserva()));
            mLblPrecio.setText(Utils.formatoMoney(mDetalleReserva.getPrecioReserva()));
            mLblNroReserva.setText(String.valueOf(mDetalleReserva.getIDReserva()));

            configurarControles();

            mRecyclerView.setHasFixedSize(true);

            mLstAbonos = new ArrayList<>();
            mAdaptador = new AbonosAdapter(mLstAbonos);

            mRecyclerView.setAdapter(mAdaptador);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            mRecyclerView.setItemAnimator(new DefaultItemAnimator());

            loadParques();
            loadAbonos();

            if (BuildConfig.DEBUG == false) {
                mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
                Bundle bundle = new Bundle();
                bundle.putString(FirebaseAnalytics.Param.ITEM_ID, String.valueOf(mDetalleReserva.getNombreServicio()));
                bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "Detalle reserva");
                bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, Enumerator.ContentTypeAnalitic.PARQUES);
                mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);
            }

        }else{
            finish();
        }
    }
    private void configurarControles(){
        mLblEstado.setText(mDetalleReserva.getEstadoNombre());
        if (mDetalleReserva.getEstadoReserva() == 0){
            mBtnCancelarReserva.setVisibility(View.VISIBLE);
            mBtnGenerarTiquete.setVisibility(View.GONE);
            mBtnEnviarConsignacion.setVisibility(View.VISIBLE);
            mLyPagoElectronico.setVisibility(View.VISIBLE);
            mLblEnviarConsignacion.setVisibility(View.VISIBLE);
        }else{
            mBtnCancelarReserva.setVisibility(View.GONE);
            mBtnGenerarTiquete.setVisibility(View.GONE);
            mBtnEnviarConsignacion.setVisibility(View.GONE);
            mLyPagoElectronico.setVisibility(View.GONE);
            mLblEnviarConsignacion.setVisibility(View.GONE);
            if (mDetalleReserva.getEstadoReserva() == 1){
                mBtnGenerarTiquete.setVisibility(View.VISIBLE);
            }
        }

    }

    private void  loadAbonos(){
        Abonos abonos = new Abonos();
        showProgress(mProgressView,true);
        abonos.list(mDetalleReserva.getIDReserva(), new IAbono() {
            @Override
            public void onSuccess(List<Abono> lstAbono) {
                showProgress(mProgressView,false);
                mLstAbonos.clear();
                mLstAbonos.addAll(lstAbono);
                mAdaptador.notifyDataSetChanged();
                mLblAbonos.setVisibility(View.VISIBLE);
            }
            @Override
            public void onSuccess() {
                showProgress(mProgressView,false);
            }
            @Override
            public void onError(ErrorApi error) {
                showProgress(mProgressView,false);
            }
        });
    }

    private void loadParques(){
        ParquePresenter parques = new ParquePresenter(iViewParque);
        parques.list();

    }
    IViewParque iViewParque = new IViewParque() {
        @Override
        public void onSuccess(List<Parque> lstParques) {
            for(Parque item: lstParques){
                if (item.getNombreParque().trim().equals(mDetalleReserva.getNombreParque().replace("PARQUE","").trim())){
                    mLblEnviarConsignacion.setText(item.getDetalleCuenta() + " " + getString(R.string.envie_consignacion_des));
                }
            }
        }

        @Override
        public void onError(ErrorApi error) {
            Snackbar.make(mRecyclerView, error.getMessage(), Snackbar.LENGTH_INDEFINITE)
                    //.setActionTextColor(Color.CYAN)
                    .setActionTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green) )
                    .setAction("REINTENTAR", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            loadParques();
                        }
                    })
                    .show();

        }
    };

    @OnClick(R.id.btnEnviarConsignacion) void onEnviarConsignacion() {
        Intent i = new Intent(this,AbonoActivity.class);
        i.putExtra(DetalleReserva.ID_RESERVA,mDetalleReserva.getIDReserva());
        i.putExtra(DetalleReserva.VALOR_TOTAL,mDetalleReserva.getTotalValorReserva());

        startActivityForResult(i,0);
    }

    @OnClick(R.id.btnPagoElectronico) void onPagoElectronico() {
       Intent i = new Intent(this,PagoElectronicoActivity.class);
        startActivity(i);
    }

    @OnClick(R.id.btnCancelarReserva) void onCancelarReserva() {
        cancelarReservaDialog();
    }

    @OnClick(R.id.btnGenerarTiquete) void onGenerarTiquete() {
       generarTiquetePDF();
    }

    @OnClick(R.id.btnAyudaPago) void onAyudaPag() {
        Intent i = new Intent(this,AyudaPagoActivity.class);
        startActivity(i);
    }
    private void generarTiquetePDF(){
        Document doc=new Document();
       // String outpath= Environment.getExternalStorageDirectory()+"/ReservaParque_"+ mDetalleReserva.getIDReserva()+".pdf";
        String outpath=   Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/ReservaParque_"+ mDetalleReserva.getIDReserva()+".pdf";

        try {

            PdfWriter writer = PdfWriter.getInstance(doc, new FileOutputStream(outpath));
            doc.open();

            Paragraph paragraph = new Paragraph();
           paragraph.setAlignment(Element.ALIGN_CENTER);
//            paragraph.add("CORPORACIÓN AUTONOMA REGIONAL");
//            doc.add(new Paragraph(mDetalleReserva.getNombreParque()));

            Font smallfont = new Font(Font.FontFamily.HELVETICA, 12);

            PdfPTable table = new PdfPTable(3);
            table.setTotalWidth(new float[]{ 55, 140,100 });
            table.setLockedWidth(true);
            PdfContentByte cb = writer.getDirectContent();
            // first row
            PdfPCell cell = new PdfPCell(new Phrase(" CORPORACION AUTONOMA REGIONAL \n NIT 899.999.062-6 \n\n IVA Reg común-IVA incluido"));
            cell.setFixedHeight(80);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(3);
            table.addCell(cell);

            String reserva = "  Reserva Nro."+mDetalleReserva.getIDReserva() +"     "+ Utils.toStringLargeFromDate(mDetalleReserva.getFechaSistemaReserva());

            cell = new PdfPCell(new Phrase(reserva));
            cell.setFixedHeight(30);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            cell.setColspan(3);
            table.addCell(cell);

            //  row 1
            cell = new PdfPCell(new Phrase("Nro noches", smallfont));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Descripción", smallfont));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Precio noche", smallfont));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            ///row 2

            cell = new PdfPCell(new Phrase(String.valueOf(mDetalleReserva.getCantidadReserva()), smallfont));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);


            String descripcion = mDetalleReserva.getNombreServicio() +" Desde "+ Utils.toStringLargeFromDate(mDetalleReserva.getFechaInicialReserva()) +
                    " Hasta "+ Utils.toStringLargeFromDate(mDetalleReserva.getFechaFinalReserva());

            cell = new PdfPCell(new Phrase(descripcion, smallfont));
            cell.setFixedHeight(30);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase(Utils.formatoMoney(mDetalleReserva.getPrecioReserva()), smallfont));
            cell.setFixedHeight(70);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setBorder(Rectangle.BOX);
            table.addCell(cell);

            //row total

            double iva = calculoIVA(mDetalleReserva.getTotalValorReserva(),mDetalleReserva.getImpuestoReserva());
            double subTotal = mDetalleReserva.getTotalValorReserva() - iva;

            cell = new PdfPCell(new Phrase("Sin IVA "));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(Utils.formatoMoney(subTotal)));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(1);
            table.addCell(cell);

            cell = new PdfPCell(new Phrase("Base IVA "));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(Utils.formatoMoney(iva)));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(1);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase("Total  "));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.LEFT);
            cell.setHorizontalAlignment(Element.ALIGN_RIGHT);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(2);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(Utils.formatoMoney(mDetalleReserva.getTotalValorReserva())));
            cell.setFixedHeight(20);
            cell.setBorder(Rectangle.RIGHT);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(1);
            table.addCell(cell);


            cell = new PdfPCell(new Phrase(" Conserve su tiquete en el parque\n Debe presentar este tiquete al llegar al parque  ",smallfont));
            cell.setFixedHeight(40);
            cell.setBorder(Rectangle.BOX);
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            cell.setVerticalAlignment(Element.ALIGN_MIDDLE);
            cell.setColspan(3);
            table.addCell(cell);

            doc.add(table);

            doc.close();

            File file = new File(outpath);
            openPDF(file);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (DocumentException e) {
            e.printStackTrace();
        }

    }

    private double calculoIVA(double total, double iva)
    {
        double valorBase = 0;
        double valorTempIVA = 0;
        if (iva > 0)
        {
            valorTempIVA = (iva / 100) + 1;
            valorBase = total / valorTempIVA;
            iva = total - valorBase;
        }

        return Math.round(iva);
    }

    private void openPDF(final File file){

        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleReservaActivity.this);

        builder.setMessage("El tiquete se ha guardado en tu carpeta de Descargas con el nombre "+file.getName());

        builder.setPositiveButton("Ver tiquete", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(Uri.fromFile(file), "application/pdf");
                intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                try {
                    startActivity(intent);
                }catch (ActivityNotFoundException ex){
                    Log.d("OpenPDF", ex.toString());
                    Crashlytics.logException(ex);
                }catch (Exception ex){
                    Log.d("OpenPDF", ex.toString());
                    Crashlytics.logException(ex);
                }
            }
        });

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();


    }

    private void cancelarReservaDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DetalleReservaActivity.this);

        builder.setMessage("¿Desea cancelar la reserva?");

        builder.setPositiveButton("Cancelar Reserva", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                cancelarReserva();
            }
        });

        builder.setNegativeButton("Cerrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();
    }


    private void cancelarReserva(){
        Usuario usuario = new Usuarios().leer();

        Reservas reservas = new Reservas();
        ServicioReserva servicioReserva = new ServicioReserva();
        servicioReserva.setIDReserva(mDetalleReserva.getIDReserva());
        servicioReserva.setLogin(usuario.getLogin());
        servicioReserva.setClaveUsuario(usuario.getClaveUsuario());
        mostrarProgressDialog();
        reservas.cancelarReserva(servicioReserva, new IReserva() {
            @Override
            public void onSuccess(ServicioReserva servicioReserva) {
                if (mProgressDialog!=null) mProgressDialog.dismiss();
                mDetalleReserva.setEstadoReserva(3);
                mDetalleReserva.setEstadoNombre("Anulada");
                configurarControles();
                if (mLstAbonos.size() > 0)
                    mostrarMensajeDialog(getString(R.string.mensaje_cancelar_reserva));
                else
                    mostrarMensaje("La reserva fue cancelada ",mActivity_detalle_reserva);
            }

            @Override
            public void onSuccess(boolean res) {

            }

            @Override
            public void onError(ErrorApi error) {
                if (mProgressDialog!=null) mProgressDialog.dismiss();
                mostrarMensaje(error.getMessage(),mActivity_detalle_reserva);
            }
        });
    }

    private void mostrarProgressDialog(){
        mProgressDialog = new ProgressDialog(this);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setMessage("Espere...");
        mProgressDialog.setCancelable(false);
        mProgressDialog.show();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK){
            loadAbonos();
        }
    }
}
