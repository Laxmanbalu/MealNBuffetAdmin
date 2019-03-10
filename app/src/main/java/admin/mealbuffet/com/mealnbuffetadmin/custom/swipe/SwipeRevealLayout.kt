package admin.mealbuffet.com.mealnbuffetadmin.custom.swipe


import admin.mealbuffet.com.mealnbuffetadmin.R
import admin.mealbuffet.com.mealnbuffetadmin.util.Constants.EMPTY_STRING
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Rect
import android.support.v4.view.GestureDetectorCompat
import android.support.v4.view.ViewCompat
import android.support.v4.widget.ViewDragHelper
import android.util.AttributeSet
import android.util.DisplayMetrics
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.accessibility.AccessibilityEvent
import android.widget.FrameLayout

@SuppressLint("RtlHardcoded")
open class SwipeRevealLayout @JvmOverloads constructor(
        context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : ViewGroup(context, attrs, defStyleAttr) {

    /**
     * Main view is the view which is shown when the layout is closed.
     */
    private lateinit var mMainView: View

    /**
     * Secondary view is the view which is shown when the layout is opened.
     */
    private var mSecondaryView: View? = null

    /**
     * Secondary's first child, if any; use for centering the child view as view is expanded.
     */
    private var mSecondaryChildView: View? = null

    /**
     * The rectangle position of the main view when the layout is closed.
     */
    private val mRectMainClose = Rect()

    /**
     * The rectangle position of the main view when the layout is opened.
     */
    private val mRectMainOpen = Rect()

    /**
     * The rectangle position of the secondary view when the layout is closed.
     */
    private val mRectSecClose = Rect()

    /**
     * The rectangle position of the secondary view when the layout is opened.
     */
    private val mRectSecOpen = Rect()

    /**
     * Percentage of swipe at which the secondary view snap-closes or snap-opens. Default is 50% (0.5).
     */
    private var pivotThresholdFactor = 0.5f

    /**
     * When true, centers the secondary child window within the the secondary's parent.
     * This automatically repositions the view to be centered as the slider is opened or closed.
     */
    private var centerSecondaryChild = false

    /**
     * The minimum distance (px) to the closest drag edge that the SwipeRevealLayout
     * will disallow the parent to intercept touch event.
     */
    private var mMinDistRequestDisallowParent = 0

    private var mIsOpenBeforeInit = false
    @Volatile private var mAborted = false
    @Volatile private var mIsScrolling = false
    /**
     * @return true if the drag/swipe motion is currently locked.
     */
    @Volatile
    var isDragLocked = false
        private set

    /**
     * Get the minimum fling velocity to cause the layout to open/close.
     *
     * @return dp per second
     */
    /**
     * Set the minimum fling velocity to cause the layout to open/close.
     *
     */
    var minFlingVelocity = DEFAULT_MIN_FLING_VELOCITY
    private var mState = STATE_CLOSE
    private var mMode = MODE_NORMAL

    private var mLastMainLeft = 0
    private var mLastMainTop = 0

    /**
     * Get the edge where the layout can be dragged from.
     *
     * @return Can be one of these
     *
     *  * [.DRAG_EDGE_LEFT]
     *  * [.DRAG_EDGE_TOP]
     *  * [.DRAG_EDGE_RIGHT]
     *  * [.DRAG_EDGE_BOTTOM]
     *
     */
    /**
     * Set the edge where the layout can be dragged from.
     *
     * @param dragEdge Can be one of these
     *
     *  * [.DRAG_EDGE_LEFT]
     *  * [.DRAG_EDGE_TOP]
     *  * [.DRAG_EDGE_RIGHT]
     *  * [.DRAG_EDGE_BOTTOM]
     *
     */
    var dragEdge = DRAG_EDGE_LEFT

    private var mDragHelper: ViewDragHelper? = null
    private var mGestureDetector: GestureDetectorCompat? = null

    private var mDragStateChangeListener: DragStateChangeListener? = null // only used for ViewBindHelper
    private var mSwipeListener: SwipeListener? = null

    private var revealedOptionsLayout: FrameLayout? = null
    private var accessabilityDescriptionText = EMPTY_STRING
    private var previousFocusedRevealedOptionsLayout = false
    private var isAnnounncementDisplayed = false
    private var mOnLayoutCount = 0

    /**
     * @return true if layout is fully opened, false otherwise.
     */
    val isOpened: Boolean
        get() = mState == STATE_OPEN

    /**
     * @return true if layout is fully closed, false otherwise.
     */
    val isClosed: Boolean
        get() = mState == STATE_CLOSE


    private val mainOpenLeft: Int
        get() {
            return when (dragEdge) {
                DRAG_EDGE_LEFT ->  mRectMainClose.left + getSecondaryViewWidth()

                DRAG_EDGE_RIGHT ->  mRectMainClose.left - getSecondaryViewWidth()

                DRAG_EDGE_TOP ->  mRectMainClose.left

                DRAG_EDGE_BOTTOM ->  mRectMainClose.left

                else ->  0
            }
        }

    private val mainOpenTop: Int
        get() {
            return when (dragEdge) {
                DRAG_EDGE_LEFT ->  mRectMainClose.top

                DRAG_EDGE_RIGHT ->  mRectMainClose.top

                DRAG_EDGE_TOP ->  mRectMainClose.top + getSecondaryViewHeight()

                DRAG_EDGE_BOTTOM ->  mRectMainClose.top - getSecondaryViewHeight()

                else ->  0
            }
        }

    private val secOpenLeft: Int
        get() {
            if (mMode == MODE_NORMAL || dragEdge == DRAG_EDGE_BOTTOM || dragEdge == DRAG_EDGE_TOP) {
                return mRectSecClose.left
            }

            return if (dragEdge == DRAG_EDGE_LEFT) {
                mRectSecClose.left + getSecondaryViewWidth()
            } else {
                mRectSecClose.left - getSecondaryViewWidth()
            }
        }

    private val secOpenTop: Int
        get() {
            if (mMode == MODE_NORMAL || dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                return mRectSecClose.top
            }

            return if (dragEdge == DRAG_EDGE_TOP) {
                mRectSecClose.top + getSecondaryViewHeight()
            } else {
                mRectSecClose.top - getSecondaryViewHeight()
            }
        }

    private val mGestureListener = object : GestureDetector.SimpleOnGestureListener() {
        internal var hasDisallowed = false

        override fun onDown(e: MotionEvent): Boolean {
            mIsScrolling = false
            hasDisallowed = false
            return true
        }

        override fun onFling(e1: MotionEvent, e2: MotionEvent, velocityX: Float, velocityY: Float): Boolean {
            mIsScrolling = true
            return false
        }

        override fun onScroll(e1: MotionEvent, e2: MotionEvent, distanceX: Float, distanceY: Float): Boolean {
            mIsScrolling = true

            if (parent != null) {
                val shouldDisallow: Boolean

                if (!hasDisallowed) {
                    shouldDisallow = distToClosestEdge >= mMinDistRequestDisallowParent
                    if (shouldDisallow) {
                        hasDisallowed = true
                    }
                } else {
                    shouldDisallow = true
                }

                /**
                 * Disallow parent to intercept touch event so that the layout will work
                 * properly on RecyclerView or view that handles scroll gesture.
                 */
                parent.requestDisallowInterceptTouchEvent(shouldDisallow)
            }

            return false
        }
    }

    private val distToClosestEdge: Int
        get() {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> {
                    val pivotRight = mRectMainClose.left + getSecondaryViewWidth()

                    return Math.min(
                            mMainView.left - mRectMainClose.left,
                            pivotRight - mMainView.left
                    )
                }

                DRAG_EDGE_RIGHT -> {
                    val pivotLeft = mRectMainClose.right - getSecondaryViewWidth()

                    return Math.min(
                            mMainView.right - pivotLeft,
                            mRectMainClose.right - mMainView.right
                    )
                }

                DRAG_EDGE_TOP -> {
                    val pivotBottom = mRectMainClose.top + getSecondaryViewHeight()

                    return Math.min(
                            mMainView.bottom - pivotBottom,
                            pivotBottom - mMainView.top
                    )
                }

                DRAG_EDGE_BOTTOM -> {
                    val pivotTop = mRectMainClose.bottom - getSecondaryViewHeight()

                    return Math.min(
                            mRectMainClose.bottom - mMainView.bottom,
                            mMainView.bottom - pivotTop
                    )
                }
            }

            return 0
        }

    private val halfwayPivotHorizontal: Int
        get() = if (dragEdge == DRAG_EDGE_LEFT) {
            mRectMainClose.left + (getSecondaryViewWidth() * pivotThresholdFactor).toInt()
        } else {
            mRectMainClose.right - (getSecondaryViewWidth() * pivotThresholdFactor).toInt()
        }

    private val halfwayPivotVertical: Int
        get() = if (dragEdge == DRAG_EDGE_TOP) {
            mRectMainClose.top + (getSecondaryViewHeight() * pivotThresholdFactor).toInt()
        } else {
            mRectMainClose.bottom - (getSecondaryViewHeight() * pivotThresholdFactor).toInt()
        }

    /**
     * Centers the first child view within the secondary view as the secondary view width increases.
     * When the slider position is greater than the width of the background child view, adjusts
     *  the position of the child view to its new horizontal position.
     *
     * Currently is supported on in horizontal (left and right) dragging.
     * For a right-edge drag, the child layout_gravity should be "end".
     * For a left-edge drag, the child layout_gravity should be "start".
     *
     *  @param slideOffset is a percentage of the shown background window, 0.0 closed to 1.0 opened
     */
    private fun centerSecondaryChildOnSlide(slideOffset: Float) {

        if (mSecondaryChildView != null && (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT)) {

            // width of the screen to be used: this is the width of the parent view's layout
            val fullWidth = mSecondaryView!!.width

            // get the child view's viewable width, including horizontal margins
            val childViewWidth = mSecondaryChildView!!.measuredWidth

            // get the current viewable width of the background slider window, based on slider position:
            val viewableWidth = (fullWidth * slideOffset).toInt()

            // If the viewable width is larger than the child width, slide the child into the center:
            if (viewableWidth > childViewWidth) {

                // To move the child window, calculate the difference (delta) the child needs
                // to be moved. Since this is centering the child in the sliding-open window,
                // determine the center of that sliding window, determine half the child width,
                // calculate the desired x-position of the child view, and calculate the delta
                // of the desired position vs. the current position.
                // If there is a difference, adjust the child position.

                // determine the desired adjusted center of the slide-out background view
                val newCenter = if (dragEdge == DRAG_EDGE_RIGHT) {
                    fullWidth - (viewableWidth / 2)
                } else {
                    (viewableWidth / 2)
                }

                // determine the desired start of the TextView:
                val newTextStart = newCenter - (childViewWidth / 2)

                // calculate the difference of the existing TextView start to the desired start:
                val delta = newTextStart - mSecondaryChildView!!.x.toInt()

                if (delta != 0) {
                    mSecondaryChildView!!.offsetLeftAndRight(delta)
                }

            } else {
                // The sliding position is less than the child width.
                // As the slider window is being reduced, it is possible the child window
                // needs to be moved one final time, to its initial position.
                // This calculates if there is such a delta that needs to be applied
                // and moves the window accordingly.

                val delta = if (dragEdge == DRAG_EDGE_RIGHT) {
                    fullWidth - childViewWidth - mSecondaryChildView!!.x.toInt()
                } else {
                    0 - mSecondaryChildView!!.x.toInt()
                }

                if (delta != 0) {
                    mSecondaryChildView!!.offsetLeftAndRight(delta)
                }
            }
        }
    }

    private val mDragHelperCallback = object : ViewDragHelper.Callback() {

        private val slideOffset: Float
            get() {
                return when (dragEdge) {
                    DRAG_EDGE_LEFT ->  (mMainView.left - mRectMainClose.left).toFloat() / getSecondaryViewWidth()

                    DRAG_EDGE_RIGHT ->  (mRectMainClose.left - mMainView.left).toFloat() / getSecondaryViewWidth()

                    DRAG_EDGE_TOP ->  (mMainView.top - mRectMainClose.top).toFloat() / getSecondaryViewHeight()

                    DRAG_EDGE_BOTTOM ->  (mRectMainClose.top - mMainView.top).toFloat() / getSecondaryViewHeight()

                    else ->  0f
                }
            }

        override fun tryCaptureView(child: View, pointerId: Int): Boolean {
            mAborted = false

            if (isDragLocked)
                return false

            mMainView.let { mDragHelper?.captureChildView(it, pointerId) }
            return false
        }

        override fun clampViewPositionVertical(child: View, top: Int, dy: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_TOP ->  Math.max(
                        Math.min(top, mRectMainClose.top + getSecondaryViewHeight()),
                        mRectMainClose.top
                )

                DRAG_EDGE_BOTTOM ->  Math.max(
                        Math.min(top, mRectMainClose.top),
                        mRectMainClose.top - getSecondaryViewHeight()
                )

                else ->  child.top
            }
        }

        override fun clampViewPositionHorizontal(child: View, left: Int, dx: Int): Int {
            return when (dragEdge) {
                DRAG_EDGE_RIGHT ->  Math.max(
                        Math.min(left, mRectMainClose.left),
                        mRectMainClose.left - getSecondaryViewWidth()
                )

                DRAG_EDGE_LEFT ->  Math.max(
                        Math.min(left, mRectMainClose.left + getSecondaryViewWidth()),
                        mRectMainClose.left
                )

                else ->  child.left
            }
        }

        override fun onViewReleased(releasedChild: View, xvel: Float, yvel: Float) {
            val velRightExceeded = pxToDp(xvel.toInt()) >= minFlingVelocity
            val velLeftExceeded = pxToDp(xvel.toInt()) <= -minFlingVelocity
            val velUpExceeded = pxToDp(yvel.toInt()) <= -minFlingVelocity
            val velDownExceeded = pxToDp(yvel.toInt()) >= minFlingVelocity

            val pivotHorizontal = halfwayPivotHorizontal
            val pivotVertical = halfwayPivotVertical

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> if (velRightExceeded) {
                    closeSecondaryView(true)
                } else if (velLeftExceeded) {
                    openSecondaryView(true)
                } else {
                    if (mMainView.right < pivotHorizontal) {
                        openSecondaryView(true)
                    } else {
                        closeSecondaryView(true)
                    }
                }

                DRAG_EDGE_LEFT -> if (velRightExceeded) {
                    openSecondaryView(true)
                } else if (velLeftExceeded) {
                    closeSecondaryView(true)
                } else {
                    if (mMainView.left < pivotHorizontal) {
                        closeSecondaryView(true)
                    } else {
                        openSecondaryView(true)
                    }
                }

                DRAG_EDGE_TOP -> if (velUpExceeded) {
                    closeSecondaryView(true)
                } else if (velDownExceeded) {
                    openSecondaryView(true)
                } else {
                    if (mMainView.top < pivotVertical) {
                        closeSecondaryView(true)
                    } else {
                        openSecondaryView(true)
                    }
                }

                DRAG_EDGE_BOTTOM -> if (velUpExceeded) {
                    openSecondaryView(true)
                } else if (velDownExceeded) {
                    closeSecondaryView(true)
                } else {
                    if (mMainView.bottom < pivotVertical) {
                        openSecondaryView(true)
                    } else {
                        closeSecondaryView(true)
                    }
                }
            }
        }

        override fun onEdgeDragStarted(edgeFlags: Int, pointerId: Int) {
            super.onEdgeDragStarted(edgeFlags, pointerId)

            if (isDragLocked) {
                return
            }

            val edgeStartLeft = dragEdge == DRAG_EDGE_RIGHT && edgeFlags == ViewDragHelper.EDGE_LEFT

            val edgeStartRight = dragEdge == DRAG_EDGE_LEFT && edgeFlags == ViewDragHelper.EDGE_RIGHT

            val edgeStartTop = dragEdge == DRAG_EDGE_BOTTOM && edgeFlags == ViewDragHelper.EDGE_TOP

            val edgeStartBottom = dragEdge == DRAG_EDGE_TOP && edgeFlags == ViewDragHelper.EDGE_BOTTOM

            if (edgeStartLeft || edgeStartRight || edgeStartTop || edgeStartBottom) {
                mMainView.let { mDragHelper?.captureChildView(it, pointerId) }
            }
        }

        override fun onViewPositionChanged(changedView: View, left: Int, top: Int, dx: Int, dy: Int) {
            super.onViewPositionChanged(changedView, left, top, dx, dy)
            if (mMode == MODE_SAME_LEVEL) {
                if (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                    mSecondaryView?.offsetLeftAndRight(dx)
                } else {
                    mSecondaryView?.offsetTopAndBottom(dy)
                }
            }

            val isMoved = (mMainView.left != mLastMainLeft || mMainView.top != mLastMainTop)
            if (mSwipeListener != null && isMoved) {
                if (mMainView.left == mRectMainClose.left && mMainView.top == mRectMainClose.top) {
                    mSwipeListener?.onClosed(this@SwipeRevealLayout)
                } else if (mMainView.left == mRectMainOpen.left && mMainView.top == mRectMainOpen.top) {
                    mSwipeListener?.onOpened(this@SwipeRevealLayout)
                } else {
                    if (centerSecondaryChild) {
                        centerSecondaryChildOnSlide(slideOffset)
                    }
                    mSwipeListener?.onSlide(this@SwipeRevealLayout, slideOffset,if (dx < 0) STATE_OPENING else STATE_CLOSING)
                }
            }

            mLastMainLeft = mMainView.left
            mLastMainTop = mMainView.top
            ViewCompat.postInvalidateOnAnimation(this@SwipeRevealLayout)
        }

        override fun onViewDragStateChanged(state: Int) {
            super.onViewDragStateChanged(state)
            val prevState = mState

            when (state) {
                ViewDragHelper.STATE_DRAGGING -> mState = STATE_DRAGGING

                ViewDragHelper.STATE_IDLE ->

                    // drag edge is left or right
                    if (dragEdge == DRAG_EDGE_LEFT || dragEdge == DRAG_EDGE_RIGHT) {
                        if (mMainView.left == mRectMainClose.left) {
                            mState = STATE_CLOSE
                        } else {
                            mState = STATE_OPEN
                        }
                    } else {
                        if (mMainView.top == mRectMainClose.top) {
                            mState = STATE_CLOSE
                        } else {
                            mState = STATE_OPEN
                        }
                    }// drag edge is top or bottom
            }

            if (mState == STATE_OPEN  && revealedOptionsLayout != null) {
                 revealedOptionsLayout?.announceForAccessibility(accessabilityDescriptionText)
            }

            if (mDragStateChangeListener != null && !mAborted && prevState != mState) {
                mDragStateChangeListener?.onDragStateChanged(mState)
            }
        }
    }

    internal interface DragStateChangeListener {
        fun onDragStateChanged(state: Int)
    }

    /**
     * Listener for monitoring events about swipe layout.
     */
    interface SwipeListener {
        /**
         * Called when the main view becomes completely closed.
         */
        fun onClosed(view: SwipeRevealLayout)

        /**
         * Called when the main view becomes completely opened.
         */
        fun onOpened(view: SwipeRevealLayout)

        /**
         * Called when the main view's position changes.
         *
         * @param slideOffset The new offset of the main view within its range, from 0-1
         */
        fun onSlide(view: SwipeRevealLayout, slideOffset: Float, draggingState: Int)
    }

    /**
     * No-op stub for [SwipeListener]. If you only want ot implement a subset
     * of the listener methods, you can extend this instead of implement the full interface.
     */
    open class SimpleSwipeListener : SwipeListener {
        override fun onClosed(view: SwipeRevealLayout) {}

        override fun onOpened(view: SwipeRevealLayout) {}

        override fun onSlide(view: SwipeRevealLayout, slideOffset: Float, draggingState: Int) {}
    }

   /* constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr)*/


    init {
       attrs?.let {
            val a = context.theme.obtainStyledAttributes(
                    attrs,
                    R.styleable.SwipeRevealLayout,
                    0, 0
            )

            dragEdge = a.getInteger(R.styleable.SwipeRevealLayout_dragEdge, DRAG_EDGE_LEFT)
            minFlingVelocity = a.getInteger(R.styleable.SwipeRevealLayout_flingVelocity, DEFAULT_MIN_FLING_VELOCITY)
            mMode = a.getInteger(R.styleable.SwipeRevealLayout_mode, MODE_NORMAL)

            mMinDistRequestDisallowParent = a.getDimensionPixelSize(
                    R.styleable.SwipeRevealLayout_minDistRequestDisallowParent,
                    dpToPx(DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT)
            )
            pivotThresholdFactor = a.getFloat(R.styleable.SwipeRevealLayout_pivotThreshold, pivotThresholdFactor)
            centerSecondaryChild = a.getBoolean(R.styleable.SwipeRevealLayout_centerSecondary, false)
        }

        mDragHelper = ViewDragHelper.create(this, 1.0f, mDragHelperCallback)
        mDragHelper?.setEdgeTrackingEnabled(ViewDragHelper.EDGE_ALL)

        mGestureDetector = GestureDetectorCompat(context, mGestureListener)
    }
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mGestureDetector?.onTouchEvent(event)
        mDragHelper?.processTouchEvent(event)
        return true
    }

    override fun onInterceptTouchEvent(ev: MotionEvent): Boolean {
        mDragHelper?.processTouchEvent(ev)
        mGestureDetector?.onTouchEvent(ev)

        val settling = mDragHelper?.viewDragState == ViewDragHelper.STATE_SETTLING
        val idleAfterScrolled = mDragHelper?.viewDragState == ViewDragHelper.STATE_IDLE && mIsScrolling

        return settling || idleAfterScrolled
    }

    override fun onFinishInflate() {
        super.onFinishInflate()

        // get views
        if (childCount >= 2) {
            mSecondaryView = getChildAt(0)
            mMainView = getChildAt(1)

            if (mSecondaryView != null &&
                    mSecondaryView is ViewGroup && (mSecondaryView as ViewGroup).childCount > 0) {
                mSecondaryChildView = (mSecondaryView as ViewGroup).getChildAt(0)
            }
        } else if (childCount == 1) {
            mMainView = getChildAt(0)
        }
    }

    /**
     * {@inheritDoc}
     */
    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        mAborted = false

        for (index in 0 until childCount) {
            val child = getChildAt(index)

            var left: Int
            var right: Int
            var top: Int
            var bottom: Int
            bottom = 0
            top = bottom
            right = top
            left = right

            val minLeft = paddingLeft
            val maxRight = Math.max(r - paddingRight - l, 0)
            val minTop = paddingTop
            val maxBottom = Math.max(b - paddingBottom - t, 0)

            var measuredChildHeight = child.measuredHeight
            var measuredChildWidth = child.measuredWidth

            // need to take account if child size is match_parent
            val childParams = child.layoutParams
            var matchParentHeight = false
            var matchParentWidth = false

            if (childParams != null) {
                matchParentHeight = childParams.height == ViewGroup.LayoutParams.MATCH_PARENT
                matchParentWidth = childParams.width == ViewGroup.LayoutParams.MATCH_PARENT
            }

            if (matchParentHeight) {
                measuredChildHeight = maxBottom - minTop
            }

            if (matchParentWidth) {
                measuredChildWidth = maxRight - minLeft
            }

            when (dragEdge) {
                DRAG_EDGE_RIGHT -> {
                    left = Math.max(r - measuredChildWidth - paddingRight - l, minLeft)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.max(r - paddingRight - l, minLeft)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_LEFT -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_TOP -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.min(paddingTop, maxBottom)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.min(measuredChildHeight + paddingTop, maxBottom)
                }

                DRAG_EDGE_BOTTOM -> {
                    left = Math.min(paddingLeft, maxRight)
                    top = Math.max(b - measuredChildHeight - paddingBottom - t, minTop)
                    right = Math.min(measuredChildWidth + paddingLeft, maxRight)
                    bottom = Math.max(b - paddingBottom - t, minTop)
                }
            }

            child.layout(left, top, right, bottom)
        }

        // taking account offset when mode is SAME_LEVEL
        if (mMode == MODE_SAME_LEVEL) {
            when (dragEdge) {
                DRAG_EDGE_LEFT -> mSecondaryView?.offsetLeftAndRight(-getSecondaryViewWidth())

                DRAG_EDGE_RIGHT -> mSecondaryView?.offsetLeftAndRight(getSecondaryViewWidth())

                DRAG_EDGE_TOP -> mSecondaryView?.offsetTopAndBottom(-getSecondaryViewHeight())

                DRAG_EDGE_BOTTOM -> mSecondaryView?.offsetTopAndBottom(getSecondaryViewHeight())
            }
        }

        initRects()

        if (mIsOpenBeforeInit) openSecondaryView(false) else closeSecondaryView(false)


        mLastMainLeft = mMainView.left
        mLastMainTop = mMainView.top

        mOnLayoutCount++
    }

    /**
     * {@inheritDoc}
     */
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (childCount < 2) {
            throw RuntimeException("Layout must have two children")
        }

        val params = layoutParams

        val widthMode = View.MeasureSpec.getMode(widthMeasureSpec)
        val heightMode = View.MeasureSpec.getMode(heightMeasureSpec)

        val measuredWidth = View.MeasureSpec.getSize(widthMeasureSpec)
        val measuredHeight = View.MeasureSpec.getSize(heightMeasureSpec)

        var desiredWidth = 0
        var desiredHeight = 0

        /* This block gets the size of the largest child window and uses that as potentially
         * the size of this view (if AT_MOST is used).
         * For MATCH_PARENT and EXACT, the measured dimension (defined above) is ultimately used for sizing.
         * Regardless, this measures the child views to fit within this view.
         *
         * Note that for a SwipeRevealLayout, there are typically 2 child views:
         *  one sliding/hidden view, and one main view. This measures the potential size of both views.
         */

        for (i in 0 until childCount) {
            val child = getChildAt(i)
            val childParams = child.layoutParams

            if (childParams != null) {
                if (childParams.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumHeight = measuredHeight
                }

                if (childParams.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                    child.minimumWidth = measuredWidth
                }
            }

            measureChild(child, widthMeasureSpec, heightMeasureSpec)
            desiredWidth = Math.max(child.measuredWidth, desiredWidth)
            desiredHeight = Math.max(child.measuredHeight, desiredHeight)
        }

        // taking accounts of padding
        desiredWidth += paddingLeft + paddingRight
        desiredHeight += paddingTop + paddingBottom

        // Adjust desired width based on Measured Spec
        if (widthMode == View.MeasureSpec.EXACTLY) {
            desiredWidth = measuredWidth
        } else {
            if (params.width == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredWidth = measuredWidth
            }

            if (widthMode == View.MeasureSpec.AT_MOST) {
                desiredWidth = if (desiredWidth > measuredWidth) measuredWidth else desiredWidth
            }
        }

        // adjust desired height
        if (heightMode == View.MeasureSpec.EXACTLY) {
            desiredHeight = measuredHeight
        } else {
            if (params.height == ViewGroup.LayoutParams.MATCH_PARENT) {
                desiredHeight = measuredHeight
            }

            if (heightMode == View.MeasureSpec.AT_MOST) {
                desiredHeight = if (desiredHeight > measuredHeight) measuredHeight else desiredHeight
            }
        }

        setMeasuredDimension(desiredWidth, desiredHeight)
    }

    override fun computeScroll() {
        if (mDragHelper?.continueSettling(true) ?: false) {
            ViewCompat.postInvalidateOnAnimation(this)
        }
    }

    /**
     * Opens (alters the size of layouts) the panel to reveal the secondary view
     *
     * @param animation true to animate the openSecondaryView motion. [SwipeListener] won't be
     * called if is animation is false.
     */
    fun openSecondaryView(animation: Boolean) {
        mIsOpenBeforeInit = true
        mAborted = false

        if (animation) {
            mState = STATE_OPENING
            mMainView.let { mDragHelper?.smoothSlideViewTo(it, mRectMainOpen.left, mRectMainOpen.top) }

            if (mDragStateChangeListener != null) {
                mDragStateChangeListener?.onDragStateChanged(mState)
            }
        } else {
            mState = STATE_OPEN
            mDragHelper?.abort()

            mMainView.layout(
                    mRectMainOpen.left,
                    mRectMainOpen.top,
                    mRectMainOpen.right,
                    mRectMainOpen.bottom
            )

            mSecondaryView?.layout(
                    mRectSecOpen.left,
                    mRectSecOpen.top,
                    mRectSecOpen.right,
                    mRectSecOpen.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this@SwipeRevealLayout)
    }

    /**
     * Closes (alters the size of layouts) the panel to hide the secondary view
     *
     * @param animation true to animate the closeSecondaryView motion. [SwipeListener] won't be
     * called if is animation is false.
     */
    fun closeSecondaryView(animation: Boolean) {
        mIsOpenBeforeInit = false
        mAborted = false

        if (animation) {
            mState = STATE_CLOSING
            mMainView.let { mDragHelper?.smoothSlideViewTo(it, mRectMainClose.left, mRectMainClose.top) }

            if (mDragStateChangeListener != null) {
                mDragStateChangeListener?.onDragStateChanged(mState)
            }

        } else {
            mState = STATE_CLOSE
            mDragHelper?.abort()

            mMainView.layout(
                    mRectMainClose.left,
                    mRectMainClose.top,
                    mRectMainClose.right,
                    mRectMainClose.bottom
            )

            mSecondaryView?.layout(
                    mRectSecClose.left,
                    mRectSecClose.top,
                    mRectSecClose.right,
                    mRectSecClose.bottom
            )
        }

        ViewCompat.postInvalidateOnAnimation(this@SwipeRevealLayout)
    }

    /**
     * Sets the FrameLayout containing the reveal options and the ADA texts.
     *
     * @param optionsLayout FrameLayout that will be revealed/hidden on swipe.
     * @param adaText       contentDescription to be read on Open.
     */
    fun setOnSwipeOpenADAText(optionsLayout: FrameLayout, adaText: String) {
        setRevealedOptionsLayout(optionsLayout)
        this.accessabilityDescriptionText = adaText
    }

    private fun setRevealedOptionsLayout(optionsLayout: FrameLayout) {
        this.revealedOptionsLayout = optionsLayout
    }

    fun setSwipeListener(listener: SwipeListener) {
        mSwipeListener = listener
    }

    /**
     * @param lock if set to true, the user cannot drag/swipe the layout.
     */
    fun setLockDrag(lock: Boolean) {
        isDragLocked = lock
    }

    /**
     * Only used for [SwipeRevealViewBinderHelper]
     */
    internal fun setDragStateChangeListener(listener: DragStateChangeListener) {
        mDragStateChangeListener = listener
    }

    /**
     * Abort current motion in progress. Only used for [SwipeRevealViewBinderHelper]
     */
    fun abort() {
        mAborted = true
        mDragHelper?.abort()
    }

    /**
     * In RecyclerView/ListView, onLayout should be called 2 times to display children views correctly.
     * This method check if it've already called onLayout two times.
     *
     * @return true if you should call [.requestLayout].
     */
    fun shouldRequestLayout(): Boolean {
        return mOnLayoutCount < 2
    }

    private fun initRects() {
        // close position of main view
        mRectMainClose.set(
                mMainView.left,
                mMainView.top,
                mMainView.right,
                mMainView.bottom
        )

        // close position of secondary view
        var secRect = Rect()
        mSecondaryView?.let {
            secRect.left = it.left
            secRect.top = it.top
            secRect.right = it.right
            secRect.bottom = it.bottom
        }
        mRectSecClose.set(secRect)

        // open position of the main view
        mRectMainOpen.set(
                mainOpenLeft,
                mainOpenTop,
                mainOpenLeft + mMainView.width,
                mainOpenTop + mMainView.height
        )

        // open position of the secondary view
        mRectSecOpen.set(
                secOpenLeft,
                secOpenTop,
                secOpenLeft + getSecondaryViewWidth(),
                secOpenTop + getSecondaryViewHeight()
        )
    }


    private fun pxToDp(px: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (px / (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    private fun dpToPx(dp: Int): Int {
        val resources = context.resources
        val metrics = resources.displayMetrics
        return (dp * (metrics.densityDpi.toFloat() / DisplayMetrics.DENSITY_DEFAULT)).toInt()
    }

    /**
     * Custom AccessibilityEvents handling to prevent the Secondary Layout / revealedOptionsLayout
     * from being read through Accessibility without being seen.
     * Therefore, check all AccessibilityEvents and if the Secondary Layout / revealedOptionsLayout
     * view is focused via Accessibility reveal the layout without animation.
     */
    override fun onRequestSendAccessibilityEvent(childView: View, event: AccessibilityEvent): Boolean {
        Log.d(TAG, "onRequestSendAccessibilityEvent childView = " + childView.toString() + " & AccessibilityEvent = " + event)
        if (childView === revealedOptionsLayout && event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUSED && mState != STATE_OPEN) {
            Log.d(TAG, "revealedOptionsLayout")
            previousFocusedRevealedOptionsLayout = true
            Log.d(TAG, "openSecondaryView() no Animation")
            openSecondaryView(false)
            return true
        } else if (childView === revealedOptionsLayout && event.eventType == AccessibilityEvent.TYPE_VIEW_HOVER_ENTER) {
            /**
             * If user as selected the a revealedOptionsLayout option through tapping prevent
             * AccessibilityEvent openSecondaryView from triggering Normal operations.
             * i.e. closing the Secondary Layout / revealedOptionsLayout while the option
             * remains focused.
             */
            super.onRequestSendAccessibilityEvent(childView, event)
            previousFocusedRevealedOptionsLayout = false
            return true
        } else if (previousFocusedRevealedOptionsLayout && event.eventType == AccessibilityEvent.TYPE_WINDOW_CONTENT_CHANGED) {
            Log.d(TAG, "Middle State")
            /**
             * To prevent openSecondaryView from triggering Normal operations
             * i.e. (closing the Secondary Layout / revealedOptionsLayout) return false.
             */
            return false
        } else if (event.eventType == AccessibilityEvent.TYPE_ANNOUNCEMENT) {
            isAnnounncementDisplayed = true
            super.onRequestSendAccessibilityEvent(childView, event)
            return true
        } else {
            Log.d(TAG, "Normal Operations")
            if (previousFocusedRevealedOptionsLayout || isAnnounncementDisplayed && event.eventType == AccessibilityEvent.TYPE_VIEW_ACCESSIBILITY_FOCUS_CLEARED && mState == STATE_OPEN) {
                isAnnounncementDisplayed = false
                previousFocusedRevealedOptionsLayout = false
                Log.d(TAG, "closeSecondaryView() no Animation")
                closeSecondaryView(false)
            }
            super.onRequestSendAccessibilityEvent(childView, event)
            return true
        }
    }

    companion object {
        private val TAG = SwipeRevealLayout::class.java.simpleName
        // These states are used only for ViewBindHelper
        const val STATE_CLOSE = 0
        const val STATE_CLOSING = 1
        const val STATE_OPEN = 2
        const val STATE_OPENING = 3
        const val STATE_DRAGGING = 4

        private const val DEFAULT_MIN_FLING_VELOCITY = 300 // dp per second
        private const val DEFAULT_MIN_DIST_REQUEST_DISALLOW_PARENT = 1 // dp

        const val DRAG_EDGE_LEFT = 0x1
        const val DRAG_EDGE_RIGHT = 0x1 shl 1
        const val DRAG_EDGE_TOP = 0x1 shl 2
        const val DRAG_EDGE_BOTTOM = 0x1 shl 3

        /**
         * The secondary view will be under the main view.
         */
        const val MODE_NORMAL = 0

        /**
         * The secondary view will stick the edge of the main view.
         */
        const val MODE_SAME_LEVEL = 1

        fun getStateString(state: Int): String {
            return when (state) {
                STATE_CLOSE ->  "state_close"

                STATE_CLOSING ->  "state_closing"

                STATE_OPEN ->  "state_open"

                STATE_OPENING ->  "state_opening"

                STATE_DRAGGING ->  "state_dragging"

                else ->  "undefined"
            }
        }
    }

    private fun getSecondaryViewWidth() = if (mSecondaryView != null) mSecondaryView!!.width else 0

    private fun getSecondaryViewHeight() = if (mSecondaryView != null) mSecondaryView!!.height else 0

}