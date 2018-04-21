package hospitalmanagement.haresh.com.rssfeedexample;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Haresh on 21-Jan-18.
 */

public class VerticalSpace extends RecyclerView.ItemDecoration {
    int space;

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left=space;
        outRect.right=space;
        outRect.bottom=space;
        if (parent.getChildLayoutPosition(view)==0){
            outRect.top=space;
        }
    }

    public VerticalSpace(int space){
        this.space=space;
    }
}
