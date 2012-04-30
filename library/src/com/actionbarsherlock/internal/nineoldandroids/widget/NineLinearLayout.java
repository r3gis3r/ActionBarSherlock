package com.actionbarsherlock.internal.nineoldandroids.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.LinearLayout;

import com.actionbarsherlock.internal.nineoldandroids.view.animation.AnimatorProxy;
import com.actionbarsherlock.internal.utils.UtilityWrapper;

import java.lang.reflect.Method;

public class NineLinearLayout extends LinearLayout {
    private final AnimatorProxy mProxy;

    private static Method superOnConfigurationChangedMethod;
    private static Method superSetAlphaMethod;
    private static Method superGetAlphaMethod;
    private static Method superGetTranslationXMethod;
    private static Method superSetTranslationXMethod;

    public NineLinearLayout(Context context) {
        super(context);
        mProxy = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(this) : null;
        loadStaticMethods();
    }
    
    
    
    public NineLinearLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        mProxy = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(this) : null;
        loadStaticMethods();
    }
    
    private void loadStaticMethods() {

        // Building compatibility
        Class<?> cls = getClass();
        if (!AnimatorProxy.NEEDS_PROXY && superGetAlphaMethod == null) {
            superGetAlphaMethod = UtilityWrapper.safelyGetSuperclassMethod(cls, "getAlpha");
            superSetAlphaMethod = UtilityWrapper.safelyGetSuperclassMethod(cls, "setAlpha", float.class);

            superGetTranslationXMethod = UtilityWrapper.safelyGetSuperclassMethod(cls,
                    "getTranslationX");
            superSetTranslationXMethod = UtilityWrapper.safelyGetSuperclassMethod(cls,
                    "setTranslationX", float.class);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH
                && superOnConfigurationChangedMethod == null) {
            superOnConfigurationChangedMethod = UtilityWrapper.safelyGetSuperclassMethod(cls,
                    "onConfigurationChanged", Configuration.class);
        }
    }
    
/*
    public NineLinearLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mProxy = AnimatorProxy.NEEDS_PROXY ? AnimatorProxy.wrap(this) : null;
        loadStaticMethods();
    }
*/
    @Override
    public void setVisibility(int visibility) {
        if (mProxy != null) {
            if (visibility == GONE) {
                clearAnimation();
            } else if (visibility == VISIBLE) {
                setAnimation(mProxy);
            }
        }
        super.setVisibility(visibility);
    }

    public float supportGetAlpha() {
        if (AnimatorProxy.NEEDS_PROXY) {
            return mProxy.getAlpha();
        } else {
            Float res = (Float) UtilityWrapper.safelyInvokeMethod(superGetAlphaMethod, this);
            if(res != null) {
                return res;
            }
        }
        return 0.0f;
    }

    public void supportSetAlpha(float alpha) {
        if (AnimatorProxy.NEEDS_PROXY) {
            mProxy.setAlpha(alpha);
        } else {
            UtilityWrapper.safelyInvokeMethod(superSetAlphaMethod, this, alpha);
        }
    }

    public float supportGetTranslationX() {
        if (AnimatorProxy.NEEDS_PROXY) {
            return mProxy.getTranslationX();
        } else {
            Float res = (Float) UtilityWrapper.safelyInvokeMethod(superGetTranslationXMethod, this);
            if(res != null) {
                return res;
            }
            return 0.0f;
        }
    }
    
    public void supportSetTranslationX(float translationX) {
        if (AnimatorProxy.NEEDS_PROXY) {
            mProxy.setTranslationX(translationX);
        } else {
            UtilityWrapper.safelyInvokeMethod(superSetTranslationXMethod, this, translationX);
        }
    }

    protected void supportOnConfigurationChanged(Configuration newConfig) {
        if(superOnConfigurationChangedMethod != null) {
            UtilityWrapper.safelyInvokeMethod(superOnConfigurationChangedMethod, this, newConfig);
        }
    }
}
