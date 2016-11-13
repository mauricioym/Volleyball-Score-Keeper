package com.yuddi.volleyballscorekeeper;

import android.widget.TextView;

/**
 * Created by Mauricio on 10/15/2016.
 */
public class TeamPoints {

    private int total;
    private int[] stats;
    
    private TextView total_textview;
    private TextView[] stats_textviews;

    public static int SPIKE=0;
    public static int BLOCK=1;
    public static int SERVE=2;
    public static int OP_ERRORS=3;

    public TeamPoints(TextView total_textview,TextView[] stats_textviews){
        this.total_textview=total_textview;
        this.stats_textviews=stats_textviews;
        reset();
    }

    public void reset(){
        total=0;
        total_textview.setText("0");

        stats=new int[]{0,0,0,0};
        for(int i=0;i<stats_textviews.length;i++){
            stats_textviews[i].setText("0");
        }
    }

    public void increment(int type){
        total_textview.setText("" + (++total));
        stats_textviews[type].setText("" + (++stats[type]));
    }

    public void decrement(int type){
        total_textview.setText("" + (--total));
        stats_textviews[type].setText("" + (--stats[type]));
    }

    public int getTotal(){
        return total;
    }

}
