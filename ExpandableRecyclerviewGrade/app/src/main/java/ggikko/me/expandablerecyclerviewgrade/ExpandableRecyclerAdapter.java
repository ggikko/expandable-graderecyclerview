package ggikko.me.expandablerecyclerviewgrade;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.SparseIntArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ggikko on 16. 4. 21..
 */
public class ExpandableRecyclerAdapter <T extends ExpandableRecyclerAdapter.ListItem> extends RecyclerView.Adapter<ExpandableRecyclerAdapter.ViewHolder> {

    protected static final int TYPE_HEADER = 1000;
    public static final int TYPE_CONTENT = 1001;

    private static final int ARROW_ROTATION_DURATION = 150;

    protected Context mContext;
    protected List<T> allItems = new ArrayList<>();
    protected List<T> visibleItems = new ArrayList<>();
    private List<Integer> indexList = new ArrayList<>();
    private SparseIntArray expandMap = new SparseIntArray();

    public ExpandableRecyclerAdapter(Context context) {
        mContext = context;
    }

    public static class ListItem {

        public int ItemType;

        public ListItem(int itemType) {
            ItemType = itemType;
        }
    }

    @Override
    public ExpandableRecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case TYPE_HEADER:
                return new CustomViewHolder(inflate(R.layout.item_header, parent));
            case TYPE_CONTENT:
            default:
                return new CustomViewHolder(inflate(R.layout.item_content, parent));
        }
    }

    @Override
    public void onBindViewHolder(ExpandableRecyclerAdapter.ViewHolder holder, int position) {
        switch (getItemViewType(position)) {
            case TYPE_HEADER:
                ((CustomViewHolder) holder).bind(position);
                break;
            case TYPE_CONTENT:
            default:
                ((CustomViewHolder) holder).bind(position);
                break;
        }
    }

    /** item id */
    @Override
    public long getItemId(int i) {
        return i;
    }

    /** visible item count */
    @Override
    public int getItemCount() {
        return visibleItems == null ? 0 : visibleItems.size();
    }

    /** resourceID(Header또는 content layout inflate)를 받아와서 inflate */
    protected View inflate(int resourceID, ViewGroup viewGroup) {
        return LayoutInflater.from(mContext).inflate(resourceID, viewGroup, false);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(View view) {
            super(view);
        }
    }

    /**
     * 타입 : 헤더와 컨텐츠로 리스트를 나누는 구분자
     * 이를 통해 애니메이션을 주어 확장시킬 때(리스트를 헤더사이에 껴넣을 때) 구분자가 되는 타입을 얻어온다.
     * @param position
     * @return
     */
    @Override
    public int getItemViewType(int position) {
        return visibleItems.get(position).ItemType;
    }

    /**
     * expanded 됬는지 아닌지 판단함. 최적화를 위해 map이 아니라 SparseIntArray 를 사용.
     * 맵과 비슷한 놈임 하지만 관리하는 방식이 다름. indexList의 position에 해당하는 값을 받아아와서
     * 값이 있으면 true 없으면 false반환. true = expanded 되어있다는 거임. false는 그 반대
     * @param position
     * @return
     */
    protected boolean isExpanded(int position) {
        int allItemsPosition = indexList.get(position);
        return expandMap.get(allItemsPosition, -1) >= 0;
    }

    /** 뷰 홀더 custom */
    public class CustomViewHolder extends ViewHolder {

        ImageView arrow;

        public CustomViewHolder(View view) {
            super(view);

            arrow = (ImageView) view.findViewById(R.id.item_arrow);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    toggleExpandedItems(getLayoutPosition());
                }
            });
        }

        /** item expanded toggle */
        private void toggleExpandedItems(int position) {
            int allItemsPosition = indexList.get(position);

            /** expanded -> close , closed -> expand */
            if (isExpanded(position)) {
                collapseItems(position);
                expandMap.delete(allItemsPosition);
                closeArrow(arrow);
            } else {
                expandItems(position);
                expandMap.put(allItemsPosition, 1);
                openArrow(arrow);
            }
        }

        /**
         * index +1에서부터 모든 아이템에서 visibleitem으로 add하고 index list도 순차저긍로 증가시킨다. Item type이 header를 만나면 stop
         * @param position
         */
        private void expandItems(int position) {
            int count = 0;
            int index = indexList.get(position);
            int insert = position;

            for (int i = index + 1; i < allItems.size() && allItems.get(i).ItemType != TYPE_HEADER; i++) {
                insert++;
                count++;
                visibleItems.add(insert, allItems.get(i));
                indexList.add(insert, i);
            }

            notifyItemRangeInserted(position + 1, count);
        }

        /**
         * index + 1 에서부터 total item size보다 작고 total item list 에서 얻은 type이 type
         * header가 아닌것 가지 remove하고 notifydatasetchanged partially
         * @param position
         */
        private void collapseItems(int position) {
            int count = 0;
            int index = indexList.get(position);

            for (int i = index + 1; i < allItems.size() && allItems.get(i).ItemType != TYPE_HEADER; i++) {
                count++;
                visibleItems.remove(position + 1);
                indexList.remove(position + 1);
            }
            notifyItemRangeRemoved(position + 1, count);
        }

        public void bind(int position) {
            arrow.setRotation(isExpanded(position) ? 90 : 0);
        }
    }

    /** 아이템 set, expand, index clear */
    public void setItems(List<T> items) {
        allItems = items;
        List<T> visibleItems = new ArrayList<>();
        expandMap.clear();
        indexList.clear();

        for (int i = 0; i < items.size(); i++) {
            if (items.get(i).ItemType == TYPE_HEADER) {
                indexList.add(i);
                visibleItems.add(items.get(i));
            }
        }

        this.visibleItems = visibleItems;
        notifyDataSetChanged();
    }

    /** arrow 90 degree rotation */
    public static void openArrow(View view) {
        view.animate().setDuration(ARROW_ROTATION_DURATION).rotation(90);
    }

    /** arrow rotate again originally */
    public static void closeArrow(View view) {
        view.animate().setDuration(ARROW_ROTATION_DURATION).rotation(0);
    }

}
