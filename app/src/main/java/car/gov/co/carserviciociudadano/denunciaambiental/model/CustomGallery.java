package car.gov.co.carserviciociudadano.denunciaambiental.model;

/**
 * Created by Olger on 13/05/2017.
 */

import android.os.Parcel;
import android.os.Parcelable;

public class CustomGallery implements Parcelable {

    public String sdcardPath;
    public boolean isSeleted = false;
    public boolean isButton=false;
    public int idBitmap;

    @Override
    public int describeContents() {
        return 0;
    }

    public CustomGallery(){
    }
    public CustomGallery(Parcel in) {
        this.sdcardPath = in.readString();
        this.isSeleted = in.readInt()==1;
        this.isButton = in.readInt()==1;
        this.idBitmap = in.readInt();

    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

        dest.writeString(this.sdcardPath);
        dest.writeInt(this.isSeleted ? 1 : 0);
        dest.writeInt(this.isButton ? 1 : 0);
        dest.writeInt(this.idBitmap);

    }

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public CustomGallery createFromParcel(Parcel in) {
            return new CustomGallery(in);
        }

        public CustomGallery[] newArray(int size) {
            return new CustomGallery[size];
        }
    };
}
