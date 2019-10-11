package com.zch.last.view.recycler.adapter;

import android.net.Uri;
import android.view.View;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.TextView;

final class ToolsAdapter {

    public static void setData2View(View v, Object data) {
        if (v == null) return;
        if (v instanceof Checkable) {
            if (data instanceof Boolean) {
                ((Checkable) v).setChecked((Boolean) data);
            } else if (v instanceof TextView) {
                // Note: keep the instanceof TextView check at the bottom of these
                // ifs since a lot of views are TextViews (e.g. CheckBoxes).
                setViewText((TextView) v, data == null ? "" : data.toString());
            } else {
                throw new IllegalStateException(v.getClass().getName() +
                        " should be bound to a Boolean, not a " +
                        (data == null ? "<unknown type>" : data.getClass()));
            }
        } else if (v instanceof TextView) {
            // Note: keep the instanceof TextView check at the bottom of these
            // ifs since a lot of views are TextViews (e.g. CheckBoxes).
            setViewText((TextView) v, data == null ? "" : data.toString());
        } else if (v instanceof ImageView) {
            if (data instanceof Integer) {
                setViewImage((ImageView) v, (Integer) data);
            } else {
                setViewImage((ImageView) v, data == null ? "" : data.toString());
            }
        } else {
            throw new IllegalStateException(v.getClass().getName() + " is not a " +
                    " view that can be bounds by this SimpleAdapter");
        }
    }

    public static void setViewImage(ImageView v, String value) {
        try {
            v.setImageResource(Integer.parseInt(value));
        } catch (NumberFormatException nfe) {
            v.setImageURI(Uri.parse(value));
        }
    }

    public static void setViewImage(ImageView v, int value) {
        v.setImageResource(value);
    }

    public static void setViewText(TextView v, String text) {
        v.setText(text);
    }
}
