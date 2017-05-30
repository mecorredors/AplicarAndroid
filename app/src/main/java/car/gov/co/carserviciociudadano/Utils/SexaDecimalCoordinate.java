package car.gov.co.carserviciociudadano.Utils;

/**
 * Created by Olger on 28/05/2017.
 */

import java.math.BigDecimal;

/**
 * Created by Olger on 17/09/2016.
 */
public class SexaDecimalCoordinate {

    double _valorRefRadianLongitud = -1.292896415;//ʎo
    double _valorRefRadianLatitud = 0.08021883;//ɸo
    double _valorRef_a = 6378137; //Elipsoide GRS80
    double _valorRef_b = 6356752.31414; //Elipsoide GRS80
    double _valorRef_e2 = 0.0066943800229; //Elipsoide GRS80
    double _valorRef_er2 = 0.00673949677548; //Elipsoide GRS80

    double _latitudSexa;
    double _longitudSexa;

    double _latitudRadianes; //ɸp
    double _longitudRadianes;

    double _coordenadaPlanaNorte;
    double _coordenadaPlanaEste;

    double _coorPlanaNorteFinal;
    double _coorPlanaEsteFinal;

    public double get_coorPlanaEsteFinal() {
        return _coorPlanaEsteFinal;
    }

    public double get_coorPlanaNorteFinal() {
        return _coorPlanaNorteFinal;
    }

    public SexaDecimalCoordinate(double latitud, double longitud)
    {
        this._latitudSexa = latitud;
        this._longitudSexa = longitud;
    }

    private void CalcularCoordRadianes()
    {
        this._longitudRadianes = (this._longitudSexa * Math.PI) / 180; //K14*PI()/180
        this._latitudRadianes = (this._latitudSexa * Math.PI) / 180; //K15*PI()/180
    }


    /**
     * Convertir coordenadas SEXA a coordenadas planas
     */
    public void ConvertToFlatCoordinate()
    {
        //1. Obtener Coordenadas en Radianes
        this.CalcularCoordRadianes();

        //2. Calcular punto norte
        this.CalcularCoordenadas();
    }

