package github.hellocsl.cursorwheellayout.data;

public class ImageData {
    public int mImageRes;
    public String mImageRes_1;
    public String mDesc;

    public ImageData(int imageRes, String desc) {
        mImageRes = imageRes;
        mDesc = desc;
    }

    public ImageData(String imageRes, String desc) {
        mImageRes_1 = imageRes;
        mDesc = desc;
    }
    public int getIMageRes(){
        return mImageRes;
    }

    public String getmImageRes_1(){
        return mImageRes_1;
    }
    public String getmDesc(){
        return mDesc;
    }
}
