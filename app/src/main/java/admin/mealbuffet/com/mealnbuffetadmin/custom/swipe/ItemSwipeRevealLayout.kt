package admin.mealbuffet.com.mealnbuffetadmin.custom.swipe

import android.content.Context
import android.util.AttributeSet
import android.view.View

class ItemSwipeRevealLayout @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyleAttr: Int = 0
) : SwipeRevealLayout(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        var maxHeight = 0

        if (View.MeasureSpec.getMode(heightMeasureSpec) == View.MeasureSpec.EXACTLY) {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
        } else {
            //find a child with largest height
            for (i in 0 until childCount) {
                val child = getChildAt(i)
                measureChild(child, widthMeasureSpec, heightMeasureSpec)
                maxHeight = Math.max(maxHeight, child.measuredHeight)
            }

            if (maxHeight > 0) {
                val heightMSpec = View.MeasureSpec.makeMeasureSpec(maxHeight, View.MeasureSpec.EXACTLY)
                measureChildren(widthMeasureSpec, heightMSpec)
            }
        }

        // Find rightmost and bottom-most child
        for (i in 0 until childCount) {
            val child = getChildAt(i)
            if (child.visibility != View.GONE) {
                maxHeight = Math.max(maxHeight, child.measuredHeight)
            }
        }

        maxHeight += paddingTop + paddingBottom
        maxHeight = Math.max(maxHeight, suggestedMinimumHeight)

        setMeasuredDimension(View.resolveSize(suggestedMinimumWidth, widthMeasureSpec),
                View.resolveSize(maxHeight, heightMeasureSpec))
    }
}
