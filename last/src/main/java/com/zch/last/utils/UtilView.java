package com.zch.last.utils;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.ColorRes;
import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;

import com.zch.last.R;

/**
 * 视图工具
 */
public class UtilView {

    public static boolean setViewVisibility(View view, int visibility) {
        if (view == null) return false;
        if (view.getVisibility() == visibility) {
            return false;
        }
        view.setVisibility(visibility);
        return true;
    }

    //
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

    public static ViewGroup getContentView(Activity activity) {
        ViewGroup contentView = (ViewGroup) activity.findViewById(Window.ID_ANDROID_CONTENT);
        if (contentView != null && contentView.getChildCount() == 1) {
            return contentView;
        } else {
            throw new IllegalStateException("未找到" + activity.getClass().getCanonicalName() + "根view的子view");
        }
    }

    public static View getBodyView(Activity activity) {
        ViewGroup contentView = getContentView(activity);
        return contentView == null ? null : contentView.getChildAt(0);
    }

    public static void addViewToRoot(Activity activity, View view) {
        ViewGroup rootView = getContentView(activity);
        if (rootView != null) {
            View content = rootView.getChildAt(0);
            rootView.removeAllViews();
            rootView.addView(view);
            rootView.addView(content);
        }
    }

    public static void addRootToGroup(Activity activity, ViewGroup viewGroup) {
        ViewGroup rootView = getContentView(activity);
        if (rootView != null) {
            View content = rootView.getChildAt(0);
            rootView.removeAllViews();
            viewGroup.addView(content);
            rootView.addView(viewGroup);
        }
    }

    public static Bitmap convertViewToBitmap(View view) {
        view.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED), View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap bitmap = view.getDrawingCache();
        if (bitmap != null) {
            Bitmap.Config cfg = bitmap.getConfig();
            Log.d("convertViewToBitmap", "---- cache.getConfig() = " + cfg);
        }

