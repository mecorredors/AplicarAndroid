package car.gov.co.carserviciociudadano.common;

import android.content.Context;
import androidx.viewpager.widget.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * Created by Olger on 10/06/2017.
 */

public class ViewPagerExtend extends ViewPager {

    public ViewPagerExtend(Context context){
        super(context);
    }
    public ViewPagerExtend(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        try {
            return super.onTouchEvent(ev);
        } catch (IllegalArgumentException ex) {
            ex.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        try {

            return super.onInterceptTouchEvent(event);

        } catch (IllegalArgumentException e) {
        } catch (ArrayIndexOutOfBoundsException e) {
        }

        return false;
    }
}
