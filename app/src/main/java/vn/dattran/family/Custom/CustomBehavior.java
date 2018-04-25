package vn.dattran.family.Custom;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPropertyAnimatorCompat;
import android.support.v4.view.animation.LinearOutSlowInInterpolator;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.Interpolator;

/**
 * Created by DatTran on 13/02/2018.
 */

public class CustomBehavior extends CoordinatorLayout.Behavior<BottomNavigationView> {
    private ViewPropertyAnimatorCompat mOffsetValueAnimator;
    private boolean isHide;

    public CustomBehavior() {
        super();
    }

    public CustomBehavior(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onStartNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                                       @NonNull View directTargetChild, @NonNull View target, int axes, int type) {
        return axes == ViewCompat.SCROLL_AXIS_VERTICAL;
    }

    @Override
    public void onStopNestedScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                                   @NonNull View target, int type) {
        super.onStopNestedScroll(coordinatorLayout, child, target, type);
    }

    @Override
    public boolean layoutDependsOn(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
        return dependency instanceof NestedScrollView;
    }

//    @Override
//    public boolean onDependentViewChanged(CoordinatorLayout parent, BottomNavigationView child, View dependency) {
//        Log.d("thunghiem","c");
//
//        return true;
//
//    }

//    @Override
//    public boolean onNestedFling(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
//                                 @NonNull View target, float velocityX, float velocityY, boolean consumed) {
//        Log.d("thunghiem",""+velocityY);
//        return super.onNestedFling(coordinatorLayout, child, target, velocityX, velocityY, consumed);
//    }

        @Override
    public void onNestedPreScroll(@NonNull CoordinatorLayout coordinatorLayout, @NonNull BottomNavigationView child,
                                  @NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(coordinatorLayout, child, target, dx, dy, consumed, type);
        Log.d("thunghiem",""+dy);
        if (isHide && dy < -5) {
            isHide=false;
            showBottomNavigationView(child);
        } else if (!isHide && dy > 5) {
            isHide=true;
            hideBottomNavigationView(child);
        }
    }
    private void hideBottomNavigationView(BottomNavigationView view) {
//        view.setTranslationY(view.getHeight());
        view.clearAnimation();
        view.animate().translationY(view.getHeight()).setDuration(200);
//        animateOffset(view,200);
    }

    private void showBottomNavigationView(BottomNavigationView view) {
//        view.setTranslationY(0);
        view.clearAnimation();
        view.animate().translationY(0).setDuration(200);
//        animateOffset(view,0);
    }
    private void animateOffset(final View child, final int offset) {
        ensureOrCancelAnimator(child);
        mOffsetValueAnimator.translationY(offset).start();
    }


    private static final Interpolator INTERPOLATOR = new LinearOutSlowInInterpolator();

    private void ensureOrCancelAnimator(View child) {
        if (mOffsetValueAnimator == null) {
            mOffsetValueAnimator = ViewCompat.animate(child);
            mOffsetValueAnimator.setDuration(1000);
            mOffsetValueAnimator.setInterpolator(INTERPOLATOR);
        } else {
            mOffsetValueAnimator.cancel();
        }
    }
//    @Override
//    protected boolean onNestedDirectionFling(CoordinatorLayout coordinatorLayout, View child, View target, float velocityX,
//                                             float velocityY, @ScrollDirection int scrollDirection) {
//        handleDirection(child, scrollDirection);
//        return true;
//    }
//    private void handleDirection(View child, int scrollDirection) {
//        if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_DOWN && hidden) {
//            hidden = false;
//            animateOffset(child, 0);
//        } else if (scrollDirection == ScrollDirection.SCROLL_DIRECTION_UP && !hidden) {
//            hidden = true;
//            animateOffset(child, child.getHeight());
//        }
//    }
//    private void animateOffset(final V child, final int offset) {
//        ensureOrCancelAnimator(child);
//        mTranslatenimator.translationY(offset).start();
//        //...
//    }
}