        return bitmap;
    }

    public static void setStringToFocusEditText(Activity activity, String string) {
        View view = activity.getCurrentFocus();
        if (view != null && view instanceof EditText) {
            ((EditText) view).setText(string);
        }

    }

    public static EditText getFocusEditText(Activity activity) {
        View view = activity.getCurrentFocus();
        return view != null && view instanceof EditText ? (EditText) view : null;
    }

    public static void showViewGently(final View view, int time, final boolean show) {
        showViewGentlyDelay(view, time, 0, show, null);
    }

    public static void showViewGently(final View view, int time, final boolean show, final Animator.AnimatorListener listener) {
        showViewGentlyDelay(view, time, 0, show, listener);
    }

    public static void showViewGentlyDelay(final View view, int time, int delay, final boolean show, final Animator.AnimatorListener listener) {
        if (view == null) return;
        if ((view.getVisibility() == View.VISIBLE) == show) return;
        Object tag = view.getTag(Integer.MIN_VALUE);
        if (tag != null && tag instanceof ValueAnimator) {
            ValueAnimator animator = (ValueAnimator) tag;
            if (animator.isRunning()) {
                animator.removeAllListeners();
                animator.removeAllUpdateListeners();
                animator.cancel();
            }
        }
        final ValueAnimator valueAnimator;
        float vAlpha;
        if (view.getVisibility() != View.VISIBLE) {
            vAlpha = 0;
        } else {
            vAlpha = view.getAlpha();
        }
        if (show) {
            valueAnimator = ValueAnimator.ofFloat(vAlpha, 1);
        } else {
            valueAnimator = ValueAnimator.ofFloat(vAlpha, 0);
        }
        valueAnimator.setDuration(time);
        valueAnimator.setStartDelay(delay);
        valueAnimator.setRepeatMode(ValueAnimator.RESTART);
        valueAnimator.setRepeatCount(0);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                view.setAlpha(value);
                setViewVisibility(view, View.VISIBLE);
            }
        });
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                if (listener != null) {
                    listener.onAnimationStart(animation);
                }
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                setViewVisibility(view, show ? View.VISIBLE : View.GONE);
                view.setTag(Integer.MIN_VALUE, null);
                if (listener != null) {
                    listener.onAnimationEnd(animation);
                }
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if (listener != null) {
                    listener.onAnimationCancel(animation);
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                if (listener != null) {
                    listener.onAnimationRepeat(animation);
                }
            }
        });
        view.setTag(Integer.MIN_VALUE, valueAnimator);
        valueAnimator.start();
    }

    public static void setCompoundDrawables(TextView tv, @DrawableRes int l, @DrawableRes int t, @DrawableRes int r, @DrawableRes int b) {
        setCompoundDrawables(tv, l, t, r, b, 0, 0);
    }

    public static void setCompoundDrawables(TextView tv, @DrawableRes int l, @DrawableRes int t, @DrawableRes int r, @DrawableRes int b, int dx, int dy) {
        if (tv != null) {
            Context context = tv.getContext();
            Drawable left = null;
            Drawable right = null;
            Drawable top = null;
            Drawable bot = null;
            if (l != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    left = context.getResources().getDrawable(l, context.getTheme());
                } else {
                    left = context.getResources().getDrawable(l);
                }

                left.setBounds(dx, dy, dx + left.getMinimumWidth(), dy + left.getMinimumHeight());
            }

            if (t != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    top = context.getResources().getDrawable(t, context.getTheme());
                } else {
                    top = context.getResources().getDrawable(t);
                }

                top.setBounds(dx, dy, dx + top.getMinimumWidth(), dy + top.getMinimumHeight());
            }

            if (r != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    right = context.getResources().getDrawable(r, context.getTheme());
                } else {
                    right = context.getResources().getDrawable(r);
                }

                right.setBounds(dx, dy, dx + right.getMinimumWidth(), dy + right.getMinimumHeight());
            }

            if (b != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    bot = context.getResources().getDrawable(b, context.getTheme());
                } else {
                    bot = context.getResources().getDrawable(b);
                }

                bot.setBounds(dx, dy, dx + bot.getMinimumWidth(), dy + bot.getMinimumHeight());
            }

            tv.setCompoundDrawables(left, top, right, bot);
        }
    }

    public static void setCompoundDrawables(TextView tv, float scales, @DrawableRes int l, @DrawableRes int t, @DrawableRes int r, @DrawableRes int b) {
        if (tv != null) {
            Context context = tv.getContext();
            Drawable left = null;
            Drawable right = null;
            Drawable top = null;
            Drawable bot = null;
            int bWidth;
            int bHeight;
            int bsWidth;
            int bsHeight;
            if (l != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    left = context.getResources().getDrawable(l, context.getTheme());
                } else {
                    left = context.getResources().getDrawable(l);
                }

                bWidth = left.getMinimumWidth();
                bHeight = left.getMinimumHeight();
                bsWidth = (int) ((float) bWidth * scales);
                bsHeight = (int) ((float) bHeight * scales);
                left.setBounds(0, 0, bsWidth, bsHeight);
            }

            if (t != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    top = context.getResources().getDrawable(t, context.getTheme());
                } else {
                    top = context.getResources().getDrawable(t);
                }

                bWidth = top.getMinimumWidth();
                bHeight = top.getMinimumHeight();
                bsWidth = (int) ((float) bWidth * scales);
                bsHeight = (int) ((float) bHeight * scales);
                top.setBounds(0, 0, bsWidth, bsHeight);
            }

            if (r != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    right = context.getResources().getDrawable(r, context.getTheme());
                } else {
                    right = context.getResources().getDrawable(r);
                }

                bWidth = right.getMinimumWidth();
                bHeight = right.getMinimumHeight();
                bsWidth = (int) ((float) bWidth * scales);
                bsHeight = (int) ((float) bHeight * scales);
                right.setBounds(0, 0, bsWidth, bsHeight);
            }

            if (b != 0) {
                if (Build.VERSION.SDK_INT >= 21) {
                    bot = context.getResources().getDrawable(b, context.getTheme());
                } else {
                    bot = context.getResources().getDrawable(b);
                }

                bWidth = bot.getMinimumWidth();
                bHeight = bot.getMinimumHeight();
                bsWidth = (int) ((float) bWidth * scales);
                bsHeight = (int) ((float) bHeight * scales);
                bot.setBounds(0, 0, bsWidth, bsHeight);
            }

            tv.setCompoundDrawables(left, top, right, bot);
        }
    }

    public static boolean canScroll(View view, int direction) {
        AbsListView absListView;
        if (direction == 1) {
            if (Build.VERSION.SDK_INT >= 14) {
                return view.canScrollVertically(1);
            } else if (view instanceof AbsListView) {
                absListView = (AbsListView) view;
                return absListView.getChildCount() > 0 && (absListView.getLastVisiblePosition() < absListView.getChildCount() - 1 || absListView.getChildAt(absListView.getChildCount() - 1).getBottom() > absListView.getPaddingBottom());
            } else {
                return view.canScrollVertically(1) || view.getScrollY() < 0;
            }
        } else if (direction == 0) {
            if (Build.VERSION.SDK_INT >= 14) {
                return view.canScrollVertically(-1);
            } else if (view instanceof AbsListView) {
                absListView = (AbsListView) view;
                return absListView.getChildCount() > 0 && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0).getTop() < absListView.getPaddingTop());
            } else {
                return view.canScrollVertically(-1) || view.getScrollY() > 0;
            }
        } else {
            Log.e("canScroll?", "direction is wrong!!!");
            return false;
        }
    }


    public static void setTextColor(TextView textView, @ColorRes int colorInt) {
        if (textView == null) return;
        Context context = textView.getContext();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            textView.setTextColor(context.getResources().getColor(colorInt, context.getTheme()));
        } else {
            textView.setTextColor(context.getResources().getColor(colorInt));
        }
    }

    public static void setText(TextView textView, String text) {
        if (textView == null) return;
        String oldText = textView.getText().toString();
        if (text == null) {
            if (oldText.length() == 0) {
                return;
            }
        } else if (text.equals(oldText)) {
            return;
        }
        textView.setText(text);
    }

    public static void setBitmap(ImageView imageView, @Nullable Bitmap bitmap) {
        Object tag = imageView.getTag(R.id.bitmap_tag);
        if (UtilObject.equals(bitmap, tag)) return;
        imageView.setTag(R.id.bitmap_tag, bitmap);
        imageView.setImageBitmap(bitmap);
        imageView.requestFocus();
        try {
            if (tag != null && tag instanceof Bitmap) {
                ((Bitmap) tag).recycle();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
