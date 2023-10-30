import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class ScheduleAdapter extends RecyclerView.Adapter<ScheduleAdapter.ViewHolder> {

    private List<ScheduleItem> scheduleItems;

    public ScheduleAdapter(List<ScheduleItem> scheduleItems) {
        this.scheduleItems = scheduleItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_schedule, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ScheduleItem scheduleItem = scheduleItems.get(position);

        holder.subjectNameTextView.setText(scheduleItem.getSubjectName());
        holder.startTimeTextView.setText(scheduleItem.getStartTime());
        holder.endTimeTextView.setText(scheduleItem.getEndTime());
    }

    @Override
    public int getItemCount() {
        return scheduleItems.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView subjectNameTextView;
        TextView startTimeTextView;
        TextView endTimeTextView;

        public ViewHolder(View itemView) {
            super(itemView);
            subjectNameTextView = itemView.findViewById(R.id.subjectNameTextView);
            startTimeTextView = itemView.findViewById(R.id.startTimeTextView);
            endTimeTextView = itemView.findViewById(R.id.endTimeTextView);
        }
    }
}