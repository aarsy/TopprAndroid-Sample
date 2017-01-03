package arsy.com.topperandroid;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class View_Holder_Events extends RecyclerView.ViewHolder {
    ImageView image;
    TextView company_name;
    TextView eventCategory;
    ImageView eventDetails, favourite;

    View_Holder_Events(View itemView) {
        super(itemView);
        image= (ImageView) itemView.findViewById(R.id.image);
        company_name= (TextView) itemView.findViewById(R.id.txtname);
        eventCategory= (TextView) itemView.findViewById(R.id.category);
        eventDetails= (ImageView) itemView.findViewById(R.id.btnEventDetails);
        favourite=(ImageView) itemView.findViewById(R.id.favourite);
    }
}