package github.hellocsl.cursorwheellayout.data

class ImageData {
    var iMageRes: Int = 0
    lateinit var mImageRes_1: String
    var mDesc: String

    constructor(imageRes: Int, desc: String) {
        iMageRes = imageRes
        mDesc = desc
    }

    constructor(imageRes: String, desc: String) {
        mImageRes_1 = imageRes
        mDesc = desc
    }

    fun getmImageRes_1(): String {
        return mImageRes_1
    }

    fun getmDesc(): String {
        return mDesc
    }
}
