package github.hellocsl.cursorwheellayout.widget

import android.animation.ArgbEvaluator
import android.animation.ObjectAnimator
import android.annotation.TargetApi
import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import android.support.annotation.ColorInt
import android.util.AttributeSet
import android.view.SoundEffectConstants
import android.view.View

import github.hellocsl.cursorwheellayout.R

class SwitchButton : View {

    private var mDrawableGravity = DEFAULT_DRAWABLE_GRAVITY
    /**
     * 画边界
     */
    private var mBroadPaint: Paint? = null

    /**
     * 覆盖层
     */
    private var mCoverPaint: Paint? = null
    /**
     * 画扩散进度
     */
    private var mRevealBgPaint: Paint? = null
    /**
     * 边框颜色
     */
    @ColorInt
    private val mBoardColor = DEFAULT_BOARD_COLOR


    /**
     * 边框厚度
     */
    private var mBoardWidth: Int = 0
    /**
     * 选中的时候的颜色
     */
    private var mCheckRevealColor = DEFAULT_BOARD_COLOR

    /**
     * 未选中时候的颜色
     */
    private var mUnCheckRevealColor = DEFAULT_UNCHECK_REVEAL_COLOR

    /**
     * 用于动画 0-100 选中的时候;
     */
    //            if (BuildConfig.DEBUG) {
    //                Log.d(TAG, "setProgress: mProgress:" + mProgress + ",mFraction:" + mFraction);
    //            }
    var progress: Int = 0
        private set(progress) {
            if (this.progress != progress) {
                field = progress
                mFraction = this.progress * 1.0f / PROGRESS_MAX
                invalidate()
            }
        }

    // Avoid infinite recursions if setChecked() is called from a listener
    var isChecked: Boolean = false
        set(checked) {
            if (isChecked != checked) {
                field = checked
                refreshDrawableState()
                if (mBroadcasting) {
                    return
                }
                if (mAttachedToWindow) {
                    addAnim(checked)
                } else {
                    cancelAnim()
                    progress = if (checked) PROGRESS_MAX else PROGRESS_MIN
                }

                mBroadcasting = true
                if (mOnCheckedChangeListener != null) {
                    mOnCheckedChangeListener!!.onCheckedChanged(this, isChecked)
                }
                mBroadcasting = false
            }
        }

    private var mBroadcasting: Boolean = false

    private var mOnCheckedChangeListener: OnCheckedChangeListener? = null
    /**
     * 未选中的时候的资源ID
     */
    private var mUncheckDrawable: Drawable? = null
    /**
     * 未选中的时候的资源ID
     */
    private var mCheckedDrawable: Drawable? = null

    private var mArgbEvaluator: ArgbEvaluator? = null
    private var mFraction: Float = 0.toFloat()
    private var mCheckAnim: ObjectAnimator? = null
    private var mAttachedToWindow: Boolean = false

    constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0) : super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context, attrs)
    }

    private fun init(context: Context, attrs: AttributeSet?) {
        isClickable = true
        mArgbEvaluator = ArgbEvaluator()
        val density = context.resources.displayMetrics.density
        mBoardWidth = (DEFAULT_BOARD_WIDTH * density + 0.5).toInt()
        //TODO 更多自定义属性的获取，这里hardcode先，以后有需要在提取出来
        if (attrs != null) {
            val ta = context.obtainStyledAttributes(attrs, R.styleable.SwitchButton)
            val checkRes = ta.getResourceId(R.styleable.SwitchButton_checkSrc, DEFAULT_CHECK_DRAWABLE_ID)
            mCheckedDrawable = context.resources.getDrawable(checkRes)
            val uncheckRes = ta.getResourceId(R.styleable.SwitchButton_uncheckSrc, DEFAULT_UNCHECK_DRAWABLE_ID)
            mUncheckDrawable = context.resources.getDrawable(uncheckRes)
            mBoardWidth = ta.getDimensionPixelOffset(R.styleable.SwitchButton_boardWidth, mBoardWidth)
            mCheckRevealColor = ta.getColor(R.styleable.SwitchButton_checkRevealColor, DEFAULT_CHECK_REVEAL_COLOR)
            mUnCheckRevealColor = ta.getColor(R.styleable.SwitchButton_uncheckRevealColor, -1)
            ta.recycle()
        }
        //init paint
        mBroadPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mBroadPaint!!.style = Paint.Style.STROKE
        mBroadPaint!!.color = mBoardColor
        mBroadPaint!!.strokeWidth = mBoardWidth.toFloat()
        mBroadPaint!!.isDither = true

        mCoverPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mCoverPaint!!.style = Paint.Style.FILL
        mCoverPaint!!.color = DEFAULT_DISABLE_COVER_COLOR
        mCoverPaint!!.isDither = true


        mRevealBgPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        mRevealBgPaint!!.isFilterBitmap = true
        mRevealBgPaint!!.style = Paint.Style.FILL
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val density = context.resources.displayMetrics.density
        val defaultSize = (DEFAULT_SIZE * density + 0.5).toInt()
        val desiredWidth = paddingLeft + paddingRight + defaultSize
        val desiredHeight = paddingBottom + paddingTop + defaultSize
        val widthSpec = resolveSizeAndState(desiredWidth, widthMeasureSpec)
        val heightSpec = resolveSizeAndState(desiredHeight, heightMeasureSpec)
        super.onMeasure(widthSpec, heightSpec)
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        mAttachedToWindow = true
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        mAttachedToWindow = false
    }

    override fun getSuggestedMinimumWidth(): Int {
        val density = context.resources.displayMetrics.density
        return (DEFAULT_SIZE * density + 0.5).toInt()
    }

    override fun getSuggestedMinimumHeight(): Int {
        return suggestedMinimumWidth
    }

    fun toggle() {
        isChecked = !isChecked
    }

    fun setOnCheckedChangeListener(listener: OnCheckedChangeListener) {
        mOnCheckedChangeListener = listener
    }


    fun onDoubClick(): Boolean {
        var flag = false
        val time = System.currentTimeMillis() - slastTime
        if (time < 500) {
            flag = true
        }
        slastTime = System.currentTimeMillis()
        return flag
    }

    override fun performClick(): Boolean {
        if (onDoubClick()) {
            return false
        }
        toggle()
        val handled = super.performClick()
        if (!handled) {
            // View only makes a sound effect if the onClickListener was
            // called, so we'll need to make one here instead.
            playSoundEffect(SoundEffectConstants.CLICK)
        }
        return handled
    }

    private fun cancelAnim() {
        if (mCheckAnim != null) {
            mCheckAnim!!.cancel()
        }
    }


    private fun addAnim(isChecked: Boolean) {
        mCheckAnim = ObjectAnimator.ofInt(this, "progress", if (isChecked) PROGRESS_MAX else PROGRESS_MIN)
        mCheckAnim!!.duration = 300
        mCheckAnim!!.start()
    }

    private fun generateCurColor(): Int {
        return mArgbEvaluator!!.evaluate(mFraction, mUnCheckRevealColor, mCheckRevealColor) as Int
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(paddingLeft.toFloat(), paddingTop.toFloat())
        val width = width - paddingLeft - paddingRight
        val height = height - paddingBottom - paddingTop
        val radius = Math.min(width, height) / 2
        val buttonDrawable = if (isChecked) mCheckedDrawable else mUncheckDrawable
        if (buttonDrawable != null) {
            //// FIXME: 16-4-20 臨時使用
            if (mDrawableGravity == DRAWABLE_GRAVITY_LEFT) {
                //偏右
                val drawableHeight = buttonDrawable.intrinsicHeight
                val drawableWidth = buttonDrawable.intrinsicWidth
                val right = width / 2 - drawableWidth / 2
                val left = right - drawableWidth
                val top = (height - drawableHeight) / 2
                val bottom = top + drawableHeight
                buttonDrawable.setBounds(left, top, right, bottom)
            } else if (mDrawableGravity == DRAWABLE_GRAVITY_RIGHT) {
                //偏左
                val drawableHeight = buttonDrawable.intrinsicHeight
                val drawableWidth = buttonDrawable.intrinsicWidth
                val left = width / 2 + drawableWidth / 2
                val right = left + drawableWidth
                val top = (height - drawableHeight) / 2
                val bottom = top + drawableHeight
                buttonDrawable.setBounds(left, top, right, bottom)
            } else {
                //居中
                val drawableHeight = buttonDrawable.intrinsicHeight
                val drawableWidth = buttonDrawable.intrinsicWidth
                val left = (width - drawableWidth) / 2
                val right = left + drawableWidth
                val top = (height - drawableHeight) / 2
                val bottom = top + drawableHeight
                buttonDrawable.setBounds(left, top, right, bottom)
            }
            //            if (BuildConfig.DEBUG) {
            //                Log.v(TAG, "onDraw:button drawable bound" + buttonDrawable.getBounds());
            //            }
        }
        if (mUnCheckRevealColor != -1 && !isChecked) {
            mRevealBgPaint!!.color = mUnCheckRevealColor
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (radius - mBoardWidth).toFloat(), mRevealBgPaint!!)
        }
        //draw broad
        if (mBoardWidth > 0) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), (radius - mBoardWidth / 2).toFloat(), mBroadPaint!!)
        }

        mRevealBgPaint!!.color = generateCurColor()
        canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius * mFraction, mRevealBgPaint!!)

        buttonDrawable?.draw(canvas)
        if (!isEnabled) {
            canvas.drawCircle((width / 2).toFloat(), (height / 2).toFloat(), radius.toFloat(), mCoverPaint!!)
        }
        canvas.restore()
    }

    fun setDrawableGravity(drawableGravity: Int) {
        if (mDrawableGravity != drawableGravity) {
            mDrawableGravity = drawableGravity
            invalidate()
        }
    }

    /**
     * Interface definition for a callback to be invoked when the checked state
     * of a compound button changed.
     */
    interface OnCheckedChangeListener {
        /**
         * Called when the checked state of a compound button has changed.
         *
         * @param buttonView The compound button view whose state has changed.
         * @param isChecked  The new checked state of buttonView.
         */
        fun onCheckedChanged(buttonView: SwitchButton, isChecked: Boolean)
    }

    /**
     * @author chensuilun
     */
    internal class SavedState : View.BaseSavedState {
        var mChecked: Boolean = false

        constructor(superState: Parcelable) : super(superState) {}

        /**
         * Constructor called from [.CREATOR]
         */
        private constructor(`in`: Parcel) : super(`in`) {
            mChecked = `in`.readValue(null) as Boolean
        }

        override fun writeToParcel(out: Parcel, flags: Int) {
            super.writeToParcel(out, flags)
            out.writeValue(mChecked)
        }

        override fun toString(): String {
            return ("CompoundButton.SavedState{"
                    + Integer.toHexString(System.identityHashCode(this))
                    + " checked=" + mChecked + "}")
        }

        companion object {

            val CREATOR: Parcelable.Creator<SavedState> = object : Parcelable.Creator<SavedState> {
                override fun createFromParcel(`in`: Parcel): SavedState {
                    return SavedState(`in`)
                }

                override fun newArray(size: Int): Array<SavedState?> {
                    return arrayOfNulls(size)
                }
            }
        }
    }

    public override fun onSaveInstanceState(): Parcelable? {
        val superState = super.onSaveInstanceState()

        val ss = SavedState(superState)

        ss.mChecked = isChecked
        return ss
    }

    public override fun onRestoreInstanceState(state: Parcelable) {
        val ss = state as SavedState

        super.onRestoreInstanceState(ss.superState)
        isChecked = ss.mChecked
        requestLayout()
    }

    companion object {
        private val TAG = "SwitchButton"
        private val PROGRESS_MIN = 0
        private val PROGRESS_MAX = 100
        private val DEFAULT_BOARD_WIDTH = 4 // dp
        private val DEFAULT_BOARD_COLOR = -0xc3b6ae
        private val DEFAULT_UNCHECK_REVEAL_COLOR = -0xc3b6ae
        //DRAWABLE GRAVITY
        val DRAWABLE_GRAVITY_LEFT = 0
        val DRAWABLE_GRAVITY_RIGHT = 1
        val DEFAULT_DRAWABLE_GRAVITY = -1

        //    private static final int DEFAULT_UNCHECK_REVEAL_COLOR = 0X1D1F22;
        private val DEFAULT_CHECK_REVEAL_COLOR = -0x3ad6
        private val DEFAULT_DISABLE_COVER_COLOR = 0x4cffffff
        private val DEFAULT_CHECK_DRAWABLE_ID = R.mipmap.ic_launcher
        private val DEFAULT_UNCHECK_DRAWABLE_ID = R.mipmap.ic_launcher

        private val DEFAULT_SIZE = 126 //dp

        fun resolveSizeAndState(desireSize: Int, measureSpec: Int): Int {
            var result = desireSize
            val specMode = View.MeasureSpec.getMode(measureSpec)
            val specSize = View.MeasureSpec.getSize(measureSpec)
            when (specMode) {
                View.MeasureSpec.UNSPECIFIED -> result = desireSize
                View.MeasureSpec.AT_MOST -> if (specSize < desireSize) {
                    result = specSize
                } else {
                    result = desireSize
                }
                View.MeasureSpec.EXACTLY -> result = specSize
            }
            return result
        }


        var slastTime: Long = 0
    }

}