    private void CalcularCoordenadas()
    {
        double L = 0, t = 0, n2 = 0, n = 0, N = 0, alfa = 0, beta = 0, y = 0, sigma = 0, varepsilon = 0, G_latRad = 0, G_latRef = 0, G_difLat = 0, t1 = 0, t2 = 0, t3 = 0, t4 = 0;
        double valConstante15 = 15, valConstante16 = 16, valConstante32 = 32, valConstante4 = 4, valConstante3 = 3, valConstante2 = 2, valConstante9 = 9;
        double valConstante35 = 35, valConstante48 = 48, valConstante105 = 105, valConstante256 = 256, valConstante315 = 315, valConstante512 = 512, valConstante24 = 24;
        double valConstante6 = 6, valConstante720 = 720, valConstante40320 = 40320;
        double valConstante5040 = 5040, valConstante120 = 120;

        L = this._longitudRadianes - this._valorRefRadianLongitud; //Ɩ =ʎp-ʎo
        t = Math.tan(this._latitudRadianes); //t=tan(ɸp)
        n2 = this._valorRef_er2 * Math.pow(Math.cos(this._latitudRadianes), 2);  //ɳ²=e'²*cos²(ɸp)
        n = (this._valorRef_a - this._valorRef_b) / (this._valorRef_a + this._valorRef_b);//n=(a-b)/(a+b)
        N = this._valorRef_a / Math.sqrt(1- this._valorRef_e2 * Math.pow(Math.sin(this._latitudRadianes), 2)); //N=a/√(1-e²*sin²(ɸp))
        alfa = ((this._valorRef_a + this._valorRef_b) / 2) * (1 + 1 / 4 * Math.pow(n, 2) + 1 / 64 * Math.pow(n, 4)); //α=((a+b)/2)*(1+1/4*n²+1/64*n⁴)
        beta = (-valConstante3 / valConstante2 * n + valConstante9 / valConstante16 * Math.pow(n, 3) - valConstante3 / valConstante32 * Math.pow(n, 5)); //β=-3/2*n+9/16*nᶟ-3/32*n⁵     ***PENDIENTE
        y = (valConstante15 / valConstante16) * Math.pow(n, 2) - (valConstante15 / valConstante32) * Math.pow(n, valConstante4); //γ=15/16*n²-15/32*n⁴
        sigma = -valConstante35 / valConstante48 * Math.pow(n, 3) + valConstante105 / valConstante256 * Math.pow(n, 5);  //δ=-35/48*nᶟ+105/256*n⁵
        varepsilon = valConstante315 / valConstante512 * Math.pow(n, 4);  //ε=315/512*n⁴
        G_latRad = alfa * (this._latitudRadianes + beta * Math.sin(2 * this._latitudRadianes) + y * Math.sin(4 * this._latitudRadianes) + sigma * Math.sin(6 * this._latitudRadianes) + varepsilon * Math.sin(8 * this._latitudRadianes)); //G(ɸp)=α(ɸp+β*sin(2ɸp)+γ*sin(4ɸp)+δ*sin(6ɸp)+ε*sin(8ɸp))
        G_latRef = alfa * (this._valorRefRadianLatitud + beta * Math.sin(2 * this._valorRefRadianLatitud) + y * Math.sin(4 * this._valorRefRadianLatitud) + sigma * Math.sin(6 * this._valorRefRadianLatitud) + varepsilon * Math.sin(8 * this._valorRefRadianLatitud));//G(ɸo)=α(ɸo+β*sin(2ɸo)+γ*sin(4ɸo)+δ*sin(6ɸo)+ε*sin(8ɸo))

        G_difLat = G_latRad - G_latRef; //G(ɸp)-G(ɸo)
        t1 = t / valConstante2 * N * Math.pow(L, 2) * Math.pow(Math.cos(_latitudRadianes), 2);  //t/2*N*Ɩ²*cos²(ɸp)
        t2 = t / valConstante24  * N * Math.pow(Math.cos(_latitudRadianes), 4) * (5 - Math.pow(t,2) + 9 * n + 4 * Math.pow(n,4)) * Math.pow(L, 4);//t/24*cos⁴(ɸp)(5-t²+9ɳ²+4ɳ⁴)Ɩ⁴
        t3 = t / valConstante720 * N * Math.pow(Math.cos(_latitudRadianes), 6) * (61 - 58 * Math.pow(t, 2) + Math.pow(t, 4) + 270 *  n2 - 330 * Math.pow(t, 2) * n2)* Math.pow(L, 6);//t/720*cos⁶(ɸp)(61-58t²+t⁴+270ɳ²-330t²ɳ²)Ɩ⁶
        t4 = t / valConstante40320 * N * Math.pow(Math.cos(_valorRefRadianLatitud), 8) * (1385 - 3111 * Math.pow(t, 2) + 543 * Math.pow(t, 4) - Math.pow(t, 6)) * Math.pow(L, 8); //t/40320*cos⁸(ɸp)(1385-3111t²+543t⁴-t⁶)Ɩ⁸

        this._coordenadaPlanaNorte = 1000000 + G_difLat + t1 + t2 + t3 + t4; // 1000000 + O13 + O14 + O15 + O16 + O17;
        this._coorPlanaNorteFinal = truncate(_coordenadaPlanaNorte);

        double eque1 = 0, eque2 = 0, eque3 = 0, eque4 = 0;

        eque1 = N * L * Math.cos(this._latitudRadianes); //N*Ɩ*cos²(ɸp)
        eque2 = 1 / valConstante6 * N * Math.pow(Math.cos(this._latitudRadianes), 3) * (1 - Math.pow(t, 2) + n2) * Math.pow(L, 3);//1/6*N*cosᶟ(ɸp)(1-t²+ɳ²)Ɩᶟ
        eque3 = 1 / valConstante120 * N * Math.pow(Math.cos(this._latitudRadianes), 5) * (5 - 18 * Math.pow(t, 2) + Math.pow(t, 4) + 14 * n2 - 58 * Math.pow(t, 2) * n2) * Math.pow(L, 5); //1/120*N*cos⁵(ɸp)(5-18*t²+t⁴+14*ɳ²-58*t²ɳ²)Ɩ⁵
        eque4 = 1 / valConstante5040 * N * Math.pow(Math.cos(this._latitudRadianes), 7) * (61 - 479 * Math.pow(t, 2) + 179 * Math.pow(t, 4) - Math.pow(t, 6)) * Math.pow(L, 7);  //1/5040*N*cos⁷(ɸp)(61-479*t²-179*t⁴-t⁶)Ɩ⁷

        this._coordenadaPlanaEste = 1000000 + eque1 + eque2 + eque3 + eque4;  //1000000 + O19 + O20 + O21 + O22;
        this._coorPlanaEsteFinal = truncate(_coordenadaPlanaEste);
    }

    public  double truncate(double d) {
        BigDecimal bd = new BigDecimal(d);
        bd = bd.setScale(7, BigDecimal.ROUND_HALF_UP);
        return bd.doubleValue();
      //  return bd.toString();
    }
}