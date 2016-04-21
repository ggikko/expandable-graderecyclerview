package ggikko.me.expandablerecyclerviewgrade;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggikko on 16. 4. 21..
 */
public class RecyclerViewAdapter extends ExpandableRecyclerAdapter<RecyclerViewAdapter.GradeListItem> {

    List<GradeListItem> items = new ArrayList<>();

    public RecyclerViewAdapter(Context context) {
        super(context);
        setItems(items);
    }

    public void setItmes(List<GradeListItem> items){
        this.items = items;
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    /** expandable recyclerview adapter에 static으로 자리잡고 있는 List에 헤더인지 컨텐츠인지 타입을 넘겨준다 */
    public static class GradeListItem extends ExpandableRecyclerAdapter.ListItem {
        public int position;
        public int type;
        public int grade;
        public int number;
        public int money;

        public GradeListItem(String group) {
            super(TYPE_HEADER);
            this.type = TYPE_HEADER;
        }

        public GradeListItem(int position) {
            super(TYPE_CONTENT);
            this.type = TYPE_CONTENT;
            this.position = position;
        }
    }

    public class HeaderViewHolder extends ExpandableRecyclerAdapter.CustomViewHolder{

        TextView item_header_grade;
        TextView item_header_number;
        TextView item_header_money;

        public HeaderViewHolder(View view) {
            super(view);

            item_header_grade = (TextView) view.findViewById(R.id.item_header_grade);
            item_header_number = (TextView) view.findViewById(R.id.item_header_number);
            item_header_money = (TextView) view.findViewById(R.id.item_header_money);
        }

        public void bind(int position) {
            item_header_grade.setText(visibleItems.get(position).grade);
            item_header_number.setText(visibleItems.get(position).number);
            item_header_money.setText(visibleItems.get(position).money);
        }
    }

    public class ContentViewHolder extends ExpandableRecyclerAdapter.ViewHolder{

        TextView item_content_grade;
        TextView item_content_number;
        TextView item_content_money;

        public ContentViewHolder(View view) {
            super(view);

            item_content_grade = (TextView) view.findViewById(R.id.item_content_grade);
            item_content_number = (TextView) view.findViewById(R.id.item_content_number);
            item_content_money = (TextView) view.findViewById(R.id.item_content_money);

        }

        public void bind(int position) {
            item_content_grade.setText(visibleItems.get(position).grade);
            item_content_number.setText(visibleItems.get(position).number);
            item_content_money.setText(visibleItems.get(position).money);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new HeaderViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_CONTENT:
            default:
                return new ContentViewHolder(inflate(R.layout.item_content, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((HeaderViewHolder) holder).bind(position);
                break;
            case TYPE_CONTENT:
            default:
                ((ContentViewHolder) holder).bind(position);
                break;
        }
    }

}
