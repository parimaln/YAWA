package app.parimal.yawa;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.lib.WeatherDataModel;

import java.util.ArrayList;

/**
 * Created by Parimal on 5/16/2015.
 */
public class RowAdapter extends RecyclerView.Adapter<RowAdapter.ViewHolder> {
    private ArrayList<WeatherDataModel> daysWeather;
    MainActivity _activity;
    public RowAdapter(ArrayList<WeatherDataModel> daysWeather, MainActivity _activity) {
        this.daysWeather = daysWeather;
        this._activity = _activity;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemLayoutView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recyclerlayout, null);
        ViewHolder viewHolder = new ViewHolder(itemLayoutView);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.txtViewTitle.setText("Day "+position);
        holder.setClickListener(new ViewHolder.ClickListener() {
            @Override
            public void onClick(View v, int position) {
                _activity.updateUI(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return daysWeather.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        public TextView txtViewTitle;
        private ClickListener clickListener;
        public ViewHolder(View itemLayoutView) {
            super(itemLayoutView);
            txtViewTitle = (TextView) itemLayoutView.findViewById(R.id.dayTextView);
            itemLayoutView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickListener.onClick(v, getAdapterPosition());
                }
            });
        }
        public void setClickListener(ClickListener clickListener) {
            this.clickListener = clickListener;
        }
        public interface ClickListener {

            public void onClick(View v, int position);

        }
    }


}
