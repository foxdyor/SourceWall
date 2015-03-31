package net.nashlegend.sourcewall.commonview;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.Layout;
import android.text.Selection;
import android.text.Spannable;
import android.text.method.LinkMovementMethod;
import android.text.method.Touch;
import android.text.style.ImageSpan;
import android.text.style.URLSpan;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.TextView;

import net.nashlegend.sourcewall.util.ImageSizeMap;
import net.nashlegend.sourcewall.util.UrlCheckUtil;

/**
 * Created by NashLegend on 2015/1/13 0013
 * http://www.cnblogs.com/TerryBlog/archive/2013/04/02/2994815.html
 */
public class TTextView extends TextView {
    boolean noConsumeNonUrlClicks = true;
    boolean linkHit;

    public TTextView(Context context) {
        super(context);
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }

    public TTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }

    public TTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TTextView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        setMovementMethod(LocalLinkMovementMethod.getInstance());
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        linkHit = false;
        boolean res = super.onTouchEvent(event);
        if (noConsumeNonUrlClicks)
            return linkHit;
        return res;
    }

    private static void handleURLSpanClick(URLSpan urlSpan) {
        UrlCheckUtil.redirectRequest(urlSpan.getURL());
    }

    public static class LocalLinkMovementMethod extends LinkMovementMethod {
        static LocalLinkMovementMethod sInstance;

        public static LocalLinkMovementMethod getInstance() {
            if (sInstance == null)
                sInstance = new LocalLinkMovementMethod();
            return sInstance;
        }

        @Override
        public boolean onTouchEvent(TextView widget, Spannable buffer, MotionEvent event) {
            int action = event.getAction();

            if (action == MotionEvent.ACTION_UP ||
                    action == MotionEvent.ACTION_DOWN) {
                int x = (int) event.getX();
                int y = (int) event.getY();

                x -= widget.getTotalPaddingLeft();
                y -= widget.getTotalPaddingTop();

                x += widget.getScrollX();
                y += widget.getScrollY();

                Layout layout = widget.getLayout();
                int line = layout.getLineForVertical(y);
                int off = layout.getOffsetForHorizontal(line, x);

                ImageSpan[] images = buffer.getSpans(off, off, ImageSpan.class);
                if (images.length > 0) {
                    ImageSpan span = images[0];
                    String source = span.getSource();//TODO，这个是图片url，显示图片用
                    //显示图片时，打开ImageActivity，传入text和当前Image地址,使用正则匹(或者其他方式，使用Html.From同样的方式最好)配出所有url，构成一个列表
                    //或者先生成列表再传给ImageActivity，打开ImageActivity。
                    //现在的问题是如果有两个相同图片怎么办……
//                    ((TTextView) widget).linkHit = true;
                    return true;
                }

                URLSpan[] link = buffer.getSpans(off, off, URLSpan.class);

                if (link.length != 0) {
                    if (action == MotionEvent.ACTION_UP) {
                        handleURLSpanClick(link[0]);
                    } else {
                        Selection.setSelection(buffer,
                                buffer.getSpanStart(link[0]),
                                buffer.getSpanEnd(link[0]));
                    }
                    if (widget instanceof TTextView) {
                        ((TTextView) widget).linkHit = true;
                    }
                    return true;
                } else {
                    Selection.removeSelection(buffer);
                    Touch.onTouchEvent(widget, buffer, event);
                    return false;
                }
            }
            return Touch.onTouchEvent(widget, buffer, event);
        }
    }
}

